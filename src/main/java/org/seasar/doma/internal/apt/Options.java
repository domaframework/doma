package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.processing.ProcessingEnvironment;
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

    public static final String HOLDER_CONVERTERS = "doma.holder.converters";

    public static final String SQL_VALIDATION = "doma.sql.validation";

    public static final String VERSION_VALIDATION = "doma.version.validation";

    public static final String CONFIG_PATH = "doma.config.path";

    public static final String RESOURCES_DIR = "doma.resources.dir";

    public static final String LOMBOK_ALL_ARGS_CONSTRUCTOR = "doma.lombok.AllArgsConstructor";

    public static final String LOMBOK_VALUE = "doma.lombok.Value";

    private static final Map<String, Map<String, String>> configCache = new ConcurrentHashMap<>();

    private final Context ctx;

    private final ProcessingEnvironment env;

    public Options(Context ctx) {
        assertNotNull(ctx);
        this.ctx = ctx;
        this.env = ctx.getEnv();
    }

    public boolean isTestEnabled() {
        String test = getOption(Options.TEST);
        return Boolean.valueOf(test);
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
        String debug = getOption(Options.DEBUG);
        return Boolean.valueOf(debug);
    }

    public String getDaoPackage() {
        return getOption(Options.DAO_PACKAGE);
    }

    public String getDaoSubpackage() {
        return getOption(Options.DAO_SUBPACKAGE);
    }

    public String getDaoSuffix() {
        String suffix = getOption(Options.DAO_SUFFIX);
        return suffix != null ? suffix : Constants.DEFAULT_DAO_SUFFIX;
    }

    public String getEntityFieldPrefix() {
        String prefix = getOption(Options.ENTITY_FIELD_PREFIX);
        if ("none".equalsIgnoreCase(prefix)) {
            return "";
        }
        return prefix != null ? prefix : Constants.DEFAULT_ENTITY_FIELD_PREFIX;
    }

    public String getExprFunctions() {
        return getOption(Options.EXPR_FUNCTIONS);
    }

    public String getHolderConverters() {
        return getOption(Options.HOLDER_CONVERTERS);
    }

    public boolean getSqlValidation() {
        String v = getOption(Options.SQL_VALIDATION);
        return v == null || Boolean.valueOf(v);
    }

    public boolean getVersionValidation() {
        String v = getOption(Options.VERSION_VALIDATION);
        return v == null || Boolean.valueOf(v);
    }

    public String getLombokAllArgsConstructor() {
        String name = getOption(Options.LOMBOK_ALL_ARGS_CONSTRUCTOR);
        return name != null ? name : Constants.DEFAULT_LOMBOK_ALL_ARGS_CONSTRUCTOR;
    }

    public String getLombokValue() {
        String name = getOption(Options.LOMBOK_VALUE);
        return name != null ? name : Constants.DEFAULT_LOMBOK_VALUE;
    }

    private String getOption(String key) {
        String v = env.getOptions().get(key);
        if (v != null) {
            return v;
        }
        String configPath = getConfigPath();
        Map<String, String> config = getConfig(ctx.getResources(), configPath);
        return config.get(key);
    }

    private String getConfigPath() {
        String configPath = env.getOptions().get(Options.CONFIG_PATH);
        return configPath != null ? configPath : Constants.DEFAULT_CONFIG_PATH;
    }

    private static Map<String, String> getConfig(Resources resources, String path) {
        FileObject config = getFileObject(resources, path);
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

    private static FileObject getFileObject(Resources resources, String path) {
        try {
            return resources.getResource(path);
        } catch (IOException e) {
            return null;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map<String, String> loadProperties(FileObject config) throws IOException {
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
