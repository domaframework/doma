package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.TenantId;

@Entity
public class TenantIdEntity {

  @TenantId String tenantDiscriminator;
}
