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
package org.seasar.doma.jdbc.holder;

import java.lang.reflect.Method;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Holder;
import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.message.Message;

/**
 * {@link HolderDesc} のファクトリクラスです。
 * 
 * @author taedium
 * @since 1.8.0
 */
public final class HolderDescFactory {

    /**
     * {@link HolderDesc} のインスタンスを生成します。
     * 
     * @param <BASIC>
     *            基本型
     * @param <HOLDER>
     *            ドメイン型
     * @param holderClass
     *            ドメインクラス
     * @return {@link HolderDesc} のインスタンス
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             ドメインクラスに {@link Holder} もしくは {@code EnumHolder} が注釈されていない場合
     * @throws HolderDescNotFoundException
     *             ドメインクラスに対応するメタクラスが見つからない場合
     * @since 2.0.0
     */
    public static <BASIC, HOLDER> HolderDesc<BASIC, HOLDER> getHolderDesc(
            Class<HOLDER> holderClass) {
        return getHolderDesc(holderClass, new ClassHelper() {
        });
    }

    /**
     * {@link ClassHelper} を使って {@link HolderDesc} のインスタンスを生成します。
     * 
     * @param <BASIC>
     *            基本型
     * @param <HOLDER>
     *            ドメイン型
     * @param holderClass
     *            ドメインクラス
     * @param classHelper
     *            クラスヘルパー
     * @return {@link HolderDesc} のインスタンス
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             ドメインクラスに {@link Holder} もしくは {@code EnumHolder} が注釈されていない場合
     * @throws HolderDescNotFoundException
     *             ドメインクラスに対応するメタクラスが見つからない場合
     * @since 1.27.0
     */
    public static <BASIC, HOLDER> HolderDesc<BASIC, HOLDER> getHolderDesc(Class<HOLDER> holderClass,
            ClassHelper classHelper) {
        if (holderClass == null) {
            throw new DomaNullPointerException("holderClass");
        }
        if (classHelper == null) {
            throw new DomaNullPointerException("classHelper");
        }
        if (!holderClass.isAnnotationPresent(Holder.class)) {
            throw new DomaIllegalArgumentException("holderClass",
                    Message.DOMA2205.getMessage(holderClass.getName()));
        }
        String holderDescClassName = Conventions.createDescClassName(holderClass.getName());
        try {
            Class<HOLDER> clazz = classHelper.forName(holderDescClassName);
            Method method = ClassUtil.getMethod(clazz, "getSingletonInternal");
            return MethodUtil.invoke(method, null);
        } catch (WrapException e) {
            throw new HolderDescNotFoundException(e.getCause(), holderClass.getName(),
                    holderDescClassName);
        } catch (Exception e) {
            throw new HolderDescNotFoundException(e, holderClass.getName(), holderDescClassName);
        }
    }

    /**
     * {@link HolderDesc} のインスタンスを生成します。
     * 
     * @param <BASIC>
     *            基本型
     * @param <HOLDER>
     *            ドメイン型
     * @param holderClass
     *            ドメインクラス
     * @return {@link HolderDesc} のインスタンス、存在しない場合 {@code null}
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @since 2.0.0
     */
    public static <BASIC, HOLDER> HolderDesc<BASIC, HOLDER> getExternalHolderDesc(
            Class<HOLDER> holderClass) {
        return getExternalHolderDesc(holderClass, new ClassHelper() {
        });
    }

    /**
     * {@link ClassHelper} を使って {@link HolderDesc} のインスタンスを生成します。
     * 
     * @param <BASIC>
     *            基本型
     * @param <HOLDER>
     *            ドメイン型
     * @param holderClass
     *            ドメインクラス
     * @param classHelper
     *            クラスヘルパー
     * @return {@link HolderDesc} のインスタンス、存在しない場合 {@code null}
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @since 1.27.0
     */
    public static <BASIC, HOLDER> HolderDesc<BASIC, HOLDER> getExternalHolderDesc(
            Class<HOLDER> holderClass, ClassHelper classHelper) {
        if (holderClass == null) {
            throw new DomaNullPointerException("holderClass");
        }
        if (classHelper == null) {
            throw new DomaNullPointerException("classHelper");
        }
        String holderDescClassName = Conventions.createExternalDescClassName(holderClass.getName());
        try {
            Class<HOLDER> clazz = classHelper.forName(holderDescClassName);
            Method method = ClassUtil.getMethod(clazz, "getSingletonInternal");
            return MethodUtil.invoke(method, null);
        } catch (WrapException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
