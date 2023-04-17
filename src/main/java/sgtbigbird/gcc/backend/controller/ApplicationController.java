package sgtbigbird.gcc.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import sgtbigbird.gcc.backend.model.DataPointWrapper;
import sgtbigbird.gcc.backend.model.RunKey;
import sgtbigbird.gcc.backend.model.RunKeyWrapper;
import sgtbigbird.gcc.backend.repository.DataPointRepository;
import sgtbigbird.gcc.backend.repository.RunKeyRepository;

import javax.naming.AuthenticationException;
import java.util.*;

@RestController
@Slf4j
public class ApplicationController {

    final String ERROR = "ERROR";
    @Autowired
    RunKeyRepository runKeyRepository;
    @Autowired
    DataPointRepository dataPointRepository;
    RestTemplate restTemplate = new RestTemplate();
    @Value("${auth.url}")
    String url;

    @Value("${auth.secret}")
    String app_secret;

    @Autowired
    ObjectMapper objectMapper;
    Map<String, UUID> cache = new HashMap<>();
    Map<UUID, String> tokensIssued = new HashMap<>();

    private UUID authenticateUuid(String uuid) throws AuthenticationException {
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
    @GetMapping("/runkey")
    public List<RunKey> getRunKeys(@RequestParam String token) throws AuthenticationException {
        authenticateUuid(token);
        return runKeyRepository.findAll();
    }
    @PostMapping("/runkey")
    public void addRunKey(@RequestBody RunKeyWrapper runKeyWrapper) throws AuthenticationException {
        authenticateUuid(runKeyWrapper.getToken());
        runKeyRepository.save(runKeyWrapper.getRunKey());
    }
    @PostMapping("/datapoint")
    public void postPoints(@RequestBody DataPointWrapper inList) throws AuthenticationException {
        if (inList.getToken().equals(ERROR)) {
            log.warn("Got the error token :O");
            throw new AuthenticationException();
        }

        UUID u = authenticateUuid(inList.getToken());
        Optional<RunKey> runKey = runKeyRepository.findByRkId(inList.getRkId());

        if (runKey.isPresent()) {
            log.info("Persisting data for {}", tokensIssued.get(u));
            inList.getData().forEach(
                    (ol) -> ol.forEach(li -> {
                        li.setUsername(tokensIssued.get(u));
                        li.setRunKey(runKey.get());
                    })
            );

            inList.getData().forEach((l) -> dataPointRepository.saveAll(l));
        } else {
            log.warn("Couldn't find rkid {}!", inList.getRkId());
        }
    }

    @GetMapping("/auth")
    public String token(@RequestParam String secret) throws JsonProcessingException, InterruptedException {
        if (secret == null || secret.length() < 5 || secret.contains(" ")) {
            return ERROR;
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
            resp = restTemplate.exchange(url, HttpMethod.POST, requestBodyFormUrlEncoded, Object.class);
        } catch (HttpServerErrorException e) {
            return ERROR;
        }

        if (resp.getStatusCode() == HttpStatus.OK) {
            Map<String, String> body = (Map<String, String>) resp.getBody();
            UUID r = UUID.randomUUID();
            tokensIssued.put(r, body.get("display_name"));
            log.info("Issued token {} to {}", r, body.get("display_name"));
            cache.put(secret, r);
            return r.toString();
        } else {
            return ERROR;
        }
    }


    /*
    account_id -> b65b3cb1-6a5f-4616-9f3e-33be293aeac3
    display_name -> sgt_bigbird
    token_time -> {Integer@13810} 1679699017
     */
}
