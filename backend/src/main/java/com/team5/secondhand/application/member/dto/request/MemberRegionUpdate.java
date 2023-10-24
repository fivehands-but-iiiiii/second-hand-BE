package com.team5.secondhand.application.member.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@RequiredArgsConstructor
public class MemberRegionUpdate {

    @Size(min = 1, max = 2)
    private List<Long> regions;

}
