===================
Expression language
===================

.. contents:: Contents
   :depth: 3

You can write simple expressions in directives of :doc:`sql`.
The grammar is almost the same as Java.
However, not everything is possible that Java can do.

.. note::

  Especially, the big difference is how to use optional types like ``java.util.Optional``.
  In the expression, a value of ``Optional`` type is always converted
  to a value of the element type automatically.
  For example a value of the ``Optional<String>`` type is treated as a value of ``String`` type.
  Therefore, we can't call methods of ``Optional`` type,
  nor do we call methods which have an ``Optional`` type in the parameters.

  When you want to check existence of a value, use ``/*%if optional != null */``
  instead of ``/*%if optional.isPresent() */``.

  The same is true for ``java.util.OptionalInt``, ``java.util.OptionalDouble``,
  and ``java.util.OptionalLong``.

Literals
========

You can use the following literals:

+----------+----------------------+
| Literal  | Type                 |
+==========+======================+
| null     | void                 |
+----------+----------------------+
| true     | boolean              |
+----------+----------------------+
| false    | boolean              |
+----------+----------------------+
| 10       | int                  |
+----------+----------------------+
| 10L      | long                 |
+----------+----------------------+
| 0.123F   | float                |
+----------+----------------------+
| 0.123D   | double               |
+----------+----------------------+
| 0.123B   | java.math.BigDecimal |
+----------+----------------------+
| 'a'      | char                 |
+----------+----------------------+
| "a"      | java.lang.String     |
+----------+----------------------+

The numeral types are distinguished by suffix letters such as ``L`` or ``F``
at the end of the literals. The suffixes must be capital letters.

.. code-block:: sql

  select * from employee where
  /*%if employeeName != null && employeeName.length() > 10 */
      employee_name = /* employeeName */'smith'
  /*%end*/

Comparison operators
====================

You can use the following comparison operators:

+-----------+-------------------------------------+
| Operator  |   Description                       |
+===========+=====================================+
| ==        |   Equal to operator                 |
+-----------+-------------------------------------+
| !=        |   Not equal to operator             |
+-----------+-------------------------------------+
| <         |   Less than operator                |
+-----------+-------------------------------------+
| <=        |   Less than or equal to operator    |
+-----------+-------------------------------------+
| >         |   Greater than operator             |
+-----------+-------------------------------------+
| >=        |   Greater than or equal to operator |
+-----------+-------------------------------------+

To use comparison operators, operands must implement ``java.lang.Comparable``.

The operands for ``<``, ``<=``, ``>`` and ``>=`` must not be ``null``.

.. code-block:: sql

  select * from employee where
  /*%if employeeName.indexOf("s") > -1 */
      employee_name = /* employeeName */'smith'
  /*%end*/

Logical operators
=================

You can use the following logical operators:

========= ===========================
Operator  Description
========= ===========================
!         Logical complement operator
&&        Conditional-AND operator
||        Conditional-OR operator
========= ===========================

With parentheses, you can override the precedence of operators.

.. code-block:: sql

  select * from employee where
  /*%if (departmentId == null || managerId == null) and employee_name != null */
      employee_name = /* employeeName */'smith'
  /*%end*/

Arithmetic operators
====================

You can use the following arithmetic operators:

+----------+----------------------------+
| Operator |    Description             |
+==========+============================+
| \+       |    Additive operator       |
+----------+----------------------------+
| \-       |    Subtraction operator    |
+----------+----------------------------+
| \*       |    Multiplication operator |
+----------+----------------------------+
| /        |    Division operator       |
+----------+----------------------------+
| %        |    Remainder operator      |
+----------+----------------------------+

Operands must be numeric type.

.. code-block:: sql

  select * from employee where
      salary = /* salary + 1000 */0

String concatenation operator
=============================

You can concatenate characters using a concatenation operator ``+``.

The operand must be one of the following types:

* java.lang.String
* java.lang.Character
* char

.. code-block:: sql

  select * from employee where
     employee_name like /* employeeName + "_" */'smith'

Calling instance methods
========================

You can call instance methods with the method names separated by dots ``.``.
The method visibility must be public.

.. code-block:: sql

  select * from employee where
  /*%if employeeName.startsWith("s") */
      employee_name = /* employeeName */'smith'
  /*%end*/

If the method has no argument, specify ``()`` after the method name.

.. code-block:: sql

  select * from employee where
  /*%if employeeName.length() > 10 */
      employee_name = /* employeeName */'smith'
  /*%end*/

Accessing to instance fields
============================

You can access instance fields with the field names separated by dots ``.``.
Even if the visibility is private, you can access it.

.. code-block:: sql

  select * from employee where
      employee_name = /* employee.employeeName */'smith'

Calling static methods
======================

You can call static methods by continuing the method names
with the fully qualified class names enclosed in ``@``.
The method visibility must be public.

.. code-block:: sql

  select * from employee where
  /*%if @java.util.regex.Pattern@matches("^[a-z]*$", employeeName) */
      employee_name = /* employeeName */'smith'
  /*%end*/

Accessing to static fields
==========================

You can access static fields by continuing the field name
with the fully qualified class name enclosed in ``@``.
Even if the visibility is private, you can access it.

.. code-block:: sql

  select * from employee where
  /*%if employeeName.length() < @java.lang.Byte@MAX_VALUE */
    employee_name = /* employeeName */'smith'
  /*%end*/

Using built-in functions
========================

Built-in functions are utilities mainly for changing values of binding variables
before binding them to SQL.

For example, when you run a prefix search with a LIKE clause,
you can write like this:

