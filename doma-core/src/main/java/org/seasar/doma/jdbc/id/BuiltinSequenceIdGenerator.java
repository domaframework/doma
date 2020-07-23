package org.seasar.doma.jdbc.id;

import org.seasar.doma.GenerationType;
import org.seasar.doma.jdbc.Sql;

/** The built-in implementation for {@link SequenceIdGenerator}. */
public class BuiltinSequenceIdGenerator extends AbstractPreGenerateIdGenerator
    implements SequenceIdGenerator {

  protected String qualifiedSequenceName;

  @Override
  public void setQualifiedSequenceName(String qualifiedSequenceName) {
    this.qualifiedSequenceName = qualifiedSequenceName;
  }

  @Override
  public void initialize() {}

  @Override
  protected long getNewInitialValue(IdGenerationConfig config) {
    Sql<?> sql = config.getDialect().getSequenceNextValSql(qualifiedSequenceName, allocationSize);
    return getGeneratedValue(config, sql);
  }

  @Override
  public GenerationType getGenerationType() {
    return GenerationType.SEQUENCE;
  }
}
