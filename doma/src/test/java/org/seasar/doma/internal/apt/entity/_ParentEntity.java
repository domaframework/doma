package org.seasar.doma.internal.apt.entity;

import java.util.List;

import org.seasar.doma.internal.jdbc.entity.BasicPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.internal.jdbc.entity.VersionPropertyType;
import org.seasar.doma.wrapper.Wrapper;

public class _ParentEntity implements EntityType<ParentEntity> {

    public BasicPropertyType<ParentEntity, Integer> aaa = new BasicPropertyType<ParentEntity, Integer>(
            "aaa", "AAA", true, true) {

        public Wrapper<Integer> getWrapper(ParentEntity entity) {
            return null;
        }
    };

    public BasicPropertyType<ParentEntity, Integer> bbb = new BasicPropertyType<ParentEntity, Integer>(
            "bbb", "BBB", true, true) {

        public Wrapper<Integer> getWrapper(ParentEntity entity) {
            return null;
        }
    };

    private _ParentEntity() {
    }

    @Override
    public void saveCurrentStates(ParentEntity entity) {
    }

    @Override
    public String getCatalogName() {

        return null;
    }

    @Override
    public Class<ParentEntity> getEntityClass() {

        return null;
    }

    @Override
    public EntityPropertyType<ParentEntity, ?> getEntityPropertyType(String name) {

        return null;
    }

    @Override
    public List<EntityPropertyType<ParentEntity, ?>> getEntityPropertyTypes() {

        return null;
    }

    @Override
    public GeneratedIdPropertyType<ParentEntity, ?> getGeneratedIdPropertyType() {

        return null;
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public ParentEntity getOriginalStates(ParentEntity entity) {

        return null;
    }

    @Override
    public String getSchemaName() {

        return null;
    }

    @Override
    public String getTableName() {

        return null;
    }

    @Override
    public VersionPropertyType<ParentEntity, ?> getVersionPropertyType() {

        return null;
    }

    @Override
    public ParentEntity newEntity() {

        return null;
    }

    @Override
    public void preDelete(ParentEntity entity) {

    }

    @Override
    public void preInsert(ParentEntity entity) {

    }

    @Override
    public void preUpdate(ParentEntity entity) {

    }

    @Override
    public List<EntityPropertyType<ParentEntity, ?>> getIdPropertyTypes() {
        return null;
    }

    @Override
    public String getQualifiedTableName() {
        return null;
    }

    public static _ParentEntity get() {
        return null;
    }

}
