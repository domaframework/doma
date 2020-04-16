package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/** Thrown to indicate that the SQL file is not found. */
public class SqlFileNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** the SQL file path */
  protected final String path;

  /**
   * Creates an instance.
   *
   * @param path the SQL file path
   */
  public SqlFileNotFoundException(String path) {
    super(Message.DOMA2011, path);
    this.path = path;
  }

  /**
   * Returns the SQL file path.
   *
   * @return the SQL file path
   */
  public String getPath() {
    return path;
  }
}
