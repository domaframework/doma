package org.seasar.doma.it;

import ch.qos.logback.classic.BasicConfigurator;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public class LogConfigurator extends BasicConfigurator {

  @Override
  public void configure(LoggerContext lc) {
    super.configure(lc);
    Logger logger = lc.getLogger("org.seasar.doma.jdbc.LogKind.SQL");
    logger.setLevel(Level.DEBUG);
    Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
    rootLogger.setLevel(Level.INFO);
  }
}
