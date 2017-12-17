package org.seasar.doma.internal.jdbc.command;

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;

/**
 * @author nakamura-to
 * 
 */
public class ResultListCallback<ELEMENT> implements IterationCallback<ELEMENT, List<ELEMENT>> {

    protected final List<ELEMENT> resultList = new ArrayList<>();

    @Override
    public List<ELEMENT> defaultResult() {
        return resultList;
    }

    @Override
    public List<ELEMENT> iterate(ELEMENT target, IterationContext context) {
        resultList.add(target);
        return resultList;
    }

}
