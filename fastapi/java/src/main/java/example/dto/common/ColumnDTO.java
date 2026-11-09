package example.dto.common;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDTO {
    private String key;
    private String label;
    private String type;
}