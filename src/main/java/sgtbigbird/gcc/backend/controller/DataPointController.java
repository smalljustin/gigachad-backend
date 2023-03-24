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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sgtbigbird.gcc.backend.model.DataPoint;
import sgtbigbird.gcc.backend.repository.DataPointRepository;

import java.net.URI;
import java.util.*;

@RestController
@Slf4j
public class DataPointController {
    @Autowired
    DataPointRepository dataPointRepository;

    RestTemplate restTemplate = new RestTemplate();

    @Value("${auth.url}")
    String url;

    @Value("${auth.secret}")
    String app_secret;

    @Autowired
    ObjectMapper objectMapper;

    Map<UUID, String> tokensIssued = new HashMap<>();

    @PostMapping("/datapoint")
    public void postPoints(@RequestBody List<List< DataPoint >> inList) {
        inList.forEach((l) -> dataPointRepository.saveAll(l));
    }

    @GetMapping("/auth")
    public String token(@RequestParam String secret) throws JsonProcessingException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("token", secret);
        map.add("secret", app_secret);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("User-Agent", "sergeant-bigbird#5050");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestBodyFormUrlEncoded = new HttpEntity<>(map, headers);
        ResponseEntity resp = restTemplate.exchange(url, HttpMethod.POST, requestBodyFormUrlEncoded, Object.class);
        if (resp.getStatusCode() == HttpStatus.OK) {
            Map<String, String> body = (Map<String, String>) resp.getBody();
            UUID r = UUID.randomUUID();
            tokensIssued.put(r, body.get("display_name"));
            log.info("Issued token {} to {}", r, body.get("display_name"));
            return r.toString();
        } else {
            return "ERROR";
        }
    }
    /*
    account_id -> b65b3cb1-6a5f-4616-9f3e-33be293aeac3
    display_name -> sgt_bigbird
    token_time -> {Integer@13810} 1679699017
     */
}
