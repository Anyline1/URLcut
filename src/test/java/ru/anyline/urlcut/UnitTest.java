package ru.anyline.urlcut;

import ru.anyline.urlcut.model.ShortenedUrl;
import ru.anyline.urlcut.repository.ShortenedUrlRepository;
import ru.anyline.urlcut.service.UrlShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UnitTest {

    @Mock
    private ShortenedUrlRepository shortenedUrlRepository;

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShortenUrl_WithCustomShortUrl_Success() {
        String originalUrl = "https://www.google.com";
        String customShortUrl = "abc123";

        when(shortenedUrlRepository.findByShortUrl(customShortUrl)).thenReturn(Optional.empty());
        when(shortenedUrlRepository.save(any(ShortenedUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = urlShortenerService.shortenUrl(originalUrl, customShortUrl);

        assertEquals("local/api/" + customShortUrl, result);
        verify(shortenedUrlRepository, times(1)).findByShortUrl(customShortUrl);
        verify(shortenedUrlRepository, times(1)).save(any(ShortenedUrl.class));
    }

    @Test
    public void testShortenUrl_WithCustomShortUrl_AlreadyExists() {
        String originalUrl = "https://www.google.com";
        String customShortUrl = "custom123";

        when(shortenedUrlRepository.findByShortUrl(customShortUrl)).thenReturn(Optional.of(new ShortenedUrl()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            urlShortenerService.shortenUrl(originalUrl, customShortUrl);
        });

        assertEquals("Custom short URL is already in use.", exception.getMessage());
        verify(shortenedUrlRepository, times(1)).findByShortUrl(customShortUrl);
        verify(shortenedUrlRepository, times(0)).save(any(ShortenedUrl.class));
    }

    @Test
    public void testShortenUrl_WithRandomShortUrl_Success() {
        String originalUrl = "https://www.google.com";
        String generatedShortUrl = "abc123";

        when(shortenedUrlRepository.findByShortUrl(anyString())).thenReturn(Optional.empty());
        when(shortenedUrlRepository.save(any(ShortenedUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = urlShortenerService.shortenUrl(originalUrl, null);

        assertTrue(result.startsWith("local/"));
        verify(shortenedUrlRepository, atLeastOnce()).findByShortUrl(anyString());
        verify(shortenedUrlRepository, times(1)).save(any(ShortenedUrl.class));
    }

    @Test
    public void testGetOriginalUrl_ValidShortUrl() {
        String shortUrl = "abc123";
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setShortUrl(shortUrl);
        shortenedUrl.setOriginalUrl("https://www.google.com");

        when(shortenedUrlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(shortenedUrl));

        String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        assertEquals("https://www.google.com", originalUrl);

        verify(shortenedUrlRepository, times(1)).findByShortUrl(shortUrl);
    }

    @Test
    public void testGetOriginalUrl_InvalidShortUrl() {
        String shortUrl = "invalid";

        when(shortenedUrlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

        String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        assertNull(originalUrl);

        verify(shortenedUrlRepository, times(1)).findByShortUrl(shortUrl);
    }
}
