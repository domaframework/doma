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
 * A factory for holder descriptions.
 */
public final class HolderDescFactory {

    @Deprecated
    public static <BASIC, HOLDER> HolderDesc<BASIC, HOLDER> getHolderDesc(
            Class<HOLDER> holderClass) {
        return getHolderDesc(holderClass, new ClassHelper() {
        });
    }

    /**
     * Returns the holder description.
     * 
     * @param <BASIC>
     *            the basic type
     * @param <HOLDER>
     *            the holder type
     * @param holderClass
     *            the holder class
     * @param classHelper
     *            the class helper
     * @return the holder description
     * @throws DomaNullPointerException
     *             if any arguments are {@code null}
     * @throws DomaIllegalArgumentException
     *             if the holder class is not annotated with the {@link Holder}
     * @throws HolderDescNotFoundException
     *             if the holder description is not found
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

    @Deprecated
    public static <BASIC, HOLDER> HolderDesc<BASIC, HOLDER> getExternalHolderDesc(
            Class<HOLDER> holderClass) {
        return getExternalHolderDesc(holderClass, new ClassHelper() {
        });
    }

    /**
     * Returns the external holder description.
     * 
     * @param <BASIC>
     *            the basic type
     * @param <HOLDER>
     *            the holder type
     * @param holderClass
     *            the holder class
     * @param classHelper
     *            the class helper
     * @return the external holder description or {@code null} if it is not
     *         found
     * @throws DomaNullPointerException
     *             if any arguments are {@code null}
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
