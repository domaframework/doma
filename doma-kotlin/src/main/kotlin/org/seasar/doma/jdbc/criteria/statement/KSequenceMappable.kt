package org.seasar.doma.jdbc.criteria.statement

import org.seasar.doma.jdbc.Sql

interface KSequenceMappable<ELEMENT> : KListable<ELEMENT> {

    fun <RESULT> mapSequence(sequenceMapper: (Sequence<ELEMENT>) -> RESULT): RESULT
}
