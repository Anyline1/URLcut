package ru.anyline.urlcut;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.anyline.urlcut.controller.UrlShortenerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.anyline.urlcut.service.UrlShortenerServiceImpl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MockTest {


    @Mock
    private UrlShortenerServiceImpl urlShortenerServiceImpl;
    @InjectMocks
    private UrlShortenerController urlShortenerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShortenUrl_WithExistingOriginalUrl_ReturnsExistingShortUrl() {
        String originalUrl = "https://www.google.com";
        String existingShortUrl = "abc123";

        when(urlShortenerServiceImpl.shortenUrl(originalUrl))
                .thenReturn("local/api/" + existingShortUrl);

        ResponseEntity<String> response = urlShortenerController.shortenUrl(originalUrl);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("local/api/" + existingShortUrl, response.getBody());
        verify(urlShortenerServiceImpl, times(1)).shortenUrl(originalUrl);
    }

    @Test
    public void testShortenUrl_WithCustomShortUrl() {
        String originalUrl = "https://www.google.com";
        String customShortUrl = "custom123";

        when(urlShortenerServiceImpl.customUrl(originalUrl, customShortUrl))
                .thenReturn("local/api/" + customShortUrl);

        ResponseEntity<String> response = urlShortenerController.customUrl(originalUrl, customShortUrl);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("local/api/" + customShortUrl, response.getBody());
        verify(urlShortenerServiceImpl, times(1)).customUrl(originalUrl, customShortUrl);
    }

    @Test
    public void testShortenUrl_WithRandomShortUrl() {
        String originalUrl = "https://www.google.com";
        String generatedShortUrl = "abc123";

        when(urlShortenerServiceImpl.shortenUrl(originalUrl))
                .thenReturn("local/api/" + generatedShortUrl);

        ResponseEntity<String> response = urlShortenerController.shortenUrl(originalUrl);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("local/api/" + generatedShortUrl, response.getBody());
        verify(urlShortenerServiceImpl, times(1)).shortenUrl(originalUrl);
    }



}

