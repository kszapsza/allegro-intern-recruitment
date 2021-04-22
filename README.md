# allegro-intern-recruitment

## Description

RESTful server application created in Java 11 with Spring Boot framework. The application communicates with GitHub REST
API and allows to:

* list repositories created by given GitHub user,
* count total stars (*stargazers*) in all repos for specific GitHub user.

## Setup

### From terminal

* Clone this repository.
* Ensure you have installed Java ≥11 and Gradle.
* Open local project directory in any terminal.
* Build the project: `./gradlew build`.
* Make sure you have configured `GITHUB_TOKEN` environment variable, containing GitHub authorization token
  (without `Bearer` prefix).
* Finally, run built project: `./gradlew run`.

### From IntelliJ IDEA

* Clone this repository, either manually or from IDE itself (*Get from VCS*).
* Wait for symbols to index, then run Gradle build task.
* Make sure you have configured `GITHUB_TOKEN` environment variable, containing GitHub authorization token
  (without `Bearer` prefix).
* Run the project.

## API documentation

### Get user repositories

Returns all repositories in user `{username}` profile. Shows at most 30 repos in a single page. `{page}` parameter
specifies page number.

**Sample request:**

```bash
curl -X GET http://localhost:8080/api/v1/repos/allegro?page=1
```

**Sample response:**

* `repositories` – list of user repositories
    * `name` – repository name
    * `stargazers_count` – amount of "stars" in this repository
* `links` – pagination links (previous/next, last/first page)

```json5
{
  "repositories": [
    {
      "name": "akubra",
      "stargazers_count": 79
    },
    /* for the sake of clarity, repos were skipped */
    {
      "name": "fogger",
      "stargazers_count": 59
    }
  ],
  "links": {
    "prevPage": null,
    "nextPage": "http://localhost:8080/api/v1/repos/allegro?per_page=30&page=2",
    "lastPage": "http://localhost:8080/api/v1/repos/allegro?per_page=30&page=3",
    "firstPage": null
  }
}
```

### Get total stars amount

Returns total amount of stars (*stargazers*) in all user repositories.

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

## Further development

* ⏳ **Anynchronous API calls.** For user profiles with significant amount of repos, it takes a long time to determine
  total stars amount (for huge `microsoft` GitHub profile with over 4000 repos – even up to 1 minute). This is because
  the application has to send multiple `GET` requests to GitHub API, in order to fetch all pages of paginated result.
  Those requests are now sent synchronously and could be sent asynchronously to improve performance.

* 🧠 **Caching.** Requests sent to this API could be somehow cached in order to reduce amount of calls sent to GitHub
  API. This could improve overall API performace as well.