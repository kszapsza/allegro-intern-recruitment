package com.kszapsza.allegrointernrecruitment.stargazers;

import com.kszapsza.allegrointernrecruitment.repo.RepoService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class StargazersServiceTest {
    private final String repoGetRequestUri = "api/v1/stargazers/allegro";

    @Mock
    private RepoService repoService;

    @InjectMocks
    private StargazersService stargazersService;



}