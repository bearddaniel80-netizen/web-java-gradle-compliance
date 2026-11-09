package example.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO {
    private String name;
    private String query;
    private Boolean skipped;
    private Object error;
    
    @JsonProperty("duration_ms")
    private Integer durationMs;
}