===================
Doma CodeGen Plugin
===================

.. contents::
   :depth: 4

Overview
========

The `Doma CodeGen Plugin <https://plugins.gradle.org/plugin/org.domaframework.doma.codegen>`_ is a Gradle plugin 
that generates Java, Kotlin, and SQL files from a database schema.

Key Benefits
------------

- **Database-First Development**: Generate type-safe entity and DAO classes directly from your existing database schema
- **Multi-Language Support**: Generate both Java and Kotlin code with the same configuration
- **SQL Template Generation**: Automatically create SQL template files for common READ operations
- **Testcontainers Integration**: Seamlessly work with Testcontainers for database testing and code generation
- **Customizable**: Use custom templates to control the generated code structure and style
- **Multiple Database Support**: Works with PostgreSQL, MySQL, Oracle, H2, and other JDBC-compatible databases

Use Cases
---------

- **Rapid Prototyping**: Quickly bootstrap data access layers from database designs
- **Schema Evolution**: Keep your code in sync with database schema changes
- **Team Development**: Ensure consistent entity and DAO implementations across team members

.. admonition:: Are you looking for documentation for Ant-based Doma-Gen?
    :class: important

    Documentation for Ant-based Doma-Gen is available at 
    `the Doma-Gen GitHub repository <https://github.com/domaframework/doma-gen/tree/master/docs>`_.

    Please note that Ant-based Doma-Gen is no longer maintained. We recommend using the Doma CodeGen Plugin described on this page instead.

Getting Started
===============

Prerequisites
-------------

- Gradle 8.0 or higher
- Java 17 or higher
- Access to a database (can be local, remote, or Testcontainers-based)

Step-by-Step Setup
------------------

1. **Add the Plugin**

   Add the Doma CodeGen plugin to your Gradle build file:

   .. code-block:: kotlin

       plugins {
           java
           id("org.domaframework.doma.codegen") version "{{ doma_codegen_version }}"
       }

2. **Configure Dependencies**

   Add the necessary JDBC driver dependency:

   .. code-block:: kotlin

       dependencies {
           // For code generation
           domaCodeGen("org.postgresql:postgresql:42.7.7")
       }

3. **Set up Local PostgreSQL Database**

   Make sure you have PostgreSQL installed and running locally. Create a database and tables:

   .. code-block:: sql

       -- Connect to PostgreSQL and create database
       CREATE DATABASE myapp;
       
       -- Switch to the new database and create tables
       CREATE TABLE users (
           id SERIAL PRIMARY KEY,
           name VARCHAR(100) NOT NULL,
           email VARCHAR(255) UNIQUE NOT NULL,
           version INTEGER NOT NULL DEFAULT 1,
           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
       );

       CREATE TABLE orders (
           id SERIAL PRIMARY KEY,
           user_id INTEGER REFERENCES users(id),
           total_amount DECIMAL(10,2) NOT NULL,
           order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
           version INTEGER NOT NULL DEFAULT 1
       );

4. **Configure Code Generation**

   Complete build.gradle.kts example:

   .. code-block:: kotlin

       plugins {
           java
           id("org.domaframework.doma.codegen") version "{{ doma_codegen_version }}"
       }

       dependencies {
           // Code generation dependencies
           domaCodeGen("org.postgresql:postgresql:42.7.7")
       }

       domaCodeGen {
           val basePackage = "com.example.myapp"

           register("postgresql") {
               // Database connection to local PostgreSQL
               url.set("jdbc:postgresql://localhost:5432/myapp")
               user.set("postgres")  // Replace with your PostgreSQL username
               password.set("password")  // Replace with your PostgreSQL password
               
               // Entity generation settings
               entity {
                   packageName.set("$basePackage.entity")
                   useAccessor.set(true)           // Generate getters/setters
                   useListener.set(true)           // Generate entity listeners
                   showDbComment.set(true)         // Include database comments
               }
               
               // DAO generation settings
               dao {
                   packageName.set("$basePackage.dao")
               }
           }
       }

