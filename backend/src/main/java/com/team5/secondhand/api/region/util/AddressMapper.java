package com.team5.secondhand.api.region.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddressMapper {

    //TODO: 근미래 가능하면 엘리스틱 서치 구현해보기
    private final List<MappingRule> mappingRules;

    public AddressMapper() {
        mappingRules = new ArrayList<>();
        mappingRules.add(new MappingRule(List.of("서울시", "서울 "), "서울특별시"));
    }

    public String mapAddress(String input) {
        for (MappingRule rule : mappingRules) {
            if (rule.isMapping(input)) {
                return rule.getMapping();
            }
        }
        return input;
    }

    private static class MappingRule {
        private final List<String> input;
        private final String mapping;

        public MappingRule(List<String> input, String mapping) {
            this.input = input;
            this.mapping = mapping;
        }

        private boolean isMapping(String key) {
            return input.contains(key);
        }

        public String getMapping() {
            return mapping;
        }
    }
}
