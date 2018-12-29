package org.seasar.doma.internal.apt.domain;

import org.seasar.doma.DomainConverters;

@DomainConverters({MondayConverter.class, WednesdayConverter.class})
public class DayConvertersProvider {}
