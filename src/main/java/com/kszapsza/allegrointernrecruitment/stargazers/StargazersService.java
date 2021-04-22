package com.kszapsza.allegrointernrecruitment.stargazers;

import com.kszapsza.allegrointernrecruitment.repo.Repo;
import com.kszapsza.allegrointernrecruitment.repo.RepoService;
import com.kszapsza.allegrointernrecruitment.repo.Repos;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StargazersService {
    private final RepoService repoService;

    public StargazersService(RepoService repoService) {
        this.repoService = repoService;
    }

    public Stargazers getTotalStargazers(String username) {
        long totalStargazers = 0L;
        long totalPages = 1L;
        long pageNumber = 1L;

        do {
            Repos repos = repoService.getRepositories(username, pageNumber, 100L);
            totalStargazers += getSinglePageTotalStargazers(repos);
            totalPages = getTotalPages(repos).orElse(totalPages);
        } while (++pageNumber <= totalPages);

        return new Stargazers(totalStargazers);
    }

    /**
     * @param reposResult Repos object retrieved from RepoService.
     * @return Retrieves last page number from last page URI in Repos object.
     * Returns Optional.empty() if page number is undeterminate from provided Repos object.
     */
    private Optional<Long> getTotalPages(Repos reposResult) {
        if (reposResult.getLinks() == null || reposResult.getLinks().getLastPage() == null) {
            return Optional.empty();
        }

        String lastPageUri = reposResult.getLinks().getLastPage();
        Pattern pagePattern = Pattern.compile("[&?]page=(\\d+).*");
        Matcher pageMatcher = pagePattern.matcher(lastPageUri);

        if (pageMatcher.find()) {
            return Optional.of(Long.parseLong(pageMatcher.group(1)));
        } else return Optional.empty();
    }

    /**
     * @param repos Single page repository list.
     * @return Sum of stargazers in repositories from given page.
     */
    private long getSinglePageTotalStargazers(Repos repos) {
        return repos.getRepositories().stream()
                .map(Repo::getStargazersCount)
                .reduce(Long::sum)
                .orElse(0L);
    }
}