5. **Generate Code**

   Run the code generation task:

   .. code-block:: sh

       $ ./gradlew domaCodeGenPostgresqlAll

   This will generate:
   
   - Entity classes in ``src/main/java/com/example/myapp/entity/``
   - DAO interfaces in ``src/main/java/com/example/myapp/dao/``
   - SQL template files in ``src/main/resources/META-INF/com/example/myapp/dao/``
   - Test classes in ``src/test/java/com/example/myapp/dao/``

What Gets Generated
-------------------

After running the code generation, you'll find the following files:

**Entity Classes**

.. code-block:: java

    @Entity
    @Table(name = "users")
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Integer id;
        
        public String name;
        
        public String email;
        
        @Version
        public Integer version;
        
        @Column(name = "created_at")
        public Timestamp createdAt;
    }

**DAO Interfaces**

.. code-block:: java

    @Dao
    public interface UserDao {
        @Select
        User selectById(Integer id);
        
        @Insert
        Result<User> insert(User entity);
        
        @Update
        Result<User> update(User entity);
        
        @Delete
        Result<User> delete(User entity);
    }

**SQL Templates**

.. code-block:: sql

    SELECT /*%expand*/* FROM users WHERE id = /* id */1


Database Examples
=================

The following examples show how to configure different database types:

PostgreSQL
----------

.. code-block:: kotlin

    dependencies {
        domaCodeGen("org.postgresql:postgresql:42.7.7")
    }

    domaCodeGen {
        register("postgresql") {
            url.set("jdbc:postgresql://localhost:5432/mydatabase")
            user.set("myuser")
            password.set("mypassword")
            entity {
                packageName.set("com.example.postgresql.entity")
            }
            dao {
                packageName.set("com.example.postgresql.dao")
            }
        }
    }

PostgreSQL with Testcontainers
-------------------------------

.. code-block:: kotlin

    dependencies {
        domaCodeGen(platform("org.testcontainers:testcontainers-bom:1.21.2"))
        domaCodeGen("org.postgresql:postgresql:42.7.7")
        domaCodeGen("org.testcontainers:postgresql")
    }

    domaCodeGen {
        register("postgresql") {
            val initScript = file("src/main/resources/schema-postgresql.sql")
            url.set("jdbc:tc:postgresql:15:///test?TC_INITSCRIPT=file:${initScript.absolutePath}")
            user.set("test")
            password.set("test")
            entity {
                packageName.set("com.example.postgresql.entity")
            }
            dao {
                packageName.set("com.example.postgresql.dao")
            }
        }
    }

MySQL with Testcontainers
--------------------------

.. code-block:: kotlin

    dependencies {
        domaCodeGen(platform("org.testcontainers:testcontainers-bom:1.21.2"))
        domaCodeGen("mysql:mysql-connector-java:8.0.33")
        domaCodeGen("org.testcontainers:mysql")
    }

    domaCodeGen {
        register("mysql") {
            val initScript = file("src/main/resources/schema-mysql.sql")
            url.set("jdbc:tc:mysql:8.0:///test?TC_INITSCRIPT=file:${initScript.absolutePath}")
            user.set("test")
            password.set("test")
            entity {
                packageName.set("com.example.mysql.entity")
            }
            dao {
                packageName.set("com.example.mysql.dao")
            }
        }
    }

Gradle Tasks
====================

The Doma CodeGen Plugin provides the following tasks:

- domaCodeGenXxxAll - Generates all.
- domaCodeGenXxxDao - Generates DAO source files.
- domaCodeGenXxxDto - Reads ResultSet metadata and generate a DTO source file.
- domaCodeGenXxxEntity - Generates Entity source files.
- domaCodeGenXxxSql - Generates SQL files.
- domaCodeGenXxxSqlTest - Generates SQL test source files.

