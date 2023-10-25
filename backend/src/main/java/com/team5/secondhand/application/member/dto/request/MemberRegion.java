package com.team5.secondhand.application.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class MemberRegion {
    private final Long id;
    @Size(min = 1, max = 2)
    private final List<BasedRegionSummary> regions;

    @JsonIgnore
    public List<Long> getRegionsId() {
        return regions.stream().map(BasedRegionSummary::getId).collect(Collectors.toList());
    }
}
