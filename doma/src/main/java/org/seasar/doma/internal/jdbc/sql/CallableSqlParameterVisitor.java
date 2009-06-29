package org.seasar.doma.internal.jdbc.sql;

/**
 * @author taedium
 * 
 */
public interface CallableSqlParameterVisitor<R, P, TH extends Throwable> {

    R visitInParameter(InParameter parameter, P p) throws TH;

    R visitOutParameter(OutParameter parameter, P p) throws TH;

    R visitInOutParameter(InOutParameter parameter, P p) throws TH;

    R visitDomainListParameter(DomainListParameter parameter, P p) throws TH;

    R visitEntityListParameter(EntityListParameter<?, ?> parameter, P p)
            throws TH;

    R visitDomainResultParameter(DomainResultParameter<?> parameter, P p)
            throws TH;

    R visitDomainListResultParameter(DomainListResultParameter<?> parameter, P p)
            throws TH;

    R visitEntityListResultParameter(EntityListResultParameter<?, ?> parameter,
            P p) throws TH;
}
