package example.dto.common;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailureDTO {
    private String name;
    private String query;
    private Object error;
}