package org.seasar.doma.it;

import ch.qos.logback.classic.BasicConfigurator;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.Configurator;

public class LogConfigurator extends BasicConfigurator {

  @Override
  public Configurator.ExecutionStatus configure(LoggerContext lc) {
    super.configure(lc);
    Logger logger = lc.getLogger("org.seasar.doma.jdbc.LogKind.SQL");
    logger.setLevel(Level.DEBUG);
    Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
    rootLogger.setLevel(Level.INFO);
    return Configurator.ExecutionStatus.NEUTRAL;
  }
}
