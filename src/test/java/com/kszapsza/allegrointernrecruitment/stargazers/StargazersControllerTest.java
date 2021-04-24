package com.kszapsza.allegrointernrecruitment.stargazers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith({MockitoExtension.class})
class StargazersControllerTest {

    @Mock
    private StargazersService stargazersService;

    @InjectMocks
    private StargazersController stargazersController;

    @Test
    public void shouldReturnHttpOkAndCorrectAmountOfStars() {
        // given
        Stargazers expectedStargazers = new Stargazers(4224L);

        Mockito
                .when(stargazersService.getTotalStargazers(anyString()))
                .thenReturn(expectedStargazers);

        // when
        ResponseEntity<?> controllerResponse = stargazersController.getTotalStargazers("foo");
        Stargazers stargazers = (Stargazers) controllerResponse.getBody();

        // then
        assertThat(controllerResponse.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(stargazers, equalTo(expectedStargazers));
    }

}