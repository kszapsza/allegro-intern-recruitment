package com.kszapsza.allegrointernrecruitment.repo;

import java.util.List;

public abstract class RepoMockDataFactory {
    public static List<Repo> getSampleRepos() {
        return List.of(
                new Repo("foo", 23L),
                new Repo("bar", 42L),
                new Repo("foobar", 11L)
        );
    }
}
