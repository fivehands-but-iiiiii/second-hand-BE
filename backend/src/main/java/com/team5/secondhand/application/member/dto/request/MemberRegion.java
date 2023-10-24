package com.team5.secondhand.application.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRegion {
    private Long id;
    @Size(min = 1, max = 2)
    private List<BasedRegionSummary> regions;

    @JsonIgnore
    public List<Long> getRegionsId() {
        return regions.stream().map(BasedRegionSummary::getId).collect(Collectors.toList());
    }
}