.. code-block:: sql

  select * from employee where
      employee_name like /* @prefix(employee.employeeName) */'smith' escape '$'

``@prefix(employee.employeeName)`` means that we pass ``employee.employeeName``
to the ``@prefix`` function.
The ``@prefix`` function converts the character sequence which is received by the parameter
to a string for forward match search.
It also escapes special characters.
For example, if the value of ``employee.employeeName`` is ``ABC``, it's converted to ``ABC%``.
If the value of ``employee.employeeName`` contains ``%`` such as ``AB%C``,
the ``%`` is escaped with a default escape sequence ``$``,
therefore the value is converted to ``AB$%C%``.

You can use following function signatures:

String @escape(CharSequence text, char escapeChar = '$')
  Escapes the character sequence for LIKE operation.
  The return value is a string which is a result of escaping the character sequence.
  If ``escapeChar`` isn't specified, ``$`` is used as a default escape sequence.
  It returns ``null`` if you pass ``null`` as a parameter.

String @prefix(CharSequence prefix, char escapeChar = '$')
  Converts the character sequence for prefix search.
  The return value is a string which is a result of escaping the character sequence
  and adding a wild card character at the end.
  If ``escapeChar`` isn't specified, ``$`` is used as a default escape sequence.
  It returns ``null`` if you pass ``null`` as a parameter.

String @infix(CharSequence infix, char escapeChar = '$')
  Converts the character sequence for infix search.
  The return value is a string which is a result of escaping the character sequence
  and adding wild card characters at the beginning and the end.
  If ``escapeChar`` isn't specified, ``$`` is used as a default escape sequence.
  It returns ``null`` if you pass ``null`` as a parameter.

String @suffix(CharSequence suffix, char escapeChar = '$')
  Converts the character sequence for suffix search.
  The return value is a string which is a result of escaping the character sequence
  and adding a wild card character at the beginning.
  If ``escapeChar`` isn't specified, ``$`` is used as a default escape sequence.
  It returns ``null`` if you pass ``null`` as a parameter.

java.util.Date @roundDownTimePart(java.util.Date date)
  Rounds down the time part.
  The return value is a new Date which is rounded down the time part.
  It returns ``null`` if you pass ``null`` as a parameter.

java.sql.Date @roundDownTimePart(java.sql.Date date)
  Rounds down the time part.
  The return value is a new Date which is rounded down the time part.
  It returns ``null`` if you pass ``null`` as a parameter.

java.sql.Timestamp @roundDownTimePart(java.sql.Timestamp timestamp)
  Rounds down the time part.
  The return value is a new Timestamp which is rounded down the time part.
  It returns ``null`` if you pass ``null`` as a parameter.

java.time.LocalDateTime @roundDownTimePart(java.time.LocalDateTime localDateTime)
  Rounds down the time part.
  The return value is a new LocalDateTime which is rounded down the time part.
  It returns ``null`` if you pass ``null`` as a parameter.

java.util.Date @roundUpTimePart(java.util.Date date)
  Rounds up the time part.
  The return value is a new Date which is rounded up the time part.
  It returns ``null`` if you pass ``null`` as a parameter.

java.sql.Date @roundUpTimePart(java.sql.Date date)
  Rounds up the time part.
  The return value is a new Date which is rounded up the time part.
  It returns ``null`` if you pass ``null`` as a parameter.

java.sql.Timestamp @roundUpTimePart(java.sql.Timestamp timestamp)
  Rounds up the time part.
  The return value is a new Timestamp which is rounded up the time part.
  It returns ``null`` if you pass ``null`` as a parameter.

java.time.LocalDateTime @roundUpTimePart(java.time.LocalDateTime localDateTime)
  Rounds up the time part.
  The return value is a new LocalDateTime which is rounded up the time part.
  It returns ``null`` if you pass ``null`` as a parameter.

java.time.LocalDate @roundUpTimePart(java.time.LocalDate localDate)
  Returns the next day.
  The return value is a new LocalDate which is the next one after the argument.
  It returns ``null`` if you pass ``null`` as a parameter.

boolean @isEmpty(CharSequence charSequence)
  Returns ``true`` if the character sequence is ``null`` or the length is ``0``.

boolean @isNotEmpty(CharSequence charSequence)
  Returns ``true`` if the character sequence isn't ``null`` and the length isn't ``0``.

boolean @isBlank(CharSequence charSequence)
  Returns ``true`` only if the character sequence is ``null``, the length is ``0``,
  or the sequence is formed with whitespaces only.

boolean @isNotBlank(CharSequence charSequence)
  Returns ``true`` if the character sequence isn't ``null``, the length isn't ``0``,
  and the sequence isn't formed with whitespaces only.

These functions are correspond to the methods of ``org.seasar.doma.expr.ExpressionFunctions``.

Using custom functions
======================

You can define and use your own functions.

You need to follow these settings when you use custom functions which you define by yourself:

* The function is defined as a method of a class which implements
  ``org.seasar.doma.expr.ExpressionFunctions``.
* The method is a public instance method.
* The class is registered as an option in :doc:`annotation-processing`.
  The key of the option is ``doma.expr.functions``.
* The instance of the class you create is used in an RDBMS dialect in your configuration class
  (The implementations of RDBMS dialect provided by Doma can receive
  ``ExpressionFunctions`` in the constructor).

To call a custom function, add ``@`` at the beginning of the function name like built-in functions.
For example, you can call ``myfunc`` function like this:

.. code-block:: sql

  select * from employee where
      employee_name = /* @myfunc(employee.employeeName) */'smith'
