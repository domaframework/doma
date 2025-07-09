=============
SLF4J support
=============

.. contents::
   :depth: 4

Overview
========

Doma uses Java Util Logging as its underlying logging framework,
but you can easily replace it with `SLF4J <http://www.slf4j.org/>`_.

Gradle
======

Doma provides the doma-slf4j artifact to integrate with SLF4J.

.. code-block:: kotlin

    dependencies {
        implementation("org.seasar.doma:doma-slf4j:{{ doma_version }}")
        // Use an arbitrary SLF4J binding
        runtimeOnly("ch.qos.logback:logback-classic:{{ logback_classic_version }}")
    }

Configuration
=============

Return an ``org.seasar.doma.slf4j.Slf4jJdbcLogger`` instance from
the ``getJdbcLogger`` method of your ``org.seasar.doma.jdbc.Config`` implementation class.

See also :ref:`Logger<config-logger>`.

Loggers
=======

The doma-slf4j module provides the following loggers:

* org.seasar.doma.jdbc.LogKind.DAO
* org.seasar.doma.jdbc.LogKind.FAILURE
* org.seasar.doma.jdbc.LogKind.LOCAL_TRANSACTION
* org.seasar.doma.jdbc.LogKind.SKIP.STATE_UNCHANGED
* org.seasar.doma.jdbc.LogKind.SKIP.BATCH_TARGET_NONEXISTENT
* org.seasar.doma.jdbc.LogKind.SQL.BATCH_DELETE
* org.seasar.doma.jdbc.LogKind.SQL.BATCH_INSERT
* org.seasar.doma.jdbc.LogKind.SQL.BATCH_UPDATE
* org.seasar.doma.jdbc.LogKind.SQL.DELETE
* org.seasar.doma.jdbc.LogKind.SQL.FUNCTION
* org.seasar.doma.jdbc.LogKind.SQL.INSERT
* org.seasar.doma.jdbc.LogKind.SQL.PROCEDURE
* org.seasar.doma.jdbc.LogKind.SQL.SCRIPT
* org.seasar.doma.jdbc.LogKind.SQL.SELECT
* org.seasar.doma.jdbc.LogKind.SQL.SQL_PROCESSOR
* org.seasar.doma.jdbc.LogKind.SQL.UPDATE

For more information, see the `source code <https://github.com/domaframework/doma/blob/master/doma-slf4j/src/main/java/org/seasar/doma/slf4j/Slf4jJdbcLogger.java>`_.

Examples
========

Below are some sample `logback <http://logback.qos.ch/>`_ configurations.

Log all
-------

.. code-block:: xml

    <configuration>
        <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
            <encoder>
                <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>

        <logger name="org.seasar.doma.jdbc.LogKind" level="debug"/>

        <root level="info">
            <appender-ref ref="STDOUT" />
        </root>
    </configuration>

Log all SQL statements
----------------------

.. code-block:: xml

    <configuration>
        <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
            <encoder>
                <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>

        <logger name="org.seasar.doma.jdbc.LogKind.SQL" level="debug"/>

        <root level="info">
            <appender-ref ref="STDOUT" />
        </root>
    </configuration>

Log only SELECT statements
--------------------------

.. code-block:: xml

    <configuration>
        <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
            <encoder>
                <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>

        <logger name="org.seasar.doma.jdbc.LogKind.SQL.SELECT" level="debug"/>

        <root level="info">
            <appender-ref ref="STDOUT" />
        </root>
    </configuration>
