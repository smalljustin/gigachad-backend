package sgtbigbird.gcc.backend.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
@JsonDeserialize
public class MapTagWrapper {
    MapTag mapTag;
    String token;
}
