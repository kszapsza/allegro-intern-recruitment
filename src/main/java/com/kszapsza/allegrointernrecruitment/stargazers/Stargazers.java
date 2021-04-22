package com.kszapsza.allegrointernrecruitment.stargazers;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Stargazers {
    private final long stargazersCount;

    public Stargazers(long stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public long getStargazersCount() {
        return stargazersCount;
    }
}
