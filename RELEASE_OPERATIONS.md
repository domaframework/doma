# Release Operations

## Check before releasing

- Make sure the latest [CI](https://github.com/domaframework/doma/actions/workflows/ci.yml)
  and [Code Scanning](https://github.com/domaframework/doma/actions/workflows/codeql-analysis.yml)
  runs on `master` are green.
- Open [Releases](https://github.com/domaframework/doma/releases) and make sure the title of the
  draft release created by [Release Drafter](.github/release-drafter.yml) is the version you want to release.
  The version is resolved from the labels of the merged pull requests
  (`major`, `minor`/`feat`/`feature`, otherwise patch).
- Make sure every pull request in the draft release appears under a category.
  The [autolabeler](.github/workflows/autolabeler.yml) labels pull requests only by branch name
  (`fix/...`, `feat/...`, `docs/...`, etc.; see [release-drafter.yml](.github/release-drafter.yml)),
  so pull requests from other branches, such as `claude/...`, have no label.
  Add the missing labels, then run the [Release Drafter workflow](.github/workflows/release-draft.yml)
  to regenerate the draft, because changing labels alone does not update it:

  ```
  $ gh workflow run release-draft.yml --repo domaframework/doma --ref master
  ```

## Dispatch the release workflow

Dispatch the [release workflow](.github/workflows/release.yml) as follows:

```
$ gh api repos/domaframework/doma/actions/workflows/release.yml/dispatches -F ref='master'
```

The workflow uses the title of the latest draft release as the release version.
To release a different version, pass it explicitly:

```
$ gh api repos/domaframework/doma/actions/workflows/release.yml/dispatches -F ref='master' -F 'inputs[version]=X.Y.Z'
```

The workflow runs the Gradle `release` task, which:

- updates the version in `gradle.properties`, `Artifact.java` of `doma-core`, `README.md`, and `docs/conf.py`,
- commits and pushes the release commit and the version tag (e.g. `3.14.1`), and
- commits and pushes the next development version (e.g. `3.14.2-SNAPSHOT`).

## Build and Publish

(No operation required)

The [ci workflow](.github/workflows/ci.yml) follows the above release workflow,
verifies the tagged commit, and publishes artifacts to [Maven Central](https://repo1.maven.org/).

## Wait for the artifacts to appear on Maven Central

(Optional)

It can take a while for the published artifacts to be synchronized to Maven Central.
The following command waits until the new version of `doma-core` is available
(replace `X.Y.Z` with the released version):

```
$ V=X.Y.Z; until curl -sfI "https://repo1.maven.org/maven2/org/seasar/doma/doma-core/$V/doma-core-$V.pom" > /dev/null; do sleep 60; done; echo "$V is available"
```

The following directories will then contain the new artifacts:

- https://repo1.maven.org/maven2/org/seasar/doma/doma-core/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-mock/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-kotlin/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-processor/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-slf4j/
- https://repo1.maven.org/maven2/org/seasar/doma/doma-template/

## Publish documentation

(No operation required)

The documentation lives in the [docs](docs) directory and is built by
[ReadTheDocs](https://docs.domaframework.org/) according to [.readthedocs.yaml](.readthedocs.yaml).
When the release workflow pushes the version tag, ReadTheDocs activates and builds the new version
and updates `stable` for both the English (`doma`) and Japanese (`doma-japanese`) projects.

Make sure that the new version and `stable` are built and show the new version:

- https://docs.domaframework.org/en/stable/
- https://docs.domaframework.org/ja/stable/

If the new version is not activated, activate it in the ReadTheDocs dashboard
(Versions) of each project.

## Publish release notes

Open [Releases](https://github.com/domaframework/doma/releases),
edit the draft release if needed, and publish it.

## Announce the release

Announce the release of new version using
[X](https://x.com/domaframework) and
[Zulip](https://domaframework.zulipchat.com).
