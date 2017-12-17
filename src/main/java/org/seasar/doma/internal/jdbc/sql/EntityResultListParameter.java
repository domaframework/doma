package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.query.Query;

/**
 * @author taedium
 * 
 */
public class EntityResultListParameter<ENTITY> extends AbstractResultListParameter<ENTITY> {

    EntityDesc<ENTITY> entityDesc;
    boolean resultMappingEnsured;

    public EntityResultListParameter(EntityDesc<ENTITY> entityDesc, boolean resultMappingEnsured) {
        super(new ArrayList<ENTITY>());
        assertNotNull(entityDesc);
        this.entityDesc = entityDesc;
        this.resultMappingEnsured = resultMappingEnsured;
    }

    @Override
    public List<ENTITY> getResult() {
        return list;
    }

    @Override
    public EntityProvider<ENTITY> createObjectProvider(Query query) {
        return new EntityProvider<>(entityDesc, query, resultMappingEnsured);
    }

}
