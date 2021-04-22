package com.kszapsza.allegrointernrecruitment.repo;

import com.kszapsza.allegrointernrecruitment.util.Links;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith({MockitoExtension.class})
class RepoControllerTest {

    @Mock
    private RepoService repoService;

    @InjectMocks
    private RepoController repoController;

    @Test
    public void shouldReturnHttpOkAndEmptyResponse() {
        // given
        Repos repos = new Repos(Collections.emptyList(), null);

        Mockito.when(repoService.getRepositories(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong())
        ).thenReturn(repos);

        // when
        ResponseEntity<?> controllerResponse = repoController.getRepositories("foo", 1L, 30L);

        // then
        assertThat(controllerResponse.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(controllerResponse.getBody(), notNullValue());
        assertThat(controllerResponse.getBody(), instanceOf(Repos.class));
        assertThat(((Repos) controllerResponse.getBody()).getRepositories(), equalTo(Collections.emptyList()));
        assertThat(((Repos) controllerResponse.getBody()).getLinks(), nullValue());
    }

    @Test
    public void shouldReturnHttpOkAndSampleResponse() {
        // given
        List<Repo> sampleRepos = RepoMockDataFactory.getSampleRepos();
        Links links = new Links();
        links.setNextPage("foo");
        Repos reposResponse = new Repos(sampleRepos, links);

        Mockito.when(repoService.getRepositories(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong())
        ).thenReturn(reposResponse);

        // when
        ResponseEntity<?> controllerResponse = repoController.getRepositories("foo", 1L, 30L);

        // then
        assertThat(controllerResponse.getStatusCode(), equalTo(HttpStatus.OK));

        assertThat(controllerResponse.getBody(), notNullValue());
        assertThat(controllerResponse.getBody(), instanceOf(Repos.class));
        assertThat(((Repos) controllerResponse.getBody()).getRepositories(), equalTo(sampleRepos));

        assertThat(((Repos) controllerResponse.getBody()).getLinks(), notNullValue());
        assertThat(((Repos) controllerResponse.getBody()).getLinks().getNextPage(), equalTo("foo"));
    }
}