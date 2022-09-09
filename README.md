# quarkus-cache
![](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white&style=flat)


## Development
### Setup (First time)
1. Clone the repository: `git clone git@github.com:ag04/quarkus-cache.git`
4. Build project with: ` ./gradlew clean build `

### Manual Release
Make sure that file gradle.properties in the folder ${USER_HOME}/.gradle/ contains the following two variables defined:

* github_username
* github_password : personal github token to be used to install/update packages

1) Commit and push everything
2) `./gradlew cache-api:release`
3) `./gradlew cache-redis:release`

And simply follow the instructions on the console for each of the libearies.
