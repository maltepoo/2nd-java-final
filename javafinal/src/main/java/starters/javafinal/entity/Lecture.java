package starters.javafinal.entity;

import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
public class Lecture {
    // 강의정보	강의명	담당교수	강의요일	강의시간	정원	학점
    @Id
    @GeneratedValue
    @NotNull
    private Long id;

    private String name;
    private String professor;
    private int date; // 0 ~ 6 월 ~일
    private int time; // 24시간c
    private int studentLimit;
    private int credit;
}
