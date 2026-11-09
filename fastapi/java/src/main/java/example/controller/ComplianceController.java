package example.controller;

import example.dto.ComplianceDTO;
import example.security.Secrets;
import example.service.ProcessRunner;
import example.service.ReportService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/{folder:manifest|suite|tag}")
public class ComplianceController {
    private static final String H = "x-internal-key";
    private static final Pattern SAFE_NAME = Pattern.compile("[A-Za-z0-9][A-Za-z0-9._-]*");

    private final Secrets secrets;
    private final ProcessRunner runner;
    private final ReportService reports;

    public ComplianceController(Secrets secrets, ProcessRunner runner, ReportService reports) {
        this.secrets = secrets;
        this.runner = runner;
        this.reports = reports;
    }

    private List<String> cmd(String folder, String filename, String... extra) {
        if (!SAFE_NAME.matcher(filename).matches())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filename");
        var c = new java.util.ArrayList<>(List.of("compliance", folder, "run", filename));
        c.addAll(Arrays.asList(extra));
        return c;
    }

    private ResponseEntity<String> html(String body) {
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(body);
    }

    // ---- list ----
    @GetMapping("/list")
    public java.util.Map<String, List<String>> list(@PathVariable String folder,
                                                    @RequestHeader(value = H, required = false) String key) {
        secrets.verifyInternalKey(key);
        String out = runner.run(List.of("compliance", folder, "list"));
        List<String> lines = Arrays.stream(out.split("\n+")).filter(s -> !s.isEmpty()).toList();
        return java.util.Map.of("result", lines);
    }

    // ---- formats ----
    @GetMapping("/github/{filename}")
    public ResponseEntity<String> github(@PathVariable String folder, @PathVariable String filename,
                                         @RequestHeader(value = H, required = false) String key) {
        secrets.verifyInternalKey(key);
        return html(reports.renderGithub(cmd(folder, filename, "--format", "github")));
    }

    @GetMapping("/html/{filename}")
    public ResponseEntity<String> html(@PathVariable String folder, @PathVariable String filename,
                                       @RequestHeader(value = H, required = false) String key) {
        secrets.verifyInternalKey(key);
        return html(reports.renderHtml(cmd(folder, filename, "--format", "html")));
    }

    @GetMapping("/markdown/{filename}")
    public ResponseEntity<String> markdown(@PathVariable String folder, @PathVariable String filename,
                                           @RequestHeader(value = H, required = false) String key) {
        secrets.verifyInternalKey(key);
        return html(reports.renderMarkdown(cmd(folder, filename, "--format", "md")));
    }

    @GetMapping("/pdf/{filename}")
    public ResponseEntity<String> pdf(@PathVariable String folder, @PathVariable String filename,
                                      @RequestHeader(value = H, required = false) String key) {
        secrets.verifyInternalKey(key);
        return html(reports.renderPdf(cmd(folder, filename, "--format", "pdf")));
    }

    // ---- features ----
    @GetMapping("/cache/{filename}")
    public ComplianceDTO cache(@PathVariable String folder, @PathVariable String filename,
                               @RequestHeader(value = H, required = false) String key) {
        secrets.verifyInternalKey(key);
        runner.run(cmd(folder, filename));
        return reports.renderJson(cmd(folder, filename, "--cache", "--format", "json"));
    }

    @GetMapping("/failfast/{filename}")
    public ComplianceDTO failfast(@PathVariable String folder, @PathVariable String filename,
                                  @RequestHeader(value = H, required = false) String key) {
        secrets.verifyInternalKey(key);
        return reports.renderJson(cmd(folder, filename, "--fail-fast", "--format", "json"));
    }

    @GetMapping("/failfast/query/{filename}")
    public ComplianceDTO failfastQuery(@PathVariable String folder, @PathVariable String filename,
                                       @RequestHeader(value = H, required = false) String key) {
        secrets.verifyInternalKey(key);
        return reports.renderJson(cmd(folder, filename, "--fail-fast-query", "--format", "json"));
    }

    @GetMapping("/profile/{filename}")
    public ComplianceDTO profile(@PathVariable String folder, @PathVariable String filename,
                                 @RequestHeader(value = H, required = false) String key) {
        secrets.verifyInternalKey(key);
        return reports.renderJson(cmd(folder, filename, "--profile", "--format", "json"));
    }

    @GetMapping("/retry/{times}/{filename}")
    public ComplianceDTO retry(@PathVariable String folder, @PathVariable String times,
                               @PathVariable String filename,
                               @RequestHeader(value = H, required = false) String key) {
        secrets.verifyInternalKey(key);
        return reports.renderJson(cmd(folder, filename, "--retry", times, "--format", "json"));
    }

    @GetMapping("/{filename}")
    public ComplianceDTO run(@PathVariable String folder, @PathVariable String filename,
                             @RequestHeader(value = H, required = false) String key) {
        secrets.verifyInternalKey(key);
        return reports.renderJson(cmd(folder, filename, "--format", "json"));
    }
}