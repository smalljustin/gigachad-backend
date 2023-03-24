package sgtbigbird.gcc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sgtbigbird.gcc.backend.model.DataPoint;

import javax.xml.crypto.Data;

public interface DataPointRepository extends JpaRepository<DataPoint, DataPoint> {

}
