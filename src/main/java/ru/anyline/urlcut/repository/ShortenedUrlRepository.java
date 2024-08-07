package ru.anyline.urlcut.repository;

import ru.anyline.urlcut.model.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {
    Optional<ShortenedUrl> findByShortUrl(String shortUrl);
    Optional<ShortenedUrl> findByOriginalUrl(String originalUrl);
}
