package org.seasar.doma.message;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.seasar.doma.internal.message.MessageResourceBundle;

/** Defines messages that are sent to application developers. */
public enum Message implements MessageResource {

  // doma
  DOMA0001("The parameter[{0}] is null"),
  DOMA0002("The parameter[{0}] is illegal. The cause is as follows: {1}"),
  DOMA0003(
      "The version of Doma's jar file is different between runtime and compile-time (runtime={0}, compile-time={1}). If you use Eclipse, check the Build path and the Factory path. Otherwise if you use javac, check the classpath option and the processorpath option. In the case of Web application, check whether there is no old jar file in WEB-INF/lib directory."),

  // wrapper
  DOMA1007("The wrapper class that corresponds to the value[{1}] of the type[{0}] is not found."),

  // jdbc
  DOMA2001("The SQL result is not a single row.\nPATH=[{0}].\nSQL=[{1}]."),
  DOMA2002(
      "While the column[{0}] is in the result set, the property that corresponds the column is not found in the entity class[{2}]. You have to do any of followings: 1)Define the property whose name is [{1}] in the entity class. 2)Annotate @Column(name = \"{0}\") to the property in the entity class. 3)Inplement UnknownColumnHandler to ignore unknown columns and configure in org.seasar.doma.jdbc.Config.\\nPATH=[{3}].\\nSQL=[{4}]\""),
  DOMA2003("The SQL execution is failed because of optimistic locking.\nPATH=[{0}].\nSQL=[{1}]."),
  DOMA2004(
      "The SQL execution is failed because of unique constraint violation.\nPATH=[{0}].\nSQL=[{1}].\nThe detail cause is as follows: {2}"),
  DOMA2005("The SQL result is none.\nPATH=[{0}].\nSQL=[{1}]."),
  DOMA2006("The SQL result is not a single column.\nPATH=[{0}].\nSQL=[{1}]."),
  DOMA2008("The JDBC operation is failed. The cause is as follows: {1}"),
  DOMA2009(
      "The SQL execution is failed.\nPATH=[{0}].\nSQL=[{1}].\nThe cause is as follows: {2}\nThe root cause is as follows: {3}"),
  DOMA2010("Cannot get content from the SQL file[{0}]. The cause is as follows: {1}"),
  DOMA2011("The SQL file[{0}] is not found from classpath."),
  DOMA2012("The script file[{0}] is not found from classpath."),
  DOMA2015("Cannot get java.sql.Connection. The cause is as follows: {0}"),
  DOMA2016(
      "Cannot get java.sql.PreparedStatement.\nPATH=[{0}].\nSQL=[{1}].\nThe cause is as follows: {2}"),
  DOMA2017("Cannot generate an identity value of the entity[{0}]."),
  DOMA2018("Cannot generate an identity value of the entity[{0}]. The cause is as follows: {1}"),
  DOMA2020(
      "No value is set for the ID property[{1}] of the entity[{0}]. If the ID property is not annotated with @GeneratedValue, the ID property must have a value before the SQL INSERT execution."),
  DOMA2021(
      "While the ID property[{1}] of the entity[{0}] is specified with the strategy[{2}], the strategy is not supported in the DBMS[{3}]."),
  DOMA2022("The entity[{0}] that have no ID property cannot be updated or deleted."),
  DOMA2023("Pessimistic locking is not supported in the DBMS[{0}]."),
  DOMA2024("Pessimistic locking with aliases is not supported in the DBMS[{0}]."),
  DOMA2025("Cannot get java.sql.CallableStatement.\nSQL=[{0}].\nThe cause is as follows: {1}"),
  DOMA2028("The batch execution is failed because of optimistic locking.\nPATH=[{0}].\nSQL=[{1}]."),
  DOMA2029(
      "The batch execution is failed because of unique constraint violation.\nPATH=[{0}].\nSQL=[{1}].\nThe detail cause is as follows: {2}"),
  DOMA2030(
      "The batch execution is failed.\nPATH=[{0}].\nSQL=[{1}].\nThe cause is as follows: {2}.\nThe root cause is as follows: {3}"),
  DOMA2032("Cannot get java.sql.Statement. The cause is as follows: {0}"),
  DOMA2033("The instance variable[{0}] is not set."),
  DOMA2034("The unsupported method[{1}] of the class[{0}] is invoked."),
  DOMA2035(
      "The method[{1}] of the implementation class[{0}] of org.seasar.doma.jdbc.Config returns null."),
  DOMA2040("The constant[{1}] is not found in the Enum[{0}]."),
  DOMA2041("Cannot disable auto commit mode. The cause is as follows: {0}"),
  DOMA2042("Cannot enable auto commit mode. The cause is as follows: {0}"),
  DOMA2043("Cannot commit. The cause is as follows: {0}"),
  DOMA2044("Cannot rollback. The cause is as follows: {0}"),
  DOMA2045("Cannot start a transaction. The transaction[{0}] has been already started."),
  DOMA2046("Cannot commit a transaction. The transaction has not been started yet."),
  DOMA2048("Cannot get a connection. The transaction has not been started yet."),
  DOMA2049("Cannot get a connection lazily. The cause is as follows: {0}"),
  DOMA2051("Cannot create the savepoint[{0}]. The cause is as follows: {1}"),
  DOMA2052("Cannot rollback to the savepoint[{0}]. The cause is as follows: {1}"),
  DOMA2053("Cannot create the savepoint[{0}]. The transaction has not been started yet."),
  DOMA2054(
      "The savepoint[{0}] is not found. Before the rollback, ensure that the savepoint[{0}] has been creataed."),
  DOMA2055("Cannot set the transaction isolation level[{0}]. The cause is as follows: {1}"),
  DOMA2056("Cannot get the transaction isolation level. The cause is as follows: {0}"),
  DOMA2057(
      "Cannot check whether that the savepoint[{0}] exists. The transaction has not been started yet."),
  DOMA2059(
      "The savepoint[{0}] already exists. You cannot create another savepoint with same name."),
  DOMA2060("Cannot remove the savepoint[{0}]. The cause is as follows: {1}"),
  DOMA2061("Cannot remove the savepoint[{0}]. The transaction has not been started yet."),
  DOMA2062("Cannot rollback to the savepoint[{0}]. The transaction has not been started yet."),
  DOMA2063("The local transaction[{0}] is begun."),
  DOMA2064("The local transaction[{0}] is ended."),
  DOMA2065("The savepoint[{1}] of the local transaction[{0}] is created."),
  DOMA2067("The local transaction[{0}] is committed."),
  DOMA2068("The local transaction[{0}] is rolled back."),
  DOMA2069("The local transaction[{0}] is rolled back to the savepoint[{1}]."),
  DOMA2070("The rollback of the local transaction[{0}] is failed."),
  DOMA2071("Cannot enable auto commit mode."),
  DOMA2072("Cannot set the transaction isolation level[{0}]."),
  DOMA2073("Cannot close java.sql.Connection."),
  DOMA2074("Cannot close java.sql.Statement."),
  DOMA2075("Cannot close java.sql.ResultSet."),
  DOMA2076("SQL LOG : PATH=[{0}],\n{1}"),
  DOMA2077(
      "Cannot execute the script file.\nPAHT=[{1}].\nLINE NUMBER=[{2}].\nThe cause is as follows: {3}.\nSQL=[{0}]."),
  DOMA2078("Cannot get content from the SQL file[{0}].\nThe cause is as follows: {1}"),
  DOMA2079("Pessimistic locking with WAIT option is not supported in the DBMS[{0}]."),
  DOMA2080("Pessimistic locking with NOWAIT option is not supported in the DBMS[{0}]"),
  DOMA2081("Pessimistic locking with aliases and WAIT option is not supported in the DBMS[{0}]."),
  DOMA2082("Pessimistic locking with aliases and NOWAIT option is not supported in the DBMS[{0}]."),
  DOMA2083("Cannot reserve identity values for the entity[{0}]. The cause is as follows: {1}"),
  DOMA2101(
      "Failed to parse the SQL on line [{1}] at column [{2}]. The quotation mark \"''\" that indicates the end of a string literal is not found. SQL[{0}]"),
  DOMA2102(
      "Failed to parse the SQL on line [{1}] at column [{2}]. The string \"*/\" that indicates the end of a block comment is not found. SQL[{0}]"),
  DOMA2104(
      "Failed to parse the SQL on line [{1}] at column [{2}]. \"/*%if ...*/\" or \"/*%for ...*/\" that corresponds to \"/*%end*/\" is not found. SQL[{0}]"),
  DOMA2109(
      "Failed to parse the SQL on line [{1}] at column [{2}]. The open parenthesis that corresponds to the close parenthesis is not found. SQL[{0}]"),
  DOMA2110(
      "Failed to parse the SQL on line [{1}] at column [{2}]. The test literal or the parenthesis is not found just after the bind variable comment or the literal variable comment[{3}]. SQL[{0}]"),
  DOMA2111(
      "Failed to build the SQL on line [{1}] at column [{2}]. The cause is as follows: {3}. SQL[{0}]"),
  DOMA2112(
      "Failed to build the SQL on line [{1}] at column [{2}]. The object type[{4}] that corresponds to the bind variable comment or the literal variable comment[{3}] that is just before the parenthesis is not subtype of java.lang.Iterable. SQL[{0}]"),
  DOMA2115(
      "Failed to build the SQL on line [{1}] at column [{2}]. The [{4}]th element of java.lang.Iterable that corresponds to the bind variable comment or the literal variable comment[{3}] that is just before the parenthesis is null. SQL[{0}]"),
  DOMA2116(
      "Failed to build the SQL on line [{1}] at column [{2}]. The single quotation is contained in the embedded variable[{3}]. SQL[{0}]"),
  DOMA2117(
      "Failed to build the SQL on line [{1}] at column [{2}]. The semi-colon is contained in the embedded variable[{3}]. SQL[{0}]"),
  DOMA2118(
      "Failed to build the SQL on line [{1}] at column [{2}]. The bind variable comment[{3}] is properly processed. The cause is as follows: {4}. SQL[{0}]"),
  DOMA2119(
      "Failed to build the SQL on line [{1}] at column [{2}]. When the block comment starts with \"/*%\", the following string must be either \"if\", \"else\", \"elseif\", \"for\", \"end\", \"expand\" or \"populate\". SQL[{0}]"),
  DOMA2120(
      "Failed to parse the SQL on line [{1}] at column [{2}]. While the bind variable comment[{3}] is defined, the variable name is none. SQL[{0}]"),
  DOMA2121(
      "Failed to parse the SQL on line [{1}] at column [{2}]. While the embedded variable comment[{3}] is defined, the variable name is none. SQL[{0}]"),
  DOMA2122(
      "Failed to build the SQL on line [{1}] at column [{2}]. The line comment is contained in the embedded comment[{3}]. SQL[{0}]"),
  DOMA2123(
      "Failed to build the SQL on line [{1}] at column [{2}]. The block comment is contained in the embedded comment[{3}]. SQL[{0}]"),
  DOMA2124(
      "Failed to parse the SQL on line [{1}] at column [{2}]. The token \":\" is not found in the block comment \"/*%for ...*/\". SQL[{0}]"),
  DOMA2125(
      "Failed to parse the SQL on line [{1}] at column [{2}]. The identifier is not found before the tonken \":\" in the block comment \"/*%for ...*/\". SQL[{0}]"),
  DOMA2126(
      "Failed to parse the SQL on line [{1}] at column [{2}]. The expression is not found after the tonken \":\" in the block comment \"/*%for ...*/\". SQL[{0}]"),
  DOMA2129(
      "Failed to build the SQL on line [{1}] at column [{2}]. The object type[{4}] that corresponds to the expression[{3}] that is after the token \":\" in the block comment \"/*%for ...*/\" is not subtype of java.lang.Iterable. SQL[{0}]"),
  DOMA2133(
      "Failed to parse the SQL on line [{1}] at column [{2}]. \"/%if ...*/\" is not closed with \"/*%end*/\". The pair of \"/%if ...*/\" and \"/*%end*/\" must exist in the same clause such as SELECT, FROM, WHERE and so on. SQL[{0}]"),
  DOMA2134(
      "Failed to parse the SQL on line [{1}] at column [{2}]. \"/%for ...*/\" is not closed with \"/*%end*/\". The pair of \"/%for ...*/\" and \"/*%end*/\" must exist in the same clause such as SELECT, FROM, WHERE and so on. SQL[{0}]"),
  DOMA2135(
      "Failed to parse the SQL on line [{1}] at column [{2}]. The parenthesis is not closed. Otherwise, using with \"/%if ...*/～/*%end*/\" or \"/%for ...*/～/*%end*/\", the open parenthesis and the close parenthesis are not in a same block. SQL[{0}]"),
  DOMA2136(
      "The result set that corresponds to the parameter[{0}] of the Dao method is not returned from the stored procecdure or the stored function."),
  DOMA2137(
      "The result set that corresponds to the [{0}]th parameter[{1}] is not returned from the stored procecdure or the stored function."),
  DOMA2138(
      "Failed to parse the SQL on line [{1}] at column [{2}]. \"/*%if ...*/\" that corresponds to \"/*%elseif ...*/\" is not found. SQL[{0}]"),
  DOMA2139(
      "Failed to parse the SQL on line [{1}] at column [{2}]. \"/*%elseif ...*/\" is behind \"/*%else*/\". SQL[{0}]"),
  DOMA2140(
      "Failed to parse the SQL on line [{1}] at column [{2}]. \"/*%if ...*/\" that corresponds to \"/*%else ...*/\" is not found. SQL[{0}]"),
  DOMA2141(
      "Failed to parse the SQL on line [{1}] at column [{2}]. There are multiple \"/*%else*/\". SQL[{0}]"),
  DOMA2142(
      "Failed to parse the SQL on line [{1}] at column [{2}]. The value[{4}] that is just after the bind variable comment or the literal variable comment[{3}] is illegal as a test literal. The value must be the open parenthesis or the literal whose type is string, number, date, time or timestamp. SQL[{0}]"),
  DOMA2143(
      "Failed to parse the SQL on line [{1}] at column [{2}]. The asterisk \"*\" is not found just after the column expanse comment[{3}]. SQL[{0}]"),
  DOMA2144(
      "Failed to build the SQL on line [{1}] at column [{2}]. The column expanse comment[{3}] doesn't work. Check that the result set is mapped to an entity class. SQL[{0}]"),
  DOMA2201(
      "The original SQL must have an \"ORDER BY\" clause to translate the SQL for pagination."),
  DOMA2202(
      "The domain description class[{1}] that corresponds to the domain class[{0}] is not found. The cause is as follows: {2}"),
  DOMA2203(
      "The entity class[{1}] that corresponds to the entity class[{0}] is not found. The cause is as follows: {2}"),
  DOMA2204(
      "The type of the class[{0}] must be the basic type or the domain type. The detail cause as follows: {1}"),
  DOMA2205("The class[{0}] must be annotated with @Domain."),
  DOMA2206("The class[{0}] must be annotated with @Entity."),
  DOMA2207("The property[{1}] is not defined in the entity class[{0}]."),
  DOMA2208(
      "Failed to access the property[{1}] in the entity class[{0}]. The cause is as follows: {2}"),
  DOMA2211("The property[{1}] is not found in the entity class[{0}]."),
  DOMA2212(
      "Failed to access the field[{1}] that is annotated with @OriginalStates in the entity class[{0}]. The cause is as follows: {2}"),
  DOMA2213(
      "The field[{1}] that is annotated with @OriginalStates is not found in the entity class[{0}]. The cause is as follows: {2}"),
  DOMA2215("The method[{1}] is not found in the class[{0}]. The cause is as follows: {2}"),
  DOMA2216(
      "The property[{1}] in the entity class[{0}] is not mapped to the column in the result set. To resolve this error entirely, the result set must contain the column[{2}]. To ignore this error, set \"false\" to the ensureResultMapping element of @Select, @Function or @ResultSet.\nPATH=[{3}].\nSQL=[{4}]"),
  DOMA2218("The parameter[{0}] must be instance of the interface[{1}]."),
  DOMA2219("The class[{0}] is not annotated with @Entity."),
  DOMA2220("ENTER  : CLASS=[{0}], METHOD=[{1}]"),
  DOMA2221("EXIT   : CLASS=[{0}], METHOD=[{1}]"),
  DOMA2222("THROW  : CLASS=[{0}], METHOD=[{1}], EXCEPTION=[{2}]"),
  DOMA2223("SKIP   : CLASS=[{0}], METHOD=[{1}], CAUSE=[{2}]"),
  DOMA2224(
      "Failed to build the SQL on line [{1}] at column [{2}]. The single quotation is contained in the literal variable comment[{3}]. SQL[{0}]"),
  DOMA2228(
      "Failed to parse the SQL on line [{1}] at column [{2}]. While the literal variable comment[{3}] is defined, the variable name is none. SQL[{0}]"),
  DOMA2229("The parameter types are different between batched queries."),
  DOMA2230("The parameter or the literal must be consistent between batched queries."),
  DOMA2231("The number of parameters is different between batched queries."),
  DOMA2232("The parameter is empty."),
  DOMA2233("The key[{0}] is not found in the map that is an element of Iterable."),

