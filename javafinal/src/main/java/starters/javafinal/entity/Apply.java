package starters.javafinal.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Apply {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private int status;
    // 0 장바구니 1 신청완료 2 찜

    @CreatedDate
    private LocalDateTime createdAt;


    public Apply(Member member, Lecture lecture, int status) {
        this.member = member;
        this.lecture = lecture;
        this.status = status;
    }

    public Apply() {

    }
}
