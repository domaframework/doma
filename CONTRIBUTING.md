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
- Install JDK 21
  - We recommend that you use [SDKMAN](https://sdkman.io/jdks) to get JDKs

### Build

Clone the repository and navigate to the root directory.
Then run the Gradle `build` task:

```bash
$ git clone https://github.com/domaframework/doma.git
$ cd doma
$ ./gradlew build
```

#### Using snapshots

Instead of building Doma from the master branch, you may want to use snapshots.
Snapshots are automatically published when each pull request is merged into the master branch.

##### Gradle configuration

Add the Sonatype snapshot repository to your `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/")
}
```

Then use the snapshot version (e.g., `3.10.0-SNAPSHOT`):

```kotlin
dependencies {
    implementation("org.seasar.doma:doma-core:3.10.0-SNAPSHOT")
    annotationProcessor("org.seasar.doma:doma-processor:3.10.0-SNAPSHOT")
}
```

##### Maven configuration

Add the repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>sonatype-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

### IDE - IntelliJ IDEA

Import the root directory as a Gradle project.

### Code Style

We use [spotless](https://github.com/diffplug/spotless) and
[google-java-format](https://github.com/google/google-java-format) for code formatting.

#### Automatic formatting

Code formatting is automatically applied when running the build:

```bash
$ ./gradlew build
```

To only check formatting without building:

```bash
$ ./gradlew spotlessCheck
```

To apply formatting without building:

```bash
$ ./gradlew spotlessApply
```

#### IDE integration

To run google-java-format in your IDE:
- IntelliJ IDEA: Install the [google-java-format plugin](https://plugins.jetbrains.com/plugin/8527-google-java-format)
- Eclipse: Follow instructions at https://github.com/google/google-java-format#eclipse
- VS Code: Install the [Language Support for Java extension](https://marketplace.visualstudio.com/items?itemName=redhat.java)

**Important**: Do not use wildcard imports (e.g., `import java.util.*;`). Always use explicit imports.

### Documentation

We use [Sphinx](http://sphinx-doc.org) to generate documents.
The generated documents are hosted on ReadTheDocs.

- **Production**: https://doma.readthedocs.io/
- **Latest (master branch)**: https://doma.readthedocs.io/en/latest/

To contribute to documentation, you need Python 3.x.

#### Install Sphinx

Navigate to the docs directory and install dependencies:

```bash
$ cd docs
$ pip install -r requirements.txt
```

#### Generate HTML files

Use `sphinx-autobuild` for live-reloading development server:

```bash
$ cd docs
$ sphinx-autobuild . _build/html
```

Visit http://127.0.0.1:8000 to preview your changes. The page automatically reloads when you modify RST files.

#### Build documentation

To build HTML documentation without auto-reload:

```bash
$ cd docs
$ make html
```

The generated HTML files will be in `docs/_build/html/`.

#### Translations

Doma documentation supports Japanese translations. To work with translations:

Generate POT files (translation templates):

```bash
$ cd docs
$ sphinx-build -b gettext . _build/gettext
```

Update PO files (translation files):

```bash
$ cd docs
$ sphinx-intl update -p _build/gettext -l ja
```

The Japanese translation files are located in `docs/locale/ja/LC_MESSAGES/`.

#### Documentation guidelines

- Write documentation in English first
- Use clear, concise language
- Include code examples where appropriate
- Test all code examples to ensure they work
- Verify RST syntax and rendering before submitting PRs
- Update the table of contents (`index.rst`) when adding new pages
