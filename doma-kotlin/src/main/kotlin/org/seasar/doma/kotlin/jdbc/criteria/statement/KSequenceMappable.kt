package org.seasar.doma.kotlin.jdbc.criteria.statement

import java.util.stream.Stream

interface KSequenceMappable<ELEMENT> : KListable<ELEMENT> {

    fun openStream(): Stream<ELEMENT>

    fun <RESULT> mapSequence(sequenceMapper: (Sequence<ELEMENT>) -> RESULT): RESULT
}
