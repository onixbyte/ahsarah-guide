package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.domain.dto.*;
import com.onixbyte.deltaforceguide.exeption.BadRequestException;
import com.onixbyte.deltaforceguide.manager.ModificationManager;
import com.onixbyte.deltaforceguide.manager.WebhookManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class WebhookService {

    private static final Logger log = LoggerFactory.getLogger(WebhookService.class);
    private static final String TRIGGER_LABEL = "weapon-mod";
    private static final Duration DEDUP_TTL = Duration.ofHours(12);
    private static final Pattern YAML_FENCE =
            Pattern.compile("```ya?ml\\s*\\R(.*?)```", Pattern.DOTALL);

    private final ModificationManager modificationManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final WebhookManager webhookManager;
    private final Yaml yaml;

    public WebhookService(
            ModificationManager modificationManager,
            RedisTemplate<String, Object> redisTemplate,
            WebhookManager webhookManager
    ) {
        this.modificationManager = modificationManager;
        this.redisTemplate = redisTemplate;
        this.webhookManager = webhookManager;
        this.yaml = new Yaml();
    }

    public void processIssueEvent(GitHubIssueRequest request) {
        var issue = request.issue();
        var repository = request.repository();
        var sender = request.sender();

        if (!isAllowedSender(sender)) {
            log.info("Issue #{} sender={} not in allowed-users, skipping",
                    issue.number(), sender != null ? sender.login() : "null");
            return;
        }

        if (!hasTriggerLabel(issue.labels())) {
            log.debug("Issue #{} lacks trigger label, skipping", issue.number());
            return;
        }

        var dedupKey = "github:webhook:processed:%s:%d"
                .formatted(repository.fullName(), issue.number());
        var acquired = redisTemplate.opsForValue()
                .setIfAbsent(dedupKey, "1", DEDUP_TTL);
        if (acquired == null || !acquired) {
            log.info("Issue #{} already processed, skipping", issue.number());
            return;
        }

        var parsedYaml = extractYaml(issue.body());
        if (parsedYaml == null) {
            log.warn("No YAML block found in issue #{} body", issue.number());
            return;
        }

        var data = yaml.<Map<String, Object>>load(parsedYaml);
        if (data == null) {
            log.warn("Empty YAML block in issue #{}", issue.number());
            return;
        }

        if (data.containsKey("modifications")) {
            processBatch(issue.number(), data);
        } else {
            processSingle(issue.number(), data);
        }
    }

    private void processSingle(Long issueNumber, Map<String, Object> data) {
        var request = mapToRequest(data);
        log.info("Creating modification from issue #{}: name={}", issueNumber, request.name());
        modificationManager.create(request, null);
    }

    @SuppressWarnings("unchecked")
    private void processBatch(Long issueNumber, Map<String, Object> data) {
        var list = (List<Map<String, Object>>) data.get("modifications");
        if (list == null || list.isEmpty()) {
            log.warn("Empty modifications list in issue #{}", issueNumber);
            return;
        }
        var requests = list.stream()
                .map(this::mapToRequest)
                .toList();
        log.info("Batch creating {} modifications from issue #{}", requests.size(), issueNumber);
        modificationManager.batchCreate(requests, null);
    }

    private ModificationRequest mapToRequest(Map<String, Object> data) {
        Long firearmId = modificationManager.resolveFirearmId(
                toLong(data.get("firearmId")),
                (String) data.get("firearmName"));
        if (firearmId == null) {
            throw new BadRequestException(
                    "YAML must contain firearmId or firearmName");
        }
        String name = (String) data.get("name");
        String code = (String) data.get("code");
        List<String> tags = toStringList(data.get("tags"));
        List<AccessoryRequest> accessories = mapAccessories(data.get("accessories"));
        String note = (String) data.get("note");
        String author = (String) data.get("author");
        String videoUrl = (String) data.get("videoUrl");

        return new ModificationRequest(firearmId, name, code, tags, accessories,
                note, author, videoUrl);
    }

    private List<AccessoryRequest> mapAccessories(Object raw) {
        if (!(raw instanceof List<?> list)) {
            return new ArrayList<>();
        }
        var result = new ArrayList<AccessoryRequest>();
        for (var item : list) {
            if (item instanceof Map<?, ?> map) {
                result.add(new AccessoryRequest(
                        (String) map.get("slotName"),
                        (String) map.get("accessoryName"),
                        mapTunings(map.get("tunings"))
                ));
            }
        }
        return result;
    }

    private List<TuningRequest> mapTunings(Object raw) {
        if (!(raw instanceof List<?> list)) {
            return new ArrayList<>();
        }
        var result = new ArrayList<TuningRequest>();
        for (var item : list) {
            if (item instanceof Map<?, ?> map) {
                result.add(new TuningRequest(
                        (String) map.get("tuningName"),
                        toDouble(map.get("tuningValue"))
                ));
            }
        }
        return result;
    }

    private List<String> toStringList(Object raw) {
        if (raw instanceof List<?> list) {
            return list.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();
        }
        return new ArrayList<>();
    }

    private boolean isAllowedSender(
            GitHubWebhookSender sender
    ) {
        var allowedUsers = webhookManager.allowedUsers();
        if (allowedUsers == null || allowedUsers.isEmpty()) {
            return true;
        }
        if (sender == null || sender.login() == null) {
            return false;
        }
        return allowedUsers.contains(sender.login());
    }

    private boolean hasTriggerLabel(List<GitHubWebhookLabel> labels) {
        if (labels == null) {
            return false;
        }
        return labels.stream().anyMatch(label -> TRIGGER_LABEL.equals(label.name()));
    }

    private String extractYaml(String body) {
        if (body == null) {
            return null;
        }
        var matcher = YAML_FENCE.matcher(body);
        return matcher.find() ? matcher.group(1) : null;
    }

    private Long toLong(Object value) {
        if (value instanceof Number num) {
            return num.longValue();
        }
        if (value instanceof String s) {
            return Long.parseLong(s);
        }
        return null;
    }

    private Double toDouble(Object value) {
        if (value instanceof Number num) {
            return num.doubleValue();
        }
        if (value instanceof String s) {
            return Double.parseDouble(s);
        }
        return null;
    }
}
