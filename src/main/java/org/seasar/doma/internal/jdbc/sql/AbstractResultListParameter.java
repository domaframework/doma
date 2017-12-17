package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;

import org.seasar.doma.jdbc.ResultListParameter;
import org.seasar.doma.jdbc.SqlParameterVisitor;

/**
 * @author taedium
 * 
 */
public abstract class AbstractResultListParameter<ELEMENT> implements ResultListParameter<ELEMENT> {

    protected final List<ELEMENT> list;

    public AbstractResultListParameter(List<ELEMENT> list) {
        assertNotNull(list);
        this.list = list;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void add(ELEMENT element) {
        list.add(element);
    }

    @Override
    public Object getValue() {
        return list;
    }

    @Override
    public List<ELEMENT> getResult() {
        return list;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p)
            throws TH {
        return visitor.visitResultListParameter(this, p);
    }
}
