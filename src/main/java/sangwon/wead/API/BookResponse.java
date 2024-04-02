package sangwon.wead.API;

import lombok.Data;
import java.util.List;

@Data
public class BookResponse {

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
    }
}
