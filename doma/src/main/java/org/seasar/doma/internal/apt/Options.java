/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

/**
 * @author taedium
 * 
 */
public final class Options {

    public static final String TEST = "test";

    public static final String DEBUG = "debug";

    public static final String DAO_SUBPACKAGE = "dao.subpackage";

    public static final String ENTITY_SUBPACKAGE = "entity.subpackage";

    public static final String DTO_PACKAGE = "dto.package";

    public static final String DAO_SUFFIX = "dao.suffix";

    public static final String ENTITY_SUFFIX = "entity.suffix";

    public static final String DTO_SUFFIX = "dto.suffix";

    public static final String DTO_GENERATION = "dto.generation";

    private static final String DEFAULT_DAO_SUFFIX = "_";

    private static final String DEFAULT_ENTITY_SUFFIX = "_";

    private static final String DEFAULT_DTO_SUFFIX = "Dto";

    public static boolean isTestEnabled(ProcessingEnvironment env) {
        String test = env.getOptions().get(Options.TEST);
        return Boolean.valueOf(test).booleanValue();
    }

    public static boolean isDebugEnabled(ProcessingEnvironment env) {
        String debug = env.getOptions().get(Options.DEBUG);
        return Boolean.valueOf(debug).booleanValue();
    }

    public static boolean isDtoGenerationEnabled(ProcessingEnvironment env) {
        String generation = env.getOptions().get(Options.DTO_GENERATION);
        return Boolean.valueOf(generation).booleanValue();
    }

    public static String getDaoSubpackage(ProcessingEnvironment env) {
        String subpackage = env.getOptions().get(Options.DAO_SUBPACKAGE);
        return subpackage != null ? subpackage : null;
    }

    public static String getEntitySubpackage(ProcessingEnvironment env) {
        String subpackage = env.getOptions().get(Options.ENTITY_SUBPACKAGE);
        return subpackage != null ? subpackage : null;
    }

    public static String getDtoPackage(ProcessingEnvironment env) {
        String pkg = env.getOptions().get(Options.DTO_PACKAGE);
        return pkg != null ? pkg : null;
    }

    public static String getDaoSuffix(ProcessingEnvironment env) {
        String suffix = env.getOptions().get(Options.DAO_SUFFIX);
        return suffix != null ? suffix : DEFAULT_DAO_SUFFIX;
    }

    public static String getEntitySuffix(ProcessingEnvironment env) {
        String suffix = env.getOptions().get(Options.ENTITY_SUFFIX);
        return suffix != null ? suffix : DEFAULT_ENTITY_SUFFIX;
    }

    public static String getDtoSuffix(ProcessingEnvironment env) {
        String suffix = env.getOptions().get(Options.DTO_SUFFIX);
        return suffix != null ? suffix : DEFAULT_DTO_SUFFIX;
    }

    public static Date getDate(ProcessingEnvironment env) {
        if (isTestEnabled(env)) {
            return new Date(0L);
        }
        return new Date();
    }
}
