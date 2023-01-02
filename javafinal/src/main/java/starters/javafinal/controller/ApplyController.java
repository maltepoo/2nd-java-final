package starters.javafinal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import starters.javafinal.dto.ApplyStatusDto;
import starters.javafinal.exception.EndDueDateException;
import starters.javafinal.exception.NotAllowedException;
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
    @ResponseBody
    public void applyLecture(Long lectureId, Long memberId) {
        applyService.applyLecture(lectureId, memberId);
    }

    @DeleteMapping("")
    @ResponseBody
    public void cancelLecture(Long lectureId) {
        applyService.cancelLecture(lectureId);
    }

    @PostMapping("/scheduler")
    public void setApplyService() {
        applyService.setBasketToApply();
    }

}
