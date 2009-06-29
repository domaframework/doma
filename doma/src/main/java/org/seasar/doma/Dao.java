package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DomaAbstractDao;


/**
 * @author taedium
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dao {

    Class<? extends Config> config();

    Class<? extends DomaAbstractDao> implementedBy() default DomaAbstractDao.class;

}
