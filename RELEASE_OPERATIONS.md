# Release Operations

## Run the Gradle release task

The Gradle release task creates a release commit and push it to the origin/master branch.

```
$ git checkout master
$ git pull
$ ./gradlew release -Prelease.releaseVersion=1.0.0 -Prelease.newVersion=1.1.0-SNAPSHOT
```

The value of `release.releaseVersion` is decided by the draft name of
[Releases](https://github.com/domaframework/doma/releases).

## Build with GitHub Action

The GitHub Action workflow [Java CI with Gradle](.github/workflows/ci.yml) handles the above push event.

The workflow builds the "doma-core" and the "doma-processor" artifacts
and pushes them to [Sonatype OSSRH](https://central.sonatype.org/pages/ossrh-guide.html).

## Publish artifacts to Maven Central

Follow the instructions below:

- Open [Nexus Repository Manager](https://oss.sonatype.org/).
- Log in to the manager.
- Select "Staging Repositories" from the side menu.
- Check the repository of Doma.
- "Close" and "Release".

In a few minutes, the "doma-core" and the "doma-processor" artifacts
are copied to the [Maven Central Repository](https://repo1.maven.org/).

Each artifact is listed in the following directories:

- https://repo1.maven.org/maven2/org/seasar/doma/doma-core/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-processor/

## Publish documentation to ReadTheDocs

[Documentation](https://doma.readthedocs.io/en/latest/)
is published with a [webhook](https://docs.readthedocs.io/en/stable/webhooks.html).

## Publish release notes

Open [Releases](https://github.com/domaframework/doma/releases)
and publish release notes.

## Announce the release

Announce the release of new version using Twitter.
- [@domaframework](https://twitter.com/domaframework)
