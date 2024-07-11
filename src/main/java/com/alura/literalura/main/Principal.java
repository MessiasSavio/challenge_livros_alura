package com.alura.literalura.main;

import com.alura.literalura.model.InformacoesDeAtores;
import com.alura.literalura.model.Book;
import com.alura.literalura.model.Data;
import com.alura.literalura.model.Languages;
import com.alura.literalura.repository.BookRepository;
import com.alura.literalura.services.DataConvert;
import com.alura.literalura.services.RequestAPI;

import java.util.*;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private RequestAPI requestAPI = new RequestAPI();
    private DataConvert dataConvert = new DataConvert();
    private BookRepository repository;
    private final String BASE_URL = "https://gutendex.com/books/";
    private List<Book> books;
    private String bookSelected;

    public Principal(BookRepository repository ) {
        this.repository = repository;
    }

    public void showMenu () {
        var option = - 1;
        while ( option != 0 ) {
            var menu = """
                    Lista de Opções:
                    1 - Buscar livro por título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros por idioma
                    0 - Sair
                    """;
            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    getBookData();
                    break;
                case 2:
                    showStoredBooks();
                    break;
                case 3:
                    authorsListStored();
                    break;
                case 4:
                    getAuthorYear();
                    break;
                case 5:
                    findBooksByLanguages();
                    break;
            }
        }
    }


    private String getDataFromUser () {
        System.out.println("Digite o nome do livro que deseja buscar:");
        bookSelected = scanner.nextLine();
        return bookSelected;
    }

    private Data getBookDataFromAPI ( String bookTitle ) {
        var json = requestAPI.getData(BASE_URL + "?search=%20" + bookTitle.replace(" ", "+"));
        var data = dataConvert.getData(json, Data.class);

        return data;
    }

    private Optional<Book> getBookInfo ( Data bookData, String bookTitle ) {
        Optional<Book> books = bookData.results().stream()
                .filter(l -> l.title().toLowerCase().contains(bookTitle.toLowerCase()))
                .map(b -> new Book(b.title(), b.languages(), b.downloads(), b.authors()))
                .findFirst();
        return books;
    }

    private void buscarSerieWeb () {
        String title = getDataFromUser();
        Data datos = getBookDataFromAPI(title);
        Book book = new Book(datos.results());
        repository.save(book);

        System.out.println(book);
    }


    private Optional<Book> getBookData () {
        String bookTitle = getDataFromUser();
        Data bookInfo = getBookDataFromAPI(bookTitle);
        Optional<Book> book = getBookInfo(bookInfo, bookTitle);

        if ( book.isPresent() ) {
            var b = book.get();
            repository.save(b);
            System.out.println(b);
        } else {
            System.out.println("\nLivro não encontrado\n");
        }

        return book;
    }

    private void showStoredBooks () {
        books = repository.findAll();

        books.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .forEach(System.out::println);
    }

    private void authorsListStored () {
        List<InformacoesDeAtores> authors = repository.getAuthorsInfo();

        authors.stream()
                .sorted(Comparator.comparing(InformacoesDeAtores::getName))
                .forEach(a -> System.out.printf("Autor: %s Nascimento: %s Falecimento: %s\\n",
                        a.getName(), a.getBirthYear(), a.getDeathYear()));
    }


    public void getAuthorYear () {
        System.out.println("Digite o ano a partir do qual deseja saber quais autores estavam vivos:");
        int date = scanner.nextInt();
        scanner.nextLine();

        List<InformacoesDeAtores> informacoesDeAtores = repository.getAuthorLiveAfter(date);

        informacoesDeAtores.stream()
                .sorted(Comparator.comparing(InformacoesDeAtores::getName))
                .forEach(a -> System.out.printf("Autor: %s Nascimento: %s Falecimento: %s\n",
                        a.getName(), a.getBirthYear(), a.getDeathYear()));
    }

    // encontrar libros por idioma

    public void findBooksByLanguages () {
        String languagesList = """
                Escolha uma das opções de idioma do livro que deseja buscar:
                
                en - Inglês
                es - Espanhol
                fr - Francês
                it - Italiano
                pt - Português
                
                """;
        System.out.println(languagesList);
        String text =  scanner.nextLine();

        var language = Languages.fromString(text);

        List<Book> bookLanguage = repository.findByLanguages(language);

        bookLanguage.stream()
                .forEach(System.out::println);
    }
}
