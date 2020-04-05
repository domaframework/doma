package org.seasar.doma.jdbc.id;

import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.seasar.doma.jdbc.JdbcException;

/** A generator that generates an identity value before an execution of insert. */
public abstract class AbstractPreGenerateIdGenerator extends AbstractIdGenerator {

  protected long initialValue;

  protected long allocationSize;

  // the keys are data source names and the values are identity contexts
  protected ConcurrentMap<String, IdContext> idContextMap =
      new ConcurrentHashMap<String, IdContext>();

  /**
   * Sets the initial value.
   *
   * @param initialValue the initial value
   */
  public void setInitialValue(long initialValue) {
    this.initialValue = initialValue;
  }

  /**
   * Sets the allocation size.
   *
   * @param allocationSize the allocation size
   */
  public void setAllocationSize(long allocationSize) {
    this.allocationSize = allocationSize;
  }

  @Override
  public boolean supportsBatch(IdGenerationConfig config) {
    return true;
  }

  @Override
  public boolean supportsAutoGeneratedKeys(IdGenerationConfig config) {
    return false;
  }

  @Override
  public boolean includesIdentityColumn(IdGenerationConfig config) {
    return true;
  }

  @Override
  public Long generatePreInsert(IdGenerationConfig config) {
    IdContext idContext = getIdContext(config);
    return idContext.getNextValue(config);
  }

  @Override
  public Long generatePostInsert(IdGenerationConfig config, Statement statement) {
    return null;
  }

  /**
   * Returns the identity context.
   *
   * @param config the configuration
   * @return the identity context
   */
  protected IdContext getIdContext(IdGenerationConfig config) {
    String dataSourceName = config.getDataSourceName();
    IdContext context = idContextMap.get(dataSourceName);
    if (context != null) {
      return context;
    }
    context = new IdContext();
    IdContext existent = idContextMap.putIfAbsent(dataSourceName, context);
    if (existent != null) {
      return existent;
    }
    return context;
  }

  /**
   * Return the next initial value.
   *
   * @param config the configuration
   * @return the new initial value
   * @throws JdbcException if it fails to retrieve the next initial value.
   */
  protected abstract long getNewInitialValue(IdGenerationConfig config);

  /**
   * A context for identity values.
   *
   * <p>This object increments identity value and holds it.
   */
  public class IdContext {

    protected long initValue = AbstractPreGenerateIdGenerator.this.initialValue;

    protected long allocated = Long.MAX_VALUE;

    /**
     * Returns the next identity value.
     *
     * @param config the configuration
     * @return the next identity value
     * @throws JdbcException if the identity generation is failed
     */
    public synchronized long getNextValue(IdGenerationConfig config) {
      if (allocated < allocationSize) {
        return initValue + allocated++;
      }
      initValue = getNewInitialValue(config);
      allocated = 1;
      return initValue;
    }
  }
}
