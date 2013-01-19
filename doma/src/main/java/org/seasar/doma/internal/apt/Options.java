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

import java.util.Date;

import javax.annotation.processing.ProcessingEnvironment;

import org.seasar.doma.internal.Artifact;

/**
 * @author taedium
 * 
 */
public final class Options {

    public static final String TEST = "test";

    public static final String DEBUG = "debug";

    public static final String DAO_PACKAGE = "dao.package";

    public static final String DAO_SUBPACKAGE = "dao.subpackage";

    public static final String DAO_SUFFIX = "dao.suffix";

    public static final String ENTITY_FIELD_PREFIX = "entity.field.prefix";

    public static final String EXPR_FUNCTIONS = "expr.functions";

    public static final String DOMAIN_CONVERTERS = "domain.converters";

    public static final String SQL_VALIDATION = "sql.validation";

    public static final String VERSION_VALIDATION = "version.validation";

    public static boolean isTestEnabled(ProcessingEnvironment env) {
        String test = env.getOptions().get(Options.TEST);
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
        String debug = env.getOptions().get(Options.DEBUG);
        return Boolean.valueOf(debug).booleanValue();
    }

    public static String getDaoPackage(ProcessingEnvironment env) {
        String pkg = env.getOptions().get(Options.DAO_PACKAGE);
        return pkg != null ? pkg : null;
    }

    public static String getDaoSubpackage(ProcessingEnvironment env) {
        String subpackage = env.getOptions().get(Options.DAO_SUBPACKAGE);
        return subpackage != null ? subpackage : null;
    }

    public static String getDaoSuffix(ProcessingEnvironment env) {
        String suffix = env.getOptions().get(Options.DAO_SUFFIX);
        return suffix != null ? suffix : Constants.DEFAULT_DAO_SUFFIX;
    }

    public static String getEntityFieldPrefix(ProcessingEnvironment env) {
        String prefix = env.getOptions().get(Options.ENTITY_FIELD_PREFIX);
        if ("none".equalsIgnoreCase(prefix)) {
            return "";
        }
        return prefix != null ? prefix : Constants.DEFAULT_ENTITY_FIELD_PREFIX;
    }

    public static String getExprFunctions(ProcessingEnvironment env) {
        String name = env.getOptions().get(Options.EXPR_FUNCTIONS);
        return name != null ? name : null;
    }

    public static String getDomainConverters(ProcessingEnvironment env) {
        String converters = env.getOptions().get(Options.DOMAIN_CONVERTERS);
        return converters != null ? converters : null;
    }

    public static boolean getSqlValidation(ProcessingEnvironment env) {
        String v = env.getOptions().get(Options.SQL_VALIDATION);
        return v != null ? Boolean.valueOf(v).booleanValue() : true;
    }

    public static boolean getVersionValidation(ProcessingEnvironment env) {
        String v = env.getOptions().get(Options.VERSION_VALIDATION);
        return v != null ? Boolean.valueOf(v).booleanValue() : true;
    }

    protected static class Constants {

        public static final String DEFAULT_DAO_SUFFIX = "Impl";

        public static final String DEFAULT_ENTITY_FIELD_PREFIX = "$";

    }
}
