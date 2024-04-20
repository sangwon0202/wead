package sangwon.wead.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="isbn")
    private Book book;
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments;
    private String title;
    private String content;
    private LocalDate uploadDate;
    private int views;
    @Formula("(select count(*) from comment c where c.post_id = post_Id)")
    private int commentCount;

    @Builder
    public Post(User user, Book book, String title, String content) {
        this.user = user;
        this.book = book;
        this.comments = new ArrayList<>();
        this.title = title;
        this.content = content;
        this.uploadDate = LocalDate.now();
        this.views = 0;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void increaseViews() {
        this.views += 1;
    }

}