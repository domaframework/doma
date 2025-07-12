% Doma documentation master file, created by
% sphinx-quickstart on Thu Feb 13 12:43:15 2014.
% You can adapt this file completely to your liking, but it should at least
% contain the root `toctree` directive.

```{image} images/doma.png
:align: right
:height: 200px
:target: https://github.com/domaframework/doma
:width: 200px
```

# Welcome to Doma

Doma is a database access framework for Java with several notable strengths:

- It checks and generates source code at compile time using annotation processing.
- It supports associations between entities.
- It offers a type-safe Criteria API.
- It includes SQL templates, known as “two-way SQL.”
- It runs independently, without relying on any other libraries.

:::{admonition} Support Doma Development
:class: important

We kindly ask for your support to help us continue the development and maintenance of Doma.
Your donation will enable us to fix bugs faster, improve the library, and add new features.

You can make a contribution here: <https://opencollective.com/doma>

Thank you very much for supporting Doma!
:::

# User Documentation

```{toctree}
:maxdepth: 2

getting-started.md
config.md
basic.md
domain.md
embeddable.md
entity.md
dao.md
aggregate-strategy.md
query/index.md
query-builder/index.md
criteria-api.md
query-dsl.md
sql.md
expression.md
transaction.md
build.md
annotation-processing.md
lombok-support.md
kotlin-support.md
slf4j-support.md
jpms-support.md
spring-boot-support.md
quarkus-support.md
codegen.md
```

# About Doma

```{toctree}
:maxdepth: 1

faq
```

# Links

- [News and announcements](https://twitter.com/domaframework)
- [GitHub repository](https://github.com/domaframework/doma)
- [Release Notes](https://github.com/domaframework/doma/releases)
- [JavaDoc](https://www.javadoc.io/doc/org.seasar.doma/doma-core/latest/index.html)
- [Examples](https://github.com/domaframework/simple-examples)
- [Doma Compile Plugin](https://github.com/domaframework/doma-compile-plugin)
- [Doma CodeGen Plugin](https://github.com/domaframework/doma-codegen-plugin)
- [Doma version 1](http://doma.seasar.org/)
