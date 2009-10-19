package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.jdbc.entity.CamelNamingConvention;
import org.seasar.doma.jdbc.entity.NamingConvention;

@javax.annotation.Generated(value = { "Doma", "@VERSION@" }, date = "1970-01-01 09:00:00")
public class ParentEntity_
        implements
        org.seasar.doma.internal.jdbc.entity.EntityTypeFactory<org.seasar.doma.internal.apt.entity.ParentEntity> {

    @Override
    public org.seasar.doma.internal.jdbc.entity.EntityType<org.seasar.doma.internal.apt.entity.ParentEntity> createEntityType() {
        return new ParentEntityType();
    }

    @Override
    public org.seasar.doma.internal.jdbc.entity.EntityType<org.seasar.doma.internal.apt.entity.ParentEntity> createEntityType(
            org.seasar.doma.internal.apt.entity.ParentEntity entity) {
        return new ParentEntityType(entity);
    }

    private static class ParentEntityType
            implements
            org.seasar.doma.internal.jdbc.entity.EntityType<org.seasar.doma.internal.apt.entity.ParentEntity> {

        private static final org.seasar.doma.jdbc.entity.NullEntityListener __listener = new org.seasar.doma.jdbc.entity.NullEntityListener();

        private static final NamingConvention __naimingConvention = new CamelNamingConvention();

        private final org.seasar.doma.internal.jdbc.entity.BasicPropertyType<org.seasar.doma.wrapper.IntegerWrapper> aaa = new org.seasar.doma.internal.jdbc.entity.BasicPropertyType<org.seasar.doma.wrapper.IntegerWrapper>(
                "aaa", null, new org.seasar.doma.wrapper.IntegerWrapper(),
                true, true);

        private final org.seasar.doma.internal.jdbc.entity.BasicPropertyType<org.seasar.doma.wrapper.IntegerWrapper> bbb = new org.seasar.doma.internal.jdbc.entity.BasicPropertyType<org.seasar.doma.wrapper.IntegerWrapper>(
                "bbb", null, new org.seasar.doma.wrapper.IntegerWrapper(),
                true, true);

        private final org.seasar.doma.internal.apt.entity.ParentEntity __entity;

        private final String __catalogName = null;

        private final String __schemaName = null;

        private final String __tableName = null;

        private final java.util.Set<java.lang.String> __changedProperties;

        private final String __name = "ParentEntity";

        private java.util.List<org.seasar.doma.internal.jdbc.entity.EntityPropertyType<?>> __entityProperties;

        private java.util.Map<String, org.seasar.doma.internal.jdbc.entity.EntityPropertyType<?>> __entityPropertyMap;

        private ParentEntityType() {
            this(new org.seasar.doma.internal.apt.entity.ParentEntity());
        }

        private ParentEntityType(
                org.seasar.doma.internal.apt.entity.ParentEntity entity) {
            __entity = entity;
            __changedProperties = null;
            aaa
                    .getWrapper()
                    .set(
                            org.seasar.doma.internal.apt.entity.ParentEntity_.ParentEntityAccessor
                                    .getAaa(entity));
            bbb
                    .getWrapper()
                    .set(
                            org.seasar.doma.internal.apt.entity.ParentEntity_.ParentEntityAccessor
                                    .getBbb(entity));
        }

        @Override
        public String getName() {
            return __name;
        }

        @Override
        public String getCatalogName() {
            return __catalogName;
        }

        @Override
        public String getSchemaName() {
            return __schemaName;
        }

        @Override
        public String getTableName() {
            return __tableName;
        }

        @Override
        public void preInsert() {
            __listener.preInsert(__entity);
        }

        @Override
        public void preUpdate() {
            __listener.preUpdate(__entity);
        }

        @Override
        public void preDelete() {
            __listener.preDelete(__entity);
        }

        @Override
        public java.util.List<org.seasar.doma.internal.jdbc.entity.EntityPropertyType<?>> getEntityPropertyTypes() {
            if (__entityProperties == null) {
                java.util.List<org.seasar.doma.internal.jdbc.entity.EntityPropertyType<?>> __list = new java.util.ArrayList<org.seasar.doma.internal.jdbc.entity.EntityPropertyType<?>>();
                __list.add(aaa);
                __list.add(bbb);
                __entityProperties = java.util.Collections
                        .unmodifiableList(__list);
            }
            return __entityProperties;
        }

        @Override
        public org.seasar.doma.internal.jdbc.entity.EntityPropertyType<?> getEntityPropertyType(
                String __name) {
            if (__entityPropertyMap == null) {
                java.util.Map<String, org.seasar.doma.internal.jdbc.entity.EntityPropertyType<?>> __map = new java.util.HashMap<String, org.seasar.doma.internal.jdbc.entity.EntityPropertyType<?>>();
                __map.put("aaa", aaa);
                __map.put("bbb", bbb);
                __entityPropertyMap = java.util.Collections
                        .unmodifiableMap(__map);
            }
            return __entityPropertyMap.get(__name);
        }

        @Override
        public org.seasar.doma.internal.jdbc.entity.GeneratedIdPropertyType<?> getGeneratedIdPropertyType() {
            return null;
        }

        @Override
        public org.seasar.doma.internal.jdbc.entity.VersionPropertyType<?> getVersionPropertyType() {
            return null;
        }

        @Override
        public void refreshEntity() {
            refreshEntityInternal();
        }

        public void refreshEntityInternal() {
            org.seasar.doma.internal.apt.entity.ParentEntity_.ParentEntityAccessor
                    .setAaa(__entity, aaa.getWrapper().get());
            org.seasar.doma.internal.apt.entity.ParentEntity_.ParentEntityAccessor
                    .setBbb(__entity, bbb.getWrapper().get());
        }

        @Override
        public org.seasar.doma.internal.apt.entity.ParentEntity getEntity() {
            refreshEntityInternal();
            return __entity;
        }

        @Override
        public Class<org.seasar.doma.internal.apt.entity.ParentEntity> getEntityClass() {
            return org.seasar.doma.internal.apt.entity.ParentEntity.class;
        }

        @Override
        public java.util.Set<String> getChangedProperties() {
            return __changedProperties;
        }

        @Override
        public NamingConvention getNamingConvention() {
            return __naimingConvention;
        }

    }

    public static class ParentEntityAccessor {

        public static void setAaa(
                org.seasar.doma.internal.apt.entity.ParentEntity entity,
                java.lang.Integer aaa) {
            entity.aaa = aaa;
        }

        public static java.lang.Integer getAaa(
                org.seasar.doma.internal.apt.entity.ParentEntity entity) {
            return entity.aaa;
        }

        public static void setBbb(
                org.seasar.doma.internal.apt.entity.ParentEntity entity,
                java.lang.Integer bbb) {
            entity.bbb = bbb;
        }

        public static java.lang.Integer getBbb(
                org.seasar.doma.internal.apt.entity.ParentEntity entity) {
            return entity.bbb;
        }

    }

}
