# Release Operations

## Dispatch the release workflow

Dispatch the [release workflow](.github/workflows/release.yml) as follows:

```
$ gh api repos/domaframework/doma/actions/workflows/release.yml/dispatches -F ref='master'
```

## Build and Publish

(No operation required)

The [ci workflow](.github/workflows/ci.yml) follows the above release workflow
and publishes artifacts to [Maven Central](https://repo1.maven.org/).

## Wait for the artifacts to appear on Maven Central

(Optional)

Use [Dependency Watch](https://github.com/JakeWharton/dependency-watch):

```
$ dependency-watch await org.seasar.doma:doma-core:$(gh release list -L 1 | tee /dev/tty  | awk '{print $1}') && say "New version is available!"
```

If the above command is successful, the following directories will contain the new artifacts:

- https://repo1.maven.org/maven2/org/seasar/doma/doma-core/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-mock/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-kotlin/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-processor/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-slf4j/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-template/

## Publish documentation

Creating a new tag at [domaframework/doma-docs](https://github.com/domaframework/doma-docs) will trigger synchronization with [ReadTheDocs](https://doma.readthedocs.io/en/latest/).

## Publish release notes

Open [Releases](https://github.com/domaframework/doma/releases)
and publish release notes.

## Announce the release

Announce the release of new version using
[Twitter](https://twitter.com/domaframework) and
[Zulip](https://domaframework.zulipchat.com).
