package hu.szte.brawlers.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DummyRequestDto {
    private String status;
    private String message;
}
