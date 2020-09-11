package org.seasar.doma.jdbc.criteria.statement

interface KSequenceMappable<ELEMENT> : KListable<ELEMENT> {

    fun <RESULT> mapSequence(sequenceMapper: (Sequence<ELEMENT>) -> RESULT): RESULT
}
