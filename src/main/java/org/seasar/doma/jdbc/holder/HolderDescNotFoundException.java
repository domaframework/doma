package org.seasar.doma.jdbc.holder;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/** Thrown to indicate that the holder description is not found. */
public class HolderDescNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String holderClassName;

  private final String holderDescClassName;

  public HolderDescNotFoundException(
      Throwable cause, String holderClassName, String holderDescClassName) {
    super(Message.DOMA2202, cause, holderClassName, holderDescClassName, cause);
    this.holderClassName = holderClassName;
    this.holderDescClassName = holderDescClassName;
  }

  public String getHolderClassName() {
    return holderClassName;
  }

  public String getHolderDescClassName() {
    return holderDescClassName;
  }
}
