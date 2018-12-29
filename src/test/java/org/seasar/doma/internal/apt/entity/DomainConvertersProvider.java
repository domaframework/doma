package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.DomainConverters;

/** @author taedium */
@DomainConverters({BranchConverter.class, PrimaryKeyConverter.class, VersionNoConverter.class})
public class DomainConvertersProvider {}
