package org.seasar.doma.internal.apt.processor.entity;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.seasar.doma.jdbc.entity.AbstractEntityDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyDesc;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.entity.VersionPropertyDesc;

/**
 * @author taedium
 * 
 */
public class _ImmutableEmp extends AbstractEntityDesc<ImmutableEmp> {

    @Override
    public String getCatalogName() {

        return null;
    }

    @Override
    public Class<ImmutableEmp> getEntityClass() {

        return null;
    }

    @Override
    public EntityPropertyDesc<ImmutableEmp, ?> getEntityPropertyDesc(String name) {

        return null;
    }

    @Override
    public List<EntityPropertyDesc<ImmutableEmp, ?>> getEntityPropertyDescs() {

        return null;
    }

    @Override
    public GeneratedIdPropertyDesc<ImmutableEmp, ?, ?> getGeneratedIdPropertyDesc() {

        return null;
    }

    @Override
    public List<EntityPropertyDesc<ImmutableEmp, ?>> getIdPropertyDescs() {

        return null;
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public ImmutableEmp getOriginalStates(ImmutableEmp entity) {

        return null;
    }

    @Override
    public String getSchemaName() {

        return null;
    }

    @Override
    public String getTableName(BiFunction<NamingType, String, String> namingFunction) {

        return null;
    }

    @Override
    public VersionPropertyDesc<ImmutableEmp, ?, ?> getVersionPropertyDesc() {

        return null;
    }

    @Override
    public void preDelete(ImmutableEmp entity, PreDeleteContext<ImmutableEmp> context) {
    }

    @Override
    public void preInsert(ImmutableEmp entity, PreInsertContext<ImmutableEmp> context) {
    }

    @Override
    public void preUpdate(ImmutableEmp entity, PreUpdateContext<ImmutableEmp> context) {
    }

    @Override
    public void postDelete(ImmutableEmp entity, PostDeleteContext<ImmutableEmp> context) {
    }

    @Override
    public void postInsert(ImmutableEmp entity, PostInsertContext<ImmutableEmp> context) {
    }

    @Override
    public void postUpdate(ImmutableEmp entity, PostUpdateContext<ImmutableEmp> context) {
    }

    @Override
    public void saveCurrentStates(ImmutableEmp entity) {

    }

    @Override
    public NamingType getNamingType() {
        return null;
    }

    public static _ImmutableEmp getSingletonInternal() {
        return null;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public ImmutableEmp newEntity(Map<String, Property<ImmutableEmp, ?>> args) {
        return null;
    }

    @Override
    public boolean isQuoteRequired() {
        return false;
    }

}
