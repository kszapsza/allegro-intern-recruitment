package com.kszapsza.allegrointernrecruitment.repo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kszapsza.allegrointernrecruitment.util.Pagination;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Repos {
    private final List<Repo> repositories;
    private final Pagination pagination;

    public Repos(List<Repo> repositories, Pagination pagination) {
        this.repositories = repositories;
        this.pagination = pagination;
    }

    public List<Repo> getRepositories() {
        return repositories;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
