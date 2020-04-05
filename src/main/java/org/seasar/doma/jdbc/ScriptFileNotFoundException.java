package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/** Thrown to indicate that a script file is not found. */
public class ScriptFileNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** the script file path */
  protected final String path;

  /**
   * Creates an instance.
   *
   * @param path the script file path
   */
  public ScriptFileNotFoundException(String path) {
    super(Message.DOMA2012, path);
    this.path = path;
  }

  /**
   * Returns the script file path.
   *
   * @return the script file path
   */
  public String getPath() {
    return path;
  }
}
