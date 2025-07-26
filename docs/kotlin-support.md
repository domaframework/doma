# Kotlin support

```{contents}
:depth: 4
```

Doma supports [Kotlin](https://kotlinlang.org/) 1.4.0 or later.

## Best practices

Here are some recommended approaches for defining classes and working with Kotlin in Doma.

### Entity classes

- Define as a plain class
- Specify a `Metamodel` annotation in the `metamodel` element of `@Entity`

```kotlin
@Entity(metamodel = Metamodel())
class Person : AbstractPerson() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = -1

    var name: Name? = null

    var age: Int? = -1

    var address: Address? = null

    @Column(name = "DEPARTMENT_ID")
    var departmentId: Int = -1

    @Version
    var version: Int = -1
}
```

### Domain classes

- Define as a data class
- Define only one constructor
- Define only one property named `value` in the constructor
- Use `val` for the property definition

```kotlin
@Domain(valueType = String::class)
data class Name(val value: String)
```

### Embeddable classes

- Define as a data class
- Define only one constructor
- Define properties only in the constructor
- Use `val` for the property definitions

```kotlin
@Embeddable
data class Address(val city: String, val street: String)
```

### Dao interfaces

- Specify a SQL template with `@org.seasar.doma.Sql`

```kotlin
@Dao
interface PersonDao {
  @Sql("""
  select * from person where id = /*id*/0
  """)
  @Select
  fun selectById(id: Int): Person

  @Insert
  fun insert(person: Person): Int
}
```

```kotlin
val dao: PersonDao = new PersonDaoImpl(config)
val person = Person(name = Name("John"), address = Address(city = "Tokyo", street = "Yaesu"))
val count = dao.insert(person)
```

(kotlin-specific-criteria-api)=

### Kotlin-specific Criteria API

:::{note}
It is recommended to use the Kotlin-specific Criteria API rather than DAO interfaces.
:::

Doma provides `KQueryDsl`, a Criteria API specifically designed for Kotlin.
It is very similar to the `QueryDsl`, which is described in [Unified Criteria API](query-dsl.md).
The main advantage of `KQueryDsl` is its simplicity.

```kotlin
val queryDsl = KQueryDsl(config)
val e = Employee_()

val list = queryDsl
    .from(e)
    .where {
        eq(e.departmentId, 2)
        isNotNull(e.managerId)
        or {
            gt(e.salary, Salary("1000"))
            lt(e.salary, Salary("2000"))
        }
    }
    .fetch()
```

You can find more sample code [here](https://github.com/domaframework/kotlin-sample).

The `KQueryDsl` is included in the doma-kotlin module.
Note that you should use doma-kotlin instead of doma-core in your build script.
You can configure your build.gradle.kts as follows:

```kotlin
dependencies {
    implementation("org.seasar.doma:doma-kotlin:{{ doma_version }}")
}
```

### Code Generation

Use [Doma CodeGen Plugin](codegen.md).
This plugin supports Kotlin code generation.

### Using kapt in Gradle

Annotation processors are supported in Kotlin with the
[kapt](https://kotlinlang.org/docs/reference/kapt.html) compiler plugin.

Add the dependencies using the `kapt` and `implementation` configurations in your dependencies block.
For example, you can write build.gradle.kts as follows:

```kotlin
dependencies {
    kapt("org.seasar.doma:doma-processor:{{ doma_version }}")
    implementation("org.seasar.doma:doma-kotlin:{{ doma_version }}")
}
```

To simplify your build script, we recommend using
the [Doma Compile Plugin](https://github.com/domaframework/doma-compile-plugin).

## Sample project

- [kotlin-sample](https://github.com/domaframework/kotlin-sample)
