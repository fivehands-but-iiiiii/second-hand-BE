package com.team5.secondhand.api.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BasedRegionSummary {
    private Long id;
    private boolean onFocus;
}
