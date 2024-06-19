package sgtbigbird.gcc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sgtbigbird.gcc.backend.model.RunKey;

import java.util.Optional;

public interface RunKeyRepository extends JpaRepository<RunKey, RunKey> {
    Optional<RunKey> findByRkId(int rkId);
    Optional<RunKey> findByName(String name);
}
