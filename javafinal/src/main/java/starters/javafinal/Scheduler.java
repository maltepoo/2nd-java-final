package starters.javafinal;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import starters.javafinal.service.ApplyService;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final ApplyService applyService;

    // 강의 신청 가능 기간은 2023년 1월 11일 오후 2시부터 오후 6시까지로 설정합니다.

    @Scheduled(cron = "0 0 14 11 1 *")
    public void afterSupply() {
        applyService.setBasketToApply();
    }
}
