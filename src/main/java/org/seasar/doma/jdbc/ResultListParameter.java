package org.seasar.doma.jdbc;

import java.util.List;

/** @author nakamura-to */
public interface ResultListParameter<ELEMENT>
    extends ResultParameter<List<ELEMENT>>, ListParameter<ELEMENT> {}
