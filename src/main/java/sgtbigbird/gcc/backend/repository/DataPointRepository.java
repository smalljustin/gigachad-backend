package sgtbigbird.gcc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sgtbigbird.gcc.backend.model.DataPoint;

import java.util.List;


public interface DataPointRepository extends JpaRepository<DataPoint, DataPoint> {
    List<DataPoint> findByMapUuid(String mapUuid);

}
