package com.kszapsza.allegrointernrecruitment.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

import static com.kszapsza.allegrointernrecruitment.repo.RepoMockDataFactory.getSampleRepos;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith({MockitoExtension.class})
class RepoServiceTest {

    private final String repoGetRequestUri = "api/v1/repos/allegro";

    @Mock
    private RepoClient repoClient;

    @InjectMocks
    private RepoService repoService;

    @BeforeEach
    public void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(repoGetRequestUri);
        request.setParameter("per_page", "30");
        request.setParameter("page", "3");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void shouldReturnEmptyListAndEmptyLinks() {
        // given
        Mockito.when(repoClient.queryGithubApiForRepos(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong())
        ).thenReturn(new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK));

        // when
        Repos repos = repoService.getRepositories("allegro", 1L, 30L);

        // then
        assertThat(repos.getRepositories(), hasSize(0));
        assertThat(repos.getLinks(), nullValue());
    }

    @Test
    public void shouldReturnValidReposAndEmptyLinks() {
        // given
        Mockito.when(repoClient.queryGithubApiForRepos(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong())
        ).thenReturn(new ResponseEntity<>(getSampleRepos(), HttpStatus.OK));

        // when
        Repos repos = repoService.getRepositories("allegro", 1L, 30L);

        // then
        assertThat(repos.getRepositories(), hasSize(3));
        assertThat(repos.getRepositories(), equalTo(getSampleRepos()));
        assertThat(repos.getLinks(), nullValue());
    }

    @Test
    public void shouldReturnValidReposAndValidLinks() {
        // given
        String linkHeaderContent = "<https://api.github.com/user/6154722/repos?per_page=30&page=2>; rel=\"prev\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=4>; rel=\"next\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=133>; rel=\"last\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=1>; rel=\"first\"";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.LINK, linkHeaderContent);

        Mockito.when(repoClient.queryGithubApiForRepos(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong())
        ).thenReturn(new ResponseEntity<>(getSampleRepos(), httpHeaders, HttpStatus.OK));

        // when
        Repos repos = repoService.getRepositories("allegro", 1L, 30L);

        // then
        assertThat(repos.getRepositories(), hasSize(3));
        assertThat(repos.getRepositories(), equalTo(getSampleRepos()));

        assertThat(repos.getLinks(), notNullValue());
        assertThat(repos.getLinks().getPrevPage(), endsWith(repoGetRequestUri + "?per_page=30&page=2"));
        assertThat(repos.getLinks().getNextPage(), endsWith(repoGetRequestUri + "?per_page=30&page=4"));
        assertThat(repos.getLinks().getLastPage(), endsWith(repoGetRequestUri + "?per_page=30&page=133"));
        assertThat(repos.getLinks().getFirstPage(), endsWith(repoGetRequestUri + "?per_page=30&page=1"));
    }
}