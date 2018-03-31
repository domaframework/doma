package org.seasar.doma.jdbc;

/** An execution context of {@link IterationCallback}. */
public class IterationContext {

  /** whether {@link #exit()} is invoked */
  protected boolean exited;

  /**
   * Whether {@link #exit()} is invoked.
   *
   * @return {@code true} if {@link #exit()} is invoked
   */
  public boolean isExited() {
    return exited;
  }

  /** Exits from an execution of {@link IterationCallback}. */
  public void exit() {
    this.exited = true;
  }
}
