package sgtbigbird.gcc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sgtbigbird.gcc.backend.model.DataPoint;

import javax.xml.crypto.Data;
import java.util.List;


public interface DataPointRepository extends JpaRepository<DataPoint, DataPoint> {
    List<DataPoint> findByMapUuid(String mapUuid);
    List<DataPoint> findByRunKey_rkId(int rkId);
    @Query("SELECT MAX(dpId) FROM DataPoint ")
    Integer findMaxDpId();
}
