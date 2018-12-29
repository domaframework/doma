package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import java.util.function.Function;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.IterationContext;

/** @author nakamura-to */
public class OptionalSingleResultCallback<TARGET>
    implements IterationCallback<TARGET, Optional<TARGET>> {

  protected final Function<TARGET, Optional<TARGET>> mapper;

  public OptionalSingleResultCallback() {
    this(Optional::ofNullable);
  }

  public OptionalSingleResultCallback(Function<TARGET, Optional<TARGET>> mapper) {
    assertNotNull(mapper);
    this.mapper = mapper;
  }

  @Override
  public Optional<TARGET> defaultResult() {
    return mapper.apply(null);
  }

  @Override
  public Optional<TARGET> iterate(TARGET target, IterationContext context) {
    context.exit();
    return mapper.apply(target);
  }
}
