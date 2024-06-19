package sgtbigbird.gcc.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.naming.AuthenticationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class AuthenticationService {
    @Value("${auth.url}")
    String url;
    @Value("${auth.secret}")
    String app_secret;
    @Autowired
    ObjectMapper objectMapper;
    @Getter
    Map<UUID, String> tokensIssued = new HashMap<>();
    @Getter
    Map<String, UUID> cache = new HashMap<>();

    public UUID authenticateUuid(String uuid) throws AuthenticationException {
        log.info("Authenticating uuid {}", uuid);
        try {
            UUID u = UUID.fromString(uuid);
            log.info("Checking uuid {}", u);

            if (!tokensIssued.containsKey(u)) {
                throw new AuthenticationException();
            }
            return u;
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException();
        }
    }

    @GetMapping("/auth")
    public String token(@RequestParam String secret) throws AuthenticationException {
        if (secret == null || secret.length() < 5 || secret.contains(" ")) {
            throw new AuthenticationException();
        }
        log.info("Got token request for secret {}", secret);
        if (cache.containsKey(secret)) {
            return cache.get(secret).toString();
        }

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("token", secret);
        map.add("secret", app_secret);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("User-Agent", "sergeant-bigbird#5050");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestBodyFormUrlEncoded = new HttpEntity<>(map, headers);
        ResponseEntity resp;
        try {
            resp = restTemplate.exchange(url, HttpMethod.POST, requestBodyFormUrlEncoded, Map.class);
        } catch (HttpServerErrorException e) {
            throw new AuthenticationException();
        }

        if (resp.getStatusCode() == HttpStatus.OK) {
            Map<String, String> body = (Map<String, String>) resp.getBody();
            UUID r = UUID.randomUUID();
            assert body != null;
            tokensIssued.put(r, body.get("display_name"));
            log.info("Issued token {} to {}", r, body.get("display_name"));
            cache.put(secret, r);
            return r.toString();
        } else {
            throw new AuthenticationException();
        }
    }
}
