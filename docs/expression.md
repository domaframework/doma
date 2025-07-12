# Expression language

```{contents} Contents
:depth: 4
```

You can write simple expressions in [SQL directives](sql.md#directives).
The grammar is very similar to Java,
although not all Java features are supported.

:::{note}
A key difference is in how optional types like `java.util.Optional` are handled.
In expressions, values of `Optional` type are automatically converted
to values of their element type.
For example, a value of `Optional<String>` is treated as a `String`.
As a result, you cannot call methods on the `Optional` object itself,
nor can you call methods that take `Optional` parameters.

To check if an optional value exists, use `/*%if optional != null */`
instead of `/*%if optional.isPresent() */`.

This behavior applies to all optional types including `java.util.OptionalInt`,
`java.util.OptionalDouble`, and `java.util.OptionalLong`.
:::

## Literals

You can use the following literals:

| Literal | Type                 |
| ------- | -------------------- |
| null    | void                 |
| true    | boolean              |
| false   | boolean              |
| 10      | int                  |
| 10L     | long                 |
| 0.123F  | float                |
| 0.123D  | double               |
| 0.123B  | java.math.BigDecimal |
| 'a'     | char                 |
| "a"     | java.lang.String     |

Numeric types are distinguished by suffix letters such as `L` or `F`
at the end of the literals. Note that these suffixes must be capital letters.

```sql
select * from employee where
/*%if employeeName != null && employeeName.length() > 10 */
    employee_name = /* employeeName */'smith'
/*%end*/
```

## Comparison operators

You can use the following comparison operators:

| Operator | Description                       |
| -------- | --------------------------------- |
| ==       | Equal to operator                 |
| !=       | Not equal to operator             |
| \<       | Less than operator                |
| \<=      | Less than or equal to operator    |
| >        | Greater than operator             |
| >=       | Greater than or equal to operator |

To use comparison operators, the operands must implement `java.lang.Comparable`.

For the operators `<`, `<=`, `>`, and `>=`, the operands must not be `null`.

```sql
select * from employee where
/*%if employeeName.indexOf("s") > -1 */
    employee_name = /* employeeName */'smith'
/*%end*/
```

## Logical operators

You can use the following logical operators:

| Operator | Description          |
| -------- | -------------------- |
| !        | Logical NOT operator |
| &&       | Logical AND operator |
| \|\|     | Logical OR operator  |

You can use parentheses to control the precedence of operators.

```sql
select * from employee where
/*%if (departmentId == null || managerId == null) and employee_name != null */
    employee_name = /* employeeName */'smith'
/*%end*/
```

## Arithmetic operators

You can use the following arithmetic operators:

| Operator | Description             |
| -------- | ----------------------- |
| +        | Additive operator       |
| -        | Subtraction operator    |
| \*       | Multiplication operator |
| /        | Division operator       |
| %        | Remainder operator      |

All operands must be of numeric type.

```sql
select * from employee where
    salary = /* salary + 1000 */0
```

## String concatenation operator

You can concatenate strings using the concatenation operator `+`.

The operand must be one of the following types:

- java.lang.String
- java.lang.Character
- char

```sql
select * from employee where
   employee_name like /* employeeName + "_" */'smith'
```

## Calling instance methods

You can call instance methods using dot notation (`.`).
The called methods must have public visibility.

```sql
select * from employee where
/*%if employeeName.startsWith("s") */
    employee_name = /* employeeName */'smith'
/*%end*/
```

If the method has no argument, specify `()` after the method name.

```sql
select * from employee where
/*%if employeeName.length() > 10 */
    employee_name = /* employeeName */'smith'
/*%end*/
```

## Accessing instance fields

You can access instance fields using dot notation (`.`).
Fields can be accessed regardless of their visibility, even if they are private.

```sql
select * from employee where
    employee_name = /* employee.employeeName */'smith'
```

## Calling static methods

You can call static methods by prefixing the method name
with the fully qualified class name enclosed in `@` symbols.
The method must have public visibility.

```sql
select * from employee where
/*%if @java.util.regex.Pattern@matches("^[a-z]*$", employeeName) */
    employee_name = /* employeeName */'smith'
/*%end*/
```

## Accessing to static fields

You can access static fields by prefixing the field name
with the fully qualified class name enclosed in `@` symbols.
Fields can be accessed regardless of their visibility, even if they are private.

```sql
select * from employee where
/*%if employeeName.length() < @java.lang.Byte@MAX_VALUE */
  employee_name = /* employeeName */'smith'
/*%end*/
```

## Using built-in functions

Built-in functions are utilities primarily designed to transform values of binding variables
before they are bound to SQL statements.

For example, when you run a prefix search with a LIKE clause,
you can write like this:

```sql
select * from employee where
    employee_name like /* @prefix(employee.employeeName) */'smith' escape '$'
```

`@prefix(employee.employeeName)` passes the value of `employee.employeeName`
to the `@prefix` function.
This function converts the received character sequence
into a string suitable for prefix (forward match) search.
It also escapes any special characters in the string.
For example, if the value of `employee.employeeName` is `ABC`, it's converted to `ABC%`.
If the value of `employee.employeeName` contains `%` such as `AB%C`,
the `%` is escaped with a default escape sequence `$`,
therefore the value is converted to `AB$%C%`.

You can use following function signatures:

String @escape(CharSequence text, char escapeChar = '\$')

: Escapes the character sequence for LIKE operation.
  The return value is a string which is a result of escaping the character sequence.
  If `escapeChar` isn't specified, `$` is used as a default escape sequence.
  It returns `null` if you pass `null` as a parameter.

String @prefix(CharSequence prefix, char escapeChar = '\$')

: Converts the character sequence for prefix search.
  The return value is a string which is a result of escaping the character sequence
  and adding a wild card character at the end.
  If `escapeChar` isn't specified, `$` is used as a default escape sequence.
  It returns `null` if you pass `null` as a parameter.

String @infix(CharSequence infix, char escapeChar = '\$')

: Converts the character sequence for infix search.
  The return value is a string which is a result of escaping the character sequence
  and adding wild card characters at the beginning and the end.
  If `escapeChar` isn't specified, `$` is used as a default escape sequence.
  It returns `null` if you pass `null` as a parameter.

String @suffix(CharSequence suffix, char escapeChar = '\$')

: Converts the character sequence for suffix search.
  The return value is a string which is a result of escaping the character sequence
  and adding a wild card character at the beginning.
  If `escapeChar` isn't specified, `$` is used as a default escape sequence.
  It returns `null` if you pass `null` as a parameter.

java.util.Date @roundDownTimePart(java.util.Date date)

: Rounds down the time part.
  The return value is a new Date which is rounded down the time part.
  It returns `null` if you pass `null` as a parameter.

java.sql.Date @roundDownTimePart(java.sql.Date date)

: Rounds down the time part.
  The return value is a new Date which is rounded down the time part.
  It returns `null` if you pass `null` as a parameter.

java.sql.Timestamp @roundDownTimePart(java.sql.Timestamp timestamp)

: Rounds down the time part.
  The return value is a new Timestamp which is rounded down the time part.
  It returns `null` if you pass `null` as a parameter.

java.time.LocalDateTime @roundDownTimePart(java.time.LocalDateTime localDateTime)

: Rounds down the time part.
  The return value is a new LocalDateTime which is rounded down the time part.
  It returns `null` if you pass `null` as a parameter.

java.util.Date @roundUpTimePart(java.util.Date date)

: Rounds up the time part.
  The return value is a new Date which is rounded up the time part.
  It returns `null` if you pass `null` as a parameter.

java.sql.Date @roundUpTimePart(java.sql.Date date)

: Rounds up the time part.
  The return value is a new Date which is rounded up the time part.
  It returns `null` if you pass `null` as a parameter.

java.sql.Timestamp @roundUpTimePart(java.sql.Timestamp timestamp)

: Rounds up the time part.
  The return value is a new Timestamp which is rounded up the time part.
  It returns `null` if you pass `null` as a parameter.

java.time.LocalDateTime @roundUpTimePart(java.time.LocalDateTime localDateTime)

: Rounds up the time part.
  The return value is a new LocalDateTime which is rounded up the time part.
  It returns `null` if you pass `null` as a parameter.

java.time.LocalDate @roundUpTimePart(java.time.LocalDate localDate)

: Returns the next day.
  The return value is a new LocalDate which is the next one after the argument.
  It returns `null` if you pass `null` as a parameter.

boolean @isEmpty(CharSequence charSequence)

: Returns `true` if the character sequence is `null` or the length is `0`.

boolean @isNotEmpty(CharSequence charSequence)

: Returns `true` if the character sequence isn't `null` and the length isn't `0`.

boolean @isBlank(CharSequence charSequence)

: Returns `true` only if the character sequence is `null`, the length is `0`,
  or the sequence is formed with whitespaces only.

boolean @isNotBlank(CharSequence charSequence)

: Returns `true` if the character sequence isn't `null`, the length isn't `0`,
  and the sequence isn't formed with whitespaces only.

These functions are correspond to the methods of `org.seasar.doma.expr.ExpressionFunctions`.

## Using custom functions

You can define and use your own custom functions.

To use custom functions that you define, follow these steps:

- Define the function as a method in a class that implements
  `org.seasar.doma.expr.ExpressionFunctions`.
- The method must be a public instance method.
- Register the class as an option in the annotation processing configuration.
  Use `doma.expr.functions` as the option key.
- Use an instance of your class in the RDBMS dialect within your configuration class
  (Doma's RDBMS dialect implementations accept an `ExpressionFunctions` parameter
  in their constructors).

To call a custom function, add `@` at the beginning of the function name like built-in functions.
For example, you can call `myfunc` function like this:

```sql
select * from employee where
    employee_name = /* @myfunc(employee.employeeName) */'smith'
```
