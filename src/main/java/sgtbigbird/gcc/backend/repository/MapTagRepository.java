package sgtbigbird.gcc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sgtbigbird.gcc.backend.model.DataPoint;
import sgtbigbird.gcc.backend.model.MapTag;
import sgtbigbird.gcc.backend.model.RunKey;

import java.util.List;
import java.util.Optional;

public interface MapTagRepository extends JpaRepository<MapTag, MapTag> {
    Optional<MapTag> findByMtId(int mtId);
    List<MapTag> findByTag(String tag);

}
