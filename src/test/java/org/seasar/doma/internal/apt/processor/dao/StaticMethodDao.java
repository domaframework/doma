package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface StaticMethodDao {

    static String hello(String name) {
        return "hello " + name;
    }
}
