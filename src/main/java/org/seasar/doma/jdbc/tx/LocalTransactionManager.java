package org.seasar.doma.jdbc.tx;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Supplier;
import org.seasar.doma.DomaNullPointerException;

/** A transaction manager for local transactions. */
public class LocalTransactionManager implements TransactionManager {

  protected final LocalTransaction transaction;

  public LocalTransactionManager(LocalTransaction transaction) {
    if (transaction == null) {
      throw new DomaNullPointerException("transaction");
    }
    this.transaction = transaction;
  }

  @Override
  public void required(Runnable block) {
    if (block == null) {
      throw new DomaNullPointerException("block");
    }
    requiredInternal(TransactionIsolationLevel.DEFAULT, toSupplier(block));
  }

  @Override
  public void required(TransactionIsolationLevel isolationLevel, Runnable block) {
    if (isolationLevel == null) {
      throw new DomaNullPointerException("isolationLevel");
    }
    if (block == null) {
      throw new DomaNullPointerException("block");
    }
    requiredInternal(isolationLevel, toSupplier(block));
  }

  @Override
  public <RESULT> RESULT required(Supplier<RESULT> supplier) {
    if (supplier == null) {
      throw new DomaNullPointerException("supplier");
    }
    return requiredInternal(TransactionIsolationLevel.DEFAULT, supplier);
  }

  @Override
  public <RESULT> RESULT required(
      TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
    if (isolationLevel == null) {
      throw new DomaNullPointerException("isolationLevel");
    }
    if (supplier == null) {
      throw new DomaNullPointerException("supplier");
    }
    return requiredInternal(isolationLevel, supplier);
  }

  protected <RESULT> RESULT requiredInternal(
      TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
    assertNotNull(isolationLevel, supplier);
    if (transaction.isActive()) {
      return supplier.get();
    } else {
      return executeInTransaction(isolationLevel, supplier);
    }
  }

  @Override
  public void requiresNew(Runnable block) {
    if (block == null) {
      throw new DomaNullPointerException("block");
    }
    requiresNewInternal(TransactionIsolationLevel.DEFAULT, toSupplier(block));
  }

  @Override
  public void requiresNew(TransactionIsolationLevel isolationLevel, Runnable block) {
    if (isolationLevel == null) {
      throw new DomaNullPointerException("isolationLevel");
    }
    if (block == null) {
      throw new DomaNullPointerException("block");
    }
    requiresNewInternal(isolationLevel, toSupplier(block));
  }

  @Override
  public <RESULT> RESULT requiresNew(Supplier<RESULT> supplier) {
    if (supplier == null) {
      throw new DomaNullPointerException("supplier");
    }
    return requiresNewInternal(TransactionIsolationLevel.DEFAULT, supplier);
  }

  @Override
  public <RESULT> RESULT requiresNew(
      TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
    if (isolationLevel == null) {
      throw new DomaNullPointerException("isolationLevel");
    }
    if (supplier == null) {
      throw new DomaNullPointerException("supplier");
    }
    return requiresNewInternal(isolationLevel, supplier);
  }

  protected <RESULT> RESULT requiresNewInternal(
      TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
    assertNotNull(isolationLevel, supplier);
    if (transaction.isActive()) {
      var context = transaction.suspend();
      try {
        return executeInTransaction(isolationLevel, supplier);
      } finally {
        transaction.resume(context);
      }
    } else {
      return executeInTransaction(isolationLevel, supplier);
    }
  }

  @Override
  public void notSupported(Runnable block) {
    if (block == null) {
      throw new DomaNullPointerException("block");
    }
    notSupportedInternal(TransactionIsolationLevel.DEFAULT, toSupplier(block));
  }

  @Override
  public void notSupported(TransactionIsolationLevel isolationLevel, Runnable block) {
    if (isolationLevel == null) {
      throw new DomaNullPointerException("isolationLevel");
    }
    if (block == null) {
      throw new DomaNullPointerException("block");
    }
    notSupportedInternal(isolationLevel, toSupplier(block));
  }

  @Override
  public <RESULT> RESULT notSupported(Supplier<RESULT> supplier) {
    if (supplier == null) {
      throw new DomaNullPointerException("supplier");
    }
    return notSupportedInternal(TransactionIsolationLevel.DEFAULT, supplier);
  }

  @Override
  public <RESULT> RESULT notSupported(
      TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
    if (isolationLevel == null) {
      throw new DomaNullPointerException("isolationLevel");
    }
    if (supplier == null) {
      throw new DomaNullPointerException("supplier");
    }
    return notSupportedInternal(isolationLevel, supplier);
  }

  protected <RESULT> RESULT notSupportedInternal(
      TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
    assertNotNull(isolationLevel, supplier);
    if (transaction.isActive()) {
      var context = transaction.suspend();
      try {
        return supplier.get();
      } finally {
        transaction.resume(context);
      }
    } else {
      return supplier.get();
    }
  }

  protected Supplier<Void> toSupplier(Runnable block) {
    return () -> {
      block.run();
      return null;
    };
  }

  @Override
  public void setRollbackOnly() {
    transaction.setRollbackOnly();
  }

  @Override
  public boolean isRollbackOnly() {
    return transaction.isRollbackOnly();
  }

  protected <RESULT> RESULT executeInTransaction(
      TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
    assertNotNull(isolationLevel, supplier);
    transaction.begin(isolationLevel);
    try {
      var result = supplier.get();
      if (!transaction.isRollbackOnly()) {
        transaction.commit();
      }
      return result;
    } finally {
      transaction.rollback();
    }
  }

  @Override
  public void setSavepoint(String savepointName) {
    transaction.setSavepoint(savepointName);
  }

  @Override
  public boolean hasSavepoint(String savepointName) {
    return transaction.hasSavepoint(savepointName);
  }

  @Override
  public void releaseSavepoint(String savepointName) {
    transaction.releaseSavepoint(savepointName);
  }

  @Override
  public void rollback(String savepointName) {
    transaction.rollback(savepointName);
  }
}
