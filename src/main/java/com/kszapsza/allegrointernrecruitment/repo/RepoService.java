package com.kszapsza.allegrointernrecruitment.repo;

import com.kszapsza.allegrointernrecruitment.util.LinkHeaderParser;
import com.kszapsza.allegrointernrecruitment.util.Links;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Service
public class RepoService {
    private final RepoClient repoClient;

    public RepoService(RepoClient repoClient) {
        this.repoClient = repoClient;
    }

    public Repos getRepositories(String username, Long page, Long perPage) {
        ResponseEntity<List<Repo>> githubReposResponse = repoClient.queryGithubApiForRepos(username, page, perPage);

        List<String> linkHeader = githubReposResponse.getHeaders().get(HttpHeaders.LINK);
        Links links = preparePaginationLinks(linkHeader);

        return new Repos(githubReposResponse.getBody(), links);
    }

    /**
     * @param linkHeader HTTP "Link" header, retrieved from GitHub API response.
     * @return An object containing HATEOAS hyperlinks for navigating between multiple pages in this REST API.
     */
    private Links preparePaginationLinks(List<String> linkHeader) {
        if (linkHeader != null && linkHeader.size() != 0) {
            String contextUri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replaceQueryParam("page", Collections.emptyList())
                    .replaceQueryParam("per_page", Collections.emptyList())
                    .toUriString();
            return LinkHeaderParser.parseLinks(linkHeader, contextUri);
        }
        return null;
    }
}
