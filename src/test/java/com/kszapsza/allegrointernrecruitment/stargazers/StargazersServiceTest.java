package com.kszapsza.allegrointernrecruitment.stargazers;

import com.kszapsza.allegrointernrecruitment.MockDataFactory;
import com.kszapsza.allegrointernrecruitment.repo.Repo;
import com.kszapsza.allegrointernrecruitment.repo.RepoService;
import com.kszapsza.allegrointernrecruitment.repo.Repos;
import com.kszapsza.allegrointernrecruitment.util.Pagination;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith({MockitoExtension.class})
class StargazersServiceTest {

    @Mock
    private RepoService repoService;

    @InjectMocks
    private StargazersService stargazersService;

    @Test
    public void shouldReturnZeroStarsForEmptyList() {
        // given
        Mockito
                .when(repoService.getRepositories(anyString(), anyLong(), anyLong()))
                .thenReturn(new Repos(Collections.emptyList(), null));

        // when
        Stargazers stargazers = stargazersService.getTotalStargazers("foo");

        // then
        assertThat(stargazers.getStargazersCount(), equalTo(0L));
    }

    @Test
    public void shouldReturnCorrectAmountOfStarsForSampleList() {
        // given
        Repos repos = MockDataFactory.getSampleReposWithNullPagination();

        Long expectedStargazersCount = repos.getRepositories().stream()
                .map(Repo::getStargazersCount)
                .reduce(Long::sum)
                .orElse(0L);

        Mockito
                .when(repoService.getRepositories(anyString(), anyLong(), anyLong()))
                .thenReturn(repos);

        // when
        Stargazers stargazers = stargazersService.getTotalStargazers("foo");

        // then
        assertThat(stargazers.getStargazersCount(), equalTo(expectedStargazersCount));
    }

    @Test
    public void shouldReturnCorrectAmountOfStarsForSampleListWithMultiplePages() {
        // given
        Repos reposFirstPage = new Repos(
                MockDataFactory.getSampleRepoList(),
                new Pagination(2L, null, "foo?page=2", "foo?page=2", null));

        Repos reposSecondPage = new Repos(
                MockDataFactory.getSampleRepoList(),
                new Pagination(2L, "foo?page=1", null, null, "foo?page=1"));

        Long singlePageStargazersCount = reposFirstPage.getRepositories().stream()
                .map(Repo::getStargazersCount)
                .reduce(Long::sum)
                .orElse(0L);

        Mockito
                .when(repoService.getRepositories(anyString(), eq(1L), anyLong()))
                .thenReturn(reposFirstPage);

        Mockito
                .when(repoService.getRepositories(anyString(), eq(2L), anyLong()))
                .thenReturn(reposSecondPage);

        // when
        Stargazers stargazers = stargazersService.getTotalStargazers("foo");

        // then
        assertThat(stargazers.getStargazersCount(), equalTo(singlePageStargazersCount * 2));
    }
}