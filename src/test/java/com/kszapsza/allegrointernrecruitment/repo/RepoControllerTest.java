package com.kszapsza.allegrointernrecruitment.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith({MockitoExtension.class})
class RepoControllerTest {

    @Mock
    private RepoService repoService;

    @InjectMocks
    private RepoController repoController;

    @Test
    public void shouldReturnHttpOkAndEmptyResponse() {
        // given
        Repos repos = RepoMockDataFactory.getSampleReposWithNullPagination();

        Mockito.when(repoService.getRepositories(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong())
        ).thenReturn(repos);

        // when
        ResponseEntity<?> controllerResponse = repoController.getRepositories("foo", 1L, 30L);
        Repos actualReposResponse = (Repos) controllerResponse.getBody();

        // then
        assertThat(controllerResponse.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(repos, equalTo(actualReposResponse));
    }

    @Test
    public void shouldReturnHttpOkAndSampleResponse() {
        // given
        Repos repos = RepoMockDataFactory.getSampleReposWithSamplePagination();

        Mockito.when(repoService.getRepositories(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong())
        ).thenReturn(repos);

        // when
        ResponseEntity<?> controllerResponse = repoController.getRepositories("foo", 1L, 30L);
        Repos actualReposResponse = (Repos) controllerResponse.getBody();

        // then
        assertThat(controllerResponse.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(repos, equalTo(actualReposResponse));
    }
}