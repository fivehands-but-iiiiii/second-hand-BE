package com.team5.secondhand.api.item.dto.response;

import com.team5.secondhand.api.item.domain.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ItemList {
    private final Integer maxPage;
    private final List<ItemSummary> items;

    public static ItemList getPage(Integer maxPage, List<ItemSummary> page){
        return new ItemList(maxPage, page);
    }
}
