package com.patreon.vault;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class JsonMapper {
    public static final JsonMapper JSON = new JsonMapper();
    private ObjectMapper mapper;

    private JsonMapper() {
        this.mapper = new ObjectMapper();
        // config
    }

    public Optional<String> toJson(Object obj) {
        try {
            return Optional.of(mapper.writeValueAsString(obj));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Object> fromJson(String json, Class klass) {
        try {
            return Optional.of(mapper.readValue(json, klass));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

}
