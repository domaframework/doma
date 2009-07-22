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
package org.seasar.doma.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author taedium
 * 
 */
public class Artifact {

    private static final String PATH = "/META-INF/maven/org.seasar.doma/doma/pom.properties";

    private static final String name = "Doma";

    private static String version;

    private static String groupId;

    private static String artifactId;

    static {
        Properties props = loadProperties();
        version = props.getProperty("version");
        if (version == null) {
            version = "unknown";
        }
        groupId = props.getProperty("groupId");
        if (groupId == null) {
            groupId = "unknown";
        }
        artifactId = props.getProperty("artifactId");
        if (artifactId == null) {
            artifactId = "unknown";
        }
    }

    protected static Properties loadProperties() {
        InputStream is = Artifact.class.getResourceAsStream(PATH);
        Properties props = new Properties();
        if (is != null) {
            try {
                props.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return props;
    }

    public static String getName() {
        return name;
    }

    public static String getVersion() {
        return version;
    }

    public static String getGroupId() {
        return groupId;
    }

    public static String getArtifactId() {
        return artifactId;
    }

}
