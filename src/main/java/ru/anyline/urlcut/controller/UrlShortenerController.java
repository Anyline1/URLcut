package ru.anyline.urlcut.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.URL;
import ru.anyline.urlcut.model.ShortenedUrl;
import ru.anyline.urlcut.service.UrlCacheServiceImpl;
import ru.anyline.urlcut.service.UrlShortenerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "URL Shortener", description = "API для сокращения и управления URL")
@AllArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerServiceImpl urlShortenerServiceImpl;
    private final UrlCacheServiceImpl urlCacheServiceImpl;

    @PostMapping("/shorten")
    @Operation(summary = "Создание короткого URL", description = "Создает короткий URL для указанного оригинального URL.")
    public ResponseEntity<String> shortenUrl(@RequestParam String url) {

        String shortUrl = urlCacheServiceImpl.hasKey(url) ? urlCacheServiceImpl.getUrlFromCache(url): urlShortenerServiceImpl.shortenUrl(url);
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrl);
    }

    @PostMapping("/custom")
    @Operation(summary = "Создание своего короткого URL", description = "Создает короткий URL для указанного оригинального URL. Пользователь может указать собственный короткий URL.")
    public ResponseEntity<String> customUrl(
            @RequestParam @Valid @NotBlank @URL(message = "Invalid URL format") String url,
            @RequestParam(value = "customUrl") String customUrl
    ) {
        String shortUrl = urlShortenerServiceImpl.customUrl(url, customUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(shortUrl);
    }

    @GetMapping("/{shortUrl}")
    @Operation(summary = "Редирект по короткому URL", description = "Перенаправляет на оригинальный URL, связанный с указанным коротким URL.")
    public ResponseEntity<Void> redirectUrl(@PathVariable String shortUrl) {
        String originalUrl = urlShortenerServiceImpl.getOriginalUrl(shortUrl);
        if (originalUrl != null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", originalUrl)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/update")
    @Operation(summary = "Обновление короткого URL", description = "Обновляет существующий короткий URL, связывая его с новым коротким URL.")
    public ResponseEntity<String> updateShortUrl(@RequestParam String originalUrl,
                                                 @RequestParam String newShortUrl
    ) {
        try {
            String updatedShortUrl = urlShortenerServiceImpl.updateShortUrl(originalUrl, newShortUrl);
            return new ResponseEntity<>(updatedShortUrl, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/repo")
    @Operation(summary = "Отображение всех URL", description = "Возвращает все существующие короткие URL, с привязкой к основным URL.")
    public List<ShortenedUrl> getAllRepos(){
        return urlShortenerServiceImpl.getAllRepos();
    }
}

