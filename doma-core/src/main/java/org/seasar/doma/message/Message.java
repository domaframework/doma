/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.message;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

/** Defines messages that are displayed to application developers. */
public enum Message implements MessageResource {

  // doma
  DOMA0001("The parameter \"{0}\" is null"),
  DOMA0002("The parameter \"{0}\" is illegal. The cause is as follows: {1}"),
  DOMA0003(
      "The version of Doma''s jar file is different between runtime and compile-time (runtime={0}, compile-time={1}). "
          + "If you use Eclipse, check the Build path and the Factory path. "
          + "Otherwise if you use javac, check the classpath option and the processorpath option. "
          + "In the case of Web application, check whether there is no old jar file in WEB-INF/lib directory."),

  // wrapper
  DOMA1007("The wrapper class for the type \"{0}\" is not found."),

  // jdbc
  DOMA2001("The SQL result is not a single row.\nPATH=[{0}].\nSQL=[{1}]."),
  DOMA2002(
      "While the column \"{0}\" is in the result set, the corresponding property is not found in the entity class \"{2}\". "
          + "You have to do any of the following: "
          + "1)Define the property whose name is \"{1}\" in the entity class. "
          + "2)Annotate a field in the entity class with @Column(name = \"{0}\"). "
          + "3)Implement org.seasar.doma.jdbc.UnknownColumnHandler to ignore unknown columns and configure it in org.seasar.doma.jdbc.Config."
          + "\nPATH=[{3}].\nSQL=[{4}]\""),
  DOMA2003("The SQL execution failed because of optimistic locking.\nPATH=[{0}].\nSQL=[{1}]."),
  DOMA2004(
      "The SQL execution failed because of unique constraint violation."
          + "\nPATH=[{0}].\nSQL=[{1}].\nThe detailed cause is as follows: {2}"),
  DOMA2005("The SQL result is none.\nPATH=[{0}].\nSQL=[{1}]."),
  DOMA2006("The SQL result is not a single column.\nPATH=[{0}].\nSQL=[{1}]."),
  DOMA2008("The JDBC operation failed. The cause is as follows: {1}"),
  DOMA2009(
      "The SQL execution failed.\nPATH=[{0}].\nSQL=[{1}].\nThe cause is as follows: {2}\nThe root cause is as follows: {3}"),
  DOMA2010("Cannot get content from the SQL template [{0}]. The cause is as follows: {1}"),
  DOMA2011("The SQL file \"{0}\" is not found in the classpath."),
  DOMA2012("The script file \"{0}\" is not found in the classpath."),
  DOMA2015("Cannot get java.sql.Connection. The cause is as follows: {0}"),
  DOMA2016(
      "Cannot get java.sql.PreparedStatement.\nPATH=[{0}].\nSQL=[{1}].\nThe cause is as follows: {2}"),
  DOMA2017("Cannot generate an identity value of the entity \"{0}\"."),
  DOMA2018("Cannot generate an identity value of the entity \"{0}\". The cause is as follows: {1}"),
  DOMA2020(
      "No value is set for the ID property \"{1}\" of the entity \"{0}\". "
          + "If the ID property is not annotated with @GeneratedValue, the ID property must have a value before the SQL INSERT execution."),
  DOMA2021(
      "While the ID property \"{1}\" of the entity \"{0}\" is specified with the strategy \"{2}\", "
          + "the strategy is not supported in the RDBMS \"{3}\"."),
  DOMA2022(
      "The entity \"{0}\" cannot be updated or deleted because the entity has no ID property."),
  DOMA2023("Pessimistic locking is not supported in the RDBMS \"{0}\"."),
  DOMA2024("Pessimistic locking with aliases is not supported in the RDBMS \"{0}\"."),
  DOMA2025("Cannot get java.sql.CallableStatement.\nSQL=[{0}].\nThe cause is as follows: {1}"),
  DOMA2028("The batch execution failed because of optimistic locking.\nPATH=[{0}].\nSQL=[{1}]."),
  DOMA2029(
      "The batch execution failed because of unique constraint violation.\nPATH=[{0}].\nSQL=[{1}]."
          + "\nThe detailed cause is as follows: {2}"),
  DOMA2030(
      "The batch execution failed.\nPATH=[{0}].\nSQL=[{1}].\nThe cause is as follows: {2}."
          + "\nThe root cause is as follows: {3}"),
  DOMA2032("Cannot get java.sql.Statement. The cause is as follows: {0}"),
  DOMA2033("The instance variable \"{0}\" is not set."),
  DOMA2034("The unsupported method \"{1}\" of the class \"{0}\" is invoked."),
  DOMA2035("The method \"{1}\" of the class \"{0}\" must not return null."),
  DOMA2040("The constant \"{1}\" is not found in the Enum type \"{0}\"."),
  DOMA2041("Cannot disable auto commit mode. The cause is as follows: {0}"),
  DOMA2042("Cannot enable auto commit mode. The cause is as follows: {0}"),
  DOMA2043("Cannot commit. The cause is as follows: {0}"),
  DOMA2044("Cannot roll back. The cause is as follows: {0}"),
  DOMA2045("Cannot start a transaction. The transaction \"{0}\" has been already started."),
  DOMA2046("Cannot commit a transaction. The transaction has not been started yet."),
  DOMA2048("Cannot get a connection. The transaction has not been started yet."),
  DOMA2049("Cannot get a connection lazily. The cause is as follows: {0}"),
  DOMA2051("Cannot create the savepoint \"{0}\". The cause is as follows: {1}"),
  DOMA2052("Cannot roll back to the savepoint[{0}]. The cause is as follows: {1}"),
  DOMA2053("Cannot create the savepoint \"{0}\". The transaction has not been started yet."),
  DOMA2054(
      "The savepoint \"{0}\" is not found. Before the rollback, ensure that the savepoint \"{0}\" has been created."),
  DOMA2055("Cannot set the transaction isolation level \"{0}\". The cause is as follows: {1}"),
  DOMA2056("Cannot get the transaction isolation level. The cause is as follows: {0}"),
  DOMA2057(
      "Cannot check whether that the savepoint \"{0}\" exists. The transaction has not been started yet."),
  DOMA2059(
      "The savepoint \"{0}\" already exists. You cannot create another savepoint with same name."),
  DOMA2060("Cannot remove the savepoint \"{0}\". The cause is as follows: {1}"),
  DOMA2061("Cannot remove the savepoint \"{0}\". The transaction has not been started yet."),
  DOMA2062("Cannot roll back to the savepoint \"{0}\". The transaction has not been started yet."),
  DOMA2063("The local transaction \"{0}\" is begun."),
  DOMA2064("The local transaction \"{0}\" is ended."),
  DOMA2065("The savepoint \"{1}\" of the local transaction \"{0}\" is created."),
  DOMA2067("The local transaction \"{0}\" is committed."),
  DOMA2068("The local transaction \"{0}\" is rolled back."),
  DOMA2069("The local transaction \"{0}\" is rolled back to the savepoint[{1}]."),
  DOMA2070("The rollback of the local transaction \"{0}\" failed."),
  DOMA2071("Cannot enable auto commit mode."),
  DOMA2072("Cannot set the transaction isolation level \"{0}\"."),
  DOMA2073("Cannot close java.sql.Connection."),
  DOMA2074("Cannot close java.sql.Statement."),
  DOMA2075("Cannot close java.sql.ResultSet."),
  DOMA2076("SQL LOG : PATH=[{0}],\n{1}"),
  DOMA2077(
      "Cannot execute the script template.\nPATH=[{1}].\nLINE NUMBER=[{2}].\nThe cause is as follows: {3}.\nSQL=[{0}]."),
  DOMA2078("Cannot get contents from the SQL template[{0}].\nThe cause is as follows: {1}"),
  DOMA2079("Pessimistic locking with WAIT option is not supported in the RDBMS \"{0}\"."),
  DOMA2080("Pessimistic locking with NOWAIT option is not supported in the RDBMS \"{0}\""),
  DOMA2081(
      "Pessimistic locking with aliases and WAIT option is not supported in the RDBMS \"{0}\"."),
  DOMA2082(
      "Pessimistic locking with aliases and NOWAIT option is not supported in the RDBMS \"{0}\"."),
  DOMA2083("Cannot reserve identity values for the entity \"{0}\". The cause is as follows: {1}"),
  DOMA2084("Cannot get the auto commit mode. The cause is as follows: {0}"),
  DOMA2101(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "The single quotation mark \"''\" for the end of the string literal is not found. SQL=[{0}]"),
  DOMA2102(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "The string \"*/\" for the end of the multi-line comment is not found. SQL=[{0}]"),
  DOMA2104(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "\"/*%if ...*/\" or \"/*%for ...*/\" that corresponds to \"/*%end*/\" is not found. SQL=[{0}]"),
  DOMA2109(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "The open parenthesis that corresponds to the close parenthesis is not found. SQL=[{0}]"),
  DOMA2110(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "The directive \"{3}\" must be followed immediately by either a test literal or an open parenthesis. SQL=[{0}]"),
  DOMA2111(
      "Failed to build the SQL on line {1} at column {2}. "
          + "The cause is as follows: {3}. SQL=[{0}]"),
  DOMA2112(
      "Failed to build the SQL on line {1} at column {2}. "
          + "The type of the expression \"{3}\" must be a subtype of either java.lang.Iterable or an array type, but it is [{4}]. SQL=[{0}]"),
  DOMA2115(
      "Failed to build the SQL on line {1} at column {2}. "
          + "The null value is found in the elements of the expression \"{3}\" at index {4}. SQL=[{0}]"),
  DOMA2116(
      "Failed to build the SQL on line {1} at column {2}. "
          + "A single quotation mark \"''\" is contained in the expression \"{3}\" but the embedded variable directive doesn''t allow it. SQL=[{0}]"),
  DOMA2117(
      "Failed to build the SQL on line {1} at column {2}. "
          + "A semi-colon \";\" is contained in the expression \"{3}\" but the embedded variable directive doesn''t allow it. SQL=[{0}]"),
  DOMA2118(
      "Failed to build the SQL on line {1} at column {2}. "
          + "The expression \"{3}\" isn''t processed. The cause is as follows: {4}. SQL=[{0}]"),
  DOMA2119(
      "Failed to build the SQL on line {1} at column {2}. "
          + "When the directive starts with \"/*%\", "
          + "the following string must be either \"!\", \"if\", \"else\", \"elseif\", \"for\", \"end\", \"expand\" or \"populate\". SQL=[{0}]"),
  DOMA2120(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "While the bind variable directive \"{3}\" is defined, the expression is none. SQL=[{0}]"),
  DOMA2121(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "While the embedded variable comment \"{3}\" is defined, the expression is none. SQL=[{0}]"),
  DOMA2122(
      "Failed to build the SQL on line {1} at column {2}. "
          + "A string \"--\" is contained in the expression \"{3}\" but the embedded variable directive doesn''t allow it. SQL=[{0}]"),
  DOMA2123(
      "Failed to build the SQL on line {1} at column {2}. "
          + "A string \"/*\" is contained in the expression \"{3}\" but the embedded variable directive doesn''t allow it. SQL=[{0}]"),
  DOMA2124(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "The token \":\" is not found in \"/*%for ...*/\". SQL=[{0}]"),
  DOMA2125(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "The identifier is not found before the token \":\" in \"/*%for ...*/\". SQL=[{0}]"),
  DOMA2126(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "The sequence expression is not found after the token \":\" in \"/*%for ...*/\". SQL=[{0}]"),
  DOMA2129(
      "Failed to build the SQL on line {1} at column {2}. "
          + "The type of the expression \"{3}\" must be a subtype of either java.lang.Iterable or an array type, but it is [{4}]. SQL=[{0}]"),
  DOMA2133(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "\"/%if ...*/\" is not closed with \"/*%end*/\". "
          + "The pair of \"/%if ...*/\" and \"/*%end*/\" must exist in the same clause such as SELECT, FROM, WHERE and so on. SQL=[{0}]"),
  DOMA2134(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "\"/%for ...*/\" is not closed with \"/*%end*/\". "
          + "The pair of \"/%for ...*/\" and \"/*%end*/\" must exist in the same clause such as SELECT, FROM, WHERE and so on. SQL=[{0}]"),
  DOMA2135(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "The parenthesis is not closed. "
          + "Otherwise, the open parenthesis and the close parenthesis are not in a same directive block. SQL=[{0}]"),
  DOMA2136(
      "The result set that is mapped to the parameter \"{0}\" of the DAO method is not returned from the stored procedure or stored function."),
  DOMA2137(
      "The result set that is mapped to the parameter \"{1}\" of the DAO method is not returned from the stored procedure or stored function. "
          + "The index of callable statement parameters is {0}."),
  DOMA2138(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "\"/*%if ...*/\" that corresponds to \"/*%elseif ...*/\" is not found. SQL=[{0}]"),
  DOMA2139(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "\"/*%elseif ...*/\" is behind \"/*%else*/\". SQL=[{0}]"),
  DOMA2140(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "\"/*%if ...*/\" that corresponds to \"/*%else ...*/\" is not found. SQL=[{0}]"),
  DOMA2141(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "There are multiple \"/*%else*/\". SQL=[{0}]"),
  DOMA2142(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "The value \"{4}\" is illegal as a literal format. SQL=[{0}]"),
  DOMA2143(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "An asterisk \"*\" must follow immediately the expansion directive \"{3}\". SQL=[{0}]"),
  DOMA2144(
      "Failed to build the SQL on line {1} at column {2}. "
          + "The expansion directive \"{3}\" doesn''t work. Check that the result set is mapped to an entity class. SQL=[{0}]"),
  DOMA2201(
      "The original SQL statement must have an \"ORDER BY\" clause to translate the statement for pagination."),
  DOMA2202("The class \"{1}\" is not found. The cause is as follows: {2}"),
  DOMA2203("The class \"{1}\" is not found. The cause is as follows: {2}"),
  DOMA2204(
      "The class \"{0}\" must be one of the basic classes and the domain classes. The detailed cause as follows: {1}"),
  DOMA2205("The class \"{0}\" must be annotated with @Domain."),
  DOMA2206("The class \"{0}\" must be annotated with @Entity."),
  DOMA2207("The property \"{1}\" is not defined in the entity class \"{0}\"."),
  DOMA2208(
      "Failed to access the property \"{1}\" in the entity class \"{0}\". The cause is as follows: {2}"),
  DOMA2211("The property \"{1}\" is not found in the entity class \"{0}\"."),
  DOMA2212(
      "Failed to access the field \"{1}\" that is annotated with @OriginalStates in the entity class \"{0}\". "
          + "The cause is as follows: {2}"),
  DOMA2213(
      "The field \"{1}\" that is annotated with @OriginalStates is not found in the entity class \"{0}\". "
          + "The cause is as follows: {2}"),
  DOMA2215("The method \"{1}\" is not found in the class \"{0}\". The cause is as follows: {2}"),
  DOMA2216(
      "The property \"{1}\" in the entity class \"{0}\" is not mapped to the column in the result set. "
          + "To resolve this error entirely, the result set must contain the column \"{2}\". "
          + "To ignore this error, set \"false\" to the ensureResultMapping element of @Select, @Function or @ResultSet."
          + "\nPATH=[{3}].\nSQL=[{4}]"),
  DOMA2218("The parameter \"{0}\" must be instance of the interface \"{1}\"."),
  DOMA2219("The class \"{0}\" isn''t annotated with @Entity."),
  DOMA2220("ENTER  : CLASS={0}, METHOD={1}"),
  DOMA2221("EXIT   : CLASS={0}, METHOD={1}"),
  DOMA2222("THROW  : CLASS={0}, METHOD={1}, EXCEPTION={2}"),
  DOMA2223("SKIP   : CLASS={0}, METHOD={1}, CAUSE={2}"),
  DOMA2224(
      "Failed to build the SQL on line {1} at column {2}. "
          + "A single quotation mark \"''\" is contained in the expression \"{3}\" "
          + "but the literal variable directive doesn''t allow it. SQL=[{0}]"),
  DOMA2228(
      "Failed to parse the SQL on line {1} at column {2}. "
          + "While the literal directive \"{3}\" is defined, the expression is none. SQL=[{0}]"),
  DOMA2229("The parameter types are different between batched queries."),
  DOMA2230("The literals must be consistent between batched queries."),
  DOMA2231("The number of parameters is different between batched queries."),
  DOMA2232("The parameter is empty."),
  DOMA2233("The key \"{0}\" is not found in the map that is an element of Iterable."),
  DOMA2234("The savepoint \"{1}\" of the local transaction \"{0}\" is released."),
  DOMA2235("The dialect \"{0}\" does not support auto-increment when inserting multiple rows."),

