package com.activitystream.model.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * @author ivan
 */
public class JacksonMapper {

    private static ObjectMapper mapper;

    static {
        mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        JodaModule module = new JodaModule();
        DateTimeSerializer ser = new DateTimeSerializer(){
            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                StringBuilder sb = new StringBuilder(40).append(_format.createFormatter(provider).withOffsetParsed().print(value));
                gen.writeString(sb.toString());
            }
        };
        module.addSerializer(DateTime.class, ser);
        mapper.setDateFormat(new ISO8601DateFormat());
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.registerModule(module);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

}
