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
package org.seasar.doma.copy;

import java.util.HashMap;
import java.util.Map;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.bean.BeanWrapper;
import org.seasar.doma.bean.BeanWrapperFactory;
import org.seasar.doma.converter.Converter;
import org.seasar.doma.internal.util.StringUtil;

/**
 * コピーのオプションです。
 * <p>
 * デフォルトでは、次の値をコピーしません。
 * <ul>
 * <li> {@code null}
 * <li>空文字
 * </ul>
 * 
 * @author taedium
 * 
 */
public class CopyOptions {

    /** {@link String} の空の配列 */
    protected static final String[] EMPTY_STRINGS = new String[] {};

    /** プロパティ名をキー、コンバーターを値とするマップ */
    protected final Map<String, Converter<?>> converterMap = new HashMap<String, Converter<?>>();

    /** プロパティ名をキー、パターンを値とするマップ */
    protected final Map<String, String> patterns = new HashMap<String, String>();

    /** コピー対象のプロパティ名の配列 */
    protected String[] includedPropertyNames = EMPTY_STRINGS;

    /** コピー非対象のプロパティ名の配列 */
    protected String[] excludedPropertyNames = EMPTY_STRINGS;

    /** {@code null} がコピー対象に含まれるかどうか */
    protected boolean nullIncluded;

    /** 空文字列がコピー対象に含まれるかどうか */
    protected boolean emptyStringIncluded;

    /** 空白文字列がコピー対象に含まれるかどうか */
    protected boolean whitespaceIncluded = true;

    /** {@literal JavaBeans} へのアクセスする {@link BeanWrapper} のファクトリ */
    protected BeanWrapperFactory beanWrapperFactory;

    /**
     * コピーの対象とするプロパティ名を設定します。
     * 
     * @param propertyNames
     *            プロパティ名の可変長配列
     * @return このインスタンス
     */
    public CopyOptions include(String... propertyNames) {
        includedPropertyNames = propertyNames;
        return this;
    }

    /**
     * コピーの非対象とするプロパティ名を設定します。
     * 
     * @param propertyNames
     *            プロパティ名の可変長配列
     * @return このインスタンス
     */
    public CopyOptions exclude(String... propertyNames) {
        excludedPropertyNames = propertyNames;
        return this;
    }

    /**
     * {@code null} をコピーの対象とすることを示します。
     * 
     * @return このインスタンス
     */
    public CopyOptions includeNull() {
        nullIncluded = true;
        return this;
    }

    /**
     * 空文字をコピーの対象とすることを示します。
     * 
     * @return このインスタンス
     */
    public CopyOptions includeEmptyString() {
        emptyStringIncluded = true;
        return this;
    }

    /**
     * 空白文字をコピーの非対象とすることを示します。
     * 
     * @return このインスタンス
     */
    public CopyOptions excludeWhitespace() {
        whitespaceIncluded = false;
        return this;
    }

    /**
     * 特定のプロパティに明示的にコンバーターを指定します。
     * 
     * @param converter
     *            コンバーターを設定します。
     * @param propertyNames
     *            コンバーターを適用するプロパティ名の可変長配列
     * @return このインスタンス
     */
    public CopyOptions converter(Converter<?> converter,
            String... propertyNames) {
        if (converter == null) {
            throw new DomaNullPointerException("converter");
        }
        for (String propertyName : propertyNames) {
            converterMap.put(propertyName, converter);
        }
        return this;
    }

    /**
     * 特定のプロパティに明示的にパターンを指定します。
     * <p>
     * パターンを表す文字列は、ある型から {@link String}へ変換される際、また、 {@link String}
     * から別の型に変換される際に使用されます。実際にパターンが使用されるかどうかは使用される {@link Converter} の実装によります。
     * 
     * @param pattern
     *            パターンを表す文字列
     * @param propertyNames
     *            パターンを適用するプロパティ名の可変長配列
     * @return このインスタンス
     */
    public CopyOptions pattern(String pattern, String... propertyNames) {
        if (pattern == null) {
            throw new DomaNullPointerException("pattern");
        }
        for (String propertyName : propertyNames) {
            patterns.put(propertyName, pattern);
        }
        return this;
    }

    /**
     * {@literal JavaBeans} にどのようにアクセスするかを決める {@link BeanWrapper} のファクトリを設定します。
     * 
     * @param beanWrapperFactory
     *            {@link BeanWrapper} のファクトリ
     * @return このインスタンス
     */
    public CopyOptions beanWrapperFactory(BeanWrapperFactory beanWrapperFactory) {
        this.beanWrapperFactory = beanWrapperFactory;
        return this;
    }

    /**
     * プロパティ名に対応付けられたコンバーターを返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return コンバーター、対応するコンバーターが存在しない場合 {@code null}
     */
    public Converter<?> getConverter(String propertyName) {
        return converterMap.get(propertyName);
    }

    /**
     * プロパティ名に対応付けられたパターンを返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return パターン、対応するパターンが存在しない場合 {@code null}
     */
    public String getPattern(String propertyName) {
        return patterns.get(propertyName);
    }

    /**
     * {@link #beanWrapperFactory(BeanWrapperFactory)} で設定された
     * {@link BeanWrapperFactory} の実装を返します。
     * 
     * @return {@link BeanWrapperFactory} の実装、存在しない場合 {@code null}
     */
    public BeanWrapperFactory getBeanFactory() {
        return beanWrapperFactory;
    }

    /**
     * プロパティがコピーの対象であるかどうかを返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return コピーの対象である場合 {@code true}
     */
    public boolean isTargetProperty(String propertyName) {
        if (includedPropertyNames.length > 0) {
            for (String included : includedPropertyNames) {
                if (included.equals(propertyName)) {
                    for (String excluded : excludedPropertyNames) {
                        if (excluded.equals(propertyName)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        if (excludedPropertyNames.length > 0) {
            for (String excluded : excludedPropertyNames) {
                if (excluded.equals(propertyName)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * コピー元の値がコピーの対象であるかどうかを返します。
     * 
     * @param srcValue
     *            コピー元の値
     * @return コピーの対象である場合 {@code true}
     */
    public boolean isTargetValue(Object srcValue) {
        if (srcValue == null) {
            return nullIncluded;
        }
        if ("".equals(srcValue)) {
            return emptyStringIncluded;
        }
        if (String.class.isInstance(srcValue)) {
            String s = String.class.cast(srcValue);
            if (StringUtil.isWhitespace(s)) {
                return whitespaceIncluded;
            }
        }
        return true;
    }

}
