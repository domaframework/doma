package org.seasar.doma.internal.apt.processor.domain;

import org.seasar.doma.DomainConverters;

@DomainConverters({MondayConverter.class, WednesdayConverter.class})
public class DayConvertersProvider {}
