package org.seasar.doma.jdbc;

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public class SelectOptions implements Options {

    protected int offset = -1;

    protected int limit = -1;

    protected SelectForUpdateType forUpdateType;

    protected int waitSeconds;

    protected String[] aliases = new String[] {};

    protected SelectOptions() {
    }

    public static SelectOptions get() {
        return new SelectOptions();
    }

    public SelectOptions forUpdate() {
        forUpdateType = SelectForUpdateType.NORMAL;
        return this;
    }

    public SelectOptions forUpdate(String... aliases) {
        if (aliases == null) {
            new DomaIllegalArgumentException("aliases", aliases);
        }
        forUpdateType = SelectForUpdateType.NORMAL;
        this.aliases = aliases;
        return this;
    }

    public SelectOptions forUpdateNowait() {
        forUpdateType = SelectForUpdateType.NOWAIT;
        return this;
    }

    public SelectOptions forUpdateNowait(String... aliases) {
        if (aliases == null) {
            new DomaIllegalArgumentException("aliases", aliases);
        }
        forUpdateType = SelectForUpdateType.NOWAIT;
        this.aliases = aliases;
        return this;
    }

    public SelectOptions forUpdateWait(int waitSeconds) {
        if (waitSeconds < 0) {
            new DomaIllegalArgumentException("waitSeconds", waitSeconds);
        }
        forUpdateType = SelectForUpdateType.WAIT;
        this.waitSeconds = waitSeconds;
        return this;
    }

    public SelectOptions forUpdateWait(int waitSeconds, String... aliases) {
        if (waitSeconds < 0) {
            new DomaIllegalArgumentException("waitSeconds", waitSeconds);
        }
        if (aliases == null) {
            new DomaIllegalArgumentException("aliases", aliases);
        }
        forUpdateType = SelectForUpdateType.WAIT;
        this.waitSeconds = waitSeconds;
        this.aliases = aliases;
        return this;
    }

    public SelectOptions offset(int offset) {
        if (offset < 0) {
            new DomaIllegalArgumentException("offset", offset);
        }
        this.offset = offset;
        return this;
    }

    public SelectOptions limit(int limit) {
        if (limit < 0) {
            new DomaIllegalArgumentException("limit", limit);
        }
        this.limit = limit;
        return this;
    }

    public SelectForUpdateType getForUpdateType() {
        return forUpdateType;
    }

    public int getWaitSeconds() {
        return waitSeconds;
    }

    public String[] getAliases() {
        return aliases;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

}
