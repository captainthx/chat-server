package com.yutsuki.chatserver.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class Pagination extends PageRequest {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_LIMIT = 10;
    public static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "id");

    public Pagination(Integer page, Integer size, String sort) {
        super(page != null && page > 0 ? page - 1 : DEFAULT_PAGE,
                size != null && size > 0 ? size : DEFAULT_LIMIT,
                sort != null ? Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]) : DEFAULT_SORT);
    }

    public long getOffset() {
        return (long) getPageNumber() * getPageSize();
    }
}
