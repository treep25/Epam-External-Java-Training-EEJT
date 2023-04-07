package com.epam.esm.commercetools;

import org.springframework.data.domain.PageRequest;

public class PagePaginationBuilder {

    public PagePaginationBuilder(PageRequest pageRequest) {
        this.page = pageRequest.getPageNumber();
        this.size = pageRequest.getPageSize();
    }

    private final int page;
    private final int size;

    public int getOffset() {
        return (this.size * this.page);
    }

    public int getLimit() {
        return this.size;
    }

}
