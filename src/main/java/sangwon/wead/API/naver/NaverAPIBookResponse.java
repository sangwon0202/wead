package sangwon.wead.API.naver;

import lombok.Data;
import sangwon.wead.service.DTO.BookInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Data
public class NaverAPIBookResponse {

    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<Item> items;

    @Data
    public static class Item {
        private String title;
        private String link;
        private String image;
        private String author;
        private int discount;
        private String publisher;
        private String pubdate;
        private String isbn;
        private String description;

        public BookInfo toBookInfo() {
            String author = this.author.isBlank() ? null : Arrays.stream(this.author.split("\\^")).toList().get(0);
            LocalDate pubdate = this.pubdate.isBlank() ? null : LocalDate.parse(this.pubdate, DateTimeFormatter.ofPattern("yyyyMMdd"));
            return BookInfo.builder()
                    .isbn(this.isbn)
                    .title(this.title)
                    .image(this.image)
                    .author(author)
                    .pubdate(pubdate)
                    .build();
        }
    }


}
