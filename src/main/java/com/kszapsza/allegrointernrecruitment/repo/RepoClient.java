package com.kszapsza.allegrointernrecruitment.repo;

import com.kszapsza.allegrointernrecruitment.exception.TimeoutException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Component
public class RepoClient {
    private final WebClient.Builder webClientBuilder;
    private final Environment env;

    public RepoClient(WebClient.Builder webClientBuilder, Environment env) {
        this.webClientBuilder = webClientBuilder;
        this.env = env;
    }

    public Mono<ResponseEntity<List<Repo>>> queryGithubApiForRepos(String username, Long page, Long perPage) {
        String getReposEndpoint = env.getProperty("githubApi.baseUri")
                + String.format("users/%s/repos?per_page=%d", username, Objects.requireNonNullElse(perPage, 30L));

        if (page != null && page >= 0) {
            getReposEndpoint += "&page=" + page;
        }

        try {
            Long timeout = env.getProperty("webClient.timeout", Long.class);
            return webClientBuilder.build()
                    .get()
                    .uri(getReposEndpoint)
                    .headers(h -> h.putAll(getGitHubRequestHeaders()))
                    .retrieve()
                    .toEntityList(Repo.class)
                    .timeout(Duration.ofMillis(timeout == null ? 5000 : timeout));
        } catch (ResourceAccessException e) {
            throw new TimeoutException();
        }
    }

    /**
     * @return HTTP headers needed for calls to GitHub REST API (Authorization and Accept).
     */
    private HttpHeaders getGitHubRequestHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();

        final String token = env.getProperty("githubApi.token");

        if (token != null && token.length() != 0) {
            requestHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }

        requestHeaders.set(HttpHeaders.ACCEPT, "application/vnd.github.v3+json");
        return requestHeaders;
    }
}
