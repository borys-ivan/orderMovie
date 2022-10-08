package com.example.orderMovie.domain.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryUtils {

    private final ModelMapper modelMapper;

    public <T> Specification<T> createSpecification(SearchParams input) {
        switch (input.getOperator()){

            case EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(input.getField()),
                                castToRequiredType(root.get(input.getField()).getJavaType(),
                                        input.getValue()));

            case NOT_EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(input.getField()),
                                castToRequiredType(root.get(input.getField()).getJavaType(),
                                        input.getValue()));

            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(input.getField()),
                                (Number) castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValue()));
            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(input.getField()),
                                (Number) castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValue()));
            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(input.getField()),
                                "%"+input.getValue()+"%");

            case IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(input.getField()))
                                .value(castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValues()));
            case DATE_GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get(input.getField()),
                                (LocalDateTime) castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValue()));
            case DATE_LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get(input.getField()),
                                (LocalDateTime) castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValue()));

            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }

    private Object castToRequiredType(Class fieldType, String value) {
        if(fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if(fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if(fieldType.isAssignableFrom(BigDecimal.class)) {
            return BigDecimal.valueOf(Double.parseDouble(value));
        } else if(Enum.class.isAssignableFrom(fieldType)) {
            return Enum.valueOf(fieldType, value);
        } else if(fieldType.isAssignableFrom(Boolean.class)) {
            return Boolean.valueOf(value);
        } else if(fieldType.isAssignableFrom(LocalDateTime.class)) {
            return stringToDate(value);
        } else if(fieldType.isAssignableFrom(String.class)) {
            return value;
        }
        return null;
    }

    private LocalDateTime stringToDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(stringDate, formatter);
    }

    private Object castToRequiredType(Class fieldType, List<String> value) {
        List<Object> lists = new ArrayList<>();
        for (String s : value) {
            lists.add(castToRequiredType(fieldType, s));
        }
        return lists;
    }

    public <T, S> SearchResult<T> convertSearchResult(Page<S> pageEntity, PageRequest page, Class<T> destinationClass) {
        int pageNumber = page.getPageNumber();
        SearchResult<T> searchResult = new SearchResult<>();
        searchResult.setNumber(pageNumber);
        searchResult.setSize(page.getPageSize());
        searchResult.setHasPrevious(page.hasPrevious());
        searchResult.setTotalElements(pageEntity.getTotalElements());
        searchResult.setNumberOfElements(pageEntity.getContent().size());
        searchResult.setOffset(page.getOffset());
        searchResult.setTotalPages(pageEntity.getTotalPages());
        searchResult.setHasNext(pageNumber < pageEntity.getTotalPages());
        List<T> list = new ArrayList<>();
        for (S item : pageEntity.getContent()) {
            T map = modelMapper.map(item, destinationClass);
            list.add(map);
        }
        searchResult.setContent(list);

        return searchResult;
    }

}
