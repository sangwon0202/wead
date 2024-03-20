package sangwon.wead.service.util;


import sangwon.wead.exception.NonexistentPageException;
import sangwon.wead.service.DTO.PageBarDto;

public class PageUtil {

    static public boolean checkPageExistence(int count, int pageNumber, int countPerPage) {
        if(count == 0 && pageNumber != 1) return false;
        int pageCount = (count-1)/countPerPage + 1;
        return pageNumber <= pageCount;
    }

    static public PageBarDto buildPageBar(int count, int pageNumber, int countPerPage, int pageCountPerPageBar) throws NonexistentPageException {
        if(!checkPageExistence(count, pageNumber, countPerPage)) throw new NonexistentPageException();

        int pageCount = (count-1) / countPerPage + 1;
        int pageBarNumber = (pageNumber-1)/pageCountPerPageBar + 1;
        int pageBarCount = (pageCount-1)/pageCountPerPageBar + 1;

        boolean prev =  pageBarNumber > 1;
        boolean next = pageBarNumber != pageBarCount;
        int start = (pageBarNumber-1)*pageCountPerPageBar + 1;
        int end = next ? start + pageCountPerPageBar - 1 : pageCount;

        return PageBarDto.builder()
                .start(start)
                .end(end)
                .prev(prev)
                .next(next)
                .build();
    }

}
