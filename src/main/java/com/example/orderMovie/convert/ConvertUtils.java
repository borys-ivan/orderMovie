package com.example.orderMovie.convert;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConvertUtils {

    private final ModelMapper modelMapper;

    public ConvertUtils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.modelMapper.addConverter(new StringToDateConverter());
    }

    public <S, T> T convert(S source, T target) {
        modelMapper.map(source, target);
        return target;
    }

}
