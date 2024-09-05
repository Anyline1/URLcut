package ru.anyline.urlcut.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ShortenedUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private String customUrl;

}

