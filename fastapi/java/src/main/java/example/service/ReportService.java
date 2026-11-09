package example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.dto.ComplianceDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class ReportService {
    private final ProcessRunner runner;
    private final ColumnNormalizer normalizer;
    private final ObjectMapper mapper;

    public ReportService(ProcessRunner runner, ColumnNormalizer normalizer, ObjectMapper mapper) {
        this.runner = runner;
        this.normalizer = normalizer;
        this.mapper = mapper;
    }

    private static Path cwd(String name) {
        return Path.of(System.getProperty("user.dir"), name);
    }

    private static boolean hasItems(JsonNode n) {
        return n != null && n.isArray() && !n.isEmpty();
    }

    private static String read(Path p) {
        try {
            return Files.readString(p);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void markdownToHtml(String in, String out) {
        runner.run(List.of("pandoc", "-f", "markdown-yaml_metadata_block", in, "-o", out));
    }

    public ComplianceDTO renderJson(List<String> command) {
        runner.run(command);
        try {
            JsonNode raw = mapper.readTree(cwd("report.json").toFile());
            ComplianceDTO dto = mapper.treeToValue(raw, ComplianceDTO.class);

            dto.setSummaryColumns(normalizer.columnsFor(raw.get("summary")));
            dto.setResultsColumns(normalizer.columnsFor(raw.get("results")));
            if (hasItems(raw.get("failures")))
                dto.setFailuresColumns(normalizer.columnsFor(raw.get("failures")));
            if (hasItems(raw.get("query_performance")))
                dto.setPerformanceColumns(normalizer.columnsFor(raw.get("query_performance")));
            return dto;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String renderGithub(List<String> command) {
        return renderMarkdownFile("github-report", command);
    }

    public String renderMarkdown(List<String> command) {
        return renderMarkdownFile("report", command);
    }

    private String renderMarkdownFile(String name, List<String> command) {
        String in = cwd(name + ".md").toString();
        String out = cwd(name + ".html").toString();
        runner.run(command);
        markdownToHtml(in, out);
        return read(Path.of(out));
    }

    public String renderHtml(List<String> command) {
        runner.run(command);
        return read(cwd("index.html"));
    }

    public String renderPdf(List<String> command) {
        String in = cwd("report.pdf").toString();
        String out = cwd("report.html").toString();
        runner.run(command);
        runner.run(List.of("pdftohtml", in, out));
        return read(Path.of(out));
    }

    public String readmeHtml() {
        String in = cwd("README.md").toString();
        String out = cwd("README.html").toString();
        markdownToHtml(in, out);
        return read(Path.of(out));
    }
}