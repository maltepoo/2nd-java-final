package starters.javafinal.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ApplyStatusDto {
    private int applyCount;

    public ApplyStatusDto(int applyCount) {
        this.applyCount = applyCount;
    }
}
