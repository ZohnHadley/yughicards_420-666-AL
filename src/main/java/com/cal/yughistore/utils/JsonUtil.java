package com.cal.yughistore.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonUtil {
    private static JsonUtil instance;
    ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil(){
    }

    public static JsonUtil getInstance() {
        if (instance == null) {
            instance = new JsonUtil();
        }
        return instance;
    }

    public <T> String toJson(T object ){
        try{
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> JsonNode toJsonNode(T object ){
        if(object == null){
            throw new RuntimeException("JsonUtil : err toJsonNode");
        }
        return objectMapper.valueToTree(object);
    }

    public JsonNode fromJson(String json) {
        try {
            if (json == null || json.isBlank()) {
                throw new RuntimeException("JsonUtil : err fromJson");
            }
            JsonNode rootNode = objectMapper.readTree(json);
            return rootNode;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> T fromJson(String json, Class<T> objClass) {
        try {
            if (json == null || json.isBlank() || objClass == null) {
                throw new RuntimeException("JsonUtil : err fromJson");
            }
            return objectMapper.readValue(json, objClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
