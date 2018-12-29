package org.seasar.doma.jdbc;

/** @author taedium */
public interface SqlParameterVisitor<R, P, TH extends Throwable> {

  <BASIC> R visitInParameter(InParameter<BASIC> parameter, P p) throws TH;

  <BASIC> R visitOutParameter(OutParameter<BASIC> parameter, P p) throws TH;

  <BASIC, INOUT extends InParameter<BASIC> & OutParameter<BASIC>> R visitInOutParameter(
      INOUT parameter, P p) throws TH;

  <ELEMENT> R visitListParameter(ListParameter<ELEMENT> parameter, P p) throws TH;

  <BASIC, RESULT> R visitSingleResultParameter(SingleResultParameter<BASIC, RESULT> parameter, P p)
      throws TH;

  <ELEMENT> R visitResultListParameter(ResultListParameter<ELEMENT> parameter, P p) throws TH;
}
