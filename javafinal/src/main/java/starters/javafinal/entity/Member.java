package starters.javafinal.entity;

import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
public class Member {
    // 회원정보	아이디 (이메일)	비밀번호	회원 이름	회원 구분	학번 (사번)
    @Id
    @GeneratedValue
    @NotNull
    private Long id;
    private String email;
    private String password;
    private String name;
    private int role; //0 교수 1학생
}
