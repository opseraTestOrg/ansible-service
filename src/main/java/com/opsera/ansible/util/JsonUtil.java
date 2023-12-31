
package com.opsera.ansible.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author sreeni
 *
 */

public class JsonUtil {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    /**
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * @param <T>
     * @param entity
     * @param clazz
     * @return
     */
    public static <T> T toObject(Object entity, Class<T> clazz) {
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(entity), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param entity
     * @return
     */
    public static ObjectNode toObjectNode(Object entity) {
        return toObject(entity, ObjectNode.class);
    }

    /**
     * @param <T>
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param <T>
     * @param entity
     * @return
     */
    public static <T> String toJsonString(T entity) {
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param <T>
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> toArray(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param <T>
     * @param object
     * @param clazz
     * @return
     */
    public static <T> List<T> toArray(Object object, Class<T> clazz) {
        try {
            return objectMapper.readValue(toJsonString(object), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param <K>
     * @param <V>
     * @param json
     * @param keyClass
     * @param valueClass
     * @return
     */
    public static <K, V> Map<K, V> toHashMap(String json, Class<K> keyClass, Class<V> valueClass) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructMapType(HashMap.class, keyClass, valueClass));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param <K>
     * @param <V>
     * @param object
     * @param keyClass
     * @param valueClass
     * @return
     */
    public static <K, V> Map<K, V> toHashMap(Object object, Class<K> keyClass, Class<V> valueClass) {
        try {
            return objectMapper.readValue(toJsonString(object), objectMapper.getTypeFactory().constructMapType(HashMap.class, keyClass, valueClass));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param <T>
     * @param bean
     * @return
     */
    public static <T> Map<?, ?> bean2Map(Object bean) {
        try {
            return (Map<?, ?>) objectMapper.convertValue(bean, Map.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @param <T>
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T map2Bean(Map<?, ?> map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * @param content
     * @return
     */
    public static boolean isJSONValid(String content) {
        try {
            objectMapper.readTree(content);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
