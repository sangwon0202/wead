package sangwon.wead.DTO;

import lombok.Getter;
import org.springframework.data.domain.Page;
import sangwon.wead.exception.IllegalPageBarException;

@Getter
public class PageBar {
    private int current;
    private boolean prev;
    private boolean next;
    private int start;
    private int end;


    public PageBar(Page<?> page, int pageBarSize) {
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
            else throw new IllegalPageBarException();
        }
        if(number > total) throw new IllegalPageBarException();

        this.current = number;
        this.prev = number > pageBarSize;
        this.next = number <= ((total-1)/pageBarSize)*pageBarSize;
        this.start = ((number-1)/pageBarSize)*pageBarSize + 1;
        this.end = this.next ? this.start + pageBarSize - 1 : total;

    }
}
