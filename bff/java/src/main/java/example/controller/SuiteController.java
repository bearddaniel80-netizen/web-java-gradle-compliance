package example.controller;

import example.service.FastapiClient;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/suite")
public class SuiteController {
    private final FastapiClient fastapi;

    public SuiteController(FastapiClient fastapi) {
        this.fastapi = fastapi;
    }

    private static String segment(String value) {
        return UriUtils.encodePathSegment(value, StandardCharsets.UTF_8);
    }

    // Dynamic suite routes
    @GetMapping("/{kind}/{filename}")
    public ResponseEntity<Object> suiteKind(@PathVariable String kind, @PathVariable String filename) {
        return fastapi.get("/api/suite/" + segment(kind) + "/" + segment(filename));
    }

    @GetMapping("/failfast/query/{filename}")
    public ResponseEntity<Object> suiteFailfastQuery(@PathVariable String filename) {
        return fastapi.get("/api/suite/failfast/query/" + segment(filename));
    }

    @GetMapping("/retry/{times}/{filename}")
    public ResponseEntity<Object> suiteRetry(@PathVariable String times, @PathVariable String filename) {
        return fastapi.get("/api/suite/retry/" + segment(times) + "/" + segment(filename));
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Object> suiteDefault(@PathVariable String filename) {
        return fastapi.get("/api/suite/" + segment(filename));
    }

    @GetMapping("/list")
    public ResponseEntity<Object> suiteList() {
        return fastapi.get("/api/suite/list");
    }
}
