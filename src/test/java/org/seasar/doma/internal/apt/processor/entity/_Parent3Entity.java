package org.seasar.doma.internal.apt.processor.entity;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.entity.AbstractEntityDesc;
import org.seasar.doma.jdbc.entity.DefaultPropertyDesc;
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
import org.seasar.doma.wrapper.IntegerWrapper;

public class _Parent3Entity extends AbstractEntityDesc<Parent3Entity> {

    private final NamingType __namingType = NamingType.UPPER_CASE;

    public DefaultPropertyDesc<Parent3Entity, Integer, Integer> $aaa = new DefaultPropertyDesc<>(
            Parent3Entity.class, () -> new BasicScalar<>(new IntegerWrapper(), false), "aaa", "AAA",
            __namingType, true, true, false);

    public DefaultPropertyDesc<Parent3Entity, Integer, Integer> $bbb = new DefaultPropertyDesc<>(
            Parent3Entity.class, () -> new BasicScalar<>(new IntegerWrapper(), false), "bbb", "BBB",
            __namingType, true, true, false);

    private _Parent3Entity() {
    }

    @Override
    public void saveCurrentStates(Parent3Entity entity) {
    }

    @Override
    public String getCatalogName() {

        return null;
    }

    @Override
    public Class<Parent3Entity> getEntityClass() {

        return null;
    }

    @Override
    public EntityPropertyDesc<Parent3Entity, ?> getEntityPropertyDesc(String name) {

        return null;
    }

    @Override
    public List<EntityPropertyDesc<Parent3Entity, ?>> getEntityPropertyDescs() {

        return null;
    }

    @Override
    public GeneratedIdPropertyDesc<Parent3Entity, ?, ?> getGeneratedIdPropertyDesc() {

        return null;
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public Parent3Entity getOriginalStates(Parent3Entity entity) {

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
    public VersionPropertyDesc<Parent3Entity, ?, ?> getVersionPropertyDesc() {

        return null;
    }

    @Override
    public void preDelete(Parent3Entity entity, PreDeleteContext<Parent3Entity> context) {
    }

    @Override
    public void preInsert(Parent3Entity entity, PreInsertContext<Parent3Entity> context) {
    }

    @Override
    public void preUpdate(Parent3Entity entity, PreUpdateContext<Parent3Entity> context) {
    }

    @Override
    public void postDelete(Parent3Entity entity, PostDeleteContext<Parent3Entity> context) {
    }

    @Override
    public void postInsert(Parent3Entity entity, PostInsertContext<Parent3Entity> context) {
    }

    @Override
    public void postUpdate(Parent3Entity entity, PostUpdateContext<Parent3Entity> context) {
    }

    @Override
    public List<EntityPropertyDesc<Parent3Entity, ?>> getIdPropertyDescs() {
        return null;
    }

    @Override
    public NamingType getNamingType() {
        return null;
    }

    public static _Parent3Entity getSingletonInternal() {
        return null;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public Parent3Entity newEntity(Map<String, Property<Parent3Entity, ?>> args) {
        return null;
    }

    @Override
    public boolean isQuoteRequired() {
        return false;
    }
}
