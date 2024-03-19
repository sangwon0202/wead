package sangwon.wead.service.DTO;


import lombok.Data;

@Data
public class PageBarDto {
    private boolean prev;
    private boolean next;
    private int start;
    private int end;
    private int current;
}
