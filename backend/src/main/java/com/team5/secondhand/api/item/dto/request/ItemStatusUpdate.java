package com.team5.secondhand.api.item.dto.request;

import com.team5.secondhand.api.item.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemStatusUpdate {
    private Status status;
}
