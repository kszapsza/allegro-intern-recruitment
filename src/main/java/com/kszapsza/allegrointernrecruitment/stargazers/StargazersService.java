package com.kszapsza.allegrointernrecruitment.stargazers;

import com.kszapsza.allegrointernrecruitment.repo.Repo;
import com.kszapsza.allegrointernrecruitment.repo.RepoService;
import com.kszapsza.allegrointernrecruitment.repo.Repos;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class StargazersService {
    private final RepoService repoService;

    public StargazersService(RepoService repoService) {
        this.repoService = repoService;
    }

    @Cacheable(cacheNames = "stargazers")
    public Stargazers getTotalStargazers(String username) {
        long totalStargazers = 0L;
        long pageNumber = 1L;
        long totalPages;

        do {
            Repos repos = repoService.getRepositories(username, pageNumber, 100L);
            totalStargazers += getSinglePageTotalStargazers(repos);
            totalPages = repos.getPagination() != null ? repos.getPagination().getTotalPages() : 1L;
        } while (++pageNumber <= totalPages);

        return new Stargazers(totalStargazers);
    }

    private long getSinglePageTotalStargazers(Repos repos) {
        return repos.getRepositories().stream()
                .map(Repo::getStargazersCount)
                .reduce(Long::sum)
                .orElse(0L);
    }
}