  // expression
  DOMA3001(
      "Failed to evaluate the expression[{0}] at column[{1}]. Cannot execute the method[{3}] in the class[{2}]. The cause is as follows: {4}"),
  DOMA3002(
      "Failed to evaluate the expression[{0}] at column[{1}]. The method[{3}] is not found the class[{2}]. Check the method name, the parameter size, the parameter type and so on."),
  DOMA3003(
      "Failed to evaluate the expression[{0}] at column[{1}]. The variable[{2}] is not resolved. Check that the variable name is correct."),
  DOMA3004(
      "Failed to analyze the expression[{0}] at column[{1}]. The double quotation that indicates the end of a string literal is not found."),
  DOMA3005(
      "Failed to evaluate the expression[{0}] at column[{1}]. The class[{2}] is not found. Check that the class name is correct."),
  DOMA3006(
      "Failed to evaluate the expression[{0}] at column[{1}]. The constructor[{2}] is not found. Check that the number or type of its parameters is correct."),
  DOMA3007(
      "Failed to evaluate the expression[{0}] at column[{1}]. Cannot execute the constructor[{2}]. The cause is as follows: {3}"),
  DOMA3008(
      "Failed to evaluate the expression[{0}] at column[{1}]. Cannot execute the comparison operator[{2}]. Either of the operands may not implement java.lang.Comparable or the operands may be not comparable with each other. The cause is as follows: {3}"),
  DOMA3009(
      "Failed to evaluate the expression[{0}] at column[{1}]. Cannot execute the comparison operator[{2}]. If either of the operands is null, they are not comparable."),
  DOMA3010(
      "Failed to analyze the expression[{0}] at column[{1}]. The operand is not found for the operator[{2}]."),
  DOMA3011(
      "Failed to analyze the expression[{0}] at column[{1}]. The unsupported token[{2}] is found."),
  DOMA3012(
      "Failed to analyze the expression[{0}] at column[{1}]. The illegal number literal[{2}] is found."),
  DOMA3013(
      "Failed to evaluate the expression[{0}] at column[{1}]. Cannot execute the arithmetic operator[{2}]. The class[{4}] of the operand[{3}] is not numeric."),
  DOMA3014(
      "Failed to evaluate the expression[{0}] at column[{1}]. Cannot execute the arithmetic operator[{2}]. The cause is as follows: {3}"),
  DOMA3015(
      "Failed to evaluate the expression[{0}] at column[{1}]. Cannot execute the operator[{2}]. The operand is null."),
  DOMA3016(
      "Failed to analyze the expression[{0}] at column[{1}]. The single quotation that indicates the end of a character literal is not found."),
  DOMA3018(
      "Failed to evaluate the expression[{0}] at column[{1}]. The field[{3}] is not found in the class[{2}]. Check that the field name is correct."),
  DOMA3019(
      "Failed to evaluate the expression[{0}] at column[{1}]. Cannot access the field[{3}] in the class[{2}]. The cause is as follows: {4}"),
  DOMA3020(
      "Failed to evaluate the expression[{0}] at column[{1}]. Cannot execute the operator[{2}]. To execute concatenation, the class of the right operand[{3}] must be either String, Character or char. To execute addition, both of the operands must be numeric."),
  DOMA3021(
      "Failed to analyze the expression[{0}] at column[{1}]. The field name or the method name must follow closely the token \".\"."),
  DOMA3022(
      "Failed to analyze the expression[{0}] at column[{1}]. The character[{2}] that follows closely token \".\" is illegal as the first character in a Java identifier."),
  DOMA3023(
      "Failed to analyze the expression[{0}] at column[{1}]. The full qualified name of the class or the built-in function name must follow closely the token \"@\"."),
  DOMA3024(
      "Failed to analyze the expression[{0}] at column[{1}]. The character[{2}] that follows closely token \"@\" is illegal as the first character in a Java identifier."),
  DOMA3025(
      "Failed to analyze the expression[{0}] at column[{1}]. The token \"(\" must follow closely the name of the built-in function."),
  DOMA3026("Failed to analyze the expression[{0}] at column[{1}]. The parenthesis is not closed."),
  DOMA3027(
      "Failed to evaluate the expression[{0}] at column[{1}]. Cannot execute the method[{3}] because the expression[{2}] is evaluated as null."),
  DOMA3028(
      "Failed to evaluate the expression[{0}] at column[{1}]. The function[{2}] is not found. Check that the followings are correct: 1)the function name 2)the number and types of the function parameters."),
  DOMA3029(
      "Failed to analyze the expression[{0}] at column[{1}]. The name of the static field or method msut follow closely the token \"@\"."),
  DOMA3030(
      "Failed to analyze the expression[{0}] at column[{1}]. The character[{2}] that follows closely token \"@\" is illegal as the first character in a Java identifier."),
  DOMA3031(
      "Failed to analyze the expression[{0}] at column[{1}]. The character[{2}] is found just before the token \"@\". The character is illegal as a part of the class name."),
  DOMA3032(
      "Failed to analyze the expression[{0}] at column[{1}]. The class name must be placed between two \"@\"."),
  DOMA3033(
      "Failed to evaluate the expression[{0}] at column[{1}]. The static field[{3}] is not found in the class[{2}]. Check that the field name is correct."),

