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
import sgtbigbird.gcc.backend.model.*;
import sgtbigbird.gcc.backend.repository.DataPointRepository;
import sgtbigbird.gcc.backend.repository.MapTagRepository;
import sgtbigbird.gcc.backend.repository.RunKeyRepository;
import sgtbigbird.gcc.backend.service.AuthenticationService;

import javax.naming.AuthenticationException;
import java.util.*;

@RestController
@Slf4j
public class InController {
    @Autowired
    RunKeyRepository runKeyRepository;
    @Autowired
    DataPointRepository dataPointRepository;
    @Autowired
    MapTagRepository mapTagRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Value("${auth.url}")
    String url;
    @Value("${auth.secret}")
    String app_secret;
    @Autowired
    ObjectMapper objectMapper;
    Map<String, UUID> cache = new HashMap<>();
    
    @GetMapping("/runkey")
    public List<RunKey> getRunKeys(@RequestParam String token) throws AuthenticationException {
        authenticationService.authenticateUuid(token);
        return runKeyRepository.findAll();
    }
    @PostMapping("/runkey")
    public void addRunKey(@RequestBody RunKeyWrapper runKeyWrapper) throws AuthenticationException {
        authenticationService.authenticateUuid(runKeyWrapper.getToken());
        runKeyRepository.save(runKeyWrapper.getRunKey());
    }
    @GetMapping("/maptag")
    public List<MapTag> getRunTags(@RequestParam String token) throws AuthenticationException, JsonProcessingException {
        authenticationService.authenticateUuid(token);
        List<MapTag> ret = mapTagRepository.findAll();
        log.debug("Ret: {}", objectMapper.writeValueAsString(ret));
        return ret;
    }
    @PostMapping("/maptag")
    public void addMapTag(@RequestBody MapTagWrapper mapTagWrapper) throws AuthenticationException {
        authenticationService.authenticateUuid(mapTagWrapper.getToken());
        mapTagWrapper.getMapTag().setUsername(authenticationService.getUsername(mapTagWrapper.getToken()));
        mapTagRepository.save(mapTagWrapper.getMapTag());
    }
    @PostMapping("/datapoint")
    public void postPoints(@RequestBody DataPointWrapper inList) throws AuthenticationException {
        UUID u = authenticationService.authenticateUuid(inList.getToken());
        Optional<RunKey> runKey = runKeyRepository.findByRkId(inList.getRkId());

        if (runKey.isPresent()) {
            log.info("Persisting data for {}", authenticationService.getTokensIssued().get(u));
            inList.getData().forEach(
                    (ol) -> ol.forEach(li -> {
                        li.setUsername(authenticationService.getTokensIssued().get(u));
                        li.setRunKey(runKey.get());
                    })
            );

            inList.getData().forEach((l) -> dataPointRepository.saveAll(l));
        } else {
            log.warn("Couldn't find rkid {}!", inList.getRkId());
        }
    }

    @PostMapping("/auth")
    public String token(@RequestBody Map<String, String> authBody) throws AuthenticationException {
        if (!authBody.containsKey("secret")) {
            throw new AuthenticationException();
        }
        String secret = authBody.get("secret");
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
            resp = restTemplate.exchange(url, HttpMethod.POST, requestBodyFormUrlEncoded, Object.class);
        } catch (HttpServerErrorException e) {
            throw new AuthenticationException();
        }

        if (resp.getStatusCode() == HttpStatus.OK) {
            Map<String, String> body = (Map<String, String>) resp.getBody();
            UUID r = UUID.randomUUID();
            authenticationService.getTokensIssued().put(r, body.get("display_name"));
            log.info("Issued token {} to {}", r, body.get("display_name"));
            cache.put(secret, r);
            return r.toString();
        } else {
            throw new AuthenticationException();
        }
    }


    /*
    account_id -> b65b3cb1-6a5f-4616-9f3e-33be293aeac3
    display_name -> sgt_bigbird
    token_time -> {Integer@13810} 1679699017
     */
}
