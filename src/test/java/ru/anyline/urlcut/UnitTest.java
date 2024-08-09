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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UnitTest {

    @Mock
    private ShortenedUrlRepository urlRepository;

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShortenUrl_NewUrl() {
        String originalUrl = "https://www.google.com";
        String shortUrl = "abc123";

        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.empty());
        when(urlRepository.findByShortUrl(any(String.class))).thenReturn(Optional.empty());

        when(urlRepository.save(any(ShortenedUrl.class))).thenAnswer(invocation -> {
            ShortenedUrl url = invocation.getArgument(0);
            url.setId(1L);
            url.setShortUrl(shortUrl);
            return url;
        });

        String result = urlShortenerService.shortenUrl(originalUrl);
        assertEquals("http://localhost:8080/" + shortUrl, result);

        verify(urlRepository, times(1)).findByOriginalUrl(originalUrl);
        verify(urlRepository, times(1)).save(any(ShortenedUrl.class));
    }

    @Test
    public void testGetOriginalUrl_ValidShortUrl() {
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
    public void testGetOriginalUrl_InvalidShortUrl() {
        String shortUrl = "invalid";

        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

        String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        assertNull(originalUrl);

        verify(urlRepository, times(1)).findByShortUrl(shortUrl);
    }
}
