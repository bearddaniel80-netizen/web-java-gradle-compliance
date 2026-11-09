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
@RequestMapping("/api/manifest")
public class ManifestController {
    private final FastapiClient fastapi;

    public ManifestController(FastapiClient fastapi) {
        this.fastapi = fastapi;
    }

    private static String segment(String value) {
        return UriUtils.encodePathSegment(value, StandardCharsets.UTF_8);
    }

    // Dynamic manifest routes
    @GetMapping("/{kind}/{filename}")
    public ResponseEntity<Object> manifestKind(@PathVariable String kind, @PathVariable String filename) {
        return fastapi.get("/api/manifest/" + segment(kind) + "/" + segment(filename));
    }

    @GetMapping("/failfast/query/{filename}")
    public ResponseEntity<Object> manifestFailfastQuery(@PathVariable String filename) {
        return fastapi.get("/api/manifest/failfast/query/" + segment(filename));
    }

    @GetMapping("/retry/{times}/{filename}")
    public ResponseEntity<Object> manifestRetry(@PathVariable String times, @PathVariable String filename) {
        return fastapi.get("/api/manifest/retry/" + segment(times) + "/" + segment(filename));
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Object> manifestDefault(@PathVariable String filename) {
        return fastapi.get("/api/manifest/" + segment(filename));
    }

    @GetMapping("/list")
    public ResponseEntity<Object> manifestList() {
        return fastapi.get("/api/manifest/list");
    }

}

    