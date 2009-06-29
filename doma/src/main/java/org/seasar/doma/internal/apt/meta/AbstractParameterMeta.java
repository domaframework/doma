package org.seasar.doma.internal.apt.meta;

/**
 * @author taedium
 * 
 */
public abstract class AbstractParameterMeta implements CallableStatementParameterMeta {

    protected String name;

    protected String typeName;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
