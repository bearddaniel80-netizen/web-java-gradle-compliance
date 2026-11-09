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
@RequestMapping("/api/tag")
public class TagController {
    private final FastapiClient fastapi;

    public TagController(FastapiClient fastapi) {
        this.fastapi = fastapi;
    }

    private static String segment(String value) {
        return UriUtils.encodePathSegment(value, StandardCharsets.UTF_8);
    }

    // Dynamic tag routes
    @GetMapping("/{kind}/{filename}")
    public ResponseEntity<Object> tagKind(@PathVariable String kind, @PathVariable String filename) {
        return fastapi.get("/api/tag/" + segment(kind) + "/" + segment(filename));
    }

    @GetMapping("/failfast/query/{filename}")
    public ResponseEntity<Object> tagFailfastQuery(@PathVariable String filename) {
        return fastapi.get("/api/tag/failfast/query/" + segment(filename));
    }

    @GetMapping("/retry/{times}/{filename}")
    public ResponseEntity<Object> tagRetry(@PathVariable String times, @PathVariable String filename) {
        return fastapi.get("/api/tag/retry/" + segment(times) + "/" + segment(filename));
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Object> tagDefault(@PathVariable String filename) {
        return fastapi.get("/api/tag/" + segment(filename));
    }

    @GetMapping("/list")
    public ResponseEntity<Object> tagList() {
        return fastapi.get("/api/tag/list");
    }

}
