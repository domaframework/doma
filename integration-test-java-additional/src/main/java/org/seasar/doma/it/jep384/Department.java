package org.seasar.doma.it.jep384;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

@Entity(metamodel = @Metamodel)
@Table(name = "DEPARTMENT")
public record Department(
    @Id Integer departmentId,
    Integer departmentNo,
    String departmentName,
    String location,
    @Version Integer version) {}
