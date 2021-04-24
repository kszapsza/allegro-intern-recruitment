package com.kszapsza.allegrointernrecruitment.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith({MockitoExtension.class})
class RepoServiceTest {

    @Mock
    private RepoClient repoClient;

    @InjectMocks
    private RepoService repoService;

    @Test
    public void shouldReturnEmptyListAndEmptyLinks() {
        // given
        Mockito
                .when(repoClient.queryGithubApiForRepos(anyString(), anyLong(), anyLong()))
                .thenReturn(Mono.just(new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK)));

        // when
        Repos repos = repoService.getRepositories("allegro", 1L, 30L);

        // then
        assertThat(repos.getRepositories(), hasSize(0));

        assertThat(repos.getPagination(), notNullValue());
        assertThat(repos.getPagination().getTotalPages(), equalTo(1L));
        assertThat(repos.getPagination().getPrevPage(), nullValue());
        assertThat(repos.getPagination().getNextPage(), nullValue());
        assertThat(repos.getPagination().getLastPage(), nullValue());
        assertThat(repos.getPagination().getFirstPage(), nullValue());
    }

    @Test
    public void shouldReturnValidReposAndEmptyLinks() {
        // given
        Mockito
                .when(repoClient.queryGithubApiForRepos(anyString(), anyLong(), anyLong()))
                .thenReturn(Mono.just(new ResponseEntity<>(RepoMockDataFactory.getSampleRepoList(), HttpStatus.OK)));

        // when
        Repos repos = repoService.getRepositories("allegro", 1L, 30L);

        // then
        assertThat(repos.getRepositories(), hasSize(RepoMockDataFactory.getSampleRepoList().size()));
        assertThat(repos.getRepositories(), equalTo(RepoMockDataFactory.getSampleRepoList()));

        assertThat(repos.getPagination(), notNullValue());
        assertThat(repos.getPagination().getTotalPages(), equalTo(1L));
        assertThat(repos.getPagination().getPrevPage(), nullValue());
        assertThat(repos.getPagination().getNextPage(), nullValue());
        assertThat(repos.getPagination().getLastPage(), nullValue());
        assertThat(repos.getPagination().getFirstPage(), nullValue());
    }

    @Test
    public void shouldReturnValidReposAndValidLinks() {
        // given
        String repoGetRequestUri = "api/v1/repos/allegro";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(repoGetRequestUri);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String linkHeaderContent = "<https://api.github.com/user/6154722/repos?per_page=30&page=2>; rel=\"prev\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=4>; rel=\"next\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=133>; rel=\"last\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=1>; rel=\"first\"";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.LINK, linkHeaderContent);

        Mockito
                .when(repoClient.queryGithubApiForRepos(anyString(), anyLong(), anyLong()))
                .thenReturn(Mono.just(
                        new ResponseEntity<>(RepoMockDataFactory.getSampleRepoList(), httpHeaders, HttpStatus.OK)));

        // when
        Repos repos = repoService.getRepositories("allegro", 1L, 30L);

        // then
        assertThat(repos.getRepositories(), hasSize(3));
        assertThat(repos.getRepositories(), equalTo(RepoMockDataFactory.getSampleRepoList()));

        assertThat(repos.getPagination(), notNullValue());
        assertThat(repos.getPagination().getTotalPages(), equalTo(133L));
        assertThat(repos.getPagination().getPrevPage(), endsWith(repoGetRequestUri + "?per_page=30&page=2"));
        assertThat(repos.getPagination().getNextPage(), endsWith(repoGetRequestUri + "?per_page=30&page=4"));
        assertThat(repos.getPagination().getLastPage(), endsWith(repoGetRequestUri + "?per_page=30&page=133"));
        assertThat(repos.getPagination().getFirstPage(), endsWith(repoGetRequestUri + "?per_page=30&page=1"));
    }
}