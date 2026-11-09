package example.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDTO {
    private Integer total;
    private Integer passed;
    private Integer skipped;
    private Integer failed;

    @JsonProperty("duration_ms")
    private Integer durationMs;

    @JsonProperty("pass_rate")
    private Float passRate;
}