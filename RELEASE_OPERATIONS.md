# Release Operations

## Run the Gradle release task

The Gradle release task creates a release commit and push it to the origin/master branch.

```
$ git checkout master
$ git pull
$ ./gradlew release -Prelease.releaseVersion=$(gh release list -L 1 | tee /dev/tty  | awk '{print $1}')
```

The value of `release.releaseVersion` is decided by the draft name of
[Releases](https://github.com/domaframework/doma/releases).

## Build and Publish

(No operation required)

The GitHub Actions workflow [Java CI with Gradle](.github/workflows/ci.yml) handles the above push event.

The workflow builds artifacts and publishes them to [Maven Central](https://repo1.maven.org/).

## Wait for the artifacts to appear on Maven Central

(Optional)

Use [Dependency Watch](https://github.com/JakeWharton/dependency-watch).

```
$ dependency-watch await org.seasar.doma:doma-core:$(gh release list -L 1 | tee /dev/tty  | awk '{print $1}') && say "New version is available!"
```

If the above command is successful, the following directories will contain the new artifacts:

- https://repo1.maven.org/maven2/org/seasar/doma/doma-core/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-mock/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-kotlin/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-processor/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-slf4j/

## Publish documentation

(No operation required)

The webhook publishes documentation to the [ReadTheDocs](https://doma.readthedocs.io/en/latest/).

See also [Incoming Webhooks and Automation](https://docs.readthedocs.io/en/stable/webhooks.html).

## Publish release notes

Open [Releases](https://github.com/domaframework/doma/releases)
and publish release notes.

## Announce the release

Announce the release of new version using Twitter and Google Group.
- [@domaframework](https://twitter.com/domaframework)
- [doma-user](https://groups.google.com/g/doma-user)
