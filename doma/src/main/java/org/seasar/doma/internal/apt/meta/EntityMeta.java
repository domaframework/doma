package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author taedium
 * 
 */
public class EntityMeta {

    protected final Deque<Map<TypeMirror, TypeMirror>> typeParamMapStack = new LinkedList<Map<TypeMirror, TypeMirror>>();

    protected final List<TypeMirror> supertypes = new ArrayList<TypeMirror>();

    protected List<PropertyMeta> idPropertyMetas = new ArrayList<PropertyMeta>();

    protected List<PropertyMeta> columnPropertyMetas = new ArrayList<PropertyMeta>();

    protected List<PropertyMeta> allPropertyMetas = new ArrayList<PropertyMeta>();

    protected PropertyMeta versionPropertyMeta;

    protected PropertyMeta generatedIdPropertyMeta;

    protected TableMeta tableMeta;

    protected TypeMirror entityType;

    protected TypeElement entityElement;

    protected TypeMirror listenerType;

    protected String name;

    protected boolean mappedSuperclass;

    public EntityMeta() {
        typeParamMapStack.push(Collections.<TypeMirror, TypeMirror> emptyMap());
    }

    public TypeMirror getEntityType() {
        return entityType;
    }

    public void setEntityType(TypeMirror entityType) {
        this.entityType = entityType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TableMeta getTableMeta() {
        return tableMeta;
    }

    public void setTableMeta(TableMeta tableMeta) {
        this.tableMeta = tableMeta;
    }

    public TypeElement getEntityElement() {
        return entityElement;
    }

    public void setEntityElement(TypeElement entityElement) {
        this.entityElement = entityElement;
    }

    public TypeMirror getListenerType() {
        return listenerType;
    }

    public void setListenerType(TypeMirror listenerType) {
        this.listenerType = listenerType;
    }

    public void addPropertyMeta(PropertyMeta propertyMeta) {
        assertNotNull(propertyMeta);
        allPropertyMetas.add(propertyMeta);
        if (propertyMeta.isTrnsient()) {
            return;
        }
        if (propertyMeta.isId()) {
            idPropertyMetas.add(propertyMeta);
            if (propertyMeta.getIdGeneratorMeta() != null) {
                generatedIdPropertyMeta = propertyMeta;
            }
        }
        if (propertyMeta.isVersion()) {
            versionPropertyMeta = propertyMeta;
        }
        columnPropertyMetas.add(propertyMeta);
    }

    public List<PropertyMeta> getAllPropertyMetas() {
        return allPropertyMetas;
    }

    public boolean hasIdPropertyMeta() {
        return idPropertyMetas.size() > 0;
    }

    public List<PropertyMeta> getIdPropertyMetas() {
        return idPropertyMetas;
    }

    public List<PropertyMeta> getColumnPropertyMetas() {
        return columnPropertyMetas;
    }

    public boolean hasVersionPropertyMeta() {
        return versionPropertyMeta != null;
    }

    public PropertyMeta getVersionPropertyMeta() {
        return versionPropertyMeta;
    }

    public boolean hasGeneratedIdPropertyMeta() {
        return generatedIdPropertyMeta != null;
    }

    public PropertyMeta getGeneratedIdPropertyMeta() {
        return generatedIdPropertyMeta;
    }

    public void addTypeParameterMap(Map<TypeMirror, TypeMirror> typeParameterMap) {
        typeParamMapStack.push(typeParameterMap);
    }

    public Map<TypeMirror, TypeMirror> getTypeParameterMap() {
        return typeParamMapStack.peek();
    }

    public void addSupertype(TypeMirror supertype) {
        supertypes.add(supertype);
    }

    public List<TypeMirror> getSupertypes() {
        return supertypes;
    }

    public boolean isMappedSuperclass() {
        return mappedSuperclass;
    }

    public void setMappedSuperclass(boolean mappedSuperclass) {
        this.mappedSuperclass = mappedSuperclass;
    }

}
