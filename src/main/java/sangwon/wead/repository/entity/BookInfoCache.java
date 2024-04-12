package sangwon.wead.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sangwon.wead.service.DTO.BookInfo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfoCache {
    @Id
    @JoinColumn(name = "isbn")
    private String isbn;
    @Column(name="title")
    private String title;
    @JoinColumn(name = "image")
    private String image;
    @JoinColumn(name = "authors")
    private String authors;
    @Column(name="pubdate")
    private LocalDate pubdate;

    public BookInfoCache(BookInfo bookInfo) {
        this.isbn = bookInfo.getIsbn();
        this.title = bookInfo.getTitle();
        this.image = bookInfo.getImage().isBlank() ? null : bookInfo.getImage();
        this.authors = null;
        if(bookInfo.getAuthors() != null) {
            this.authors = "";
            int i=0;
            for(; i<bookInfo.getAuthors().size()-1; i++) {
                authors += bookInfo.getAuthors().get(i) + "^";
            }
            authors +=  bookInfo.getAuthors().get(i);
        }
        this.pubdate = bookInfo.getPubdate();
    }

    public BookInfo toBookInfo() {
        List<String> authors = null;
        if(this.authors != null) authors = Arrays.stream(this.authors.split("\\^")).toList();

        return BookInfo.builder()
                .isbn(this.isbn)
                .title(this.title)
                .image(this.image)
                .authors(authors)
                .pubdate(this.pubdate)
                .build();
    }


}
