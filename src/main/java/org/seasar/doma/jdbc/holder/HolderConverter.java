/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.jdbc.holder;

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.HolderConverters;

/**
 * A converter between a value holder and a value.
 * <p>
 * You can use an arbitrary type for the value holder. The value's type must be
 * one of the basic types.
 * <p>
 * The implementation class should be annotated with {@link ExternalHolder} and
 * be registered to {@link HolderConverters}.
 * 
 * <pre>
 * &#064;ExtenalHolder
 * public class SalaryConverter implements HolderConverter&lt;Salary, BigDecimal&gt; {
 * 
 *     public BigDecimal fromHolderToValue(Salary holder) {
 *         return holder.getValue();
 *     }
 * 
 *     public Salary fromValueToHolder(BigDecimal value) {
 *         return new Salary(value);
 *     }
 * }
 * </pre>
 * 
 * @see ExternalHolder
 * @see HolderConverters
 * 
 * @param <HOLDER>
 *            the holder type
 * @param <BASIC>
 *            the basic type
 */
public interface HolderConverter<HOLDER, BASIC> {

    /**
     * Converts the value holder to the value.
     * 
     * @param holder
     *            the value holder
     * @return the value
     */
    BASIC fromHolderToValue(HOLDER holder);

    /**
     * Converts the value to the value holder
     * 
     * @param value
     *            the value
     * @return the value hodler
     */
    HOLDER fromValueToHolder(BASIC value);
}