Note that the *Xxx* part in each task name is replaced with the block name defined under the ``domaCodeGen`` block.
In the usage example above, the *Postgresql* part corresponds to the ``postgresql`` block.

To check all defined task names, run the `tasks` task:

.. code-block:: sh

    $ ./gradlew tasks

Configuration Reference
========================

Named Configuration
--------------------

A named configuration must be defined under the ``domaCodeGen`` block.
You can choose any name for your configuration.
Multiple configurations can be defined to support different databases or environments.

**Example: Multiple Database Configurations**

.. code-block:: kotlin

    domaCodeGen {
        register("sales") {
            url.set("jdbc:postgresql://localhost:5432/sales")
            user.set("sales_user")
            password.set("sales_pass")
            entity {
                packageName.set("com.example.sales.entity")
            }
            dao {
                packageName.set("com.example.sales.dao")
            }
        }

        register("inventory") {
            url.set("jdbc:mysql://localhost:3306/inventory")
            user.set("inventory_user")
            password.set("inventory_pass")
            entity {
                packageName.set("com.example.inventory.entity")
            }
            dao {
                packageName.set("com.example.inventory.dao")
            }
        }
    }

This generates separate task sets for each database:

.. code-block:: sh

    $ ./gradlew domaCodeGenSalesAll      # Generate all for sales DB
    $ ./gradlew domaCodeGenInventoryAll  # Generate all for inventory DB

Main Configuration Options
--------------------------

These options are configured at the top level of each named configuration block:

