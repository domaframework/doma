package org.seasar.doma.kotlin.jdbc.criteria.statement

interface KSequenceMappable<ELEMENT> : KListable<ELEMENT> {

    fun <RESULT> mapSequence(sequenceMapper: (Sequence<ELEMENT>) -> RESULT): RESULT
}
