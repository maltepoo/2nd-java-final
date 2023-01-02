package starters.javafinal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import starters.javafinal.entity.Apply;
import starters.javafinal.entity.Lecture;
import starters.javafinal.entity.Member;
import starters.javafinal.exception.EndDueDateException;
import starters.javafinal.exception.NoContentException;
import starters.javafinal.exception.NotAllowedException;
import starters.javafinal.repository.ApplyRepository;
import starters.javafinal.repository.LectureRepository;
import starters.javafinal.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasketService {
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;
    private final ApplyRepository applyRepository;
    private final ApplyService applyService;

    // 사전 수강신청
    @Transactional
    public ResponseEntity applyBasket(@RequestParam Long lectureId, @RequestParam Long memberId) {
        // 강의 장바구니 기간은 2023년 1월 9일 오후 2시부터 1월 10일 오후 6시까지
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(LocalDateTime.of(2023, 1, 9, 12, 0)) || now.isAfter(LocalDateTime.of(2023, 1, 9, 18, 0))) {
            throw new EndDueDateException("장바구니기간 아님");
        }

        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new NoContentException("강의가 없음"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoContentException("멤버가 아님"));

        List<Apply> applies = applyRepository.findAllByLectureId(lectureId); // 이미 해당 lecture에 수강신청한 신청서들
        for (Apply apply: applies) {
            if (apply.getMember().getId().equals(member.getId())) {
                throw new NotAllowedException("이미 해당 수업에 수강신청함");
            }
        }
        List<Apply> myApplies = applyRepository.findAllByMemberId(memberId);
        int lectureStartTime = lecture.getTime();
        int lectureEndTime = lectureStartTime + lecture.getCredit();

        for (Apply apply: myApplies) {
            if (apply.getLecture().getDate() == lecture.getDate()) { // 요일 같은지 확인
                int myStartTime = apply.getLecture().getTime();
                int myEndTime = myStartTime + apply.getLecture().getCredit();

                if ((myStartTime <= lectureEndTime && myEndTime >= lectureStartTime)) {
                    throw new NotAllowedException("이미 신청한 수업과 같은 시간에 들을 수 없습니다.");
                }
            }
        }
        Integer credits = myApplies.stream().map(apply -> apply.getLecture().getCredit()).reduce(0, Integer::sum);
        System.out.println("credits = " + credits);
        if (credits+lecture.getCredit() > 21) {
            throw new NotAllowedException("21학점 이상 수강신청 할 수 없습니다.");
        }

        Apply apply;

        if (applies.size() > lecture.getStudentLimit()) {
            apply = new Apply(member, lecture, 2); // 찜
        } else {
            apply = new Apply(member, lecture, 0); // 장바구니
        }
        applyRepository.save(apply);

        return new ResponseEntity("장바구니 성공", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity cancelBasket(Long applyId, Long memberId) {
        Apply apply = applyRepository.findById(applyId).orElseThrow(() -> new NoContentException("지원서가 없습니다."));
        if (apply.getMember().getId() != memberId) {
            throw new NotAllowedException("내 지원서가 아닌 지원서는 삭제할 수 없습니다.");
        }
        applyRepository.delete(apply);
        return new ResponseEntity("장바구니 삭제성공", HttpStatus.OK);
    }
}
