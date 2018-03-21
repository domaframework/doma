package org.seasar.doma.internal.jdbc.command;

import java.util.Optional;

import org.seasar.doma.jdbc.entity.EntityDesc;

/**
 * 
 * @author nakamura-to
 * 
 * @param <ENTITY>
 */
public class OptionalEntitySingleResultHandler<ENTITY>
        extends AbstractSingleResultHandler<Optional<ENTITY>> {

    public OptionalEntitySingleResultHandler(EntityDesc<ENTITY> entityDesc) {
        super(new EntityIterationHandler<>(entityDesc, new OptionalSingleResultCallback<>()));
    }

}
