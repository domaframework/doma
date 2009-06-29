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

    public static final String SUFFIX = "suffix";

    public static final String DEFAULT_SUFFIX = "_";

    public static boolean isTestEnabled(ProcessingEnvironment env) {
        String test = env.getOptions().get(Options.TEST);
        return Boolean.valueOf(test).booleanValue();
    }

    public static boolean isDebugEnabled(ProcessingEnvironment env) {
        String debug = env.getOptions().get(Options.DEBUG);
        return Boolean.valueOf(debug).booleanValue();
    }

    public static String getSuffix(ProcessingEnvironment env) {
        String suffix = env.getOptions().get(Options.SUFFIX);
        return suffix != null ? suffix : DEFAULT_SUFFIX;
    }

    public static Date getDate(ProcessingEnvironment env) {
        if (isTestEnabled(env)) {
            return new Date(0L);
        }
        return new Date();
    }
}
