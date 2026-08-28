package com.onixbyte.ahsarahguide.service;

import com.onixbyte.captcha.text.TextProducer;
import com.onixbyte.ahsarahguide.client.EmailClient;
import com.onixbyte.ahsarahguide.client.TokenClient;
import com.onixbyte.ahsarahguide.domain.dto.EmailVerificationCode;
import com.onixbyte.ahsarahguide.domain.dto.LoginRequest;
import com.onixbyte.ahsarahguide.domain.dto.RegisterRequest;
import com.onixbyte.ahsarahguide.domain.dto.SendVerificationCodeRequest;
import com.onixbyte.ahsarahguide.domain.dto.UserResponse;
import com.onixbyte.ahsarahguide.domain.entity.User;
import com.onixbyte.ahsarahguide.domain.entity.UserCredential;
import com.onixbyte.ahsarahguide.exeption.BadRequestException;
import com.onixbyte.ahsarahguide.exeption.ConflictException;
import com.onixbyte.ahsarahguide.exeption.InternalServerErrorException;
import com.onixbyte.ahsarahguide.manager.CookieManager;
import com.onixbyte.ahsarahguide.manager.UserManager;
import com.onixbyte.ahsarahguide.security.authentication.UsernamePasswordAuthentication;
import com.onixbyte.ahsarahguide.shared.CookieName;
import com.onixbyte.ahsarahguide.shared.CredentialProvider;
import com.onixbyte.ahsarahguide.shared.Role;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service handling user authentication, login, and session management.
 *
 * @author zihluwang
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final AuthenticationManager authenticationManager;
    private final TokenClient tokenClient;
    private final CookieManager cookieManager;
    private final UserManager userManager;
    private final PasswordEncoder passwordEncoder;
    private final TextProducer textProducer;
    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailClient emailClient;

    public AuthService(
            AuthenticationManager authenticationManager,
            TokenClient tokenClient,
            CookieManager cookieManager,
            UserManager userManager,
            PasswordEncoder passwordEncoder,
            TextProducer textProducer,
            CacheManager cacheManager,
            RedisTemplate<String, Object> redisTemplate,
            EmailClient emailClient
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenClient = tokenClient;
        this.cookieManager = cookieManager;
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
        this.textProducer = textProducer;
        this.cacheManager = cacheManager;
        this.redisTemplate = redisTemplate;
        this.emailClient = emailClient;
    }

    /**
     * Authenticates a user with the given login credentials.
     * <p>
     * Delegates authentication to Spring Security's {@link AuthenticationManager} and verifies
     * that the result is of the expected {@link UsernamePasswordAuthentication} type.
     *
     * @param request the login credentials containing principle and password
     * @return the authenticated {@link User}
     */
    public User login(LoginRequest request) {
        var _authentication = authenticationManager.authenticate(UsernamePasswordAuthentication
                .unauthenticated(request.principle(), request.credential()));
        if (!(_authentication instanceof UsernamePasswordAuthentication authentication)) {
            log.error(
                    "Type mismatched, required type is UsernamePasswordAuthentication but got {}.",
                    _authentication.getClass()
            );
            throw new InternalServerErrorException("登录服务异常，请稍后再试。");
        }

        return authentication.getDetails();
    }

    /**
     * Registers a new user account with the LOCAL password provider and the default USER role.
     *
     * @param request the registration information
     * @return the newly created {@link User}
     */
    public User register(RegisterRequest request) {
        var username = request.username().trim();
        var email = request.email().trim();

        if (userManager.existsByUsername(username)) {
            throw new BadRequestException("用户名已被注册。");
        }
        if (userManager.existsByEmail(email)) {
            throw new BadRequestException("该邮箱已被注册。");
        }

        var code = request.verificationCode();
        var verificationCodeId = request.verificationCodeId();

        var _req = redisTemplate.opsForValue().get("verification-code:" + verificationCodeId);
        if (!(_req instanceof EmailVerificationCode verificationCode)) {
            log.error("_req not a EmailVerificationCode, storedCode={}", _req);
            throw new InternalServerErrorException("系统内部错误，请重试");
        }

        if (!request.username().equals(verificationCode.username())
                || !request.email().equals(verificationCode.email())) {
            log.error(
                    "Username and email address do not match the data submitted "
                            + "when requesting the verification code."
            );
            log.error("EmailVerificationCode={}", verificationCode);
            log.error("RegisterRequest={}", request);
            throw new BadRequestException("用户名或邮箱与获取验证码时不一致，请重新获取验证码。");
        }

        if (!code.equalsIgnoreCase(verificationCode.code())) {
            throw new BadRequestException("邮箱验证码不正确，请重试。");
        }

        var now = LocalDateTime.now();
        var user = User.builder()
                .username(username)
                .email(email)
                .nickname(request.nickname())
                .avatarUrl(request.avatarUrl())
                .createdAt(now)
                .updatedAt(now)
                .build();
        var credential = UserCredential.builder()
                .provider(CredentialProvider.LOCAL)
                .credential(passwordEncoder.encode(request.password()))
                .build();
        user.addCredential(credential);

        return userManager.createWithRole(user, Role.ROLE_USER);
    }

    @NonNull
    public ResponseEntity<UserResponse> getUserResponseResponseEntity(User user) {
        var currentTime = LocalDateTime.now();
        var accessToken = tokenClient.generateToken(user);
        var accessTokenCookie = cookieManager.buildCookie(CookieName.ACCESS_TOKEN, accessToken);
        var cookieMaxAge = accessTokenCookie.getMaxAge();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .body(UserResponse.from(user, currentTime.plus(cookieMaxAge)));
    }

    public HttpCookie logout() {
        return cookieManager.buildCookie(CookieName.ACCESS_TOKEN, "", Duration.ZERO);
    }

    /**
     * Generates and dispatches a verification code to the given email address.
     * <p>
     * The code is cached under the returned UUID for 10 minutes and emailed
     * asynchronously to the recipient. The UUID must be supplied back to the
     * server when verifying the code.
     *
     * @param request the verification code request containing the target username and email
     * @return the UUID identifying the verification record, to be used when verifying the code
     * @throws ConflictException if the username or email is already registered
     */
    public String sendVerificationCode(SendVerificationCodeRequest request) {
        // Check username.
        if (userManager.existsByUsername(request.username())) {
            throw new ConflictException("用户名【" + request.username() + "】已被使用。");
        }

        // Check email address.
        if (userManager.existsByEmail(request.email())) {
            throw new ConflictException("邮箱地址【" + request.email() + "】已被使用。");
        }

        // Generate UUID and verification code.
        var uuid = UUID.randomUUID().toString().replace("-", "");
        var code = textProducer.getText();

        // Save verification code to cache.
        redisTemplate.opsForValue().set(
                "verification-code:" + uuid,
                new EmailVerificationCode(request.username(), request.email(), code),
                Duration.ofMinutes(10L)
        );

        // Send email.
        emailClient.sendVerificationCode(request.email(), request.username(), code, 10);

        return uuid;
    }
}
