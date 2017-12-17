package org.seasar.doma.jdbc;

/**
 * A provider for a {@link Config} object.
 */
public interface ConfigProvider {

    /**
     * Returns the configuration.
     * 
     * @return the configuration
     */
    Config getConfig();
}
