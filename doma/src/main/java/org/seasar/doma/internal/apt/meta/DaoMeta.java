package org.seasar.doma.internal.apt.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.jdbc.Config;


/**
 * 
 * @author taedium
 * 
 */
public class DaoMeta {

    protected final Deque<Map<TypeMirror, TypeMirror>> typeParamMapStack = new LinkedList<Map<TypeMirror, TypeMirror>>();

    protected final List<TypeMirror> supertypes = new ArrayList<TypeMirror>();

    protected final List<QueryMeta> queryMetas = new ArrayList<QueryMeta>();

    protected TypeMirror configType;

    protected TypeMirror implementedByType;

    protected TypeElement implementedByElement;

    protected TypeMirror daoType;

    protected TypeElement daoElement;

    protected TypeElement mostSubtypeElement;

    protected String name;

    protected boolean genericDao;

    protected Config config;

    public DaoMeta() {
        typeParamMapStack.push(Collections.<TypeMirror, TypeMirror> emptyMap());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeMirror getConfigType() {
        return configType;
    }

    public void setConfigType(TypeMirror configType) {
        this.configType = configType;
    }

    public TypeMirror getImplementedByType() {
        return implementedByType;
    }

    public void setImplementedByType(TypeMirror implementedByType) {
        this.implementedByType = implementedByType;
    }

    public TypeElement getImplementedByElement() {
        return implementedByElement;
    }

    public void setImplementedByElement(TypeElement implementedByElement) {
        this.implementedByElement = implementedByElement;
    }

    public TypeElement getMostSubtypeElement() {
        return mostSubtypeElement;
    }

    public void setMostSubtypeElement(TypeElement mostSubtypeElement) {
        this.mostSubtypeElement = mostSubtypeElement;
    }

    public TypeMirror getDaoType() {
        return daoType;
    }

    public void setDaoType(TypeMirror daoType) {
        this.daoType = daoType;
    }

    public TypeElement getDaoElement() {
        return daoElement;
    }

    public void setDaoElement(TypeElement daoElement) {
        this.daoElement = daoElement;
    }

    public void addQueryMeta(QueryMeta queryMeta) {
        queryMetas.add(queryMeta);
    }

    public List<QueryMeta> getQueryMetas() {
        return queryMetas;
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

    public boolean isGenericDao() {
        return genericDao;
    }

    public void setGenericDao(boolean genericDao) {
        this.genericDao = genericDao;
    }

}
