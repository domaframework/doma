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
        } catch (Exception e) {
            return null;
        }
    }
}
