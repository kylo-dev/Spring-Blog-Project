package springblog.myblog.domain;

import lombok.*;
import springblog.myblog.domain.common.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //== 연관관계 메소드 ==//
    public void setBoard(Board board){
        this.board = board;
        board.getReplies().add(this);
    }

    public void setUser(User user){
        this.user = user;
        user.getReplies().add(this);
    }

    public static Reply createReply(User user, Board board, String content){
        Reply reply = new Reply();
        reply.setUser(user);
        reply.setBoard(board);
        reply.setContent(content);
        return reply;
    }
}
