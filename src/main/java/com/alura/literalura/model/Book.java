package com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InformacoesDeAtores> authors;
    @Enumerated(EnumType.STRING)
    private Languages languages;
    private Double downloads;
    
    public Book () {
    }
    
    public Book ( List<InformacoesDeLivro> results ) {
    }
    
    public Book ( String title, List<String> languages, Double downloads, List<Autores> authors ) {
        this.title = title;
        this.languages = Languages.fromString(languages.get(0));
        this.downloads = downloads;
        this.authors = new ArrayList<>();
        for ( Autores authorInfo : authors ) {
            InformacoesDeAtores author = new InformacoesDeAtores(authorInfo.name(), authorInfo.birthYear(), authorInfo.deathYear(), this);
            this.authors.add(author);
        }
    }
    
    
    public Long getId () {
        return id;
    }
    
    public void setId ( Long id ) {
        this.id = id;
    }
    
    public Double getDownloads () {
        return downloads;
    }
    
    public void setDownloads ( Double downloads ) {
        this.downloads = downloads;
    }
    
    public Languages getLanguages () {
        return languages;
    }
    
    public void setLanguages ( Languages languages ) {
        this.languages = languages;
    }
    
    public String getTitle () {
        return title;
    }
    
    public void setTitle ( String title ) {
        this.title = title;
    }
    
    public List<InformacoesDeAtores> getAuthors () {
        return authors;
    }
    
    public void setAuthors ( List<InformacoesDeAtores> authors ) {
        authors.forEach(e -> e.setBook(this));
        this.authors = authors;
    }
    
    @Override
    public String toString () {
        return
                "Título='" + title + '\'' + "\n" +
                        "Autores=" + authors + "\n" +
                        "Idioma=" + languages + "\n" +
                        "Downloads=" + downloads + "\n";
    }
    
    
}
