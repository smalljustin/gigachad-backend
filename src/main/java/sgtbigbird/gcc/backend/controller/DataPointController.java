package sgtbigbird.gcc.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sgtbigbird.gcc.backend.model.DataPoint;
import sgtbigbird.gcc.backend.repository.DataPointRepository;

import java.util.List;

@RestController
public class DataPointController {
    @Autowired
    DataPointRepository dataPointRepository;

    @PostMapping("/datapoint")
    public void postPoints(@RequestBody List<List< DataPoint >> inList) {
        inList.forEach((l) -> dataPointRepository.saveAll(l));
    }
}
