package example.service;

import com.fasterxml.jackson.databind.JsonNode;
import example.dto.common.ColumnDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ColumnNormalizer {

    public List<ColumnDTO> columnsFor(JsonNode node) {
        JsonNode sample = (node != null && node.isArray())
                ? (node.isEmpty() ? null : node.get(0))
                : node;
        List<ColumnDTO> columns = new ArrayList<>();
        if (sample == null || !sample.isObject()) return columns;
        sample.fields().forEachRemaining(e ->
                columns.add(new ColumnDTO(e.getKey(), title(e.getKey()), typeOf(e.getValue()))));
        return columns;
    }

    private String typeOf(JsonNode v) {
        if (v.isBoolean()) return "boolean";
        if (v.isNumber()) return "number";
        if (v.isTextual()) return "string";
        return "unknown";
    }

    /** Mirrors Python's str.title(): "duration_ms" -> "Duration_Ms". */
    private String title(String s) {
        StringBuilder sb = new StringBuilder();
        boolean prevLetter = false;
        for (char c : s.toCharArray()) {
            sb.append(prevLetter ? Character.toLowerCase(c) : Character.toUpperCase(c));
            prevLetter = Character.isLetter(c);
        }
        return sb.toString();
    }
}