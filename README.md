# quarkus-cache
![](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white&style=flat)


## Development

To build this app localy, you need to create file named `.env` in the root folder with the following content:
```
gh_username=your_github_usernam
gh_token=value_of_your_personall_access_token_with_access_to_public_repositories
```

**gh_token** should contain the value of (PAT) "personal access token" that has Access to public repositories.
You can store this value in (1) .env file (do not forget to add this file to .gitignore!) or (2) configure system environment variable to save it.
First option is the simplest for local development, while the second is more suitable for ci/cd workflows.

For more see:
* [Personal GitHub access tokens](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)


### Setup (First time)
1. Clone the repository: `git clone git@github.com:ag04/quarkus-cache.git`
4. Build project with: ` ./gradlew clean build `

### Manual Release
Make sure that file gradle.properties in the folder ${USER_HOME}/.gradle/ contains the following two variables defined:

* github_username
* github_password : personal github token to be used to install/update packages

1) Commit and push everything
2) `./gradlew cache-api:release` (to release new verison of cache-api)
3) `./gradlew cache-redis:release` (to release new verison of cache-redis)

And simply follow the instructions on the console for each of the libearies.
