package com.team5.secondhand.api.item.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.secondhand.api.item.domain.ItemImage;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import javax.validation.ValidationException;
import java.util.List;

public class ImageUrlConverter implements AttributeConverter<List<ItemImage>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ItemImage> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new ValidationException("이미지 직렬화에 실패했습니다.");
        }
    }

    @Override
    @SneakyThrows
    public List<ItemImage> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<ItemImage>>() {});
        } catch (JsonProcessingException e) {
            throw new ValidationException("이미지 URL 형식이 다릅니다.");
        }
    }
}
