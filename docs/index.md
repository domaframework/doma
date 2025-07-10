.. Doma documentation master file, created by
   sphinx-quickstart on Thu Feb 13 12:43:15 2014.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

![Image](images/doma.png)
   :height: 200px
   :width: 200px
   :align: right
   :target: https://github.com/domaframework/doma

# Welcome to Doma

Doma is a database access framework for Java with several notable strengths:

* It checks and generates source code at compile time using annotation processing.
* It supports associations between entities.
* It offers a type-safe Criteria API.
* It includes SQL templates, known as “two-way SQL.”
* It runs independently, without relying on any other libraries.

This document consists of following sections:

* `User Documentation`_
* `About Doma`_
* `Links`_

```{admonition} Support Doma Development
  :class: important

  We kindly ask for your support to help us continue the development and maintenance of Doma.
  Your donation will enable us to fix bugs faster, improve the library, and add new features.

  You can make a contribution here: https://opencollective.com/doma

  Thank you very much for supporting Doma!

# User Documentation

```{toctree}
   :maxdepth: 2

   getting-started
   config
   basic
   domain
   embeddable
   entity
   dao
   aggregate-strategy
   query/index
   query-builder/index
   criteria-api
   query-dsl
   sql
   expression
   transaction
   build
   annotation-processing
   lombok-support
   kotlin-support
   slf4j-support
   jpms-support
   spring-boot-support
   quarkus-support
   codegen

# About Doma

```{toctree}
   :maxdepth: 1

   faq

# Links

* [News and announcements](https://twitter.com/domaframework)
* [GitHub repository](https://github.com/domaframework/doma)
* [Release Notes](https://github.com/domaframework/doma/releases)
* [JavaDoc](https://www.javadoc.io/doc/org.seasar.doma/doma-core/latest/index.html)
* [Examples](https://github.com/domaframework/simple-examples)
* [Doma Compile Plugin](https://github.com/domaframework/doma-compile-plugin)
* [Doma CodeGen Plugin](https://github.com/domaframework/doma-codegen-plugin)
* [Doma version 1](http://doma.seasar.org/)
