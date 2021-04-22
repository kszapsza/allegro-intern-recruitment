package com.kszapsza.allegrointernrecruitment.repo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Objects;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Repo {
    private String name;
    private long stargazersCount;

    public Repo() {
    }

    public Repo(String name, long stargazersCount) {
        this.name = name;
        this.stargazersCount = stargazersCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(long stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repo repo = (Repo) o;
        return stargazersCount == repo.stargazersCount && Objects.equals(name, repo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, stargazersCount);
    }
}