  DOMA2236("The dialect \"{0}\" does not support multi-row insert statement."),
  DOMA2237(
      "Duplicate column name \"{0}\" found in ResultSetMetaData. Column names must be unique."
          + "\nPATH=[{1}].\nSQL=[{2}]"),
  DOMA2238(
      """
      While the column "{0}" is in the result set, the corresponding property is not found in the entity class "{1}".
      Check the following mapping information:
      {4}
      PATH=[{2}]
      SQL=[{3}]"""),
  DOMA2239(
      "The table alias \"{0}\" passed to the expansion directive is not defined in the aggregate strategy \"{1}\"."),
  DOMA2240("The dialect \"{0}\" does not support the RETURNING clause or an equivalent feature."),
  DOMA2241(
      "Failed to parse the SQL on line {1} at column {2}. The expression is not found in \"/*%if ...*/\". SQL=[{0}]"),
  DOMA2242(
      "Failed to parse the SQL on line {1} at column {2}. The expression is not found in \"/*%elseif ...*/\". SQL=[{0}]"),

  // expression
  DOMA3001(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "Cannot execute the method \"{3}\" in the class \"{2}\". The cause is as follows: {4}"),
  DOMA3002(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "The method \"{3}\" is not found in the class \"{2}\". "
          + "Check the method name, the parameter size, the parameter type and so on."),
  DOMA3003(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "The variable \"{2}\" is not resolved. Check that the variable name is correct."),
  DOMA3004(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The double quotation mark ''\"'' for the end of the string literal is not found."),
  DOMA3005(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "The class　\"{2}\" is not found. Check that the class name is correct."),
  DOMA3006(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "The constructor \"{2}\" is not found. Check that the number and types of its parameters are correct."),
  DOMA3007(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "Cannot execute the constructor \"{2}\". The cause is as follows: {3}"),
  DOMA3008(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "Cannot execute the comparison operator \"{2}\". "
          + "Either of the operands may not implement java.lang.Comparable or the operands may be not comparable with each other. "
          + "The cause is as follows: {3}"),
  DOMA3009(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "Cannot execute the comparison operator \"{2}\". If either of the operands is null, they are not comparable."),
  DOMA3010(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The operand is not found for the operator \"{2}\"."),
  DOMA3011(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The unsupported token \"{2}\" is found."),
  DOMA3012(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The illegal number literal \"{2}\" is found."),
  DOMA3013(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "Cannot execute the arithmetic operator \"{2}\". The class of the operand \"{3}\" is not numeric. It is \"{4}\"."),
  DOMA3014(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "Cannot execute the arithmetic operator \"{2}\". The cause is as follows: {3}"),
  DOMA3015(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "Cannot execute the operator \"{2}\". The operand is null."),
  DOMA3016(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The single quotation mark \"''\" for the end of the character literal is not found."),
  DOMA3018(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "The field \"{3}\" is not found in the class \"{2}\". Check that the field name is correct."),
  DOMA3019(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "Cannot access the field \"{3}\" in the class \"{2}\". The cause is as follows: {4}"),
  DOMA3020(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "Cannot execute the operator \"{2}\". "
          + "To execute concatenation, the class of the right operand \"{3}\" must be either String, Character or char. "
          + "To execute addition, both of the operands must be numeric."),
  DOMA3021(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The field name or the method name must follow immediately the token \".\"."),
  DOMA3022(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The character \"{2}\" that follows immediately the token \".\" is illegal."),
  DOMA3023(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The full qualified name of the class or the built-in function name must follow immediately the token \"@\"."),
  DOMA3024(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The character \"{2}\" that follows immediately the token \"@\" is illegal."),
  DOMA3025(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The token \"(\" must follow immediately the name of the built-in function."),
  DOMA3026(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The parenthesis is not closed."),
  DOMA3027(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "Cannot execute the method \"{3}\" because the expression \"{2}\" is evaluated as null."),
  DOMA3028(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "The function \"{2}\" is not found. "
          + "Check that the following are correct: "
          + "1)the function name. "
          + "2)the number and types of the function parameters."),
  DOMA3029(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The name of the static field or method must follow immediately the token \"@\"."),
  DOMA3030(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The character \"{2}\" that follows immediately the token \"@\" is illegal."),
  DOMA3031(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The character \"{2}\" followed by the token \"@\" is illegal as a part of the class name."),
  DOMA3032(
      "Failed to analyze the expression \"{0}\" at column {1}. "
          + "The class name must be placed between two \"@\"."),
  DOMA3033(
      "Failed to evaluate the expression \"{0}\" at column {1}. "
          + "The static field \"{3}\" is not found in the class \"{2}\". Check that the field name is correct."),

  // annotation processing
  DOMA4001("The return type must be int that indicates the affected rows count."),
  DOMA4002("The number of parameters must be one."),
  DOMA4003("The parameter type must be an entity class."),
  DOMA4005("The query annotation such as @Select and @Update is required."),
  DOMA4007(
      "The type argument of java.util.List is {0}. Such List isn''t supported as the return type."),
  DOMA4008("The return type \"{0}\" isn''t supported."),
  DOMA4011("The annotation processing for the class \"{0}\" failed. The cause is as follows: {1}"),
  DOMA4014("Cannot annotate the type with @Dao because the type isn''t an interface."),
  DOMA4015(
      "Cannot annotate the type with @Entity because the type is neither a class nor a record."),
  DOMA4016(
      "An unexpected error has occurred. It may be a bug in the Doma framework. Report the following stacktrace: {0}"),
  DOMA4017("The DAO interface must be a top level interface."),
  DOMA4019("The file \"{0}\" is not found in the classpath. The absolute path is \"{1}\"."),
  DOMA4020("The SQL template \"{0}\" is empty."),
  DOMA4021("The path \"{0}\" is a directory, but it must be a file. The absolute path is \"{1}\"."),
  DOMA4024(
      "The annotation @Version is duplicated. There must be only one field annotated with @Version in the class hierarchy."),
  DOMA4025("Cannot use a name starting with \"{0}\", because it is reserved for Doma."),
  DOMA4026("The name ending with \"{0}\" may duplicate with the auto-generated classes."),
  DOMA4030(
      "When you use @SequenceGenerator, "
          + "you must annotate the same field with @GeneratedValue(strategy = GenerationType.SEQUENCE)."),
  DOMA4031(
      "When you use @TableGenerator, "
          + "you must annotate the same field with @GeneratedValue(strategy = GenerationType.TABLE)."),
  DOMA4033("When you use @GeneratedValue, " + "you must annotate the same field with @Id."),
  DOMA4034(
      "When you use @GeneratedValue(strategy = GenerationType.SEQUENCE), "
          + "you must annotate the same field with @SequenceGenerator."),
  DOMA4035(
      "When you use @GeneratedValue(strategy = GenerationType.TABLE), "
          + "you must annotate the same field with @TableGenerator."),
  DOMA4036(
      "When you use @GeneratedValue, there must be only one field annotated with @Id in the class hierarchy."),
  DOMA4037(
      "Multiple @GeneratedValue annotations are found. There must be only one field annotated with @GeneratedValue."),
  DOMA4038("The type argument of EntityListener must be a supertype of the entity class \"{2}\"."),
  DOMA4039(
      "The annotation processing is stopped because of compilation error. "
          + "Check the error message from your execution environment such as Eclipse and javac. Stacktrace: {0}"),
  DOMA4040("The return type must be an int array that indicates the affected rows count."),
  DOMA4042("The parameter type must be a subtype of java.lang.Iterable."),
  DOMA4043("The type argument of the subtype of java.lang.Iterable must be an entity class."),
  DOMA4051("The entity class cannot have type parameters."),
  DOMA4053("Multiple SelectOption parameters aren''t allowed."),
  DOMA4059("The DAO interface cannot have type parameters."),
  DOMA4062("The parameter type annotated with @ResultSet must be java.util.List."),
  DOMA4063("The return type \"{0}\" isn''t supported in the method annotated with @Function."),
  DOMA4064("The return type must be void in the method annotated with @Procedure."),
  DOMA4065(
      "The type argument of java.util.List is {0}. Such List isn''t supported as the return type."),
  DOMA4066(
      "The parameters of the method annotated with @Function or @Procedure "
          + "must be annotated with either of @In, @InOut, @Out or @ResultSet."),
  DOMA4067(
      "The parameter that corresponds to the variable \"{0}\" "
          + "in the SQL statement at column {1} is not found in the method."),
  DOMA4068("Failed to read the SQL template \"{0}\". The cause is as follows: {1}"),
  DOMA4069("Failed to parse the SQL template \"{0}\". The cause is as follows: {1}"),
  DOMA4071(
      "The variable \"{2}\" in the expression \"{0}\" at column {1} does not have a public and non-void method \"{4}\". "
          + "The variable type is \"{3}\"."),
  DOMA4072("The function \"{2}\" in the expression \"{0}\" at column {1} is not found."),
  DOMA4076("The parameter type must be an array type."),
  DOMA4078("The number of parameters must be 0."),
  DOMA4084("The property \"{0}\" is not found in the entity class \"{1}\"."),
  DOMA4085("The property \"{0}\" is not found in the entity class \"{1}\"."),
  DOMA4086("The annotation \"{0}\" competes with the annotation \"{1}\". Use either of them."),
  DOMA4087("The annotation \"{0}\" competes with the annotation \"{1}\". Use either of them."),
  DOMA4088(
      "When you annotate the field with @Id or @Version, "
          + "you must not annotate the field with @Column(insertable=false)."),
  DOMA4089(
      "When you annotate the field with @Id or @Version, "
          + "you must not annotate the field with @Column(updatable=false) to the same field."),
  DOMA4090("Starting processing of annotation \"{0}\" on element \"{1}\"."),
  DOMA4091("Finished processing of annotation \"{0}\" on element \"{1}\"."),
  DOMA4092(
      "Failed to verify the SQL template \"{0}\" on line {2} at column {3}. The cause is as follows: {4} SQL=[{1}]."),
  DOMA4093("The field annotated with @Version must be numeric."),
  DOMA4095("The field annotated with @GeneratedValue must be numeric."),
  DOMA4096(
      "The class \"{0}\" is not supported as a persistent type. "
          + "By using @ExternalDomain, you can make that type persistent. "
          + "Or, did you perhaps forget to specify @Association or @Transient for the field?"),
  DOMA4097("The return type must be {0}."),
  DOMA4098("The type of the parameter annotated with @Out must be org.seasar.doma.jdbc.Reference."),
  DOMA4100("{0} is illegal as the type argument of Reference."),
  DOMA4101("{0} isn''t supported as the type of the parameter annotated with @In."),
  DOMA4102("{0} is not supported as a persistent type."),
  DOMA4103(
      "The non-private constructor that has the parameter type \"{0}\" is not found. "
          + "Define the constructor or specify the factory method name to the annotation @Domain if you use the factory method."),
  DOMA4104(
      "The accessor method \"{0}\" is not found. "
          + "The method must have the return type \"{1}\" and must be non-private and non-args."),
  DOMA4105("You can annotate only classes, interfaces, enums and records with @Domain"),
  DOMA4106(
      "The factory method \"{0}\" is not found. "
          + "The method must have the return type \"{1}\" and the parameter type \"{2}\" and must be non-private and static. "
          + "The type parameter of the method must be same with the type parameter of the class. "
          + "Define the factory method. "
          + "Or if you do not use the factory method, "
          + "do not specify the value to the factoryMethod element of @Domain and define the constructor."),
  DOMA4108("The type argument is required for org.seasar.doma.jdbc.Reference."),
  DOMA4109(
      "The type argument is required for the subtype of java.lang.Iterable in the method return type."),
  DOMA4111("The parameter type annotated with @InOut must be org.seasar.doma.jdbc.Reference."),
  DOMA4112("The wildcard is not supported for the parameter type \"{0}\"."),
  DOMA4113("The wildcard is not supported for the return type \"{0}\"."),
  DOMA4114(
      "The type \"{3}\" of the variable \"{2}\" in the expression \"{0}\" at column {1} "
          + "does not have the instance field \"{4}\"."),
  DOMA4115("The constructor \"{2}\" in the expression \"{0}\" at column {1} is not found."),
  DOMA4116(
      "The type \"{4}\" of the left operand \"{3}\" and the type \"{6}\" of the right operand \"{5}\" are "
          + "not the same for the operator \"{2}\" in the expression \"{0}\" at column {1}."),
  DOMA4117(
      "The type \"{4}\" of the left operand \"{3}\" for the operator \"{2}\" "
          + "in the expression \"{0}\" at column {1} is neither boolean nor Boolean."),
  DOMA4118(
      "The type \"{4}\" of the right operand \"{3}\" for the operator \"{2}\" "
          + "in the expression \"{0}\" at column {1} is neither boolean nor Boolean."),
  DOMA4119(
      "The type \"{4}\" of the operand \"{3}\" for the operator \"{2}\" "
          + "in the expression \"{0}\" at column {1} is neither boolean nor Boolean."),
  DOMA4120(
      "The type \"{4}\" of the left operand \"{3}\" for the operator \"{2}\" "
          + "in the expression \"{0}\" at column {1} is not numeric."),
  DOMA4121(
      "The type \"{4}\" of the right operand \"{3}\" for the operator \"{2}\" "
          + "in the expression \"{0}\" at column {1} is not numeric."),
  DOMA4122(
      "Failed to verify the SQL template \"{0}\". "
          + "The parameter \"{1}\" of the method is not referred in the SQL template."),
  DOMA4124(
      "The mutable entity class must have a non-private and no-args constructor. "
          + "To make it immutable, specify \"true\" to the immutable element of @Entity."),
  DOMA4125(
      "@OriginalStates is duplicated. "
          + "There must be only one field annotated with @OriginalStates in the class hierarchy."),
  DOMA4126(
      "To execute concatenation, the type \"{4}\" of the right operand \"{3}\" in the expression \"{0}\" "
          + "at column {1} must be either String, Character or char. "
          + "To execute addition, both of the operands must be numeric."),
  DOMA4132(
      "If the factoryMethod element of @Domain is specified with \"new\", the class must not be abstract."),
  DOMA4135(
      "The type of the field that is annotated with @OriginalStates must be same with the type of the entity class."),
  DOMA4138("The class \"{2}\" in the expression \"{0}\" at column {1} is not found."),
  DOMA4139(
      "The operator \"{2}\" cannot be applied to the null literal in the expression \"{0}\" at column {1}."),
  DOMA4140(
      "Failed to verify the SQL template \"{0}\" on line {2} at column {3}. "
          + "While the expression \"{4}\" in \"/*%if ...*/\" is evaluated as the type \"{5}\", "
          + "the type must be either boolean or Boolean. SQL=[{1}]"),
  DOMA4141(
      "Failed to verify the SQL template \"{0}\" on line {2} at column {3}. "
          + "While the expression \"{4}\" in \"/*%elseif ...*/\" is evaluated as the type \"{5}\", "
          + "the type must be either boolean or Boolean. SQL=[{1}]"),
  DOMA4143("Failed to get the SQL template \"{0}\". The cause is as follows: {1}"),
  DOMA4144("Failed to recognize the children files of the directory \"{0}\"."),
  DOMA4145("The class \"{2}\" in the expression \"{0}\" at column {1} is not found."),
  DOMA4146(
      "The class \"{2}\" in the expression \"{0}\" at column {1} does not have "
          + "the method \"{3}\" that returns non-void type and is public and static."),
  DOMA4148(
      "The class \"{2}\" in the expression \"{0}\" at column {1} does not have the static field \"{3}\"."),
  DOMA4149(
      "Failed to verify the SQL template \"{0}\" on line {2} at column {3}. "
          + "While the expression \"{4}\" in \"/*%for ...*/\" is evaluated as the type \"{5}\", "
          + "the type must be a subtype of java.lang.Iterable or an array type. SQL=[{1}]"),
  DOMA4150(
      "Failed to verify the SQL template \"{0}\" on line {2} at column {3}. "
          + "While the expression \"{4}\" in \"/*%for ...*/\" is evaluated as the type \"{5}\", "
          + "the type argument for the type is obscure. SQL=[{1}]"),
  DOMA4153(
      "Failed to verify the SQL template \"{0}\" on line {2} at column {3}. "
          + "The parameter type that corresponds to the expression \"{4}\" must be the basic or domain class. "
          + "But the actual type is \"{5}\". "
          + "You may forget to access to its field or to invoke its method. SQL=[{1}]"),
  DOMA4154("The entity class \"{0}\" as the return type must not be abstract."),
  DOMA4155(
      "The entity class \"{0}\" that is the type argument of java.util.List must not be abstract."),
  DOMA4156(
      "The entity class \"{0}\" that is the type argument of java.util.List must not be abstract."),
  DOMA4157(
      "The entity class \"{0}\" that is the type argument of java.util.List must not be abstract."),
  DOMA4159("The type argument is required for the subtype of java.lang.Iterable."),
  DOMA4160("The type argument for the subtype of java.lang.Iterable must not be wildcard."),
  DOMA4161(
      "Failed to verify the SQL template \"{0}\" on line {2} at column {3}. "
          + "The parameter type that corresponds to the expression \"{4}\" must be a subtype of either java.lang.Iterable or an array type. "
          + "But the actual type is \"{5}\". "
          + "If the actual type is a subtype of java.lang.Iterable, ensure that the type parameter or it is resolved with one of the supported types. "
          + "If the actual type is an array type, ensure that the component type of it is one of the supported types. "
          + "You may forget to access to its field or to invoke its method. SQL=[{1}]"),
  DOMA4163(
      "The user defined Config class must not be abstract. " + "The class \"{0}\" is abstract."),
  DOMA4164(
      "The user defined Config class must have a no-args and "
          + "public constructor or have a public static final field that is named \"INSTANCE\".　"
          + "The type of the field must be a subtype of org.seasar.doma.Config. The class \"{0}\" does not meet these requirements."),
  DOMA4166("The entity listener class must not be abstract. The class \"{0}\" is abstract."),
  DOMA4167(
      "The entity listener class must have a public no-args constructor. "
          + "The class \"{0}\" does not have such a constructor."),
  DOMA4168(
      "The implementation class \"{0}\" of org.seasar.doma.jdbc.id.TableIdGenerator must not be abstract."),
  DOMA4169(
      "The implementation class \"{0}\" of org.seasar.doma.jdbc.id.TableIdGenerator must have a public no-args constructor."),
  DOMA4170(
      "The implementation class \"{0}\" of org.seasar.doma.jdbc.id.SequenceIdGenerator must not be abstract."),
  DOMA4171(
      "The implementation class \"{0}\" of org.seasar.doma.jdbc.id.SequenceIdGenerator must have a public no-args constructor."),
  DOMA4172("The return type must be void."),
  DOMA4173("The number of parameters is 0."),
  DOMA4181(
      "The SQL template \"{0}\" contains embedded variable directives. "
          + "Because the SQL statement in a batch is immutable, the embedded variable directives cannot change the SQL statement dynamically. "
          + "To suppress this warning, annotate the method with @Suppress(messages = '{ Message.DOMA4181 }')."),
  DOMA4182(
      "The SQL template \"{0}\" contains condition directives. "
          + "Because the SQL statement in a batch is immutable, the condition directives cannot change the SQL statement dynamically. "
          + "To suppress this warning, annotate the method with @Suppress(messages = '{ Message.DOMA4182 }')."),
  DOMA4183(
      "The SQL template \"{0}\" contains loop directives. "
          + "Because the SQL statement in a batch is immutable, the loop directives cannot change the SQL statement dynamically. "
          + "To suppress this warning, annotate the method with @Suppress(messages = '{ Message.DOMA4183 }')."),
  DOMA4184(
      "When you annotate the enum type with @Domain, "
          + "you cannot specify \"new\" to the factoryMethod element of @Domain "
          + "because \"new\" means the usage of constructor. "
          + "Specify the name of the factory method that is static and non-private."),
  DOMA4185(
      " ... /** The SQL statement is too long. Only the first {0} characters are displayed. */"),
  DOMA4186(
      "The type argument \"{0}\" is not supported for java.util.List. "
          + "Supported types are as follows: basic classes, domain classes, entity classes and java.util.Map<String, Object>."),
  DOMA4188("Only one interface annotated with @Dao can be used as the parent interface."),
  DOMA4189(
      "Failed to resolve the function \"{2}\" in the expression \"{0}\" at column {1}. "
          + "The class \"{3}\" that is specified for the annotation processing option \"doma.expr.functions\" is not found."),
  DOMA4190(
      "Failed to resolve the function \"{2}\" in the expression \"{0}\" at column {1}. "
          + "The class \"{3}\" that is specified for the annotation processing option \"doma.expr.functions\" "
          + "must be a subtype of org.seasar.doma.expr.ExpressionFunctions."),
  DOMA4191(
      "You can annotate only subtypes of org.seasar.doma.jdbc.domain.DomainConverter with @ExternalDomain."),
  DOMA4192("The class annotated with @ExternalDomain must not be abstract."),
  DOMA4193("The class annotated with @ExternalDomain must have a public and no-args constructor."),
  DOMA4194(
      "The second type argument \"{0}\" of org.seasar.doma.jdbc.domain.DomainConverter is not supported as a persistent type."),
  DOMA4196("The type \"{0}\" is not annotated with @ExternalDomain."),
  DOMA4197(
      "The package of the first type argument \"{0}\" of org.seasar.doma.jdbc.domain.DomainConverter is default package. "
          + "It is not supported."),
  DOMA4198("The class that is annotated with @ExternalDomain must be a top level class."),
  DOMA4200(
      "The class \"{0}\" that is specified for the annotation processing option \"doma.domain.converters\" is not found."),
  DOMA4201(
      "The class \"{0}\" that is specified for the annotation processing option \"doma.doma.converters\" "
          + "is not annotated with @DomainConverters."),
  DOMA4202("The type argument of org.seasar.doma.jdbc.entity.EntityListener is not resolved."),
  DOMA4203(
      "All type arguments of the type \"{0}\" that is first type argument of org.seasar.doma.jdbc.doma.DomainConverter "
          + "must be wildcard."),
  DOMA4204("The raw type of the class \"{0}\" cannot be used as an entity property."),
  DOMA4205(
      "The class \"{0}\" whose type arguments contain a wildcard or type variable cannot be used as an entity property."),
  DOMA4206("The raw type of the class \"{0}\" cannot be used as a return type of the DAO method."),
  DOMA4207(
      "The class \"{0}\" whose type arguments contain a wildcard or type variable "
          + "cannot be used as a return type of the DAO method."),
  DOMA4208(
      "The raw type of the class \"{0}\" cannot be used as a parameter type of the DAO method."),
  DOMA4209(
      "The class \"{0}\" whose type arguments contain a wildcard or type variable "
          + "cannot be used as a parameter type of the DAO method."),
  DOMA4210(
      "The raw type of the class \"{0}\" cannot be used as a type argument of the subtype of Iterable."),
  DOMA4211(
      "The class \"{0}\" whose type arguments contain a wildcard or type variable "
          + "cannot be used as a type argument of the subtype of Iterable."),
  DOMA4212(
      "The raw type of the class \"{0}\" cannot be used as a type argument of the subtype of Iterable."),
  DOMA4213(
      "The class \"{0}\" whose type arguments contain a wildcard or type variable "
          + "cannot be used as a type argument of the subtype of Iterable."),
  DOMA4218("The raw type of the class \"{0}\" cannot be used as a type argument of Reference"),
  DOMA4219(
      "The class \"{0}\" whose type arguments contain a wildcard or type variable "
          + "cannot be used as a type argument of Reference."),
  DOMA4220(
      "The SQL template \"{0}\" that is not mapped to any methods was found. "
          + "Check the method names or the sqlFile elements of annotations. "
          + "To suppress this warning, annotate the DAO interface with @Suppress(messages = '{ Message.DOMA4220 }')."),
  DOMA4221("A non-private constructor is required for the immutable entity class."),
  DOMA4222(
      "When the immutable entity class is a parameter type for the method annotated with @Insert, @Update, or @Delete, "
          + "the return type must be org.seasar.doma.jdbc.Result. "
          + "The type argument of org.seasar.doma.jdbc.Result must be the same entity class as the parameter type of the method."),
  DOMA4223(
      "When the immutable entity class is the type argument of the subtype of Iterable and "
          + "the iterable is a parameter type for the method annotated with @BatchInsert, @BatchUpdate, or @BatchDelete, "
          + "the return type must be org.seasar.doma.jdbc.BatchResult. "
          + "The type argument of org.seasar.doma.jdbc.BatchResult must be the same entity class as the type argument of the iterable."),
  DOMA4224("Cannot annotate the fields of the immutable entity class with @OriginalStates."),
  DOMA4225(
      "The \"final\" modifier is required for all persistent fields of the immutable entity class."),
  DOMA4226(
      "The value of the immutable element of @Entity must be consistent in the class hierarchy."),
  DOMA4227(
      "The number of type parameters of the entity listener class must be less than or equal to 1."),
  DOMA4228(
      "The type parameter \"{0}\" of the entity listener class is not passed to "
          + "the type argument of org.seasar.doma.jdbc.entity.EntityLister in the class hierarchy."),
  DOMA4229(
      "The upper bound \"{1}\" of the type parameter \"{0}\" of the entity listener class "
          + "is not compatible with the entity class \"{2}\"."),
  DOMA4230(
      "The entity listener class \"{0}\" that is inherited from the parent entity class "
          + "must have a type parameter that accepts the entity class \"{1}\"."),
  DOMA4231(
      "The upper bound \"{2}\" of the type parameter \"{1}\" of the entity listener class \"{0}\" "
          + "that is inherited from the parent entity class is not compatible with the entity class \"{3}\"."),
  DOMA4232("The raw type of the class \"{0}\" cannot be used."),
  DOMA4233(
      "The type argument of the class \"{0}\" must not be a wildcard or type variable. "
          + "Instead specify the basic type or the domain type."),
  DOMA4234(
      "The entity class \"{0}\" as a type argument of java.util.Optional must not be abstract."),
  DOMA4235(
      "The type argument \"{0}\" is not supported for java.util.Optional. "
          + "Supported types are as follows: basic classes, domain classes and entity classes."),
  DOMA4236("The raw type of the class \"{0}\" cannot be used."),
  DOMA4237(
      "The type argument of the class \"{0}\" must not be a wildcard or type variable. "
          + "Instead specify one of basic classes and domain classes."),
  DOMA4238(
      "The raw type of the class \"{0}\" cannot be used as a type argument of java.util.Optional."),
  DOMA4239(
      "The class \"{0}\" whose type arguments contain a wildcard or type variable "
          + "cannot be used as a type argument of java.util.Optional."),
  DOMA4240("The class \"{0}\" of the parameter must not be the raw type."),
  DOMA4241("The class \"{0}\" of the parameter must not be the wildcard type."),
  DOMA4242("The raw type of the class \"{0}\" cannot be used as a type argument of Stream."),
  DOMA4243(
      "The class \"{0}\" whose type arguments contain a wildcard or type variable "
          + "cannot be used as a type argument of Stream."),
  DOMA4244("The first type argument of Function must be java.util.stream.Stream."),
  DOMA4245("The type argument of Stream is not supported."),
  DOMA4246(
      "The return type \"{0}\" and the second type argument \"{1}\" of java.util.function.Function are not the same."),
  DOMA4247(
      "When you use the java.util.function.Function parameter, "
          + "SelectStrategyType.STREAM must be specified for the strategy element of @Select."),
  DOMA4248(
      "When you specify SelectStrategyType.STREAM for the strategy element of @Select, "
          + "the java.util.function.Function parameter is required for the method."),
  DOMA4249("Multiple java.util.function.Function parameters are not allowed."),
  DOMA4250("The entity class \"{0}\" as a type argument of Stream must not be abstract."),
  DOMA4251(
      "When the primitive type is specified for the valueType element of @Domain, "
          + "the acceptNull element of @Domain must be \"false\"."),
  DOMA4252("The default method must not be annotated with the annotation \"{0}\"."),
  DOMA4253(
      "The annotated type isn''t a subtype of org.seasar.doma.Config. "
          + "@SingletonConfig is valid for only subtypes of org.seasar.doma.Config."),
  DOMA4254(
      "The method \"{0}\" is not found. "
          + "The method must be public and static. "
          + "The return type must be this class \"{1}\". "
          + "The number of parameters must be 0."),
  DOMA4255("The method \"{1}\" is not found in the class \"{0}\"."),
  DOMA4256("The constructor of the class that is annotated with @SingletonConfig must be private."),
  DOMA4257(
      "Failed to verify the SQL template \"{0}\" on line {2} at column {3}. "
          + "While the comment \"/*%expand ...*/\" is used, it cannot expand columns. "
          + "Check that the method is annotated with @Select and the result set is mapped to "
          + "the entity class returned by the method. SQL=[{1}]"),
  DOMA4258("The parameter type\"{0}\" must not be the raw type."),
  DOMA4259(
      "The first or third type argument of the parameter type \"{0}\" must not be a wildcard type."),
  DOMA4260(
      "The raw type of the class \"{0}\" cannot be used as the first type argument of Collector."),
  DOMA4261(
      "The class \"{0}\" whose type arguments contain a wildcard or type variable "
          + "cannot be used as the first type argument of Collector."),
  DOMA4262("The first type argument of Collector is not supported."),
  DOMA4263(
      "The entity class \"{0}\" as the first type argument of Collector must not be abstract."),
  DOMA4264("Multiple Collector parameters are not allowed."),
  DOMA4265(
      "The return type \"{0}\" and the third type argument \"{1}\" of Collector are not the same."),
  DOMA4266(
      "When you specify SelectStrategyType.COLLECT for the strategy element of @Select, "
          + "the java.util.stream.Collector parameter is required for the method."),
  DOMA4267(
      "The type argument \"{0}\" is not supported for java.util.Optional of java.util.List. "
          + "Supported types are as follows: basic classes and domain classes."),
  DOMA4268(
      "When you annotate the interface with @Domain, "
          + "you cannot specify \"new\" to the factoryMethod element of @Domain because \"new\" means the usage of constructor. "
          + "Specify the name of the factory method that is static and non-private."),
  DOMA4270(
      "Failed to verify the SQL template \"{0}\" on line {2} at column {3}. "
          + "While the comment \"/*%populate */\" is used, it cannot generate the SET clause. "
          + "Check that the method is annotated with either @Update or @BatchUpdate and "
          + "the type of the first parameter of the method is an entity class. SQL=[{1}]"),
  DOMA4271("\"{0}\" is not supported for the type argument of java.util.stream.Stream."),
  DOMA4272(
      "The abstract entity class \"{0}\" is not supported for the type argument of java.util.stream.Stream."),
  DOMA4274(
      "The application must close the Stream object returned from the DAO method. "
          + "To suppress this warning, annotate the method with @Suppress(messages = '{ Message.DOMA4274 }')."),
  DOMA4275(
      "The type \"{0}\" is not public and static. "
          + "The domain class and its enclosing type must be public and static."),
  DOMA4276(
      "The type \"{0}\" is a local or anonymous class. "
          + "The domain class and its enclosing type must be a top level or member class."),
  DOMA4277(
      "The simple name of the type \"{0}\" contains \"$\" or \"__\". "
          + "The simple name of the domain class and its enclosing type must not contain them."),
  DOMA4278(
      "The type \"{0}\" is not public and static. "
          + "The first type argument of DomainConverter and its enclosing type must be must be public and static."),
  DOMA4279(
      "The type \"{0}\" is a local or anonymous class. "
          + "The first type argument of DomainConverter and its enclosing type  must be a top level or member class."),
  DOMA4280(
      "The simple name of the type \"{0}\" contains \"$\" or \"__\". "
          + "The simple name of the first type argument of DomainConverter and its enclosing type must not contain them."),
  DOMA4281(
      "The number, type and name of the constructor parameters must correspond to "
          + "those of persistent fields in the immutable entity class."),
  DOMA4283("You can annotate only classes and records with @Embeddable."),
  DOMA4285("The embeddable class must not have a type parameter."),
  DOMA4286("The fields of the embeddable class cannot be annotated with @OriginalStates."),
  DOMA4288("The annotation \"{0}\" competes with the annotation \"{1}\"."),
  DOMA4289("You cannot annotate the fields of the embeddable class with @Id."),
  DOMA4290("You cannot annotate the fields of the embeddable class with @Version."),
  DOMA4291("You cannot annotate the fields of the embeddable class with @GeneratedValue."),
  DOMA4293(
      "The number, type and name of the constructor parameters must correspond to "
          + "those of persistent fields in the embeddable class."),
  DOMA4294("A non-private constructor is required for the embeddable class."),
  DOMA4295("The raw type of the class \"{0}\" cannot be used as a persistent property."),
  DOMA4296(
      "The class \"{0}\" whose type arguments contains a wildcard or type variable "
          + "cannot be used as a persistent property."),
  DOMA4297(
      "The embeddable class \"{0}\" cannot be used as a persistent property in the embeddable class."),
  DOMA4298(
      "The class \"{0}\" is not supported as a persistent type. By using @ExternalDomain, you can make that type persistent."),
  DOMA4299("The raw type of the class \"{0}\" cannot be used as a persistent property."),
  DOMA4301(
      "The type argument of the class \"{0}\" that is a wildcard or type variable cannot be used as a persistent property."),
  DOMA4302("You cannot annotate the field with @Id if the field type is an embeddable class."),
  DOMA4303(
      "You cannot annotate the field with @GeneratedValue if the field type is an embeddable class."),
  DOMA4304("You cannot annotate the field with @Version if the field type is an embeddable class."),
  DOMA4305("When the entity class has an embedded property, @OriginalStates cannot be used."),
  DOMA4306("You cannot annotate the field with @Column if the field type is an embeddable class."),
  DOMA4309(
      "The file path \"{0}\" is different from the path \"{1}\" in the filesystem when compared in a case-sensitive manner."),
  DOMA4315(
      "The type \"{0}\" is not public and static. "
          + "The entity class and its enclosing type must be public and static."),
  DOMA4316(
      "The type \"{0}\" is a local or anonymous class. "
          + "The entity class and its enclosing type must be a top level or member class."),
  DOMA4317(
      "The simple name of the type \"{0}\" contains \"$\" or \"__\". "
          + "The simple name of the entity class and its enclosing type must not contain them."),
  DOMA4415(
      "The type \"{0}\" is not public and static. "
          + "The embeddable class and the its enclosing type must be public and static."),
  DOMA4416(
      "The type \"{0}\" is a local or anonymous class. "
          + "The embeddable class and its enclosing type must be a top level or member class."),
  DOMA4417(
      "The simple name of the type \"{0}\" contains \"$\" or \"__\". "
          + "The simple name of the embeddable class and its enclosing type must not contain them."),
  DOMA4418(
      "When you annotate the entity class with @lombok.Value, "
          + "you must specify \"true\" to the immutable element of @Entity."),
  DOMA4419("The usage of the staticConstructor element of @lombok.Value is not supported."),
  DOMA4420(
      "When you annotate the entity class with @lombok.AllArgsConstructor, "
          + "you must specify \"true\" to the immutable element of @Entity."),
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
      "The method name that is generated by @lombok.Value is not the same as the value of the accessorMethod element of @Domain."),
  DOMA4430("There is no instance field to be initialized by @lombok.Value."),
  DOMA4431(
      "The number of instance fields initialized by @lombok.Value must be 1, "
          + "but the actual number is greater than or equal to 2."),
  DOMA4432(
      "The type \"{0}\" of the instance field that is initialized by @lombok.Value "
          + "is not the same as the type \"{1}\" that is specified to the valueType element of @Domain."),
  DOMA4433(
      "When you annotate the method with @SqlProcessor, the BiFunction parameter is required for the method."),
  DOMA4434("Multiple BiFunction parameters are not allowed."),
  DOMA4435("The second type argument of BiFunction must be org.seasar.doma.jdbc.PreparedSql."),
  DOMA4436(
      "The return type \"{0}\" is not the same as the third type argument \"{1}\" of BiFunction."),
  DOMA4437("The first type argument of BiFunction must be org.seasar.doma.jdbc.Config."),
  DOMA4438("The parameter type \"{0}\" must not be the raw type."),
  DOMA4439("The parameter type \"{0}\" must not contain a wildcard as a type argument."),
  DOMA4440(
      "The method \"{0}\" in the parent interface is not a default method. "
          + "When the parent interface is not annotated with @Dao, all methods in the interface must be default methods."),
  DOMA4441(
      "You cannot annotate the field with @TenantId if the field type is an embeddable class."),
  DOMA4442(
      "The annotation @TenantId is duplicated. There must be only one field annotated with @TenantId in the class hierarchy."),
  DOMA4443("You cannot annotate the fields of the embeddable class with @TenantId."),
  DOMA4444("This annotation cannot be combined with the annotation \"{0}\"."),
  DOMA4445("When the method is annotated with @Sql, the sqlFile element must be \"false\"."),
  DOMA4446("This annotation cannot be used on a default method."),
  DOMA4447("The multidimensional array is not supported as a domain class."),
  DOMA4448("The component type of the array must not have any type parameter."),
  DOMA4449("You can annotate only records with @DateType."),
  DOMA4450(
      "The simple name of the type \"{0}\" contains \"$\" or \"__\". "
          + "The simple name of the data type and its enclosing type must not contain them."),
  DOMA4451(
      "The type \"{0}\" is not public and static. "
          + "The data type and its enclosing type must be public and static."),
  DOMA4452(
      "The type \"{0}\" is a local or anonymous class. "
          + "The data type and its enclosing type must be a top level or member class."),
  DOMA4453("The non-private 1-arg constructor is required."),
  DOMA4454("The parameter type \"{0}\" is not supported as a persistent type."),
  DOMA4455("The combination of the prefix=\"{0}\" and the suffix=\"\" is not allowed."),
  DOMA4456("There must be only one non-private 1-arg constructor, but more than one was found."),
  DOMA4457("You must always receive the EntityMetamodel as the first parameter."),
  DOMA4458("You cannot use static methods."),
  DOMA4459("The method must be public."),
  DOMA4460(
      "The first type argument \"{0}\" of org.seasar.doma.jdbc.domain.DomainConverter must not be a basic type. However, enum types are exceptionally allowed."),
  DOMA4461(
      "If a method annotated with @MultiInsert targets immutable entities for insertion, the return type must be org.seasar.doma.jdbc.MultiResult. "
          + "The type argument of org.seasar.doma.jdbc.MultiResult must be the immutable entity class."),
  DOMA4462("The property \"{0}\" is not found in the entity class \"{1}\"."),
  DOMA4463("'{'\"execTimeMillis\": {0}, \"annotation\": \"{1}\", \"element\": \"{2}\"'}'"),
  DOMA4464("Fields annotated with AssociationLinker must be static."),
  DOMA4465("Fields annotated with AssociationLinker must be BiFunction or BiConsumer."),
  DOMA4466("The {0} type parameter of BiFunction must be an entity class."),
  DOMA4467("The first and third type parameters of BiFunction must be the same"),
  DOMA4468("The tableAlias must not be blank."),
  DOMA4469("No field annotated with @AssociationLinker was found in the class \"{0}\"."),
  DOMA4470("Fields annotated with AssociationLinker must be public."),
  DOMA4471("Fields annotated with AssociationLinker must be final."),
  DOMA4472("The propertyPath must not be blank."),
  DOMA4473(
      "When the aggregateStrategy element of @Select is set, the return type must be one of the following: "
          + "the entity class, "
          + "a `List` containing elements of the entity class, "
          + "an `Optional` containing an element of the entity class, "
          + "or a `List` of `Optional` containing elements of the entity class."),
  DOMA4474("The field \"{0}\" could not be found in the class \"{1}\"."),
  DOMA4475(
      "The first type parameter of BiFunction or BiConsumer differs from the type resolved by the propertyPath. type parameter=\"{0}\", resolved type=\"{1}\"."),
  DOMA4476(
      "The second type parameter of BiFunction or BiConsumer differs from the type resolved by the propertyPath. type parameter=\"{0}\", resolved type=\"{1}\"."),
  DOMA4477(
      "The type of field \"{0}\" in class \"{1}\" must be one of the following: "
          + "the entity class, "
          + "a `List` containing elements of the entity class, "
          + "an `Optional` containing an element of the entity class, "
          + "or a `List` of `Optional` containing elements of the entity class. actual type=\"{2}\""),
  DOMA4478("The root element of @AggregateStrategy must be a class annotated with @Entity."),
  DOMA4479("The root element of @AggregateStrategy must be a class annotated with @Entity."),
  DOMA4480(
      "The return type of the entity \"{0}\" does not match the root element type of @AggregateStrategy \"{1}\"."),
  DOMA4481(
      "The table alias \"{0}\" is duplicated in @AggregateStrategy or another @AssociationLinker."),
  DOMA4482("An element annotated with @AggregateStrategy must be an interface."),
  DOMA4483("The visibility of an interface annotated with @AggregateStrategy must not be private."),
  DOMA4484(
      "When the aggregateStrategy element of @Select is set, the strategy element must be SelectType.RETURN."),
  DOMA4485(
      "The type of field \"{0}\" annotated with @Association in class \"{1}\" must be one of the following: "
          + "the entity class, "
          + "a `List` containing elements of the entity class, "
          + "an `Optional` containing an element of the entity class, "
          + "or a `List` of `Optional` containing elements of the entity class. actual type=\"{2}\""),
  DOMA4486(
      "The field \"{0}\" in class \"{1}\" was found, but it is not annotated with @Association."),
  DOMA4487("An element annotated with @AggregateStrategy must not extend other interfaces."),
  DOMA4488(
      "To specify propertyPath=\"{0}\", a separate definition of @AssociationLinker(propertyPath=\"{1}\") is required."),
  DOMA4489("The property path \"{0}\" is duplicated in another @AssociationLinker."),
  DOMA4490(
      "Multiple @ExternalDomain definitions were found for type \"{0}\". \"{1}\" conflicts with \"{2}\"."),
  DOMA4491("\"returning = @Returning\" cannot be specified when \"sqlFile = true\"."),
  DOMA4492("\"returning = @Returning\" is not allowed in combination with @Sql."),
  DOMA4493("The property \"{0}\" is not found in the entity class \"{1}\"."),
  DOMA4494("The property \"{0}\" is not found in the entity class \"{1}\"."),
  DOMA4495(
      "When \"returning = @Returning\" is specified, the return type must be the same as the parameter type or an Optional whose element is the parameter type."),
  DOMA4496(
      "When \"returning = @Returning\" is specified, the return type must be a List of the entity class \"{0}\"."),
  DOMA4497("The {0} type parameter of BiConsumer must be an entity class."),
  DOMA4498(
      "You cannot annotate the field with @Embedded if the field type is not an embeddable class."),
  DOMA4499("The property \"{0}\" is not found in the embeddable class \"{1}\"."),

