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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/out")
@Slf4j
public class OutController {
    @Autowired
    MapTagRepository mapTagRepository;
    @Autowired
    RunKeyRepository runKeyRepository;
    @Autowired
    PublishingService publishingService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/data/runkey/verbose")
    public List<DataPoint> getDataPointsByRunkey_V(@RequestParam String runkey) throws MapTagNotFoundException {
        return publishingService.getDataPointsByRunkey(runkey);
    }

    @GetMapping("/data/tag/verbose")
    public List<DataPoint> getDataPointsByTag_V(@RequestParam String tag) throws MapTagNotFoundException {
        return publishingService.getDataPointsByTag(tag);
    }

    @GetMapping("/data/runkey")
    public List<DataPointSmall> getDataPointsByRunkey(@RequestParam String runkey) throws MapTagNotFoundException {
        return publishingService.getDataPointsByRunkey(runkey)
                .stream().map((DataPointSmall::new)).collect(Collectors.toList());
    }

    @GetMapping("/data/tag")
    public List<DataPointSmall> getDataPointsByTag(@RequestParam String tag) throws MapTagNotFoundException {
        return publishingService.getDataPointsByTag(tag)
                .stream().map((DataPointSmall::new)).collect(Collectors.toList());
    }

    @GetMapping("/all/verbose")
    public List<DataPoint> getAllDataPoints_V() {
        return publishingService.getAllDataPoints();
    }
    @GetMapping("/all")
    public List<DataPointSmall> getAllDataPoints() {
        return publishingService.getAllDataPoints()
                .stream().map((DataPointSmall::new)).collect(Collectors.toList());
    }
    @GetMapping("/maptag")
    public List<MapTag> getRunTags() throws AuthenticationException, JsonProcessingException {
        List<MapTag> ret = mapTagRepository.findAll();
        log.debug("Ret: {}", objectMapper.writeValueAsString(ret));
        return ret;
    }

    @GetMapping("/runkey")
    public List<RunKey> getRunKeys() throws AuthenticationException, JsonProcessingException {
        List<RunKey> ret = runKeyRepository.findAll();
        log.debug("Ret: {}", objectMapper.writeValueAsString(ret));
        return ret;
    }



    /*
    account_id -> b65b3cb1-6a5f-4616-9f3e-33be293aeac3
    display_name -> sgt_bigbird
    token_time -> {Integer@13810} 1679699017
     */
}
