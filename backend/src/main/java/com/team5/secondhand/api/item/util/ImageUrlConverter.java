package com.team5.secondhand.api.item.util;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.team5.secondhand.api.item.domain.ItemDetailImage;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import javax.validation.ValidationException;
import java.util.InvalidPropertiesFormatException;

public class ImageUrlConverter implements AttributeConverter<ItemDetailImage, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(ItemDetailImage attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new ValidationException("이미지 URL 형식이 다릅니다.");
        }
    }

    @Override
    @SneakyThrows
    public ItemDetailImage convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, ItemDetailImage.class);
        } catch (JsonProcessingException e) {
            throw new ValidationException("이미지 URL 형식이 다릅니다.");
        }
    }
}
