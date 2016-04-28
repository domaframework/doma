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
package org.seasar.aptina.commons.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import static org.seasar.aptina.commons.util.IOUtils.*;

/**
 * バージョンを扱うユーティリティです．
 * 
 * @author koichik
 */
public class VersionUtils {

    private static final String POM_PROPERTIES_NAME = "META-INF/maven/%1$s/%2$s/pom.properties";

    private VersionUtils() {
    }

    /**
     * Maven が作成した Jar に含まれている {@code pom.properties} からプロダクトのバージョンを返します．
     * <p>
     * バージョンを取得できなかった場合は {@code null} を返します．
     * </p>
     * 
     * @param groupId
     *            グループ ID
     * @param artifactId
     *            アーティファクト ID
     * @return バージョンまたは {@code null}
     */
    public static String getVersion(final String groupId,
            final String artifactId) {
        return getVersion(groupId, artifactId, null);
    }

    /**
     * Maven が作成した Jar に含まれている {@code pom.properties} からプロダクトのバージョンを返します．
     * <p>
     * バージョンを取得できなかった場合はデフォルト値を返します．
     * </p>
     * 
     * @param groupId
     *            グループ ID
     * @param artifactId
     *            アーティファクト ID
     * @param defaultValue
     *            バージョンを取得できなかった場合のデフォルト値
     * @return バージョンまたはデフォルト値
     */
    public static String getVersion(final String groupId,
            final String artifactId, final String defaultValue) {
        try {
            final String name = String.format(
                POM_PROPERTIES_NAME,
                groupId,
                artifactId);
            final URL url = Thread
                .currentThread()
                .getContextClassLoader()
                .getResource(name);
            if (url == null) {
                return defaultValue;
            }
            final InputStream is = url.openStream();
            try {
                final Properties props = new Properties();
                props.load(is);
                return props.getProperty("version");
            } finally {
                closeSilently(is);
            }
        } catch (final Exception e) {
            return defaultValue;
        }
    }

}