  // annotation processing
  DOMA4001("The return type must be int that indicates the affected rows count."),
  DOMA4002("The number of parameters must be one."),
  DOMA4003("The parameter type must be the entity class."),
  DOMA4005("The query annotation such as @Select and @Update is required."),
  DOMA4007("The type argument[{0}] of java.util.List that is the return type is not supported."),
  DOMA4008("The return type[{0}] is not supported."),
  DOMA4011("The annotation processing for the class[{0}] is failed. The cause is as follows: {1}"),
  DOMA4014("Cannot annotate @Dao to anything but interfaces."),
  DOMA4015("Cannot annotate @Entity to anything but classes."),
  DOMA4016(
      "An unexpected error has occurred. Check the logs for more information. For example, if you use Eclipse, see the error log view."),
  DOMA4017("The Dao interface must be a top level interface."),
  DOMA4019("The file[{0}] is not found from the classpath. The absolute path is \"{1}\"."),
  DOMA4020("The SQL file[{0}] is empty."),
  DOMA4021("The path[{0}] is a directory, but it must be a file. The absolute path is \"{1}\"."),
  DOMA4024(
      "The annotation @Version is duplicated. The filed that annotated with @Version must be only one in the class hierarchy."),
  DOMA4025("Cannot use the name that starts with \"{0}\", because it is reserved for Doma."),
  DOMA4026("The name that ends with \"{0}\" may be duplicate with the auto-generated classes."),
  DOMA4030(
      "When you use @SequenceGenerator, you must annotate @GeneratedValue(strategy = GenerationType.SEQUENCE) to the same field."),
  DOMA4031(
      "When you use @TableGenerator, you must annotate @GeneratedValue(strategy = GenerationType.TABLE) to the same field."),
  DOMA4033("When you use @GeneratedValue, you must annotate @Id to the same field."),
  DOMA4034(
      "When you use @GeneratedValue(strategy = GenerationType.SEQUENCE), you must annotate @SequenceGenerator to the same field."),
  DOMA4035(
      "When you use @GeneratedValue(strategy = GenerationType.TABLE), you must annotate @TableGenerator to the same field."),
  DOMA4036("When you use @GeneratedValue, @Id must be only one in the class hierarchy."),
  DOMA4037("Multiple @GeneratedValue are found. @GeneratedValue must be one."),
  DOMA4038(
      "The type argument[{1}] of EntityListener[{0}] must be supertype of the entity class[{2}]."),
  DOMA4039(
      "The annotation processing is stopped because of compilation error. Check the error message from the execution evironment such as Eclipse and javac."),
  DOMA4040("The return type must be the array of int that indicates the affected rows count."),
  DOMA4042("The parameter type must be the subtype of java.lang.Iterable."),
  DOMA4043("The type argument of the subtype of java.lang.Iterable must be the entity class."),
  DOMA4051("The entity class cannot have type parameters."),
  DOMA4053("Multiple SelectOption parameters are not allowed."),
  DOMA4059("The Dao interface cannot have type parameters."),
  DOMA4062("The parameter type that is annotated with @ResultSet must be java.util.List."),
  DOMA4063(
      "The type[{0}] is not supported as the return type of the method that is annotated with @Function."),
  DOMA4064("The return type of the method that is annotated with @Procedure must be void."),
  DOMA4065("The argument type[{0}] of java.util.List as the return type is not supported."),
  DOMA4066(
      "The parameters of the method that is annotated with @Function or @Procedure must be annotated with either of @In, @InOut, @Out or @ResultSet."),
  DOMA4067(
      "The parameter that corresponds to the variable[{0}] in the SQL at column[{1}] is not found in the method."),
  DOMA4068("Failed to read the SQL file{0}]. The cause is as follows: {1}"),
  DOMA4069("Failed to parse the SQL file{0}]. The cause is as follows: {1}"),
  DOMA4071(
      "The variable[{2}] in the expression[{0}] at column[{1}] does not have a public and non-void method[{4}]. The variable type is [{3}]."),
  DOMA4072("The function[{2}] in the expression[{0}] at column [{1}] is not found."),
  DOMA4073(
      "The public and non-void method[{4}] cannot be identified from the variable[{2}] in the expression[{0}] at column[{1}]. The variable type is [{3}]."),
  DOMA4076("The parameter type must be the array type."),
  DOMA4078("The number of parameters must be 0."),
  DOMA4079("Failed to generate the source file for the class[{0}]. The cause is as follows: {1}"),
  DOMA4084("The property[{0}] is not found in the entity class[{1}]."),
  DOMA4085("The property[{0}] is not found in the entity class[{1}]."),
  DOMA4086(
      "The annotation[{0}] competes with the annotation[{1}]. They cannot be annotated to the same field."),
  DOMA4087(
      "The annotation[{0}] competes with the annotation[{1}]. They cannot be annotated to the same method."),
  DOMA4088(
      "When you use @Id, @Version, you must not annotate @Column(insertable=false) to the same field."),
  DOMA4089(
      "When you use @Id or @Version, you must not annotate @Column(updatable=false) to the same field."),
  DOMA4090("The annotation processor[{0}] starts processing for the class[{1}]."),
  DOMA4091("The annotation processor[{0}] ends processing for the class[{1}]."),
  DOMA4092(
      "Failed to verify the SQL file[{0}] on line [{2}] at column [{3}]. The cause is as follows: {4} SQL[{1}]."),
  DOMA4093("@Version can be annotated to the property that is comaptible with the numerical type."),
  DOMA4095(
      "@GeneratedValue can be annotated to the property that is comaptible with the numerical type."),
  DOMA4096(
      "The class[{0}] is not supported as a persistent type. If you intend to map the class to the external domain class with @ExternalDomain, the configuration may be not enough. Check the class that is annotated with @DomainConverters and the annotation processing option \"doma.domain.converters\"."),
  DOMA4097("The return type must be [{0}]."),
  DOMA4098(
      "The parameter type that is annotated with @Out must be org.seasar.doma.jdbc.Reference."),
  DOMA4100("The argument type[{0}] of Reference is not supported."),
  DOMA4101("The parameter type[{0}] is not supported as the parameter that is annotated with @In."),
  DOMA4102("The type[{0}] is not supported as a persistent type."),
  DOMA4103(
      "The non-private constructor that has the parameter tyep[{0}] is not found. Define the constructor or specify the factory method name to the annotation @Domain if you use the factory method."),
  DOMA4104(
      "The accessor method[{0}] is not found. The method must have return type[{1}] and must be non-private and non-args."),
  DOMA4105("You can annotate @Domain to only classes, interfaces and enums"),
  DOMA4106(
      "The factory method[{0}] is not found. The method must have the return type[{1}] and the parameter type[{2}] and must be non-private and static. The type parameter of the method must be same with the one of the class. Define the factory method. Or if you do not use the factory method, do not specify the value to the factoryMethod element of @Domain and define the constructor."),
  DOMA4107("The class that is annotated with @Domain cannot have type parameters."),
  DOMA4108("The type argument is required for Reference."),
  DOMA4109(
      "The type argument is required for the subtype of java.lang.Iterable, that is the return type."),
  DOMA4111(
      "The parameter type that is annotated with @InOut must be org.seasar.doma.jdbc.Reference."),
  DOMA4112("The wildcard is not supported for the parameter type[{0}]."),
  DOMA4113("The wildcard is not supported for the return type[{0}]."),
  DOMA4114(
      "The type[{3}] of the variable[{2}] in the expression[{0}] at column[{1}] does not have the instance field[{4}]."),
  DOMA4115("The constructor[{2}] in the expression[{0}] at column[{1}] is not found."),
  DOMA4116(
      "The type[{4}] of the left operand[{3}] and the type[{6}] of the right operand[{5}] are not same for the comparison operator[{2}] in the expression[{0}] at column [{1}]."),
  DOMA4117(
      "The type[{4}] of the left operand[{3}] for the logical operator[{2}] in the expression[{0}] at column[{1}] is neither boolean nor Boolean."),
  DOMA4118(
      "The type[{4}] of the right operand[{3}] for the logical operator[{2}] in the expression[{0}] at column[{1}] is neither boolean nor Boolean."),
  DOMA4119(
      "The type[{4}] of the operand[{3}] for the logical operator[{2}] in the expression[{0}] at column[{1}] is neither boolean nor Boolean."),
  DOMA4120(
      "The type[{4}] of the left operand[{3}] for the arithmetic operator[{2}] in the expression[{0}] at column[{1}] is not numeric."),
  DOMA4121(
      "The type[{4}] of the right operand[{3}] for the arithmetic operator[{2}] in the expression[{0}] at column[{1}] is not numeric."),
  DOMA4122(
      "Failed to verify the SQL file[{0}]. The parameter[{1}] of the method is not referred in the SQL file."),
  DOMA4124(
      "The mutable entity class must have a non-private and no-args constructor. To make it immutable, specify \"true\" to the immutable element of @Entity."),
  DOMA4125(
      "@OriginalStates is duplicated. The filed that annotated with @OriginalStates must be only one in the class hierarchy."),
  DOMA4126(
      "To execute concatenation, the type[{4}] of the right operand[{3}] in the expression[{0}] at column [{1}] must be either String, Character or char. To execute addition, both of the operands must be numeric."),
  DOMA4127("The constructor[{2}] in the expression[{0}] at column [{1}] cannot be identified."),
  DOMA4132(
      "If the factoryMethod element of @Domain is specified with \"new\", the class must not be abstract."),
  DOMA4135(
      "The type of the field that is annotated with @OriginalStates must be same with the type of the entity class."),
  DOMA4138("The class[{2}] in the expression[{0}] at column [{1}] is not found."),
  DOMA4139(
      "The comparison operator[{2}] cannot be applied to the null literal in the expression[{0}] at column [{1}]."),
  DOMA4140(
      "Failed to verify the SQL file[{0}] on line [{2}] at column [{3}]. While the expression[{4}] in \"/*%if ...*/\" is evaluated as the type[{5}], the type must be either boolean or Boolean. SQL[{1}]"),
  DOMA4141(
      "Failed to verify the SQL file[{0}] on line [{2}] at column [{3}]. While the expression[{4}] in \"/*%elseif ...*/\" is evaluated as the type[{5}], the type must be either boolean or Boolean. SQL[{1}]"),
  DOMA4143("Failed to get the SQL file[{0}]. The cause is as follows: {1}"),
  DOMA4144("Failed to recognize the children files of the directory[{0}]."),
  DOMA4145("The class[{2}] in the expression[{0}] at column [{1}] is not found."),
  DOMA4146(
      "The class[{2}] in the expression[{0}] at column [{1}] does not have the method[{3}] that returns non-void type and is public and static."),
  DOMA4147(
      "The class[{2}] in the expression[{0}] at column [{1}] have multiple methods[{3}] that returns non-void type and is public and static."),
  DOMA4148(
      "The class[{2}] in the expression[{0}] at column [{1}] does not have the static field[{3}]."),
  DOMA4149(
      "Failed to verify the SQL file[{0}] on line [{2}] at column [{3}]. While the expression[{4}] in \"/*%for ...*/\" is evaluated as the type[{5}], the type must be the subtype of java.lang.Iterable. SQL[{1}]"),
  DOMA4150(
      "Failed to verify the SQL file[{0}] on line [{2}] at column [{3}]. While the expression[{4}] in \"/*%for ...*/\" is evaluated as the type[{5}], the type argument for the type is obscure. SQL[{1}]"),
  DOMA4153(
      "Failed to verify the SQL file[{0}] on line [{2}] at column [{3}]. The parameter type that corresponds to the bind or literal variable[{4}] must be the basic or domain type. But the actual type is [{5}]. You may forget to access its field or to invoke its method. SQL[{1}]"),
  DOMA4154("The entity class[{0}] as the return type must not be abstract."),
  DOMA4155(
      "The entity class[{0}] that is the type argument of java.util.List as a return type must not be abstract."),
  DOMA4156(
      "The entity class[{0}] that is the type argument of java.util.List as a return type must not be abstract."),
  DOMA4157(
      "The entity class[{0}] that is the type argument of java.util.List type must not be abstract."),
  DOMA4159("The type argument is required for the subtype of java.lang.Iterable."),
  DOMA4160("The subtype of java.lang.Iterable must not be wildcard."),
  DOMA4161(
      "Failed to verify the SQL file[{0}] on line [{2}] at column [{3}]. The parameter type that corresponds to the bind or literal variable[{4}] just before the open parenthesis must be the subtype of the java.lang.Iterable whose element type is the basic or doma type. But the actual type is [{5}]. You may forget to access its field or to invoke its method. SQL[{1}]"),
  DOMA4163("The user defined Config class must not be abstract. The class[{0}] is abstract."),
  DOMA4164(
      "The user defined Config class must have a no-args and public constructor or have a public static final field that is named \"INSTANCE\".　The type of the field must be the subtype of org.seasar.doma.Config. The class[{0}] does not meet these requirements."),
  DOMA4166("The entity listener class must not be abstract. The class[{0}] is abstract."),
  DOMA4167(
      "The entity listener class must have a public no-args constructor. The class[{0}] does not have such a constructor."),
  DOMA4168(
      "The implementation class[{0}] of org.seasar.doma.jdbc.id.TableIdGenerator must not be abstract."),
  DOMA4169(
      "The implementation class[{0}] of org.seasar.doma.jdbc.id.TableIdGenerator must have a public no-args constructor."),
  DOMA4170(
      "The implementation class[{0}] of org.seasar.doma.jdbc.id.SequenceIdGenerator must not be abstract."),
  DOMA4171(
      "The implementation class[{0}] of org.seasar.doma.jdbc.id.SequenceIdGenerator must have a public no-args constructor."),
  DOMA4172("The return type must be void."),
  DOMA4173("The number of parameters is 0."),
  DOMA4176(
      "The accessor method[{0}] is not found. The method must return the type[{1}] and accept no parameters. And the method must be non-private, no-args and non-static."),
  DOMA4177(
      "The factory method[{0}] is not found. The method must return the type[{1}] and accept the parameter type[{2}]. And the method must be be non-private and static."),
  DOMA4181(
      "The SQL file[{0}] contains embedded variable comments. Because the SQL in a batch is immutable, the embedded variable comments cannot change the SQL dynamically. To suppress this warning, annotate @Suppress(messages = '{ Message.DOMA4181 }') to the method."),
  DOMA4182(
      "The SQL file[{0}] contains condition comments. Because the SQL in a batch is immutable, the condition comments cannot change the SQL dynamically. To suppress this warning, annotate @Suppress(messages = '{ Message.DOMA4182 }') to the method."),
  DOMA4183(
      "The SQL file[{0}] contains iteration comments. Because the SQL in a batch is immutable, the iteration comments cannot change the SQL dynamically. To suppress this warning, annotate @Suppress(messages = '{ Message.DOMA4183 }') to the method."),
  DOMA4184(
      "When you annotate @Domain to the enum type, you cannot specify \"new\" to the factoryMethod element of @Domain because \"new\" means the usage of constructor. Specify the name of the factory method that is static and non-private."),
  DOMA4185(" ... /** The SQL is too long. The only first {0} character are displayed. */"),
  DOMA4186(
      "The type argument[{0}] is not supported for java.util.List. Supported types are as follows: basic type, domain class, entity class and java.util.Map<String, Object>."),
  DOMA4188("Interfaces annotated with @Dao must be only one as the parent interface."),
  DOMA4189(
      "Failed to resolve the function[{2}] in the expression[{0}] at column [{1}]. The class[{3}] that is specified for the annotation processing option \"doma.expr.functions\" is not found."),
  DOMA4190(
      "Failed to resolve the function[{2}] in the expression[{0}] at column [{1}]. The class[{3}] that is specified for the annotation processing option \"doma.expr.functions\" must be the subtype of org.seasar.doma.expr.ExpressionFunctions."),
  DOMA4191(
      "@ExternalDomain can be annotated to only the subtype of org.seasar.doma.jdbc.domain.DomainConverter."),
  DOMA4192("The class[{0}] that is annotated with @ExternalDomain must not be abstract."),
  DOMA4193(
      "The class[{0}] that is annotated with @ExternalDomain must have a public and no-args constructor."),
  DOMA4194(
      "The second type argument[{0}] of org.seasar.doma.jdbc.domain.DomainConverter is not supported as a persistent type."),
  DOMA4196("The type[{0}] is not annotated with @ExternalDomain."),
  DOMA4197(
      "The package of the first type argument[{0}] of org.seasar.doma.jdbc.domain.DomainConverter is default package. It is not supported."),
  DOMA4198("The class that is annotated with @ExternalDomain must be a top level class."),
  DOMA4200(
      "The class[{0}] that is specified for the annotation processing option \"doma.domain.converters\" is not found."),
  DOMA4201(
      "The class[{0}] that is specified for the annotation processing option \"doma.doma.converters\" is not annotated with @DomainConverters."),
  DOMA4202("The type argument of org.seasar.doma.jdbc.entity.EntityListener is not resolved."),
  DOMA4203(
      "All type arguments of the type[{0}] that is fisrt type argument of org.seasar.doma.jdbc.doma.DomainConverter must be wildcard."),
  DOMA4204("The raw type of the class[{0}] cannot be used as an entity property."),
  DOMA4205(
      "The class[{0}] whose type arguments contain a wildcard or type variable cannot be used as an entity prpoerty."),
  DOMA4206("The raw type of the class[{0}] cannot be used as a return type of the Dao method."),
  DOMA4207(
      "The class[{0}] whose type arguments contain a wildcard or type variable cannot be used as a return type of the Dao method."),
  DOMA4208("The raw type of the class[{0}] cannot be used as a parameter type of the Dao method."),
  DOMA4209(
      "The class[{0}] whose type arguments contain a wildcard or type variable cannot be used as a parameter type of the Dao method."),
  DOMA4210(
      "The raw type of the class[{0}] cannot be used as a type argument of the subtype of Iterable."),
  DOMA4211(
      "The class[{0}] whose type arguments contain a wildcard or type variable cannot be used as a type argument of the subtype of Iterable."),
  DOMA4212(
      "The raw type of the class[{0}] cannot be used as a type argument of the subtype of Iterable."),
  DOMA4213(
      "The class[{0}] whose type arguments contain a wildcard or type variable cannot be used as a type argument of the subtype of Iterable."),
  DOMA4218("The raw type of the class[{0}] cannot be used as a type argument of Reference"),
  DOMA4219(
      "The class[{0}] whose type arguments contain a wildcard or type variable cannot be used as a type argument of Reference."),
  DOMA4220(
      "The SQL file[{0}] that is not mapped to any methods are found. Check the method names or the sqlFile elements of annotations. To suppress this warning, annotate @Suppress(messages = '{ Message.DOMA4220 }') to the Dao interface."),
  DOMA4221("A non-private constructor is required for the immutable entity class."),
  DOMA4222(
      "When the immutable entity class is a parameter type for the method that is annotated with such as @Insert, @Update and @Delete, the return type must be org.seasar.doma.jdbc.Result. The type argument of org.seasar.doma.jdbc.Result must be same entity class with the parameter type of the method."),
  DOMA4223(
      "When the immutable entity class is the type argument of the subtype of Iterable and the iterable is parameter type for the method that is annotated with such as @BatchInsert, @BatchUpdate and @BatchDelete, the return type must be org.seasar.doma.jdbc.BatchResult. The type argument of org.seasar.doma.jdbc.BatchResult must be same entity class with the type argument of the iterable."),
  DOMA4224("@OriginalStates cannot be annotated to fields of the immutable entity class."),
  DOMA4225(
      "The \"final\" modifier is required for all persistent fields of the immutable entity class."),
  DOMA4226(
      "The value of the immutable element of @Entity must be consistent in the class hierarchy."),
  DOMA4227(
      "The number of type parameters of the entity listener class must be less than or equal to 1."),
  DOMA4228(
      "The type parameter[{0}] of the entity listener class is not passed to the type argument of org.seasar.doma.jdbc.entity.EntityLister in the class hierarchy."),
  DOMA4229(
      "The upper bound[{1}] of the type parameter[{0}] of the entity listener class is not compatible with the entity class[{2}]."),
  DOMA4230(
      "The entity listener class[{0}] that is took over from the parent entityt class must have a type parameter that accepts the entity class[{1}]."),
  DOMA4231(
      "The upper bound[{2}] of the type parameter[{1}] of the entity listener class[{0}] that is took over from the parent entityt class is not compatible with the entity class[{3}]."),
  DOMA4232("The raw type of the class[{0}] cannot be used."),
  DOMA4233(
      "The type argument of the class[{0}] must not be a wildcard or type variable. Instead specify the basic type or the domain type."),
  DOMA4234("The entity class[{0}] as a type argument of java.util.Optional must not be abstract."),
  DOMA4235(
      "The type argument[{0}] is not supported for java.util.Optional. Supported types are as follows: basic type, domain class and entity class."),
  DOMA4236("The raw type of the class[{0}] cannot be used."),
  DOMA4237(
      "The type argument of the class[{0}] must not be a wildcard or type variable. Instead specify the basic type or the domain type."),
  DOMA4238(
      "The raw type of the class[{0}] cannot be used as a type argument of java.util.Optional."),
  DOMA4239(
      "The class[{0}] whose type arguments contains a wildcard or type variable cannot be used as a type argument of java.util.Optional."),
  DOMA4240("The class[{0}] of the parameter must not be the raw type."),
  DOMA4241("The class[{0}] of the parameter must not be the wildcard type."),
  DOMA4242("The raw type of the class[{0}] cannot be used as a type argument of Stream."),
  DOMA4243(
      "The class[{0}] whose type arguments contains a wildcard or type variable cannot be used as a type argument of Stream."),
  DOMA4244("The first type argument of Function must be java.util.stream.Stream."),
  DOMA4245("The type argument of Stream is not supported."),
  DOMA4246(
      "The return type[{0}] and the second type argument[{1}] of java.util.function.Function are not same."),
  DOMA4247(
      "When you use the java.util.function.Function parameter, SelectStrategyType.STREAM must be specified for the strategy element of @Select."),
  DOMA4248(
      "When you specify SelectStrategyType.STREAM for the strategy element of @Select, the java.util.function.Function parameter is required for the method."),
  DOMA4249("Mutilple java.util.function.Function parameters are not allowed."),
  DOMA4250("The entity class[{0}] as a type argument of Stream must not be abstract."),
  DOMA4251(
      "When the primitive type is specified for the valueType element of @Domain, the acceptNull element of @Domain must be \"false\"."),
  DOMA4252("The annotation[{0}] cannot be annotated to the default method."),
  DOMA4253(
      "@SingletonConfig cannot be annotated to anything but the subtype of org.seasar.doma.Config."),
  DOMA4254(
      "The method[{0}] is not found. The method must be public and static. The retun type must be this class[{0}]. The number of parameters must be 0."),
  DOMA4255("The method[{1}] is not found in the class[{0}]."),
  DOMA4256("The constructor of the class that is annotated with @SingletonConfig must be private."),
  DOMA4257(
      "Failed to verify the SQL file[{0}] on line [{2}] at column [{3}]. While the comment \"/*%expand ...*/\" is used, it cannot exapnd columns. Check that the method is annotaed with @Select and the result set is mapped to the entity class. SQL[{1}]"),
  DOMA4258("The parameter tyep[{0}] must not be the raw type."),
  DOMA4259(
      "The first or third type argument of the parameter type[{0}] must not be a wildcard type."),
  DOMA4260(
      "The raw type of the class[{0}] cannot be used as the first type argument of Collector."),
  DOMA4261(
      "The class[{0}] whose type arguments contains a wildcard or type variable cannot be used as the fist type argument of Collector."),
  DOMA4262("The first type argument of Collector is not supported."),
  DOMA4263("The entity class[{0}] as the first type argument of Collector must not be abstract."),
  DOMA4264("Multiple Collector parameters are not allowed."),
  DOMA4265("The return type[{0}] and the third type argument[{1}] of Collector are not same."),
  DOMA4266(
      "When you specify SelectStrategyType.COLLECT for the strategy element of @Select, the java.util.stream.Collector parameter is required for the method."),
  DOMA4267(
      "The type argument[{0}] is not supported for java.util.Optional of java.util.List. Supported types are as follows: basic type and domain type."),
  DOMA4268(
      "When you annotate @Domain to the interface, you cannot specify \\\"new\\\" to the factoryMethod element of @Domain because \"new\" means the usage of constructor. Specify the name of the factory method that is static and non-private."),
  DOMA4270(
      "Failed to verify the SQL file[{0}] on line [{2}] at column [{3}]. While the comment \"/*%populate */\" is used, it cannot generate the SET clause. Check that the method is annotated with either @Update or @BatchUpdate and the first parameter is mapped to the entity class. SQL[{1}]"),
  DOMA4271("The type argument[{0}] is not supported for java.util.stream.Stream as a return type."),
  DOMA4272(
      "The abstract entity class[{0}] is not supported for the type argument of java.util.stream.Stream as a return type."),
  DOMA4273(
      "The type argument[{0}] is not supported for java.util.Optional of java.util.stream.Stream. Supported types are as follows: basic type and domain type."),
  DOMA4274(
      "The application must close the Stream object that is returned from the Dao method. To suppress this warning, annotate @Suppress(messages = '{ Message.DOMA4274 }') to the method."),
  DOMA4275(
      "The type[{0}] is not public and static. The domain class or the enclosing type of it must be public and static."),
  DOMA4276(
      "The type[{0}] is a local or anonymous class. The domain class or the enclosing type of it must be a top level or member class."),
  DOMA4277(
      "The simple name of the type[{0}] contains \"$\" or \"__\". The simple name of the domain class or the enclosing type of it must not contain them."),
  DOMA4278(
      "The type[{0}] is not public and static. The first type argument of DomainConverter or the enclosing type of it must be must be public and static."),
  DOMA4279(
      "The type[{0}] is a local or anonymous class. The first type argument of DomainConverter or the enclosing type of it must be a top level or member class."),
  DOMA4280(
      "The simple name of the type[{0}] contains \"$\" or \"__\". The simple name of the first type argument of DomainConverter or the enclosing type of it must not contain them."),
  DOMA4281(
      "The number, type and name of the constructor parameters must correspond with those of persistent fields in the immutable entity class."),
  DOMA4283("@Embeddable cannot be annotated to anything but classes."),
  DOMA4285("The embeddable class must not have a type parameter."),
  DOMA4286("@OriginalStates cannot be annotated to the fields of the embeddable class."),
  DOMA4288(
      "The annotation[{0}] competes with the annotation[{1}]. They cannot be annotated to the same field."),
  DOMA4289("@Id cannot be annotated to the fields of the embeddable class."),
  DOMA4290("@Version cannot be annotated to the fields of the embeddable class."),
  DOMA4291("@GeneratedValue cannot be annotated to the fields of the embeddable class."),
  DOMA4293(
      "The number, type and name of the constructor parameters must correspond with those of persistent fields in the embeddable class."),
  DOMA4294("A non-private constructor is required for the embeddable class."),
  DOMA4295("The raw type of the class[{0}] cannot be used as a persistent property."),
  DOMA4296(
      "The class[{0}] whose type arguments contains a wildcard or type variable cannot be used as a persistent prpoerty."),
  DOMA4297(
      "The embeddable class[{0}] cannot be used as a persistent property in the embeddable class."),
  DOMA4298(
      "The class[{0}] is not supported as a persistent type. If you intend to map the class to the external holder class with @ExternalDomain, the configuration may be not enough. Check the class that is annotated with @DomainConverters and the annotation processing option \"doma.domain.converters\"."),
  DOMA4299("The raw type of the class[{0}] cannot be used as a persistent property."),
  DOMA4300(
      "The annotation processing for the type[{0}] is failed. Are there any compilation errors that are unrelated to the annotation processing?"),
  DOMA4301(
      "The type argument of the class[{0}] that is a wildcard or type variable cannot be used as a persistent property."),
  DOMA4302("@Id cannot be annotated to the property whose type is annotated with @Embeddable."),
  DOMA4303(
      "@GeneratedValue cannot be annotated to the property whose type is annotated with @Embeddable."),
  DOMA4304(
      "@Version cannot be annotated to the property whose type is annotated with @Embeddable."),
  DOMA4305(
      "When @OriginalStates is used, the property that is annotated with @Embeddable is not supported."),
  DOMA4306("@Column cannot be annotated to the property whose type is annotated with @Embeddable."),
  DOMA4309(
      "The file path \"{0}\" is different from the path \"{1}\" in filesystem in case-sensitive."),
  DOMA4315(
      "The type[{0}] is not public and static. The entity class or the enclosing type of it must be public and static."),
  DOMA4316(
      "The type[{0}] is a local or anonymous class. The entity class or the enclosing type of it must be a top level or member class."),
  DOMA4317(
      "The simple name of the type[{0}] contains \"$\" or \"__\". The simple name of the entity class or the enclosing type of it must not contain them."),
  DOMA4415(
      "The type[{0}] is not public and static. The embeddable class or the enclosing type of it must be public and static."),
  DOMA4416(
      "The type[{0}] is a local or anonymous class. The embeddable class or the enclosing type of it must be a top level or member class."),
  DOMA4417(
      "The simple name of the type[{0}] contains \"$\" or \"__\". The simple name of the embeddable class or the enclosing type of it must not contain them."),
  DOMA4418(
      "When you annotate @lombok.Value to the entity class, you must specify \"true\" to the immutable element of @Entity."),
  DOMA4419("The usage of the staticConstructor element of @lombok.Value is not supported."),
  DOMA4420(
      "When you annotate @lombok.AllArgsConstructor to the entity class, you must specify \"true\" to the immutable element of @Entity."),
  DOMA4421("The usage of the staticName element of @lombok.AllArgsConstructor is not supported."),
  DOMA4422(
      "lombok.AccessLevel.PRIVATE is not supported for the access element of @lombok.AllArgsConstructor."),
  DOMA4423(DOMA4419.getMessagePattern()),
  DOMA4424(DOMA4421.getMessagePattern()),
  DOMA4425(DOMA4422.getMessagePattern()),
  DOMA4426(
      "lombok.AccessLevel.NONE is not supported for the access element of @lombok.AllArgsConstructor."),
  DOMA4427(DOMA4426.getMessagePattern()),
  DOMA4428(DOMA4419.getMessagePattern()),
  DOMA4429(
      "The method name that is generated by @lombok.Value is not same with the value of the accessorMethod element of @Holder."),
  DOMA4430("There is no instance field to be initialized by @lombok.Value."),
  DOMA4431(
      "The number of instance fields that is initialized by @lombok.Value must be 1, but greater than or equal to 2."),
  DOMA4432(
      "The type[{0}] of the instance field that is initialized by @lombok.Value is not same with the type[{1}] that is specified to the valueType element of @Holder."),
  DOMA4433(
      "When you annotate @SqlProcessor to the method、the BiFunction parameter is required for the method."),
  DOMA4434("Multiple BiFunction parameters are not allowed."),
  DOMA4435("The second type argument of BiFunction must be org.seasar.doma.jdbc.PreparedSql."),
  DOMA4436("The return type[{0}] is not same with the third type argument[{1}] of BiFunction."),
  DOMA4437("The first type argument of BiFunction must be org.seasar.doma.jdbc.Config."),
  DOMA4438("The parameter type[{0}] must not be the raw type."),
  DOMA4439("The parameter type[{0}] must not contain a wildcard as a type argument."),
  DOMA4440(
      "The method[{0}] in the parent interface is not default method. When the parent interface is not annotaed with @Dao, the all methods in the interface must be default methods."),
  DOMA4441(
      "@TenantId cannot be annotated to the property whose type is annotated with @Embeddable."),
  DOMA4442(
      "The annotation @TenantId is duplicated. The filed that annotated with @TenantId must be only one in the class hierarchy."),
  DOMA4443("@TenantId cannot be annotated to the fields of the embeddable class."),

