package sangwon.wead.service.DTO;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PageBarDto {
    private boolean prev;
    private boolean next;
    private int start;
    private int end;
    private int current;
}
