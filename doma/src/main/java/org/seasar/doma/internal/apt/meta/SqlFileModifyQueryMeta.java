package org.seasar.doma.internal.apt.meta;


/**
 * @author taedium
 * 
 */
public class SqlFileModifyQueryMeta extends AbstractSqlFileQueryMeta {

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visistSqlFileModifyQueryMeta(this, p);
    }
}
