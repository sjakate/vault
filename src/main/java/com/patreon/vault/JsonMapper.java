package com.patreon.vault;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class JsonMapper {
    public static final JsonMapper JSON = new JsonMapper();
    private ObjectMapper mapper;

    private JsonMapper() {
        // config
        this.mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Optional<String> toJson(Object obj) {
        try {
            return Optional.of(mapper.writeValueAsString(obj));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public <T> T fromJson(String json, Class<T> klass) {
        try {
            return mapper.readValue(json, klass);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
