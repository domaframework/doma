# Contributing guide

## Legal

All original contributions to Doma are licensed under
the ASL - [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0) or later.

## Reporting an issue

This project uses GitHub issues to manage the issues. Open an issue directly in GitHub.

Write the issue in English to share it with many people.

## Before you contribute

To contribute, use GitHub Pull Requests, from your own fork.

## Setup

- Install Git and configure your GitHub access
- Install JDK 8
  - We recommend that you use [SDKMAN](https://sdkman.io/jdks) to get JDKs

### Build

Clone the repository and navigate to the root directory.
Then run the Gradle `build` task:

```
$ git clone https://github.com/domaframework/doma.git
$ cd doma
$ ./gradlew build
```

#### Using snapshots

Instead of building Doma from the master branch, you may want to use snapshots.
To use snapshots, define a Maven repository in your build.gradle as follows:

```groovy
repositories {
    maven {url 'https://oss.sonatype.org/content/repositories/snapshots/'}
}
```

Snapshots are published when each pull request is merged into the master branch.
You can check the last publication date here:

- https://oss.sonatype.org/content/repositories/snapshots/org/seasar/doma/

### IDE

#### IntelliJ IDEA

Import the root directory as a Gradle project.

#### Eclipse

Before importing, run the Gradle `eclipse` command in the root directory:

```
$ ./gradlew eclipse
```

### Code Style

We use [spotless](https://github.com/diffplug/spotless) and
[google-java-format](https://github.com/google/google-java-format) 1.7 for code formatting.

To format, just run the Gradle `build` task:

```
$ ./gradlew build
```

To run google-java-format in your IDE,
see https://github.com/google/google-java-format#using-the-formatter.

### Documentation

We use [Sphinx](http://sphinx-doc.org) to generate documents.
The generated documents are hosted on the ReadTheDocs.

- https://doma.readthedocs.io/

To use Sphinx, you need Python.

#### Install Sphinx

Navigate to the docs directory and run the `pip install` command:

```
$ cd docs
$ pip install -r requirements.txt
```

#### Generate HTML files

Execute the `sphinx-autobuild` command in the docs directory:

```
$ sphinx-autobuild . _build/html
```

Visit the webpage served at http://127.0.0.1:8000.
