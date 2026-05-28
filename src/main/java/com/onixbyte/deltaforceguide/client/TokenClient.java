package com.onixbyte.deltaforceguide.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.onixbyte.deltaforceguide.domain.entity.User;
import com.onixbyte.deltaforceguide.properties.TokenProperties;
import com.onixbyte.deltaforceguide.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Client for generating and verifying JSON Web Tokens using the Auth0 java-jwt library.
 *
 * @author zihluwang
 */
@Component
public class TokenClient {

    private final Algorithm algorithm;
    private final TokenProperties tokenProperties;
    private final JWTVerifier verifier;

    /**
     * Constructs a new TokenClient with the necessary algorithm and token properties.
     *
     * @param algorithm       the signing algorithm used to secure the JWT
     * @param tokenProperties the configuration properties for the token, such as issuer and
     *                        validity period
     */
    @Autowired
    public TokenClient(
            Algorithm algorithm,
            TokenProperties tokenProperties,
            JWTVerifier verifier
    ) {
        this.algorithm = algorithm;
        this.tokenProperties = tokenProperties;
        this.verifier = verifier;
    }

    /**
     * Generate a JSON Web Token to the current user.
     *
     * @param user the current user for whom the token is being generated
     * @return a JWT string
     */
    public String generateToken(User user) {
        var issuedAt = LocalDateTime.now();
        var expiresAt = issuedAt.plus(tokenProperties.validTime());

        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(tokenProperties.issuer())
                .withIssuedAt(DateTimeUtil.asInstant(issuedAt))
                .withExpiresAt(DateTimeUtil.asInstant(expiresAt))
                .sign(algorithm);
    }

    /**
     * Verify and decode token.
     *
     * @param token a JWT token
     * @return information included in the given token
     * @throws com.auth0.jwt.exceptions.JWTVerificationException if the token is invalid, such as
     *                                                           expired, or not signed by
     *                                                           specific server
     */
    public DecodedJWT verifyToken(String token) {
        return verifier.verify(token);
    }
}
