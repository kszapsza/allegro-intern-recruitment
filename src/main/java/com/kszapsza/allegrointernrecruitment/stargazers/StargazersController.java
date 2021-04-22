package com.kszapsza.allegrointernrecruitment.stargazers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/stargazers")
public class StargazersController {
    private final StargazersService stargazersService;

    public StargazersController(StargazersService stargazersService) {
        this.stargazersService = stargazersService;
    }

    @GetMapping("{username}")
    public ResponseEntity<?> getTotalStargazers(@PathVariable String username) {
        Stargazers stargazers = stargazersService.getTotalStargazers(username);
        return new ResponseEntity<>(stargazers, HttpStatus.OK);
    }
}
