/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt;

import static java.util.Collections.emptyMap;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.tools.FileObject;
import org.seasar.doma.internal.Artifact;

public final class Options {

  public static final String TEST_UNIT = "doma.test.unit";

  public static final String TEST_INTEGRATION = "doma.test.integration";

  public static final String METAMODEL_ENABLED = "doma.metamodel.enabled";

  public static final String METAMODEL_PREFIX = "doma.metamodel.prefix";

  public static final String METAMODEL_SUFFIX = "doma.metamodel.suffix";

  public static final String DEBUG = "doma.debug";

  public static final String TRACE = "doma.trace";

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

  private final Map<String, String> options;

  Options(Map<String, String> options, Resources resources) {
    assertNotNull(options, resources);
    var path = getConfigPath(options);
    var config = loadConfig(resources, path);
    var map = new HashMap<>(config);
    map.putAll(options);
    this.options = Collections.unmodifiableMap(map);
  }

  private static String getConfigPath(Map<String, String> options) {
    String configPath = options.get(Options.CONFIG_PATH);
    return configPath != null ? configPath : Options.Constants.DEFAULT_CONFIG_PATH;
  }

  private static Map<String, String> loadConfig(Resources resources, String path) {
    try {
      FileObject file = resources.getResource(path);
      if (file == null) {
        return emptyMap();
      }
      try (InputStream is = file.openInputStream();
          InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8)) {
        Properties props = new Properties();
        props.load(isr);
        Map<String, String> map = new HashMap<>(props.size());
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
          map.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return map;
      }
    } catch (IOException ignored) {
      return emptyMap();
    }
  }

  public boolean isTestEnabled() {
    String test = getOption(TEST_UNIT);
    return Boolean.parseBoolean(test);
  }

  public String getVersion() {
    if (isTestEnabled()) {
      return "@VERSION@";
    }
    return Artifact.getVersion();
  }

  public boolean isMetamodelEnabled() {
    String enabled = getOption(METAMODEL_ENABLED);
    return Boolean.parseBoolean(enabled);
  }

  public String getMetamodelPrefix() {
    String prefix = getOption(METAMODEL_PREFIX);
    return prefix != null ? prefix : Constants.DEFAULT_METAMODEL_PREFIX;
  }

  public String getMetamodelSuffix() {
    String suffix = getOption(METAMODEL_SUFFIX);
    return suffix != null ? suffix : Constants.DEFAULT_METAMODEL_SUFFIX;
  }

  public Date getDate() {
    if (isTestEnabled()) {
      return new Date(0L);
    }
    return new Date();
  }

  public boolean isTraceEnabled() {
    String trace = getOption(TRACE);
    return Boolean.parseBoolean(trace);
  }

  public boolean isDebugEnabled() {
    String debug = getOption(DEBUG);
    return Boolean.parseBoolean(debug);
  }

  public String getDaoPackage() {
    return getOption(DAO_PACKAGE);
  }

  public String getDaoSubpackage() {
    return getOption(DAO_SUBPACKAGE);
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
    return getOption(EXPR_FUNCTIONS);
  }

  public String getDomainConverters() {
    return getOption(DOMAIN_CONVERTERS);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean getSqlValidation() {
    String v = getOption(SQL_VALIDATION);
    return v == null || Boolean.parseBoolean(v);
  }

  public boolean getVersionValidation() {
    String v = getOption(VERSION_VALIDATION);
    return v == null || Boolean.parseBoolean(v);
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
    return options.get(key);
  }

  public static class Constants {

    public static final String DEFAULT_METAMODEL_PREFIX = "";

    public static final String DEFAULT_METAMODEL_SUFFIX = "_";

    public static final String DEFAULT_DAO_SUFFIX = "Impl";

    public static final String DEFAULT_ENTITY_FIELD_PREFIX = "$";

    public static final String DEFAULT_CONFIG_PATH = "doma.compile.config";

    public static final String DEFAULT_LOMBOK_ALL_ARGS_CONSTRUCTOR = "lombok.AllArgsConstructor";

    public static final String DEFAULT_LOMBOK_VALUE = "lombok.Value";
  }
}
