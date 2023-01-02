package starters.javafinal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starters.javafinal.dto.ApplyStatusDto;
import starters.javafinal.entity.Apply;
import starters.javafinal.entity.Lecture;
import starters.javafinal.entity.Member;
import starters.javafinal.exception.NotAllowedException;
import starters.javafinal.repository.ApplyRepository;
import starters.javafinal.repository.LectureRepository;
import starters.javafinal.repository.MemberRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplyService {

    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;
    private final ApplyRepository applyRepository;

    @Transactional(readOnly = true)
    public ApplyStatusDto getLectureApplyStatus(Long lectureId, Long memberId) {
        Member professor = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("멤버가 아님"));
        if (professor.getRole() == 1) {
            throw new RuntimeException("조회 대상자가 교수가 아님");
        }
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new IllegalArgumentException("수업이 없음"));
        if (!Objects.equals(lecture.getProfessor(), professor.getName())) {
            throw new IllegalArgumentException("조회하는 교수의 수업이 아님");
        }
        Integer applyCount = applyRepository.countAllByLectureId(lectureId);
        return new ApplyStatusDto(applyCount);
    }

    // 수강신청
    @Transactional
    public void applyLecture(Long lectureId, Long memberId) {
        // 강의 신청 가능 기간은 2023년 1월 11일 오후 2시부터 오후 6시까지
//        LocalDateTime now = LocalDateTime.now();
//        if (now.isBefore(LocalDateTime.of(2023, 1, 11, 14, 0)) || now.isAfter(LocalDateTime.of(2023, 1, 11, 18, 0))) {
//            throw new EndDueDateException("강의신청기간 아님");
//        }

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("멤버가 아님"));
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new IllegalArgumentException("강의가 없음"));

        // 회원이 21학점이 넘지 않았는지
        List<Apply> applies = applyRepository.findAllByMemberId(memberId); // status 0, 1 장바구니, 신청완료인 것들 credit 체크
        Integer credits = applies.stream().map(apply -> apply.getLecture().getCredit()).reduce(0, Integer::sum);
        if (credits+lecture.getCredit() > 21) {
            throw new RuntimeException("21학점 이상 수강신청 할 수 없습니다.");
        }

        // 현재 수강하는 강의의 정원이 넘지 않았는지
        Integer lectureStudentLimit = applyRepository.countAllByLectureId(lectureId);
        if (lectureStudentLimit > lecture.getStudentLimit()) {
            throw new NotAllowedException("수강신청 인원 초과");
        }

        // 현재 수강한 강의와 요일/시간이 겹치지 않았는지 (장바구니 포함)
        int lectureStartTime = lecture.getTime();
        int lectureEndTime = lectureStartTime + lecture.getCredit();
        for (Apply apply: applies) {
            if (apply.getLecture().getDate() == lecture.getDate()) { // 요일 같은지 확인
                int myStartTime = apply.getLecture().getTime();
                int myEndTime = myStartTime + apply.getLecture().getCredit();

                if ((myStartTime <= lectureEndTime && myEndTime >= lectureStartTime)) {
                    throw new NotAllowedException("이미 신청한 수업과 같은 시간에 들을 수 없습니다.");
                }
            }
        }

        Apply apply = new Apply(member, lecture, 1);
        applyRepository.save(apply);
    }

    @Transactional
    public void setBasketToApply() {
        List<Lecture> allLectures = lectureRepository.findAll();
        System.out.println("allLectures = " + allLectures);

        for (Lecture lecture: allLectures) {
            Integer applyStudents = applyRepository.countAllByLectureId(lecture.getId());
            List<Apply> appliesOfLecture = applyRepository.findAllByLectureId(lecture.getId());

            if (applyStudents > lecture.getStudentLimit()) {
                // 수강신청 제한 인원보다 장바구니가 많을 때 -> 2 찜으로 상태변경
                for (Apply al: appliesOfLecture) {
                    al.setStatus(2);
                }
            } else {
                // 수강신청 제한 인원보다 장바구니가 적을 때 -> 0 장바구니 -> 1 수강완료
                for (Apply al: appliesOfLecture) {
                    al.setStatus(1);
                }
            }
        }
        log.info("======장바구니 자동 수강 동작완료=====");
    }

    @Transactional
    public void cancelLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new IllegalArgumentException("강의가 없음"));
        lectureRepository.delete(lecture);
    }
}
