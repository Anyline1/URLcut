package ru.anyline.urlcut.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import ru.anyline.urlcut.model.ShortenedUrl;
import ru.anyline.urlcut.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(
            @RequestParam @Valid @NotBlank @URL(message = "Invalid URL format") String url,
            @RequestParam(value = "customUrl", required = false) String customUrl) {
        String shortUrl = urlShortenerService.shortenUrl(url, customUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrl);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectUrl(@PathVariable String shortUrl) {
        String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        if (originalUrl != null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", originalUrl)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateShortUrl(@RequestParam String originalUrl,
                                                 @RequestParam String newShortUrl) {
        try {
            String updatedShortUrl = urlShortenerService.updateShortUrl(originalUrl, newShortUrl);
            return new ResponseEntity<>(updatedShortUrl, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/repo")
    public List<ShortenedUrl> getAllRepos(){
        return urlShortenerService.getAllRepos();
    }
}

