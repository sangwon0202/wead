package sangwon.wead.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {
    @Id
    @Column(name = "isbn")
    private String isbn;
    @Column(name="title")
    private String title;
    @Column(name = "image")
    private String image;
    @Column(name = "author")
    private String author;
    @Column(name="pubdate")
    private LocalDate pubdate;

    @Builder
    public Book(String isbn, String title, String image, String author, LocalDate pubdate) {
        this.isbn = isbn;
        this.title = title;
        this.image = image;
        this.author = author;
        this.pubdate = pubdate;
    }

}
