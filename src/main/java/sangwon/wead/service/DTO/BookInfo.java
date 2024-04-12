package sangwon.wead.service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BookInfo {
    private String isbn;
    private String title;
    private String image;
    private List<String> authors;
    private LocalDate pubdate;

}
