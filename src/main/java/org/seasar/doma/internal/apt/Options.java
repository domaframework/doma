/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.seasar.doma.internal.Artifact;

/**
 * @author taedium
 * 
 */
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

    public static final String LOMBOK_ALL_ARGS_CONSTRUCTOR = "doma.lombok.AllArgsConstructor";

    public static final String LOMBOK_VALUE = "doma.lombok.Value";

    public static boolean isTestEnabled(ProcessingEnvironment env) {
        String test = getOption(env, Options.TEST);
        return Boolean.valueOf(test).booleanValue();
    }

    public static String getVersion(ProcessingEnvironment env) {
        if (isTestEnabled(env)) {
            return "@VERSION@";
        }
        return Artifact.getVersion();
    }

    public static Date getDate(ProcessingEnvironment env) {
        if (isTestEnabled(env)) {
            return new Date(0L);
        }
        return new Date();
    }

    public static boolean isDebugEnabled(ProcessingEnvironment env) {
        String debug = getOption(env, Options.DEBUG);
        return Boolean.valueOf(debug).booleanValue();
    }

    public static String getDaoPackage(ProcessingEnvironment env) {
        String pkg = getOption(env, Options.DAO_PACKAGE);
        return pkg != null ? pkg : null;
    }

    public static String getDaoSubpackage(ProcessingEnvironment env) {
        String subpackage = getOption(env, Options.DAO_SUBPACKAGE);
        return subpackage != null ? subpackage : null;
    }

    public static String getDaoSuffix(ProcessingEnvironment env) {
        String suffix = getOption(env, Options.DAO_SUFFIX);
        return suffix != null ? suffix : Constants.DEFAULT_DAO_SUFFIX;
    }

    public static String getEntityFieldPrefix(ProcessingEnvironment env) {
        String prefix = getOption(env, Options.ENTITY_FIELD_PREFIX);
        if ("none".equalsIgnoreCase(prefix)) {
            return "";
        }
        return prefix != null ? prefix : Constants.DEFAULT_ENTITY_FIELD_PREFIX;
    }

    public static String getExprFunctions(ProcessingEnvironment env) {
        String name = getOption(env, Options.EXPR_FUNCTIONS);
        return name != null ? name : null;
    }

    public static String getDomainConverters(ProcessingEnvironment env) {
        String converters = getOption(env, Options.DOMAIN_CONVERTERS);
        return converters != null ? converters : null;
    }

    public static boolean getSqlValidation(ProcessingEnvironment env) {
        String v = getOption(env, Options.SQL_VALIDATION);
        return v != null ? Boolean.valueOf(v).booleanValue() : true;
    }

    public static boolean getVersionValidation(ProcessingEnvironment env) {
        String v = getOption(env, Options.VERSION_VALIDATION);
        return v != null ? Boolean.valueOf(v).booleanValue() : true;
    }

    public static String getConfigPath(ProcessingEnvironment env) {
        String configPath = env.getOptions().get(Options.CONFIG_PATH);
        return configPath != null ? configPath : Constants.DEFAULT_CONFIG_PATH;
    }

    public static String getLombokAllArgsConstructor(ProcessingEnvironment env) {
        String name = getOption(env, Options.LOMBOK_ALL_ARGS_CONSTRUCTOR);
        return name != null ? name : Constants.DEFAULT_LOMBOK_ALL_ARGS_CONSTRUCTOR;
    }

    public static String getLombokValue(ProcessingEnvironment env) {
        String name = getOption(env, Options.LOMBOK_VALUE);
        return name != null ? name : Constants.DEFAULT_LOMBOK_VALUE;
    }

    private static String getOption(ProcessingEnvironment env, String key) {
        String v = env.getOptions().get(key);
        if (v != null) {
            return v;
        }

        return getConfig(env).get(key);
    }

    private static Map<String, Map<String, String>> configCache = new HashMap<>();
    private static Map<String, String> getConfig(ProcessingEnvironment env) {
        FileObject config = getFileObject(env, "", getConfigPath(env));
        if (config == null) {
            return Collections.emptyMap();
        }
        return configCache.computeIfAbsent(config.toUri().getPath(), configPath -> {
            try {
                return loadProperties(config);
            } catch (IOException e) {
                return Collections.emptyMap();
            }
        });
    }

    public static FileObject getFileObject(ProcessingEnvironment env, String pkg, String relativeName) {
        Filer filer = env.getFiler();

        try {
            return filer.getResource(StandardLocation.CLASS_OUTPUT, pkg, relativeName);
        } catch (IOException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> loadProperties(FileObject config) throws IOException {
        try (InputStream is = config.openInputStream();
             InputStreamReader isr = new InputStreamReader(is, "UTF-8")){
            Properties props = new Properties();
            props.load(isr);
            return (Map<String, String>) new HashMap(props);
        }
    }

    protected static class Constants {

        public static final String DEFAULT_DAO_SUFFIX = "Impl";

        public static final String DEFAULT_ENTITY_FIELD_PREFIX = "$";

        public static final String DEFAULT_CONFIG_PATH = "doma.config";

        public static final String DEFAULT_LOMBOK_ALL_ARGS_CONSTRUCTOR = "lombok.AllArgsConstructor";

        public static final String DEFAULT_LOMBOK_VALUE = "lombok.Value";
    }
}
