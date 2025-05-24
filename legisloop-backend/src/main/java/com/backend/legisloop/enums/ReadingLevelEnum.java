package com.backend.legisloop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReadingLevelEnum {
    EASY(10),
    MODERATE(15),
    ONE_PAGE(21);

    private final int age;
}
