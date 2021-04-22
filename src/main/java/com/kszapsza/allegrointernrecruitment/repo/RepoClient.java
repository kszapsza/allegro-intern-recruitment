package com.kszapsza.allegrointernrecruitment.repo;

import com.kszapsza.allegrointernrecruitment.exception.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Component
public class RepoClient {
    @Value( "${githubApi.baseUri}" )
    private String githubApiBaseUri;

    @Value( "${githubApi.token}" )
    private String githubApiToken;

    private final RestTemplate restTemplate;

    public RepoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<List<Repo>> queryGithubApiForRepos(String username, Long page, Long perPage) {
        String uri = githubApiBaseUri + String.format("users/%s/repos?per_page=%d", username, perPage);

        if (page != null && page >= 0) {
            uri += "&page=" + page;
        }

        RequestEntity<?> requestEntity = new RequestEntity<>(getGitHubRequestHeaders(), HttpMethod.GET, URI.create(uri));

        try {
            return restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
        } catch (ResourceAccessException e) {
            throw new TimeoutException();
        }
    }

    /**
     * @return HTTP headers needed for calls to GitHub REST API (Authorization and Accept).
     */
    private HttpHeaders getGitHubRequestHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();

        if (githubApiToken != null && githubApiToken.length() != 0) {
            requestHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + githubApiToken);
        }

        requestHeaders.set(HttpHeaders.ACCEPT, "application/vnd.github.v3+json");
        return requestHeaders;
    }
}
