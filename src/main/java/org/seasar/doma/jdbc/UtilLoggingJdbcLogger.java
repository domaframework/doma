package org.seasar.doma.jdbc;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.seasar.doma.DomaNullPointerException;

/**
 * A logger that delegates to {@link java.util.logging.Logger}.
 */
public class UtilLoggingJdbcLogger extends AbstractJdbcLogger<Level> {

    protected final Logger logger;

    /**
     * Creates an instance.
     * <p>
     * The log level is {@link Level#INFO}. The logger name is the full qualified
     * name of {@link UtilLoggingJdbcLogger}.
     */
    public UtilLoggingJdbcLogger() {
        this(Level.INFO);
    }

    /**
     * Creates an instance with the specified log level.
     * <p>
     * The logger name is the full qualified name of
     * {@link UtilLoggingJdbcLogger}.
     * 
     * @param level
     *            the log level
     */
    public UtilLoggingJdbcLogger(Level level) {
        this(level, Logger.getLogger(UtilLoggingJdbcLogger.class.getName()));
    }

    /**
     * Creates an instance with the specified log level and logger.
     * 
     * @param level
     *            the log level
     * @param logger
     *            the logger
     */
    public UtilLoggingJdbcLogger(Level level, Logger logger) {
        super(level);
        if (logger == null) {
            throw new DomaNullPointerException("logger");
        }
        this.logger = logger;
    }

    @Override
    protected void log(Level level, String callerClassName, String callerMethodName,
            Throwable throwable, Supplier<String> messageSupplier) {
        logger.logp(level, callerClassName, callerMethodName, throwable, messageSupplier);
    }

}
