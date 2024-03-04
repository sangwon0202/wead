package sangwon.wead.DTO;

import lombok.Data;
import sangwon.wead.entity.Board;

import java.util.Date;

@Data
public class BoardFormDto {

    private String title;
    private String content;

}
