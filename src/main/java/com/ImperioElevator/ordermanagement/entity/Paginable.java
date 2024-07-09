package com.ImperioElevator.ordermanagement.entity;

import java.util.List;

public class Paginable<T> {
    private List<T> items;
    private Long currentPage;
    private Long totalPages;
    private Long nextPage;
    private Long previousPage;

    public Paginable(List<T> items, Long currentPage, Long totalPages) {
        this.items = items;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.nextPage = getNextPage();
        this.previousPage = getPreviousPage();
    }

    public List<T> getItems() {
        return items;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public Long getNextPage() {
        return (currentPage < totalPages) ? currentPage + 1 : null;
    }

    public Long getPreviousPage() {
        return (currentPage > 1) ? currentPage - 1 : null;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public void setNextPage(Long nextPage) {
        this.nextPage = nextPage;
    }

    public void setPreviousPage(Long previousPage) {
        this.previousPage = previousPage;
    }
}
