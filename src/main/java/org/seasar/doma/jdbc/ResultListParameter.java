package org.seasar.doma.jdbc;

import java.util.List;

/**
 * A result list parameter.
 */
public interface ResultListParameter<ELEMENT>
        extends ResultParameter<List<ELEMENT>>, ListParameter<ELEMENT> {

}