.. list-table::
   :widths: 20 40 20 20
   :header-rows: 1

   * - Option
     - Description
     - Example Values
     - Default
   * - **url**
     - JDBC connection URL to your database
     - ``jdbc:postgresql://localhost:5432/mydb``
     - *Required*
   * - **user**
     - Database username for authentication
     - ``myuser``
     - *Required*
   * - **password**
     - Database password for authentication
     - ``mypass``
     - *Required*
   * - dataSource
     - Custom data source class (advanced)
     -
     - inferred from URL
   * - codeGenDialect
     - Database dialect for SQL generation (advanced)
     -
     - inferred from URL
   * - catalogName
     - Database catalog name to filter tables
     - ``sales_catalog``
     - 
   * - schemaName
     - Database schema name to filter tables
     - ``public``, ``dbo``, ``hr``
     - 
   * - tableNamePattern
     - Regex pattern to include specific tables
     - ``user_.*`` (tables starting with "user\_")
     - ``.*`` (all tables)
   * - ignoredTableNamePattern
     - Regex pattern to exclude tables
     - ``temp_.*`` (ignore temp tables)
     - ``.*$.*`` (ignore system tables)
   * - tableTypes
     - Types of database objects to include
     - ``["TABLE", "VIEW"]`` (include tables and views)
     - ``["TABLE"]``
   * - versionColumnNamePattern
     - Regex to identify version columns
     - ``VERSION([_]?NO)?`` or ``.*_version``
     - ``VERSION([_]?NO)?``
   * - languageType
     - Target programming language
     - ``LanguageType.JAVA`` or ``LanguageType.KOTLIN`` [#]_
     - ``LanguageType.JAVA``
   * - templateDir
     - Directory containing custom FreeMarker templates
     - ``file("$projectDir/custom-templates")``
     - 
   * - encoding
     - Text encoding for generated source files
     - ``UTF-8``, ``Shift_JIS``
     - ``UTF-8``
   * - sourceDir
     - Output directory for generated source files
     - ``src/main/java``, ``src/main/kotlin``
     - depends on language
   * - resourceDir
     - Output directory for generated SQL files
     - ``src/main/resources``
     - ``src/main/resources``
   * - globalFactory
     - entry point to customize plugin behavior
     -
     - [#]_ The instance of `GlobalFactory`

Entity Configuration
--------------------

The ``entity`` block configures how entity classes are generated. This block must be defined within a named configuration.

**Basic Example**

.. code-block:: kotlin

    domaCodeGen {
        register("sales") {
            entity {
                packageName.set("com.example.sales.entity")
                useAccessor.set(true)           // Generate getters/setters
                useListener.set(true)           // Generate entity listeners
                showDbComment.set(true)         // Include database comments
                prefix.set("Sales")             // Add prefix to class names
            }
        }
    }

**Advanced Example**

.. code-block:: kotlin

    domaCodeGen {
        register("enterprise") {
            entity {
                packageName.set("com.enterprise.domain.entity")
                superclassName.set("com.enterprise.core.BaseEntity")    // Common base class
                listenerSuperclassName.set("com.enterprise.core.BaseEntityListener")
                useMetamodel.set(true)          // Generate metamodel classes
                useMappedSuperclass.set(true)   // Use @MappedSuperclass
                originalStatesPropertyName.set("originalStates")  // Property for @OriginalStates
                showTableName.set(false)        // Don't show @Table annotations
                showColumnName.set(false)       // Don't show @Column annotations
            }
        }
    }

.. list-table::
   :widths: 25 25 25 25
   :header-rows: 1

   * - Option
     - Description
     - Values
     - Default
   * - overwrite
     - where to overwrite generated entity files or not
     - 
     - `true`
   * - overwriteListener
     - allow to overwrite listeners or not
     - 
     - `false`
   * - superclassName
     - common superclass for generated entity classes
     - 
     - 
   * - listenerSuperclassName
     - common superclass for generated entity listener classes
     - 
     - 
   * - packageName
     - package name for generated entity class
     - 
     - "example.entity"
   * - generationType
     - generation type for entity identities
     - [#]_ enum value of `GenerationType`
     - 
   * - namingType
     - naming convention
     - [#]_ enum value of `NamingType`
     - 
   * - initialValue
     - initial value for entity identities
     - 
     - 
   * - allocationSize
     - allocation size for entity identities
     - 
     - 
   * - showCatalogName
     - whether to show catalog names or not
     - 
     - `false`
   * - showSchemaName
     - whether to show schema names or not
     - 
     - `false`
   * - showTableName
     - whether to show table names or not
     - 
     - `true`
   * - showColumnName
     - whether to show column names or not
     - 
     - `true`
   * - showDbComment
     - whether to show database comments or not
     - 
     - `true`
   * - useAccessor
     - whether to use accessors or not
     - 
     - `true`
   * - useListener
     - whether to use listeners or not
     - 
     - `true`
   * - useMetamodel
     - whether to use metamodels or not
     - 
     - `true`
   * - useMappedSuperclass
     - whether to use mapped superclasses or not
     - 
     - `true`
   * - originalStatesPropertyName
     - property to be annotated with `@OriginalStates`
     - 
     - 
   * - entityPropertyClassNamesFile
     - file used to resolve entity property classes
     - 
     - 
   * - prefix
     - prefix for entity classes
     - 
     - 
   * - suffix
     - suffix for entity classes
     - 
     -

DAO Configuration
-----------------

The ``dao`` block configures how DAO (Data Access Object) interfaces are generated.

**Basic Example**

.. code-block:: kotlin

    domaCodeGen {
        register("sales") {
            dao {
                packageName.set("com.example.sales.dao")
                suffix.set("Repository")         // Use "Repository" instead of "Dao"
            }
        }
    }

.. list-table::
   :widths: 25 25 25 25
   :header-rows: 1

   * - Option
     - Description
     - Values
     - Default
   * - overwrite
     - whether to overwrite generated DAO files or not
     - 
     - ``false``
   * - packageName
     - package name for generated DAO classes
     - 
     - "example.dao"
   * - suffix
     - suffix for Dao classes
     - 
     - "Dao"

SQL Configuration
-----------------

The ``sql`` block configures how SQL template files are generated.

.. code-block:: kotlin

    domaCodeGen {
        register("sales") {
            sql {
                overwrite.set(true)             // Overwrite existing SQL files
            }
        }
    }

.. note::
   SQL files are generated in ``src/main/resources/META-INF/<package>/dao/`` directory.
   These include basic READ operations like ``selectById.sql`` and ``selectByIdAndVersion.sql``.

.. list-table::
   :widths: 25 25 25 25
   :header-rows: 1

   * - Option
     - Description
     - Values
     - Default
   * - overwrite
     - whether to overwrite generated sql files or not
     - 
     - ``true``

SQL Test Configuration
----------------------

The ``sqlTest`` block configures generation of SQL test files and can use a different database for testing.

**Example: Separate Test Database**

.. code-block:: kotlin

    domaCodeGen {
        register("production") {
            // Main database configuration
            url.set("jdbc:postgresql://prod-db:5432/myapp")
            user.set("prod_user")
            password.set("prod_pass")
            
            // Test database configuration
            sqlTest {
                url.set("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
                user.set("sa")
                password.set("")
            }
        }
    }

**Example: Same Database for Tests**

.. code-block:: kotlin

    domaCodeGen {
        register("development") {
            url.set("jdbc:tc:postgresql:15:///test")
            user.set("test")
            password.set("test")
            
            sqlTest {
                // Uses same connection as main configuration
                // No need to specify url, user, password again
            }
        }
    }

.. list-table::
   :widths: 20 50 30
   :header-rows: 1

   * - Option
     - Description
     - Default
   * - url
     - JDBC URL for test database (can be different from main)
     - Same as main configuration
   * - user
     - Database username for test database
     - Same as main configuration
   * - password
     - Database password for test database
     - Same as main configuration

Customization
====================

Generating Kotlin code
----------------------

To generate Kotlin code, set the languageType option to ``LanguageType.KOTLIN`` as follows:

.. code-block:: kotlin

    import org.seasar.doma.gradle.codegen.desc.LanguageType

    ...

    domaCodeGen {
        register("dev") {
            url.set("jdbc:postgresql://localhost:5432/mydatabase")
            user.set("myuser")
            password.set("mypassword")
            languageType.set(LanguageType.KOTLIN)
            entity {
                packageName.set("org.example.entity")
            }
            dao {
                packageName.set("org.example.dao")
            }
        }
    }


Template Customization
-----------------------

The Doma CodeGen Plugin uses `Apache FreeMarker <https://freemarker.apache.org/>`_ templates to generate code. You can customize these templates to match your project's coding standards and requirements.

Available Templates
~~~~~~~~~~~~~~~~~~~

The default template files can be found in `the source code repository <https://github.com/domaframework/doma-codegen-plugin/tree/master/codegen/src/main/resources/org/seasar/doma/gradle/codegen/template>`_.

.. list-table::
   :widths: 25 35 40
   :header-rows: 1

   * - Template File
     - Purpose
     - Generated Output
   * - entity.ftl
     - Entity class generation
     - Java/Kotlin entity classes with JPA annotations
   * - entityListener.ftl
     - Entity listener generation
     - Entity listener classes for lifecycle callbacks
   * - dao.ftl
     - DAO interface generation
     - DAO interfaces with basic CRUD methods
   * - sqlTest.ftl
     - SQL test generation
     - Test classes for validating SQL files
   * - selectById.sql.ftl
     - Basic select SQL
     - SQL files for selecting by primary key
   * - selectByIdAndVersion.sql.ftl
     - Optimistic locking SQL
     - SQL files for selecting with version checking

Setting Up Custom Templates
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

1. **Create Template Directory**

   .. code-block:: text

       your-project/
       ├── custom-templates/
       │   ├── entity.ftl
       │   ├── dao.ftl
       │   └── entityListener.ftl
       └── build.gradle.kts

2. **Configure Template Directory**

   .. code-block:: kotlin

       domaCodeGen {
           register("mydb") {
               url.set("jdbc:postgresql://localhost:5432/mydb")
               user.set("user")
               password.set("pass")
               templateDir.set(file("$projectDir/custom-templates"))
               entity {
                   packageName.set("com.example.entity")
               }
               dao {
                   packageName.set("com.example.dao")
               }
           }
       }

3. **Customize Entity Template**

   Create ``custom-templates/entity.ftl`` to add custom annotations:

   .. code-block:: text

       <#-- Custom entity template with additional annotations -->
       package ${entityDesc.packageName};
       
       import java.io.Serializable;
       import org.seasar.doma.*;
       import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
       import lombok.Data;
       
       /**
        * Entity for ${entityDesc.tableName} table.
        <#if entityDesc.comment??> * ${entityDesc.comment}</#if>
        */
       @Entity<#if entityDesc.tableName??>(table = @Table(name = "${entityDesc.tableName}"))</#if>
       @Data                              // Lombok annotation
       @JsonIgnoreProperties(ignoreUnknown = true)  // Jackson annotation
       public class ${entityDesc.simpleName} implements Serializable {
       
       <#list entityDesc.propertyDescs as property>
           <#if property.id>
           @Id
           <#if property.generationType??>
           @GeneratedValue(strategy = GenerationType.${property.generationType})
           </#if>
           </#if>
           <#if property.version>
           @Version
           </#if>
           <#if property.columnName??>
           @Column(name = "${property.columnName}")
           </#if>
           public ${property.propertyClassName} ${property.propertyName};
       
       </#list>
       }

4. **Customize DAO Template**

   Create ``custom-templates/dao.ftl`` for custom DAO methods:

   .. code-block:: text

       <#-- Custom DAO template with additional methods -->
       package ${daoDesc.packageName};
       
       import org.seasar.doma.*;
       import org.springframework.transaction.annotation.Transactional;
       import java.util.List;
       import java.util.Optional;
       
       /**
        * DAO for ${daoDesc.entityDesc.simpleName}.
        */
       @Dao<#if daoDesc.configClassName??>(config = ${daoDesc.configClassName}.class)</#if>
       @Transactional  // Spring transaction annotation
       public interface ${daoDesc.simpleName} {
       
           @Select
           Optional<${daoDesc.entityDesc.simpleName}> selectById(${daoDesc.entityDesc.idPropertyDesc.propertyClassName} ${daoDesc.entityDesc.idPropertyDesc.propertyName});
           
           @Select
           List<${daoDesc.entityDesc.simpleName}> selectAll();
           
           @Select
           List<${daoDesc.entityDesc.simpleName}> selectByExample(${daoDesc.entityDesc.simpleName} example);
           
           @Insert
           Result<${daoDesc.entityDesc.simpleName}> insert(${daoDesc.entityDesc.simpleName} entity);
           
           @Update
           Result<${daoDesc.entityDesc.simpleName}> update(${daoDesc.entityDesc.simpleName} entity);
           
           @Delete
           Result<${daoDesc.entityDesc.simpleName}> delete(${daoDesc.entityDesc.simpleName} entity);
           
           @BatchInsert
           BatchResult<${daoDesc.entityDesc.simpleName}> batchInsert(List<${daoDesc.entityDesc.simpleName}> entities);
       }

Common Template Variables
~~~~~~~~~~~~~~~~~~~~~~~~~

The following variables are available in templates:

**Entity Templates**

- ``entityDesc.packageName`` - Package name for the entity
- ``entityDesc.simpleName`` - Simple class name (e.g., "User")
- ``entityDesc.tableName`` - Database table name
- ``entityDesc.comment`` - Table comment from database
- ``entityDesc.propertyDescs`` - List of property descriptors

**DAO Templates**

- ``daoDesc.packageName`` - Package name for the DAO
- ``daoDesc.simpleName`` - Simple interface name (e.g., "UserDao")
- ``daoDesc.entityDesc`` - Associated entity descriptor
- ``daoDesc.configClassName`` - Doma config class name

**Property Descriptors**

- ``property.propertyName`` - Java property name (e.g., "userId")
- ``property.propertyClassName`` - Java type (e.g., "Integer")
- ``property.columnName`` - Database column name
- ``property.id`` - True if primary key
- ``property.version`` - True if version column
- ``property.comment`` - Column comment from database

Advanced Template Features
~~~~~~~~~~~~~~~~~~~~~~~~~~~

**Conditional Generation**

.. code-block:: text

    <#-- Only generate if table has a version column -->
    <#if entityDesc.versionPropertyDesc??>
    @Version
    public ${entityDesc.versionPropertyDesc.propertyClassName} ${entityDesc.versionPropertyDesc.propertyName};
    </#if>

**Custom Imports Based on Properties**

.. code-block:: text

    <#-- Import specific types based on entity properties -->
    <#assign hasTimestamp = false>
    <#list entityDesc.propertyDescs as property>
        <#if property.propertyClassName == "java.sql.Timestamp">
            <#assign hasTimestamp = true>
        </#if>
    </#list>
    
    <#if hasTimestamp>
    import java.sql.Timestamp;
    </#if>

Troubleshooting
===============

Common Issues and Solutions
---------------------------

**Problem: "No suitable driver found" Error**

.. code-block:: text

    [DOMAGEN0033] The class "org.postgresql.Driver" to which the parameter "driverClassName" refers is not found.

*Solution:* Make sure you've added the JDBC driver dependency to the ``domaCodeGen`` configuration:

.. code-block:: kotlin

    dependencies {
        domaCodeGen("org.postgresql:postgresql:42.7.7")
    }

**Problem: Generated Code in Wrong Package**

*Solution:* Check your package configuration:

.. code-block:: kotlin

    entity {
        packageName.set("com.example.entity")  // Ensure this is set correctly
    }
    dao {
        packageName.set("com.example.dao")     // Ensure this is set correctly
    }

**Problem: Custom Templates Not Applied**

*Solution:* Verify template directory structure and filenames:

.. code-block:: text

    your-project/
    ├── template/
    │   ├── entity.ftl           # Must match exact filename
    │   ├── dao.ftl
    │   └── entityListener.ftl
    └── build.gradle.kts

.. code-block:: kotlin

    domaCodeGen {
        register("mydb") {
            templateDir.set(file("$projectDir/template"))  // Point to template directory
        }
    }

Best Practices
--------------

1. **Use Testcontainers for Development**
   
   Testcontainers ensure consistent database environments across different machines:

   .. code-block:: kotlin

       // Preferred approach
       url.set("jdbc:tc:postgresql:15:///test?TC_INITSCRIPT=file:${initScript.absolutePath}")

2. **Use Version Control for Schema Files**
   
   Keep your initialization scripts in version control:

   .. code-block:: text

       src/main/resources/
       ├── schema-postgresql.sql
       ├── schema-mysql.sql
       └── test-data.sql

3. **Incremental Generation**
   
   Use specific tasks for faster development:

   .. code-block:: sh

       # Generate only entities (faster for schema changes)
       ./gradlew domaCodeGenMydbEntity
       
       # Generate only DAOs (faster for new tables)
       ./gradlew domaCodeGenMydbDao

Sample Project
====================

- `example-codegen-plugin <https://github.com/domaframework/simple-examples/tree/master/example-codegen-plugin>`_
- `kotlin-sample <https://github.com/domaframework/kotlin-sample>`_

Footnote
====================

.. [#] The FQN of ``LanguageType`` is ``org.seasar.doma.gradle.codegen.desc.LanguageType``
.. [#] The FQN of ``GlobalFactory`` is ``org.seasar.doma.gradle.codegen.GlobalFactory``
.. [#] The FQN of ``GenerationType`` is ``org.seasar.doma.gradle.codegen.desc.GenerationType``
.. [#] The FQN of ``NamingType`` is ``org.seasar.doma.gradle.codegen.NamingType``
