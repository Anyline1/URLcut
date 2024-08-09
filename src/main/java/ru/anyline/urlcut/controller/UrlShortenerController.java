package ru.anyline.urlcut.controller;

import ru.anyline.urlcut.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestParam String url) {
        String shortUrl = urlShortenerService.shortenUrl(url);
        return new ResponseEntity<>(shortUrl, HttpStatus.CREATED);
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
}

