package org.seasar.doma.internal.apt.meta;

/**
 * @author taedium
 * 
 */
public interface CallableStatementParameterMetaVisitor<R, P> {

    R visitInParameterMeta(InParameterMeta m, P p);

    R visistOutParameterMeta(OutParameterMeta m, P p);

    R visistInOutParameterMeta(InOutParameterMeta m, P p);

    R visistDomainListParameterMeta(DomainListParameterMeta m, P p);

    R visistEntityListParameterMeta(EntityListParameterMeta m, P p);

    R visistDomainResultParameterMeta(DomainResultParameterMeta m, P p);

    R visistDomainListResultParameterMeta(DomainListResultParameterMeta m, P p);

    R visistEntityListResultParameterMeta(EntityListResultParameterMeta m, P p);

}
