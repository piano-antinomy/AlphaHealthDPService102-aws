package com.alpha.health.dp.core.lambda.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.text.SimpleDateFormat;

/**
 * singleton objectMapper for all apps. thread-safe
 */
public class SingletonInstanceFactory {
    private SingletonInstanceFactory() {};

    private static class SingletonInstanceFactoryHolder {
        public static final ObjectMapper OBJECT_MAPPER_INSTANCE =
            new ObjectMapper()
                .registerModule(new JodaModule()).setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    public static ObjectMapper getObjectMapperInstance() {
        return SingletonInstanceFactoryHolder.OBJECT_MAPPER_INSTANCE;
    }

}
