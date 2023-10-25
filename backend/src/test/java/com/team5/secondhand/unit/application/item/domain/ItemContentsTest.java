package com.team5.secondhand.unit.application.item.domain;

import com.team5.secondhand.FixtureFactory;
import com.team5.secondhand.application.item.domain.ItemContents;
import com.team5.secondhand.application.item.domain.ItemDetailImage;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemContentsTest extends FixtureFactory {

    ItemContents itemContents;


    @BeforeEach
    void init() {
        itemContents = fixtureMonkey().giveMeBuilder(ItemContents.class)
                .set("contents", "Origin Content")
                .set("detailImageUrl", fixtureMonkey().giveMe(ItemDetailImage.class, 5))
                .sample();
    }

    @Test
    @DisplayName("등록된 이미지 중 첫번째 이미지 url을 반환할 수 있다.")
    void getFirstImage_success() throws Exception {
        //given
        String contents = "content";
        String firstImageUrl = "First Image Url";
        List<ItemDetailImage> itemImagesList = List.of(
                ItemDetailImage.from(firstImageUrl),
                ItemDetailImage.from("url2"),
                ItemDetailImage.from("url3"),
                ItemDetailImage.from("url4")
        );

        //when
        ItemContents createdContents = ItemContents.of(contents, itemImagesList);

        //then
        assertThat(createdContents.getFirstImage().getUrl()).isEqualTo(firstImageUrl);
    }

    @Test
    @DisplayName("업데이트 할 수 있다.")
    void update_success() throws Exception {
        //given
        String updateContents = "updated content";
        List<ItemDetailImage> itemImagesList = List.of(
                ItemDetailImage.from("url1"),
                ItemDetailImage.from("url2"),
                ItemDetailImage.from("url3")
        );

        //when
        ItemContents update = itemContents.update(updateContents, itemImagesList);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(update.getId()).isEqualTo(itemContents.getId());
            softAssertions.assertThat(update.getContents()).isEqualTo(updateContents);
            softAssertions.assertThat(update.getDetailImageUrl()).hasSize(itemImagesList.size());
        });
    }
}
