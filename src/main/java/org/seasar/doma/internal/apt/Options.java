package org.seasar.doma.internal.apt;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.tools.FileObject;
import org.seasar.doma.internal.Artifact;

public final class Options {

  public static final String TEST = "doma.test";

  public static final String DEBUG = "doma.debug";

  public static final String DAO_PACKAGE = "doma.dao.package";

  public static final String DAO_SUBPACKAGE = "doma.dao.subpackage";

  public static final String DAO_SUFFIX = "doma.dao.suffix";

  public static final String ENTITY_FIELD_PREFIX = "doma.entity.field.prefix";

  public static final String EXPR_FUNCTIONS = "doma.expr.functions";

  public static final String DOMAIN_CONVERTERS = "doma.domain.converters";

  public static final String SQL_VALIDATION = "doma.sql.validation";

  public static final String VERSION_VALIDATION = "doma.version.validation";

  public static final String CONFIG_PATH = "doma.config.path";

  public static final String RESOURCES_DIR = "doma.resources.dir";

  public static final String LOMBOK_ALL_ARGS_CONSTRUCTOR = "doma.lombok.AllArgsConstructor";

  public static final String LOMBOK_VALUE = "doma.lombok.Value";

  private final Context ctx;

  public Options(Context ctx) {
    this.ctx = ctx;
  }

  public boolean isTestEnabled() {
    String test = getOption(TEST);
    return Boolean.valueOf(test).booleanValue();
  }

  public String getVersion() {
    if (isTestEnabled()) {
      return "@VERSION@";
    }
    return Artifact.getVersion();
  }

  public Date getDate() {
    if (isTestEnabled()) {
      return new Date(0L);
    }
    return new Date();
  }

  public boolean isDebugEnabled() {
    String debug = getOption(DEBUG);
    return Boolean.valueOf(debug).booleanValue();
  }

  public String getDaoPackage() {
    String pkg = getOption(DAO_PACKAGE);
    return pkg != null ? pkg : null;
  }

  public String getDaoSubpackage() {
    String subpackage = getOption(DAO_SUBPACKAGE);
    return subpackage != null ? subpackage : null;
  }

  public String getDaoSuffix() {
    String suffix = getOption(DAO_SUFFIX);
    return suffix != null ? suffix : Constants.DEFAULT_DAO_SUFFIX;
  }

  public String getEntityFieldPrefix() {
    String prefix = getOption(ENTITY_FIELD_PREFIX);
    if ("none".equalsIgnoreCase(prefix)) {
      return "";
    }
    return prefix != null ? prefix : Constants.DEFAULT_ENTITY_FIELD_PREFIX;
  }

  public String getExprFunctions() {
    String name = getOption(EXPR_FUNCTIONS);
    return name != null ? name : null;
  }

  public String getDomainConverters() {
    String converters = getOption(DOMAIN_CONVERTERS);
    return converters != null ? converters : null;
  }

  public boolean getSqlValidation() {
    String v = getOption(SQL_VALIDATION);
    return v != null ? Boolean.valueOf(v).booleanValue() : true;
  }

  public boolean getVersionValidation() {
    String v = getOption(VERSION_VALIDATION);
    return v != null ? Boolean.valueOf(v).booleanValue() : true;
  }

  public String getConfigPath() {
    String configPath = ctx.getEnv().getOptions().get(CONFIG_PATH);
    return configPath != null ? configPath : Constants.DEFAULT_CONFIG_PATH;
  }

  public String getLombokAllArgsConstructor() {
    String name = getOption(LOMBOK_ALL_ARGS_CONSTRUCTOR);
    return name != null ? name : Constants.DEFAULT_LOMBOK_ALL_ARGS_CONSTRUCTOR;
  }

  public String getLombokValue() {
    String name = getOption(LOMBOK_VALUE);
    return name != null ? name : Constants.DEFAULT_LOMBOK_VALUE;
  }

  private String getOption(String key) {
    String v = ctx.getEnv().getOptions().get(key);
    if (v != null) {
      return v;
    }

    return getConfig().get(key);
  }

  private Map<String, Map<String, String>> configCache = new ConcurrentHashMap<>();

  private Map<String, String> getConfig() {
    FileObject config = getFileObject(getConfigPath());
    if (config == null) {
      return Collections.emptyMap();
    }
    return configCache.computeIfAbsent(
        config.toUri().getPath(),
        configPath -> {
          try {
            return loadProperties(config);
          } catch (IOException e) {
            return Collections.emptyMap();
          }
        });
  }

  private FileObject getFileObject(String path) {
    try {
      return ctx.getResources().getResource(path);
    } catch (IOException e) {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  private Map<String, String> loadProperties(FileObject config) throws IOException {
    try (InputStream is = config.openInputStream();
        InputStreamReader isr = new InputStreamReader(is, "UTF-8")) {
      Properties props = new Properties();
      props.load(isr);
      return (Map<String, String>) new HashMap(props);
    }
  }

  public static class Constants {

    public static final String DEFAULT_DAO_SUFFIX = "Impl";

    public static final String DEFAULT_ENTITY_FIELD_PREFIX = "$";

    public static final String DEFAULT_CONFIG_PATH = "doma.compile.config";

    public static final String DEFAULT_LOMBOK_ALL_ARGS_CONSTRUCTOR = "lombok.AllArgsConstructor";

    public static final String DEFAULT_LOMBOK_VALUE = "lombok.Value";
  }
}
