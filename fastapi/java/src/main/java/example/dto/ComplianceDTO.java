package example.dto;

import example.dto.common.ColumnDTO;
import example.dto.common.FailureDTO;
import example.dto.common.PerformanceDTO;
import example.dto.common.ResultDTO;
import example.dto.common.SummaryDTO;

import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceDTO {
    private SummaryDTO summary;
    private List<ResultDTO> results;
    private List<FailureDTO> failures;

    @JsonProperty("query_performance")
    private List<PerformanceDTO> performance;

    @JsonProperty("summary_columns")
    private List<ColumnDTO> summaryColumns;

    @JsonProperty("results_columns")
    private List<ColumnDTO> resultsColumns;

    @JsonProperty("failures_columns")
    private List<ColumnDTO> failuresColumns;

    @JsonProperty("query_performance_columns")
    private List<ColumnDTO> performanceColumns;
}