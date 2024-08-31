package org.seasar.doma.it;

import static ch.qos.logback.classic.spi.Configurator.ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY;
import static ch.qos.logback.classic.spi.Configurator.ExecutionStatus.INVOKE_NEXT_IF_ANY;

import ch.qos.logback.classic.BasicConfigurator;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public class LogConfigurator extends BasicConfigurator {

  @Override
  public ExecutionStatus configure(LoggerContext lc) {
    var status = super.configure(lc);
    Logger logger = lc.getLogger("org.seasar.doma.jdbc.LogKind.SQL");
    logger.setLevel(Level.DEBUG);
    Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
    rootLogger.setLevel(Level.INFO);
    return switch (status) {
      case NEUTRAL -> DO_NOT_INVOKE_NEXT_IF_ANY;
      case INVOKE_NEXT_IF_ANY, DO_NOT_INVOKE_NEXT_IF_ANY -> status;
    };
  }
}
