package sangwon.wead.DTO;


import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import sangwon.wead.exception.server.IllegalPageBarException;
import sangwon.wead.controller.DTO.PageBar;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

class PageBarTest {

    @Test
    void test() {

        // total == 0 and number == 1
        Pageable pageable = PageRequest.of(0, 10);
        Page<Integer> page = new PageImpl<Integer>(new ArrayList<>(), pageable, 0);
        PageBar pageBar = new PageBar(page,10);

        assertThat(pageBar.getCurrent()).isEqualTo(1);
        assertThat(pageBar.getStart()).isEqualTo(1);
        assertThat(pageBar.getEnd()).isEqualTo(1);
        assertThat(pageBar.isPrev()).isFalse();
        assertThat(pageBar.isNext()).isFalse();


        // total == 0 and number != 1
        assertThatThrownBy(() -> {
            Pageable pageable2 = PageRequest.of(1, 10);
            Page<Integer> page2 = new PageImpl<Integer>(new ArrayList<>(0), pageable2, 0);
            new PageBar(page2,10);
        }).isInstanceOf(IllegalPageBarException.class);


        // number > total
        assertThatThrownBy(() -> {
            Pageable pageable2 = PageRequest.of(10, 10);
            Page<Integer> page2 = new PageImpl<Integer>(new ArrayList<>(10), pageable2, 100);
            new PageBar(page2,10);
        }).isInstanceOf(IllegalPageBarException.class);



        // pageBar size == 10
        pageable = PageRequest.of(4, 10);
        page = new PageImpl<Integer>(new ArrayList<>(10), pageable, 250);
        pageBar = new PageBar(page,10);

        assertThat(pageBar.getCurrent()).isEqualTo(5);
        assertThat(pageBar.getStart()).isEqualTo(1);
        assertThat(pageBar.getEnd()).isEqualTo(10);
        assertThat(pageBar.isPrev()).isFalse();
        assertThat(pageBar.isNext()).isTrue();



        pageable = PageRequest.of(13, 10);
        page = new PageImpl<Integer>(new ArrayList<>(10), pageable, 250);
        pageBar = new PageBar(page,10);

        assertThat(pageBar.getCurrent()).isEqualTo(14);
        assertThat(pageBar.getStart()).isEqualTo(11);
        assertThat(pageBar.getEnd()).isEqualTo(20);
        assertThat(pageBar.isPrev()).isTrue();
        assertThat(pageBar.isNext()).isTrue();



        pageable = PageRequest.of(23, 10);
        page = new PageImpl<Integer>(new ArrayList<>(10), pageable, 250);
        pageBar = new PageBar(page,10);

        assertThat(pageBar.getCurrent()).isEqualTo(24);
        assertThat(pageBar.getStart()).isEqualTo(21);
        assertThat(pageBar.getEnd()).isEqualTo(25);
        assertThat(pageBar.isPrev()).isTrue();
        assertThat(pageBar.isNext()).isFalse();


        // pageBar size == 5
        pageable = PageRequest.of(13, 10);
        page = new PageImpl<Integer>(new ArrayList<>(10), pageable, 250);
        pageBar = new PageBar(page,5);

        assertThat(pageBar.getCurrent()).isEqualTo(14);
        assertThat(pageBar.getStart()).isEqualTo(11);
        assertThat(pageBar.getEnd()).isEqualTo(15);
        assertThat(pageBar.isPrev()).isTrue();
        assertThat(pageBar.isNext()).isTrue();



    }

}