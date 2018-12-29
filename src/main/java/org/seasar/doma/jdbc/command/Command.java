package org.seasar.doma.jdbc.command;

/**
 * @author taedium
 * @param <RESULT> 結果
 */
public interface Command<RESULT> {

  RESULT execute();
}
