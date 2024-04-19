package sangwon.wead.service.DTO;

import lombok.Value;

import java.util.List;

@Value
public class ListDto<T> {
    List<T> list;
    PageBarDto pageBar;
}
