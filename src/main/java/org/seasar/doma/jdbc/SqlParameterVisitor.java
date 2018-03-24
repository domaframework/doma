package org.seasar.doma.jdbc;

/**
 * A visitor for {@link SqlParameter}.
 */
public interface SqlParameterVisitor<P, TH extends Throwable> {

    <BASIC> void visitInParameter(InParameter<BASIC> parameter, P p) throws TH;

    <BASIC> void visitOutParameter(OutParameter<BASIC> parameter, P p) throws TH;

    <BASIC, INOUT extends InParameter<BASIC> & OutParameter<BASIC>> void visitInOutParameter(
            INOUT parameter, P p) throws TH;

    <ELEMENT> void visitListParameter(ListParameter<ELEMENT> parameter, P p) throws TH;

    <BASIC, RESULT> void visitSingleResultParameter(SingleResultParameter<BASIC, RESULT> parameter,
                                                    P p) throws TH;

    <ELEMENT> void visitResultListParameter(ResultListParameter<ELEMENT> parameter, P p) throws TH;

}
