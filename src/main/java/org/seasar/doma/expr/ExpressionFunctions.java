package org.seasar.doma.expr;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The aggregation of functions that are available in expressions in SQL
 * templates.
 */
public interface ExpressionFunctions {

    /**
     * Escapes the SQL LIKE wild card characters in a target string by using the
     * default escape character.
     * <p>
     * For example, {@code a%b_} is converted to the a {@code a$%b$_}.
     * 
     * @param text
     *            the target text
     * @return the escaped text
     */
    String escape(String text);

    /**
     * Escapes the SQL LIKE wild card characters in a target string by using a
     * specified escape character.
     * 
     * @param text
     *            the target text
     * @param escapeChar
     *            the escape character
     * @return the escaped text
     */
    String escape(String text, char escapeChar);

    /**
     * Escapes the SQL LIKE wild card characters in a target string by using the
     * default escape character '$' and generate a text to perform a prefix
     * search for the SQL Like operator.
     * <p>
     * For example, {@code a%b_} is converted to the a {@code a$%b$_%}.
     * 
     * @param prefix
     *            the prefix text
     * @return the text for a prefix search
     */
    String prefix(String prefix);

    /**
     * Escapes the SQL LIKE wild card characters in a target string by using a
     * specified escape character and generate a text to perform a prefix search
     * for the SQL LIKE operator.
     * 
     * @param prefix
     *            the prefixed text
     * @param escapeChar
     *            the escape character
     * @return the text for a prefix search
     */
    String prefix(String prefix, char escapeChar);

    /**
     * Escapes the SQL LIKE wild card characters in a target string by using the
     * default escape character '$' and generate a text to perform a suffix
     * search for the SQL LIKE operator.
     * <p>
     * For example, {@code a%b_} is converted to the a {@code %a$%b$_}.
     * 
     * @param suffix
     *            the suffix text
     * @return the text for a suffix search
     */
    String suffix(String suffix);

    /**
     * Escapes the SQL LIKE wild card characters in a target string by using a
     * specified escape character and generate a text to perform a suffix search
     * for the SQL LIKE operator.
     * 
     * @param suffix
     *            the suffix text
     * @param escapeChar
     *            the escape character
     * @return the text for a suffix search
     */
    String suffix(String suffix, char escapeChar);

    /**
     * Escapes the SQL LIKE wild card characters in a target string by using the
     * default escape character '$' and generate a text to perform a infix
     * search for the SQL LIKE operator.
     * <p>
     * For example, {@code a%b_} is converted to the a {@code %a$%b$_%}.
     * 
     * @param infix
     *            the infix text
     * @return the text for a infix search
     */
    String infix(String infix);

    /**
     * Escapes the SQL LIKE wild card characters in a target string by using a
     * specified escape character and generate a text to perform a infix search
     * for the SQL LIKE operator.
     * 
     * @param infix
     *            the infix text
     * @param escapeChar
     *            the escape character
     * @return the text for a infix search
     */
    String infix(String infix, char escapeChar);

    /**
     * Round down a time part of {@link java.util.Date}.
     * 
     * @param date
     *            the target date
     * @return the date whose time part is rounded down
     */
    java.util.Date roundDownTimePart(java.util.Date date);

    /**
     * Round down a time part of {@link Date}.
     * 
     * @param date
     *            the target date
     * @return the date whose time part is rounded down
     */
    Date roundDownTimePart(Date date);

    /**
     * Round down a time part of {@link Timestamp}.
     * 
     * @param timestamp
     *            the target timestamp
     * @return the timestamp whose time part is rounded down
     */
    Timestamp roundDownTimePart(Timestamp timestamp);

    /**
     * Round down a time part of {@link LocalDateTime}.
     * 
     * @param localDateTime
     *            the target localDateTime
     * @return the localDateTime whose time part is rouded down
     */
    LocalDateTime roundDownTimePart(LocalDateTime localDateTime);

    /**
     * Round up a time part of {@link java.util.Date}.
     * 
     * @param date
     *            the target date
     * @return the date whose time part is rounded up
     */
    java.util.Date roundUpTimePart(java.util.Date date);

    /**
     * Round up a time part of {@link Date}.
     * 
     * @param date
     *            the target date
     * @return the date whose time part is rounded up
     */
    Date roundUpTimePart(Date date);

    /**
     * Round up a time part of {@link Timestamp}.
     * 
     * @param timestamp
     *            the target timestamp
     * @return the timestamp whose time part is rounded up
     */
    Timestamp roundUpTimePart(Timestamp timestamp);

    /**
     * Return the next day.
     * 
     * @param localDate
     *            the target localDate
     * @return the next day
     */
    LocalDate roundUpTimePart(LocalDate localDate);

    /**
     * Round up a time part of {@link LocalDateTime}.
     * 
     * @param localDateTime
     *            the target localDateTime
     * @return the localDateTime whose time part is rouded up
     */
    LocalDateTime roundUpTimePart(LocalDateTime localDateTime);

    /**
     * Whether a text is empty.
     * 
     * @param text
     *            the target text
     * @return {@code true} if the text is {@code null} or its length is
     *         {@code 0}, else {@code false}
     */
    boolean isEmpty(CharSequence text);

    /**
     * Whether a text is not empty.
     * 
     * @param text
     *            the target text
     * @return {@code false} if the text is {@code null} or its length is
     *         {@code 0}, else {@code true}
     */
    boolean isNotEmpty(CharSequence text);

    /**
     * Whether a text is blank.
     * 
     * @param text
     *            the target text
     * @return {@code true} if the text is {@code null}, its length is {@code 0}
     *         or the text contains only blank characters, else {@code false}.
     */
    boolean isBlank(CharSequence text);

    /**
     * Whether a text is not blank.
     * 
     * @param text
     *            the target text
     * @return {@code false} if the text is {@code null}, its length is
     *         {@code 0} or the text contains only blank characters, else
     *         {@code true}.
     */
    boolean isNotBlank(CharSequence text);
}
