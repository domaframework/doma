package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;
import org.seasar.doma.internal.apt.reflection.SqlProcessorReflection;

/**
 * @author nakamura
 *
 */
public class SqlProcessorQueryMeta extends AbstractSqlFileQueryMeta {

    private SqlProcessorReflection sqlProcessorReflection;

    private String biFunctionParameterName;

    private BiFunctionCtType biFunctionCtType;

    protected SqlProcessorQueryMeta(ExecutableElement method) {
        super(method);
    }

    @Override
    public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
        visitor.visitSqlProcessorQueryMeta(this, p);
    }

    public SqlProcessorReflection getSqlProcessorReflection() {
        return sqlProcessorReflection;
    }

    public void setSqlProcessorReflection(SqlProcessorReflection sqlProcessorReflection) {
        this.sqlProcessorReflection = sqlProcessorReflection;
    }

    public String getBiFunctionParameterName() {
        return biFunctionParameterName;
    }

    public void setBiFunctionParameterName(String biFunctionParameterName) {
        this.biFunctionParameterName = biFunctionParameterName;
    }

    public BiFunctionCtType getBiFunctionCtType() {
        return biFunctionCtType;
    }

    public void setBiFunctionCtType(BiFunctionCtType biFunctionCtType) {
        this.biFunctionCtType = biFunctionCtType;
    }

}
