package org.seasar.doma.jdbc.entity;

/** */
public final class _NotTopLevelEntity__Hoge extends
        org.seasar.doma.jdbc.entity.AbstractEntityType<NotTopLevelEntity.Hoge> {

    private static final _NotTopLevelEntity__Hoge __singleton = new _NotTopLevelEntity__Hoge();

    private final org.seasar.doma.jdbc.entity.NamingType __namingType = null;

    private final java.util.function.Supplier<org.seasar.doma.jdbc.entity.NullEntityListener<NotTopLevelEntity.Hoge>> __listenerSupplier;

    private final boolean __immutable;

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    private final boolean __isQuoteRequired;

    private final String __name;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<NotTopLevelEntity.Hoge, ?>> __idPropertyTypes;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<NotTopLevelEntity.Hoge, ?>> __entityPropertyTypes;

    private final java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<NotTopLevelEntity.Hoge, ?>> __entityPropertyTypeMap;

    private _NotTopLevelEntity__Hoge() {
        __listenerSupplier = () -> ListenerHolder.listener;
        __immutable = false;
        __name = "Hoge";
        __catalogName = "";
        __schemaName = "";
        __tableName = "";
        __isQuoteRequired = false;
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<NotTopLevelEntity.Hoge, ?>> __idList = new java.util.ArrayList<>();
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<NotTopLevelEntity.Hoge, ?>> __list = new java.util.ArrayList<>(
                0);
        java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<NotTopLevelEntity.Hoge, ?>> __map = new java.util.HashMap<>(
                0);
        __idPropertyTypes = java.util.Collections.unmodifiableList(__idList);
        __entityPropertyTypes = java.util.Collections.unmodifiableList(__list);
        __entityPropertyTypeMap = java.util.Collections.unmodifiableMap(__map);
    }

    @Override
    public org.seasar.doma.jdbc.entity.NamingType getNamingType() {
        return __namingType;
    }

    @Override
    public boolean isImmutable() {
        return __immutable;
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
        return getTableName(org.seasar.doma.jdbc.Naming.DEFAULT::apply);
    }

    @Override
    public String getTableName(
            java.util.function.BiFunction<org.seasar.doma.jdbc.entity.NamingType, String, String> namingFunction) {
        if (__tableName.isEmpty()) {
            return namingFunction.apply(__namingType, __name);
        }
        return __tableName;
    }

    @Override
    public boolean isQuoteRequired() {
        return __isQuoteRequired;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void preInsert(NotTopLevelEntity.Hoge entity,
            org.seasar.doma.jdbc.entity.PreInsertContext<NotTopLevelEntity.Hoge> context) {
        Class __listenerClass = org.seasar.doma.jdbc.entity.NullEntityListener.class;
        org.seasar.doma.jdbc.entity.NullEntityListener __listener = context
                .getConfig().getEntityListenerProvider()
                .get(__listenerClass, __listenerSupplier);
        __listener.preInsert(entity, context);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void preUpdate(NotTopLevelEntity.Hoge entity,
            org.seasar.doma.jdbc.entity.PreUpdateContext<NotTopLevelEntity.Hoge> context) {
        Class __listenerClass = org.seasar.doma.jdbc.entity.NullEntityListener.class;
        org.seasar.doma.jdbc.entity.NullEntityListener __listener = context
                .getConfig().getEntityListenerProvider()
                .get(__listenerClass, __listenerSupplier);
        __listener.preUpdate(entity, context);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void preDelete(NotTopLevelEntity.Hoge entity,
            org.seasar.doma.jdbc.entity.PreDeleteContext<NotTopLevelEntity.Hoge> context) {
        Class __listenerClass = org.seasar.doma.jdbc.entity.NullEntityListener.class;
        org.seasar.doma.jdbc.entity.NullEntityListener __listener = context
                .getConfig().getEntityListenerProvider()
                .get(__listenerClass, __listenerSupplier);
        __listener.preDelete(entity, context);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void postInsert(NotTopLevelEntity.Hoge entity,
            org.seasar.doma.jdbc.entity.PostInsertContext<NotTopLevelEntity.Hoge> context) {
        Class __listenerClass = org.seasar.doma.jdbc.entity.NullEntityListener.class;
        org.seasar.doma.jdbc.entity.NullEntityListener __listener = context
                .getConfig().getEntityListenerProvider()
                .get(__listenerClass, __listenerSupplier);
        __listener.postInsert(entity, context);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void postUpdate(NotTopLevelEntity.Hoge entity,
            org.seasar.doma.jdbc.entity.PostUpdateContext<NotTopLevelEntity.Hoge> context) {
        Class __listenerClass = org.seasar.doma.jdbc.entity.NullEntityListener.class;
        org.seasar.doma.jdbc.entity.NullEntityListener __listener = context
                .getConfig().getEntityListenerProvider()
                .get(__listenerClass, __listenerSupplier);
        __listener.postUpdate(entity, context);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void postDelete(NotTopLevelEntity.Hoge entity,
            org.seasar.doma.jdbc.entity.PostDeleteContext<NotTopLevelEntity.Hoge> context) {
        Class __listenerClass = org.seasar.doma.jdbc.entity.NullEntityListener.class;
        org.seasar.doma.jdbc.entity.NullEntityListener __listener = context
                .getConfig().getEntityListenerProvider()
                .get(__listenerClass, __listenerSupplier);
        __listener.postDelete(entity, context);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<NotTopLevelEntity.Hoge, ?>> getEntityPropertyTypes() {
        return __entityPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.EntityPropertyType<NotTopLevelEntity.Hoge, ?> getEntityPropertyType(
            String __name) {
        return __entityPropertyTypeMap.get(__name);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<NotTopLevelEntity.Hoge, ?>> getIdPropertyTypes() {
        return __idPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, NotTopLevelEntity.Hoge, ?, ?> getGeneratedIdPropertyType() {
        return null;
    }

    @Override
    public org.seasar.doma.jdbc.entity.VersionPropertyType<java.lang.Object, NotTopLevelEntity.Hoge, ?, ?> getVersionPropertyType() {
        return null;
    }

    @Override
    public TenantIdPropertyType<Object, NotTopLevelEntity.Hoge, ?, ?> getTenantIdPropertyType() {
        return null;
    }

    @Override
    public NotTopLevelEntity.Hoge newEntity(
            java.util.Map<String, org.seasar.doma.jdbc.entity.Property<NotTopLevelEntity.Hoge, ?>> __args) {
        NotTopLevelEntity.Hoge entity = new NotTopLevelEntity.Hoge();
        return entity;
    }

    @Override
    public Class<NotTopLevelEntity.Hoge> getEntityClass() {
        return NotTopLevelEntity.Hoge.class;
    }

    @Override
    public NotTopLevelEntity.Hoge getOriginalStates(
            NotTopLevelEntity.Hoge __entity) {
        return null;
    }

    @Override
    public void saveCurrentStates(NotTopLevelEntity.Hoge __entity) {
    }

    /**
     * @return the singleton
     */
    public static _NotTopLevelEntity__Hoge getSingletonInternal() {
        return __singleton;
    }

    /**
     * @return the new instance
     */
    public static _NotTopLevelEntity__Hoge newInstance() {
        return new _NotTopLevelEntity__Hoge();
    }

    private static class ListenerHolder {
        private static org.seasar.doma.jdbc.entity.NullEntityListener<NotTopLevelEntity.Hoge> listener = new org.seasar.doma.jdbc.entity.NullEntityListener<>();
    }

}
