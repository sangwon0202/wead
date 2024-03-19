package sangwon.wead.controller.model;


import lombok.Data;
import sangwon.wead.service.DTO.PageBarDto;

@Data
public class PageBarModel {
    private boolean prev;
    private boolean next;
    private int start;
    private int end;
    private int current;

    public PageBarModel(PageBarDto pageBarDto) {
        this.prev = pageBarDto.isPrev();
        this.next = pageBarDto.isNext();
        this.start = pageBarDto.getStart();
        this.end = pageBarDto.getEnd();
        this.current = pageBarDto.getCurrent();
    }

}
