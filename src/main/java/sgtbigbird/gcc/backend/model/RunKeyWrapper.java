package sgtbigbird.gcc.backend.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@Data
@JsonDeserialize
public class RunKeyWrapper {
    RunKey runKey;
    String token;
}
