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

## Build and Publish with GitHub Action

(No operation required)

The GitHub Action workflow [Java CI with Gradle](.github/workflows/ci.yml) handles the above push event.

The workflow builds the "doma-core" and the "doma-processor" artifacts
and publishes them to the [Maven Central Repository](https://repo1.maven.org/).

After about 30 minutes, each artifact is listed in the following directories:

- https://repo1.maven.org/maven2/org/seasar/doma/doma-core/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-processor/

## Publish documentation

(No operation required)

The webhook publishes documentation to the [ReadTheDocs](https://doma.readthedocs.io/en/latest/).

See also [Incoming Webhooks and Automation](https://docs.readthedocs.io/en/stable/webhooks.html).

## Publish release notes

Open [Releases](https://github.com/domaframework/doma/releases)
and publish release notes.

## Announce the release

Announce the release of new version using Twitter.
- [@domaframework](https://twitter.com/domaframework)
