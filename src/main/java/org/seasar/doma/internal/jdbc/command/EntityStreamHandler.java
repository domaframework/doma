package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Function;
import java.util.stream.Stream;

import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * 
 * @author nakamura-to
 * 
 * @param <RESULT>
 * @param <ENTITY>
 */
public class EntityStreamHandler<ENTITY, RESULT> extends AbstractStreamHandler<ENTITY, RESULT> {

    protected final EntityDesc<ENTITY> entityDesc;

    public EntityStreamHandler(EntityDesc<ENTITY> entityDesc,
            Function<Stream<ENTITY>, RESULT> mapper) {
        super(mapper);
        assertNotNull(entityDesc);
        this.entityDesc = entityDesc;
    }

    @Override
    protected ObjectProvider<ENTITY> createObjectProvider(SelectQuery query) {
        return new EntityProvider<>(entityDesc, query, query.isResultMappingEnsured());
    }

}
