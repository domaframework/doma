package org.seasar.doma.jdbc.domain;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/** Thrown to indicate that the domain description is not found. */
public class DomainTypeNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String domainClassName;

  private final String domainTypeClassName;

  public DomainTypeNotFoundException(
      Throwable cause, String domainClassName, String domainTypeClassName) {
    super(Message.DOMA2202, cause, domainClassName, domainTypeClassName, cause);
    this.domainClassName = domainClassName;
    this.domainTypeClassName = domainTypeClassName;
  }

  public String getDomainClassName() {
    return domainClassName;
  }

  public String getDomainTypeClassName() {
    return domainTypeClassName;
  }
}
