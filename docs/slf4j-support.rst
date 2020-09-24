=============
SLF4J support
=============

.. contents::
   :depth: 3

Overview
========

Doma uses java util logging as underling logging framework,
but you can replace it with `SLF4J <http://www.slf4j.org/>`_ easily.

Gradle
======

Doma provides the doma-slf4j artifact to adapt SLF4J.

.. code-block:: xml

    dependencies {
        implementation("org.seasar.doma:doma-slf4j:2.42.0")
        // Use an arbitrary SLF4J binding
        implementation("ch.qos.logback:logback-classic:1.2.3")
    }

Configuration
=============

Return a ``org.seasar.doma.jdbc.Slf4jJdbcLogger`` instance from
the ``getJdbcLogger`` method of the ``org.seasar.doma.jdbc.Config`` implementation class.

See also :ref:`Logger<config-logger>`.

Loggers
=======

doma-slf4j provides several loggers as follows:

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

See `source code <https://github.com/domaframework/doma/blob/master/doma-slf4j/src/main/java/org/seasar/doma/jdbc/Slf4jJdbcLogger.java>`_
for more information. It's simple.

Examples
========

Below, we show you `logback <http://logback.qos.ch/>`_ configurations.

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
