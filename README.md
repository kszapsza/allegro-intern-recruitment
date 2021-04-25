# allegro-intern-recruitment

## Description

RESTful server application created in Java 11 with Spring Boot framework. The application communicates with GitHub REST
API and allows to:

* list repositories created by given GitHub user,
* count total stars (*stargazers*) in all repos for specific GitHub user.

## Setup

> :warning: **Important!** Before running the application, make sure you have configured `GITHUB_TOKEN` environment
> variable (either in OS or IDE), containing GitHub authorization token (without `Bearer` prefix). Application will fail
> to start with undeterminate variable!

### From terminal

* Clone this repository (`git clone https://github.com/kszapsza/allegro-intern-recruitment.git`).
* Make sure you have installed Java ≥11 and Gradle, and `JAVA_HOME` is set in OS env variables.
* Open local project directory in any terminal.
* Build the project: `./gradlew build`.
* Finally, run built project: `./gradlew bootRun`.

### From IntelliJ IDEA

* Clone this repository, either manually or from IDE itself (*Get from VCS*).
* Wait for symbols to index, then run Gradle build task.
* Run main application class entry point (`AllegroInternRecruitmentApplication.main()`).

## API documentation

### Get user repositories

Returns all repositories in user `{username}` profile.

```bash
curl -X GET http://localhost:8080/api/v1/repos/{username}?page={page}&per_page={per_page}
```

**Sample request:**

```bash
curl -X GET http://localhost:8080/api/v1/repos/allegro?page=1&per_page=2
```

* `page` – number of page to be displayed (optional, 1st page by default)
* `per_page` – records to be displayed per page (optional, 30 records by default)

**Sample response:**

* `repositories` – list of user repositories
    * `name` – repository name
    * `stargazers_count` – amount of “stars” in this repository
* `pagination` – pagination details (total pages count, links to previous/next, last/first page)

```json5
{
  "repositories": [
    {
      "name": "akubra",
      "stargazers_count": 79
    },
    {
      "name": "allegro-api",
      "stargazers_count": 132
    }
  ],
  "pagination": {
    "totalPages": 43,
    "prevPage": null,
    "nextPage": "http://localhost:8080/api/v1/repos/allegro?per_page=2&page=2",
    "lastPage": "http://localhost:8080/api/v1/repos/allegro?per_page=2&page=43",
    "firstPage": null
  }
}
```

### Get total stars amount

Returns total amount of stars (*stargazers*) in all user (`{username}`) repositories.

```bash
curl -X GET http://localhost:8080/api/v1/stargazers/{username}
```

**Sample request:**

```bash
curl -X GET http://localhost:8080/api/v1/stargazers/allegro
```

**Sample response:**

* `stargazers_count` – total amount of stars in all user repos

```json5
{
  "stargazers_count": 13088
}
```

## Features

* **Centralized exception handling** with `@ControllerAdvice`.
* **Caching** using Ehcache to improve performance.
* **Unit tests** with Mockito.

## Further development

* **Asynchronous API calls.** For user profiles with significant amount of repos, it takes a long time to determine
  total stars amount. This is because the application has to send multiple `GET` requests to GitHub API, in order to
  fetch all pages of paginated result. Those requests are now sent synchronously and could be sent asynchronously to
  improve performance.