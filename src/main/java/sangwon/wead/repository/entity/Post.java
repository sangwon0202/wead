package sangwon.wead.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name="title", length = 50)
    private String title;
    @Column(name="content", columnDefinition = "TEXT")
    private String content;
    @Column(name="upload_date")
    private LocalDate uploadDate;
    @Column(name="views")
    private int views;
    @Column(name="isbn")
    private String isbn;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Builder
    public Post(User user, String title, String content,  String isbn) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.isbn = isbn;
        this.uploadDate = LocalDate.now();
        this.views = 0;
        this.comments = new ArrayList<>();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void increaseViews() {
        this.views += 1;
    }

}