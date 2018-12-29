package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.lombok.AccessLevel;
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;

@Entity(immutable = true)
@AllArgsConstructor(access = AccessLevel.NONE)
public class LombokAllArgsConstructorAccess_none {}
