package org.seasar.doma.it.jep384;

import java.time.LocalDate;
import org.seasar.doma.Embeddable;

@Embeddable
public record WorkerInfo(Integer managerId, LocalDate hiredate) {}
