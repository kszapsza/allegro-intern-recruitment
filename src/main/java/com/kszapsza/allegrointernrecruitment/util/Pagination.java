package com.kszapsza.allegrointernrecruitment.util;

public class Pagination {
    private long totalPages = 1L;
    private String prevPage;
    private String nextPage;
    private String lastPage;
    private String firstPage;

    public Pagination() {
    }

    public Pagination(long totalPages, String prevPage, String nextPage, String lastPage, String firstPage) {
        this.totalPages = totalPages;
        this.prevPage = prevPage;
        this.nextPage = nextPage;
        this.lastPage = lastPage;
        this.firstPage = firstPage;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public String getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(String prevPage) {
        this.prevPage = prevPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }

    public String getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(String firstPage) {
        this.firstPage = firstPage;
    }
}
