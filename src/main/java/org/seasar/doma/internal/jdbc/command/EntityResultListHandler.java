package org.seasar.doma.internal.jdbc.command;

import org.seasar.doma.jdbc.entity.EntityDesc;

/**
 * @author taedium
 * 
 */
public class EntityResultListHandler<ENTITY> extends AbstractResultListHandler<ENTITY> {

    public EntityResultListHandler(EntityDesc<ENTITY> entityDesc) {
        super(new EntityIterationHandler<>(entityDesc, new ResultListCallback<ENTITY>()));
    }

}
