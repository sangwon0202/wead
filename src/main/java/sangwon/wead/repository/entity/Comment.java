package sangwon.wead.repository.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name="content")
    private String content;
    @Column(name="upload_date")
    private LocalDate uploadDate;

    @Builder
    public Comment(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.uploadDate = LocalDate.now();
    }
}
