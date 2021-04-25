package com.kszapsza.allegrointernrecruitment;

import com.kszapsza.allegrointernrecruitment.repo.Repo;
import com.kszapsza.allegrointernrecruitment.repo.Repos;
import com.kszapsza.allegrointernrecruitment.util.Pagination;

import java.util.List;

public abstract class MockDataFactory {
    public static Repos getSampleReposWithNullPagination() {
        return new Repos(getSampleRepoList(), null);
    }

    public static Repos getSampleReposWithSamplePagination() {
        return new Repos(getSampleRepoList(), null);
    }

    public static List<Repo> getSampleRepoList() {
        return List.of(
                new Repo("foo", 23L),
                new Repo("bar", 42L),
                new Repo("foobar", 11L)
        );
    }

    public static Pagination getSamplePagination() {
        return new Pagination(42L, "foo", "bar", "oof", "rab");
    }
}
