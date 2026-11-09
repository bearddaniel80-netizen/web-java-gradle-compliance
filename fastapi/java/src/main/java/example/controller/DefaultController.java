package example.controller;

import example.security.Secrets;
import example.service.ReportService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DefaultController {
    private final Secrets secrets;
    private final ReportService reports;

    public DefaultController(Secrets secrets, ReportService reports) {
        this.secrets = secrets;
        this.reports = reports;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @GetMapping("/api/data")
    public Map<String, String> data(@RequestHeader(value = "x-internal-key", required = false) String key) {
        secrets.verifyInternalKey(key);
        return Map.of("message", "Hello from Spring");
    }

    @GetMapping("/api/index")
    public ResponseEntity<String> index(@RequestHeader(value = "x-internal-key", required = false) String key) {
        secrets.verifyInternalKey(key);
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(reports.readmeHtml());
    }
}