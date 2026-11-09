package example.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceDTO {
    private String name;
    private String query;
    
    @JsonProperty("duration_ms")
    private Integer durationMs;
}