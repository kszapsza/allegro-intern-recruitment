package com.kszapsza.allegrointernrecruitment.repo;

import com.kszapsza.allegrointernrecruitment.exception.GithubHttpException;
import com.kszapsza.allegrointernrecruitment.util.LinkHeaderParser;
import com.kszapsza.allegrointernrecruitment.util.Pagination;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Service
public class RepoService {
    private final RepoClient repoClient;

    public RepoService(RepoClient repoClient) {
        this.repoClient = repoClient;
    }

    @Cacheable(cacheNames = "repositories")
    public Repos getRepositories(String username, Long page, Long perPage) {
        try {
            ResponseEntity<List<Repo>> githubReposResponse = repoClient
                    .queryGithubApiForRepos(username, page, perPage)
                    .block();

            assert githubReposResponse != null;

            List<String> linkHeader = githubReposResponse.getHeaders().get(HttpHeaders.LINK);
            Pagination pagination = preparePaginationLinks(linkHeader, username);
            return new Repos(githubReposResponse.getBody(), pagination);
        } catch (WebClientResponseException e) {
            throw new GithubHttpException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    private Pagination preparePaginationLinks(List<String> linkHeader, String username) {
        if (linkHeader != null && linkHeader.size() != 0) {
            String uri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .toUriString();
            uri += "/api/v1/repos/" + username;
            return LinkHeaderParser.parseLinks(linkHeader, uri);
        }
        return new Pagination();
    }
}
