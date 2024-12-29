package org.seasar.doma.jdbc.statistic;

import org.seasar.doma.jdbc.SqlKind;

public record Statistic(
    String sql,
    SqlKind kind,
    long execCount,
    long execMaxTime,
    long execMinTime,
    long execTotalTime,
    double execAvgTime) {}