  // other
  DOMA5001(
      "The JDBC driver may not be loaded. "
          + "Check that the JDBC driver is in the classpath. "
          + "If the JDBC driver is not loaded automatically, load it explicitly using Class.forName. "
          + "ex) Class.forName(\"oracle.jdbc.driver.OracleDriver\")"),
  DOMA5002("The url property is not specified."),

  // criteria
  DOMA6001(
      "The parameter \"{0}\" is unknown. "
          + "Ensure that you have passed it to the from, the innerJoin, or the leftJoin method before invoking the associate method. "
          + "If the innerJoin or leftJoin method call is optional, pass the AssociationKind.OPTIONAL value to the associate method."),
  DOMA6002(
      "The propertyMetamodel \"{0}\" is unknown. Ensure that you have passed it to the select method."),
  DOMA6003("The table alias is not found for the entityMetamodel \"{0}\"."),
  DOMA6004(
      "The column alias is not found for the propertyMetamodel \"{0}\". "
          + "Ensure that you have passed its entityMetamodel to the from, the innerJoin, or the leftJoin method."),
  DOMA6005("The method \"{0}\" is not found."),
  DOMA6006(
      "Empty where clause is not allowed by default. To allow it, enable the \"allowEmptyWhere\" setting. SQL=[{0}]"),
  DOMA6007(
      "The parameter \"{0}\" is unknown. "
          + "Ensure that you have passed it to the from, the innerJoin, or the leftJoin method before invoking the selectTo method."),
  DOMA6008(
      "The propertyMetamodels[{0}] is illegal. "
          + "Ensure that the propertyMetamodel is a part of the entityMetamodel. "
          + "Any expressions such as a sum and a concat are not supported in the selectTo method."),
  DOMA6009(
      "The parameter \"{0}\" is unknown. "
          + "Ensure that you have passed it to the from, the innerJoin, or the leftJoin method before invoking the select method."),
  DOMA6010(
      "The parameter \"{0}\" is unknown. "
          + "Ensure that you have passed it to the from, the innerJoin, or the leftJoin method before invoking the associateWith method. "
          + "If the innerJoin or leftJoin method call is optional, pass the AssociationKind.OPTIONAL value to the associateWith method."),
  DOMA6011(
      "The number of propertyMetamodels in the specified derived table does not match for the propertyMetamodels in the　entityMetamodel."
          + "The number of propertyMetamodels in the entityMetamodel is {0} but the number of propertyMetamodels in the derived table is {1}."),
  DOMA6012("The specified property \"name={0}, index={1}\" is not included in the entity \"{2}\"."),
  DOMA6013("The expected entity type \"{0}\" does not match the actual entity type \"{1}\"."),
  ;

  private final String messagePattern;

  Message(String messagePattern) {
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
      return MessageFormat.format(messagePattern, args);
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
