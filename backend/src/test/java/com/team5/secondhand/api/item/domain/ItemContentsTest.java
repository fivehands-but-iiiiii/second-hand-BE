package com.team5.secondhand.api.item.domain;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.javax.validation.plugin.JavaxValidationPlugin;
import com.team5.secondhand.api.item.controller.v1.dto.request.RequestedItemImages;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemContentsTest {

    ItemContents itemContents;

    public static FixtureMonkey fixtureMonkey() {
        return FixtureMonkey.builder()
                .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
                .plugin(new JavaxValidationPlugin()) // or new JavaxValidationPlugin()
                .defaultNotNull(true)
                .build();
    }

    @BeforeEach
    void init() {
        itemContents = fixtureMonkey().giveMeBuilder(ItemContents.class)
                .set("contents", "Origin Content")
                .set("detailImageUrl", fixtureMonkey().giveMe(ItemImage.class, 5))
                .sample();
    }

    @Test
    @DisplayName("등록된 이미지 중 첫번째 이미지 url을 반환할 수 있다.")
    void getFirstImage_success() throws Exception{
        //given
        String contents = "content";
        String firstImageUrl = "First Image Url";
        List<ItemImage> itemImagesList = List.of(
                ItemImage.from(firstImageUrl),
                ItemImage.from("url2"),
                ItemImage.from("url3"),
                ItemImage.from("url4")
        );

        //when
        ItemContents createdContents = ItemContents.of(contents, itemImagesList);

        //then
        assertThat(createdContents.getFirstImage().getUrl()).isEqualTo(firstImageUrl);
    }

    @Test
    @DisplayName("등록된 이미지가 없을 때 첫번째 이미지를 찾으면 빈 문자열을 반환한다.")
    void getFirstImage_empty() throws Exception{
        //given
        String contents = "content";
        String firstImageUrl = "First Image Url";
        List<ItemImage> itemImagesList = List.of();

        //when
        ItemContents createdContents = ItemContents.of(contents, itemImagesList);

        //then
        assertThat(createdContents.getFirstImage()).isNotNull();
        assertThat(createdContents.getFirstImage().getUrl()).isBlank();
    }

    @Test
    @DisplayName("업데이트 할 수 있다.")
    void update_success() throws Exception{
        //given
        String updateContents = "updated content";
        List<RequestedItemImages> itemImagesList = List.of(
                new RequestedItemImages("url1"),
                new RequestedItemImages("url2"),
                new RequestedItemImages("url3")
        );
        
        //when
        ItemContents update = itemContents.update(updateContents, itemImagesList);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(update.getId()).isEqualTo(itemContents.getId());
            softAssertions.assertThat(update.getContents()).isEqualTo(updateContents);
            softAssertions.assertThat(update.getDetailImageUrl()).hasSize(itemImagesList.size());
            softAssertions.assertThat(update.getFirstImage()).isNotEqualTo(itemContents.getFirstImage());
        });
    }
}
