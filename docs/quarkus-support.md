===============
# Quarkus support

.. contents::
   :depth: 4

# Overview

Doma supports [Quarkus](https://quarkus.io/) v3.7.0 and later.
To integrate Doma with Quarkus, you need to use the [quarkus-doma](https://github.com/quarkiverse/quarkus-doma) extension.

```{note}

  Quarkus and the quarkus-doma module require Java 17 and later.

# Installing

## Gradle

```kotlin

    dependencies {
        annotationProcessor("org.seasar.doma:doma-processor:{{ doma_version }}")
        implementation("org.seasar.doma:doma-core:{{ doma_version }}")
        implementation("io.quarkiverse.doma:quarkus-doma:{{ quarkus_doma_version }}")
    }

## Maven

```xml

    ...
    <properties>
        <doma.version>{{ doma_version }}</doma.version>
        <quarkus-doma.version>{{ quarkus_doma_version }}</quarkus-doma.version>
        <compiler-plugin.version>3.9.0</compiler-plugin.version>
    </properties>
    ...
    <dependencies>
        <dependency>
            <groupId>org.seasar.doma</groupId>
            <artifactId>doma-core</artifactId>
            <version>${doma.version}</version>
        </dependency>
        <dependency>
            <groupId>io.quarkiverse.doma</groupId>
            <artifactId>quarkus-doma</artifactId>
            <version>${quarkus-doma.version}</version>
        </dependency>
    </dependencies>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${compiler-plugin.version}</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                    <!-- the parameters=true option is critical so that RESTEasy works fine -->
                    <parameters>true</parameters>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.seasar.doma</groupId>
                            <artifactId>doma-processor</artifactId>
                            <version>${doma.version}</version>
                        </path>
                    </annotationProcessorPaths>
                    <compilerArgs>
                        <!-- if you are using a Maven project in Eclipse, this argument is required -->
                        <arg>-Adoma.resources.dir=${project.basedir}/src/main/resources</arg>
                    </compilerArgs>
                </configuration>
            </plugin>
        </plugins>
    </build>

# Main features

## Hot reloading

In development mode, the quarkus-doma extension automatically detects and reloads SQL and Script files whenever you modify them.

## Automatic bean register

The quarkus-doma extension automatically registers all DAO beans in the Quarkus CDI container.

## Automatic SQL execution on startup

The quarkus-doma extension automatically executes the import.sql script file during application startup to initialize your database.

## Configuration

You can write the following configurations in your application.properties file:

```properties

    quarkus.doma.sql-file-repository=greedy-cache
    quarkus.doma.naming=none
    quarkus.doma.exception-sql-log-type=none
    quarkus.doma.dialect=h2
    quarkus.doma.batch-size=10
    quarkus.doma.fetch-size=50
    quarkus.doma.max-rows=500
    quarkus.doma.query-timeout=5000
    quarkus.doma.sql-load-script=import.sql

The above properties are all optional.

Please refer to the [Configuration References](https://docs.quarkiverse.io/quarkus-doma/dev/index.html#_configuration_references) for details.

## Multiple Datasources

You can bind Doma’s configurations to each datasource as follows:

```properties

    # default datasource
    quarkus.datasource.db-kind=h2
    quarkus.datasource.username=username-default
    quarkus.datasource.jdbc.url=jdbc:h2:tcp://localhost/mem:default
    quarkus.datasource.jdbc.min-size=3
    quarkus.datasource.jdbc.max-size=13

    # inventory datasource
    quarkus.datasource.inventory.db-kind=h2
    quarkus.datasource.inventory.username=username2
    quarkus.datasource.inventory.jdbc.url=jdbc:h2:tcp://localhost/mem:inventory
    quarkus.datasource.inventory.jdbc.min-size=2
    quarkus.datasource.inventory.jdbc.max-size=12

    # Doma's configuration bound to the above default datasource
    quarkus.doma.dialect=h2
    quarkus.doma.batch-size=10
    quarkus.doma.fetch-size=50
    quarkus.doma.max-rows=500
    quarkus.doma.query-timeout=5000
    quarkus.doma.sql-load-script=import.sql

    # Doma's configuration bound to the above inventory datasource
    quarkus.doma.inventory.dialect=h2
    quarkus.doma.inventory.batch-size=10
    quarkus.doma.inventory.fetch-size=50
    quarkus.doma.inventory.max-rows=500
    quarkus.doma.inventory.query-timeout=5000
    quarkus.doma.inventory.sql-load-script=import.sql

You can inject the named Doma’s resource using the `io.quarkus.agroal.DataSource` qualifier as follows:

```java

    @Inject
    Config defaultConfig;

    @Inject
    Entityql defaultEntityql;

    @Inject
    NativeSql defaultNativeSql;

    @Inject
    @DataSource("inventory")
    Config inventoryConfig;

    @Inject
    @DataSource("inventory")
    Entityql inventoryEntityql;

    @Inject
    @DataSource("inventory")
    NativeSql inventoryNativeSql;

## Support for native images

The quarkus-doma extension automatically recognizes reflective classes and resources. These are included in your native image without requiring any additional configuration.

# Sample project

[domaframework/quarkus-sample](https://github.com/domaframework/quarkus-sample)
