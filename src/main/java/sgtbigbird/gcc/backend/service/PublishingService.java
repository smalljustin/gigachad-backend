package sgtbigbird.gcc.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sgtbigbird.gcc.backend.exception.MapTagNotFoundException;
import sgtbigbird.gcc.backend.model.DataPoint;
import sgtbigbird.gcc.backend.model.MapTag;
import sgtbigbird.gcc.backend.repository.DataPointRepository;
import sgtbigbird.gcc.backend.repository.MapTagRepository;
import sgtbigbird.gcc.backend.repository.RunKeyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PublishingService {
    @Autowired
    RunKeyRepository runKeyRepository;
    @Autowired
    DataPointRepository dataPointRepository;
    @Autowired
    MapTagRepository mapTagRepository;
    @Autowired
    AuthenticationService authenticationService;
    public List<DataPoint> getDataPointsByTag(String tag) throws MapTagNotFoundException {
        List<MapTag> mapTagList = mapTagRepository.findByTag(tag);
        if (mapTagList.isEmpty()) {
            throw new MapTagNotFoundException("No matching maps found for tag: " + tag);
        }
        return mapTagList.stream()
                .map((mapTag -> dataPointRepository.findByMapUuid(mapTag.getMapUuid())))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<DataPoint> getAllDataPoints() {
        return dataPointRepository.findAll();
    }

}
