package ru.anyline.urlcut;

import ru.anyline.urlcut.model.ShortenedUrl;
import ru.anyline.urlcut.repository.ShortenedUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.anyline.urlcut.service.UrlShortenerService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class MockTest {

    @Mock
    private ShortenedUrlRepository urlRepository;

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShortenUrl_WithExistingUrl() {
        String originalUrl = "https://www.google.com";
        ShortenedUrl existingUrl = new ShortenedUrl();
        existingUrl.setOriginalUrl(originalUrl);
        existingUrl.setShortUrl("abc123");

        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.of(existingUrl));

        String shortUrl = urlShortenerService.shortenUrl(originalUrl);
        assertEquals("http://localhost:8080/abc123", shortUrl);

        verify(urlRepository, times(1)).findByOriginalUrl(originalUrl);
        verify(urlRepository, times(0)).save(any());
    }

    @Test
    public void testGetOriginalUrl_WithValidShortUrl() {
        String shortUrl = "abc123";
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setShortUrl(shortUrl);
        shortenedUrl.setOriginalUrl("https://www.google.com");

        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(shortenedUrl));

        String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        assertEquals("https://www.google.com", originalUrl);

        verify(urlRepository, times(1)).findByShortUrl(shortUrl);
    }

    @Test
    public void testGetOriginalUrl_WithInvalidShortUrl() {
        String shortUrl = "invalid";

        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

        String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        assertNull(originalUrl);

        verify(urlRepository, times(1)).findByShortUrl(shortUrl);
    }
}

