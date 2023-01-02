package starters.javafinal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import starters.javafinal.dto.ApplyStatusDto;
import starters.javafinal.service.ApplyService;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {
    private final ApplyService applyService;

    @GetMapping("")
    @ResponseBody
    public ApplyStatusDto getLectureApplyStatus(@RequestParam Long lectureId, @RequestParam Long memberId) {
        return applyService.getLectureApplyStatus(lectureId, memberId);
    }

    @PostMapping("")
    public void applyLecture(Long lectureId, Long memberId) {
        applyService.applyLecture(lectureId, memberId);
    }

    @DeleteMapping("")
    public void cancelLecture(Long lectureId) {
        applyService.cancelLecture(lectureId);
    }

    @PostMapping("/scheduler")
    public void setApplyService() {
        applyService.setBasketToApply();
    }
}
