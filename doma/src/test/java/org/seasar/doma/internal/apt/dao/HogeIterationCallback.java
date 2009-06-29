package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;


/**
 * @author taedium
 * 
 */
public class HogeIterationCallback implements IterationCallback<String, Emp> {

    @Override
    public String iterate(Emp target, IterationContext iterationContext) {
        return null;
    }

}