  // other
  DOMA5001(
      "The JDBC driver may not be loaded. Check that the JDBC driver is in the classpath. If the JDBC driver is not loaded automatically, load it explicitly using Class.forName. ex) Class.forName(\"oracle.jdbc.driver.OracleDriver\")"),
  DOMA5002("The url property is not set."),
  ;
  ;

  private final String messagePattern;

  private Message(String messagePattern) {
    this.messagePattern = messagePattern;
  }

  @Override
  public String getCode() {
    return name();
  }

  @Override
  public String getMessagePattern() {
    return messagePattern;
  }

  @Override
  public String getMessage(Object... args) {
    String simpleMessage = getSimpleMessageInternal(args);
    String code = name();
    return "[" + code + "] " + simpleMessage;
  }

  @Override
  public String getSimpleMessage(Object... args) {
    return getSimpleMessageInternal(args);
  }

  protected String getSimpleMessageInternal(Object... args) {
    try {
      boolean fallback = false;
      ResourceBundle bundle;
      try {
        bundle = ResourceBundle.getBundle(MessageResourceBundle.class.getName());
      } catch (MissingResourceException ignored) {
        fallback = true;
        bundle = new MessageResourceBundle();
      }
      String code = name();
      String pattern = bundle.getString(code);
      String message = MessageFormat.format(pattern, args);
      return fallback ? "(This is a fallback message) " + message : message;
    } catch (Throwable throwable) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      throwable.printStackTrace(pw);
      StringBuilder arguments = new StringBuilder();
      for (Object a : args) {
        arguments.append(a);
        arguments.append(", ");
      }
      return "[DOMA9001] Failed to get a message because of following error : "
          + sw
          + " : "
          + arguments;
    }
  }
}
