package com.kszapsza.allegrointernrecruitment.repo;

import com.kszapsza.allegrointernrecruitment.exception.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Component
public class RepoClient {
    private final WebClient.Builder webClientBuilder;

    @Value("${githubApi.baseUri}")
    private String githubApiBaseUri;

    @Value("${webClient.timeout}")
    private Long webClientTimeout;

    @Value("${githubApi.token}")
    private String githubApiToken;

    public RepoClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<ResponseEntity<List<Repo>>> queryGithubApiForRepos(String username, Long page, Long perPage) {
        String getReposEndpoint = githubApiBaseUri
                + "users/" + username
                + "/repos?per_page=" + Objects.requireNonNullElse(perPage, 30L);

        if (page != null && page >= 0) {
            getReposEndpoint += "&page=" + page;
        }

        try {
            return buildRepoQueryRequest(URI.create(getReposEndpoint));
        } catch (ResourceAccessException e) {
            throw new TimeoutException();
        }
    }

    private Mono<ResponseEntity<List<Repo>>> buildRepoQueryRequest(URI uri) {
        return webClientBuilder.build()
                .get()
                .uri(uri)
                .headers(h -> h.putAll(getGitHubRequestHeaders()))
                .retrieve()
                .toEntityList(Repo.class)
                .timeout(Duration.ofMillis(webClientTimeout == null ? 5000 : webClientTimeout));
    }

    private HttpHeaders getGitHubRequestHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();

        if (githubApiToken != null && githubApiToken.length() != 0) {
            requestHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + githubApiToken);
        }

        requestHeaders.set(HttpHeaders.ACCEPT, "application/vnd.github.v3+json");
        return requestHeaders;
    }
}
