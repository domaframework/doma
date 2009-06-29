package org.seasar.doma.internal.apt.entity;

import java.util.List;

import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.DomaAbstractEntity;
import org.seasar.doma.internal.jdbc.GeneratedIdProperty;
import org.seasar.doma.internal.jdbc.Property;
import org.seasar.doma.internal.jdbc.VersionProperty;


/**
 * @author taedium
 * 
 */
public class Emp_ extends DomaAbstractEntity<Emp> implements Emp {

    public Emp_() {
        super(null, null, null);
    }

    @Override
    public IntegerDomain id() {
        return null;
    }

    @Override
    public StringDomain name() {
        return null;
    }

    @Override
    public BigDecimalDomain salary() {
        return null;
    }

    @Override
    public StringDomain temp() {
        return null;
    }

    @Override
    public IntegerDomain version() {
        return null;
    }

    @Override
    public Emp __asInterface() {
        return null;
    }

    @Override
    public String __getName() {
        return null;
    }

    @Override
    public Property<?> __getPropertyByName(String propertyName) {
        return null;
    }

    @Override
    public List<Property<?>> __getProperties() {
        return null;
    }

    @Override
    public GeneratedIdProperty<?> __getGeneratedIdProperty() {
        return null;
    }

    @Override
    public VersionProperty<?> __getVersionProperty() {
        return null;
    }

    @Override
    public void __preDelete() {
    }

    @Override
    public void __preInsert() {
    }

    @Override
    public void __preUpdate() {
    }

}
