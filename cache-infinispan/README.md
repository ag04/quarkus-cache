# Cache Infinispan
![](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white&style=flat)

*Current project version $LATEST_VERSION**

## Usage
To use this jar in your project add the following to the dependencies section:

```groovy
dependencies {
    implementation "com.ag04.quarkus:cache-infinispan:$LATEST_VERSION"
    ...
}
```
(build.gradle)

```xml
<dependency>
  <groupId>com.ag04.quarkus</groupId>
  <artifactId>cache-infinispan</artifactId>
  <version>$LATEST_VERSION</version>
</dependency>
```
(pom.xml)

And regitser github package as maven repository, as is for example show in the snippet bellow:

```groovy
def props = new Properties()
file(".env").withInputStream { props.load(it) }

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/ag04/cache-redis")
        credentials {
            username = System.getenv("gh_username") ?: props.getProperty("gh_username")
            password = System.getenv("gh_token") ?: props.getProperty("gh_token")
        }
    }
}
```

**gh_token** should contain the value of (PAT) "personal access token" that has Access to public repositories.
You can store this value in (1) .env file (do not forget to add this file to .gitignore!) or (2) configure system environment variable to save it.
First option is the simplest for local development, while the second is more suitable for ci/cd workflows.

For more see:
* [Personal GitHub access tokens](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)
* [Working with the Gradle registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry)

