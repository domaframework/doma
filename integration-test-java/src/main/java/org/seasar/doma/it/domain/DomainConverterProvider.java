package org.seasar.doma.it.domain;

import org.seasar.doma.DomainConverters;

@DomainConverters({
  AgeConverter.class,
  LocationConverter.class,
  HiredateConverter.class,
  StringArrayConverter.class,
  UUIDConverter.class
})
public class DomainConverterProvider {}
