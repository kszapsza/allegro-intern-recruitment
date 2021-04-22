package com.kszapsza.allegrointernrecruitment.repo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/repos")
public class RepoController {
    private final RepoService repoService;

    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }

    @GetMapping("{username}")
    public ResponseEntity<?> getRepositories(@PathVariable String username,
                                             @RequestParam(required = false) Long page,
                                             @RequestParam(required = false, name = "per_page") Long perPage) {
        Repos repos = repoService.getRepositories(username, page, perPage);
        return new ResponseEntity<>(repos, HttpStatus.OK);
    }
}