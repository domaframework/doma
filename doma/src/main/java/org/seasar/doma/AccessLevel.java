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
package org.seasar.doma;

/**
 * Javaのアクセスレベルを表します。
 * 
 * @author nakamura-to
 * @since 2.0.0
 */
public enum AccessLevel {

    /** {@code public} */
    PUBLIC("public"),

    /** {@code protected} */
    PROTECTED("protected"),

    /** パッケージプライベート */
    PACKAGE("");

    private final String modifier;

    private AccessLevel(String modifier) {
        this.modifier = modifier;
    }

    /**
     * 修飾子を返します。
     * 
     * @return 修飾子
     */
    public String getModifier() {
        return modifier;
    }
}
