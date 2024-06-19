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
import sgtbigbird.gcc.backend.exception.MapTagNotFoundException;
import sgtbigbird.gcc.backend.model.*;
import sgtbigbird.gcc.backend.repository.DataPointRepository;
import sgtbigbird.gcc.backend.repository.MapTagRepository;
import sgtbigbird.gcc.backend.repository.RunKeyRepository;
import sgtbigbird.gcc.backend.service.AuthenticationService;
import sgtbigbird.gcc.backend.service.PublishingService;

import javax.naming.AuthenticationException;
import java.util.*;

@RestController
@RequestMapping("/out")
@Slf4j
public class OutController {
    @Autowired
    PublishingService publishingService;
    @GetMapping("/tag")
    public List<DataPoint> getDataPointsByTag(@RequestParam String tag) throws MapTagNotFoundException {
        return publishingService.getDataPointsByTag(tag);
    }

    @GetMapping("/all")
    public List<DataPoint> getAllDataPoints() {
        return publishingService.getAllDataPoints();
    }



    /*
    account_id -> b65b3cb1-6a5f-4616-9f3e-33be293aeac3
    display_name -> sgt_bigbird
    token_time -> {Integer@13810} 1679699017
     */
}
