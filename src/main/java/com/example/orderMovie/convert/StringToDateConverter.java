package com.example.orderMovie.convert;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
class StringToDateConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(MappingContext<String, LocalDateTime> mappingContext) {

        if (mappingContext.getSource() == null) {
            log.info("ignore source field with null value");
            return null;
        }

        String source = mappingContext.getSource();
        DateTimeFormatter dtoFormatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(source, dtoFormatDate);
    }
}
