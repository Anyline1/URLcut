package ru.anyline.urlcut;

import ru.anyline.urlcut.model.ShortenedUrl;
import ru.anyline.urlcut.repository.ShortenedUrlRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShortenedUrlRepository urlRepository;

    @Test
    public void testShortenUrl_Integration() throws Exception {
        String originalUrl = "https://www.anydesc.com";

        mockMvc.perform(post("/api/shorten")
                        .param("url", originalUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("http://localhost:8080/")));

        ShortenedUrl shortenedUrl = urlRepository.findByOriginalUrl(originalUrl).orElse(null);
        assert shortenedUrl != null;
        assertEquals(originalUrl, shortenedUrl.getOriginalUrl());

    }

    @Test
    public void testShortenUrl_InvalidUrl() throws Exception {
        String invalidUrl = "invalid_url";

        mockMvc.perform(post("/api/shorten")
                        .param("url", invalidUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRedirectUrl_Integration() throws Exception {
        String originalUrl = "https://www.google.com";
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setOriginalUrl(originalUrl);
        shortenedUrl.setShortUrl("abc123");
        urlRepository.save(shortenedUrl);

        mockMvc.perform(get("/api/abc123"))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.header().string("Location", originalUrl));
    }

    @Test
    public void testRedirectUrl_NotFound() throws Exception {
        mockMvc.perform(get("/nonexistent"))
                .andExpect(status().isNotFound());
    }
}

