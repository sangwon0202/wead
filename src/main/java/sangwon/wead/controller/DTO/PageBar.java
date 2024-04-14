package sangwon.wead.controller.DTO;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageBar {
    private String url;
    private int current;
    private boolean prev;
    private boolean next;
    private int start;
    private int end;


    public PageBar(Page<?> page, int pageBarSize, String url) {
        this.url = url;
        if(url.contains("?")) this.url += "&page=";
        else this.url += "?page=";

        int total = page.getTotalPages();
        int number = page.getNumber() + 1;

        if(total == 0) {
            if(number == 1) {
                this.current = 1;
                this.prev = false;
                this.next = false;
                this.start = 1;
                this.end = 1;
                return;
            }
            else throw new RuntimeException("요소가 없을 땐 무조건 페이지 넘버가 1이여야합니다.");
        }

        if(number > total) throw new RuntimeException("페이지 넘버가 최대값을 넘었습니다.");

        this.current = number;
        this.prev = number > pageBarSize;
        this.next = number <= ((total-1)/pageBarSize)*pageBarSize;
        this.start = ((number-1)/pageBarSize)*pageBarSize + 1;
        this.end = this.next ? this.start + pageBarSize - 1 : total;

    }
}
