package com.alura.literalura.repository;

import com.alura.literalura.model.InformacoesDeAtores;
import com.alura.literalura.model.Book;
import com.alura.literalura.model.Languages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT a FROM Book b JOIN b.authors a")
    List<InformacoesDeAtores> getAuthorsInfo ();
    
    @Query("SELECT a FROM Book b JOIN b.authors a WHERE birthYear > :date")
    List<InformacoesDeAtores> getAuthorLiveAfter (Integer date );
    
//    @Query("SELECT b FROM Book b WHERE b.languages ILIKE %:language%")
//    List<Book> findBookbyLanguages (Languages language);
    
    List<Book> findByLanguages ( Languages languages );
}
