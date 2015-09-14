package com.tilab.ca.sda_kurento_demo_app.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class ZonedDateTimeJsonSerializer extends JsonSerializer<ZonedDateTime> {

    private static final String DATE_ISO_8601_FORMAT="yyyy-MM-dd'T'HH:mm:ssX";
    private static final DateTimeFormatter ISO_8601_WITH_TIMEZONE_FORMATTER = DateTimeFormatter.ofPattern(DATE_ISO_8601_FORMAT);
    
    @Override
    public void serialize(ZonedDateTime zdt, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeString(zdt.format(ISO_8601_WITH_TIMEZONE_FORMATTER));
    }
    
}
