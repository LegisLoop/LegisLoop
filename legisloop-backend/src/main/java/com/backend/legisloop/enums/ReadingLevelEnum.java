package com.backend.legisloop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReadingLevelEnum {
    BEGINNER(10),
    INTERMEDIATE(15),
    ADVANCED(21);

    private final int age;
}
