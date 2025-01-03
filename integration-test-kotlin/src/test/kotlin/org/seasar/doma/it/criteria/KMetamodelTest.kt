/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.seasar.doma.DomaIllegalArgumentException
import org.seasar.doma.jdbc.entity.EntityType

class KMetamodelTest {
    @Test
    fun tableName() {
        val e = Employee_("MY_EMP")
        val entityType: EntityType<*> = e.asType()
        val tableName = entityType.getQualifiedTableName(null, null)
        assertEquals("MY_EMP", tableName)
    }

    @Test
    fun tableName_single_quotation() {
        val ex = assertThrows(
            DomaIllegalArgumentException::class.java,
        ) {
            val e = Employee_("ab'c")
            e.asType()
        }
        println(ex.message)
    }

    @Test
    fun tableName_semicolon() {
        val ex = assertThrows(
            DomaIllegalArgumentException::class.java,
        ) {
            val e = Employee_("ab;c")
            e.asType()
        }
        println(ex.message)
    }

    @Test
    fun tableName_two_hyphens() {
        val ex = assertThrows(
            DomaIllegalArgumentException::class.java,
        ) {
            val e = Employee_("ab--c")
            e.asType()
        }
        println(ex.message)
    }

    @Test
    fun tableName_slash() {
        val ex = assertThrows(
            DomaIllegalArgumentException::class.java,
        ) {
            val e = Employee_("ab/*c")
            e.asType()
        }
        println(ex.message)
    }
}
