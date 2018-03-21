package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import java.util.function.Supplier;

import org.seasar.doma.internal.jdbc.command.ScalarProvider;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.query.Query;

/**
 * @author taedium
 * 
 */
public class ScalarListParameter<BASIC, CONTAINER> extends AbstractListParameter<CONTAINER> {

    protected final Supplier<Scalar<BASIC, CONTAINER>> suppler;

    public ScalarListParameter(Supplier<Scalar<BASIC, CONTAINER>> suppler, List<CONTAINER> list,
            String name) {
        super(list, name);
        assertNotNull(suppler);
        this.suppler = suppler;
    }

    @Override
    public ScalarProvider<BASIC, CONTAINER> createObjectProvider(Query query) {
        return new ScalarProvider<>(suppler, query);
    }

}
