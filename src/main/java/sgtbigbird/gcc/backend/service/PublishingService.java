package sgtbigbird.gcc.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sgtbigbird.gcc.backend.exception.MapTagNotFoundException;
import sgtbigbird.gcc.backend.model.DataPoint;
import sgtbigbird.gcc.backend.model.MapTag;
import sgtbigbird.gcc.backend.model.RunKey;
import sgtbigbird.gcc.backend.repository.DataPointRepository;
import sgtbigbird.gcc.backend.repository.MapTagRepository;
import sgtbigbird.gcc.backend.repository.RunKeyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    Map<String, List<DataPoint>> dataPointCacheByTag = new HashMap<>();
    Map<String, List<DataPoint>> dataPointCacheByRunkey = new HashMap<>();
    List<DataPoint> allDataPointsCache;
    int maxDpId = 0;

    private void cacheCheck() {
        int curDpId = dataPointRepository.findMaxDpId();
        if (curDpId > maxDpId) {
            dataPointCacheByTag = new HashMap<>();
            dataPointCacheByRunkey = new HashMap<>();
            allDataPointsCache = null;

        }
    }
    public List<DataPoint> getDataPointsByRunkey(String runkey) throws MapTagNotFoundException {
        cacheCheck();
        Optional<RunKey> runKeyOptional = runKeyRepository.findByName(runkey);

        if (runKeyOptional.isEmpty()) {
            throw new MapTagNotFoundException("No matching runkey found for tag: " + runkey);
        }

        assert dataPointCacheByRunkey != null;
        if (dataPointCacheByRunkey.containsKey(runkey)) {
            return dataPointCacheByRunkey.get(runkey);
        }

        List<DataPoint> fetchedList = dataPointRepository.findByRunKey_rkId(runKeyOptional.get().getRkId());

        dataPointCacheByTag.put(runkey, fetchedList);
        return fetchedList;
    }

    public List<DataPoint> getDataPointsByTag(String tag) throws MapTagNotFoundException {
        cacheCheck();
        List<MapTag> mapTagList = mapTagRepository.findByTag(tag);
        if (mapTagList.isEmpty()) {
            throw new MapTagNotFoundException("No matching maps found for tag: " + tag);
        }

        assert dataPointCacheByTag != null;
        if (dataPointCacheByTag.containsKey(tag)) {
            return dataPointCacheByTag.get(tag);
        }
        log.info("Cache miss; calculating...");
        List<DataPoint> fetchedList = mapTagList.stream()
                .map((mapTag -> dataPointRepository.findByMapUuid(mapTag.getMapUuid())))
                .flatMap(List::stream)
                .toList();

        dataPointCacheByTag.put(tag, fetchedList);
        return fetchedList;
    }

    public List<DataPoint> getAllDataPoints() {
        cacheCheck();
        if (allDataPointsCache != null) {
            return allDataPointsCache;
        }
        return dataPointRepository.findAll();
    }

}
