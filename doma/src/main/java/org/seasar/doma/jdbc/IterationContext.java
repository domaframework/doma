package org.seasar.doma.jdbc;

/**
 * @author taedium
 * 
 */
public class IterationContext {

    boolean exited;

    public boolean isExited() {
        return exited;
    }

    public void exits() {
        this.exited = true;
    }

}
