package com.kszapsza.allegrointernrecruitment.repo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kszapsza.allegrointernrecruitment.util.Links;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Repos {
    private final List<Repo> repositories;
    private final Links links;

    public Repos(List<Repo> repositories, Links links) {
        this.repositories = repositories;
        this.links = links;
    }

    public List<Repo> getRepositories() {
        return repositories;
    }

    public Links getLinks() {
        return links;
    }
}
