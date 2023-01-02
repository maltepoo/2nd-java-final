package starters.javafinal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import starters.javafinal.service.BasketService;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping("")
    public ResponseEntity applyBasket(Long lectureId, Long memberId) {
        return basketService.applyBasket(lectureId, memberId);
    }

    @DeleteMapping("")
    public ResponseEntity cancelBasket(Long lectureId, Long memberId) {
        return basketService.cancelBasket(lectureId, memberId);
    }
}
