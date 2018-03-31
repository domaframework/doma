package org.seasar.doma.jdbc.tx;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.seasar.doma.message.Message;

/** A context for a local transaction. */
public class LocalTransactionContext {

  private final List<String> savepointNames = new ArrayList<>();

  private final Map<String, Savepoint> savepointMap = new HashMap<>();

  private LocalTransactionConnection localTxConnection;

  private Supplier<LocalTransactionConnection> localTxConnectionSupplier;

  private String id;

  private boolean rollbackOnly;

  void begin(Supplier<LocalTransactionConnection> localTxConnectionSupplier) {
    assertNotNull(localTxConnectionSupplier);
    id = String.valueOf(System.identityHashCode(localTxConnectionSupplier));
    this.localTxConnectionSupplier = localTxConnectionSupplier;
  }

  void end() {
    id = null;
    localTxConnectionSupplier = null;
  }

  LocalTransactionConnection getConnection() {
    if (localTxConnection == null) {
      if (localTxConnectionSupplier == null) {
        throw new TransactionNotYetBegunException(Message.DOMA2048);
      }
      localTxConnection = localTxConnectionSupplier.get();
    }
    return localTxConnection;
  }

  boolean hasConnection() {
    return localTxConnection != null;
  }

  Savepoint getSavepoint(String savepointName) {
    assertNotNull(savepointName);
    return savepointMap.get(savepointName);
  }

  void addSavepoint(String savepointName, Savepoint savepoint) {
    assertNotNull(savepointName, savepoint);
    savepointNames.add(savepointName);
    savepointMap.put(savepointName, savepoint);
  }

  Savepoint releaseAndGetSavepoint(String savepointName) {
    assertNotNull(savepointName);
    var result = savepointMap.get(savepointName);
    if (result == null) {
      return null;
    }
    var pos = savepointNames.lastIndexOf(savepointName);
    if (pos > -1) {
      var subList = savepointNames.subList(pos, savepointNames.size());
      for (var name : subList) {
        savepointMap.remove(name);
      }
      subList.clear();
    }
    return result;
  }

  String getId() {
    return id;
  }

  void setRollbackOnly() {
    this.rollbackOnly = true;
  }

  boolean isRollbackOnly() {
    return rollbackOnly;
  }
}
