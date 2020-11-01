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

## Publish documentation

(No operation required)

The webhook publishes documentation to the [ReadTheDocs](https://doma.readthedocs.io/en/latest/).

See also [Incoming Webhooks and Automation](https://docs.readthedocs.io/en/stable/webhooks.html).

## Publish release notes

Open [Releases](https://github.com/domaframework/doma/releases)
and publish release notes.

## Announce the release

Announce the release of new version using
[Twitter]((https://twitter.com/domaframework)),
[Zulip](https://domaframework.zulipchat.com), and
[Google Group](https://groups.google.com/g/doma-user).
