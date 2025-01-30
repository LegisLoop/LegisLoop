package com.backend.legisloop.dto;

import com.backend.legisloop.model.Legislation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ls_MasterListResponse {
    private Map<String, Legislation> ls_MasterListResponse;
}
