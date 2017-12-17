package org.seasar.doma.jdbc.command;

/**
 * An object that issues SQL statements to the database.
 * 
 * @param <RESULT>
 *            the result type of the execution
 */
public interface Command<RESULT> {

    /**
     * Executes this command.
     * 
     * @return the result
     */
    RESULT execute();

}
