package org.seasar.doma.internal.jdbc.command;

public enum ResultSetState {
  UNKNOWN {
    @Override
    ResultSetState apply(boolean next) {
      return next ? AT_LEAST_ONE_RESULT : NO_RESULT;
    }
  },

  NO_RESULT {
    @Override
    ResultSetState apply(boolean next) {
      return NO_RESULT;
    }
  },

  AT_LEAST_ONE_RESULT {
    @Override
    ResultSetState apply(boolean next) {
      return next ? MULTIPLE_RESULTS : SINGLE_RESULT;
    }
  },

  SINGLE_RESULT {
    @Override
    ResultSetState apply(boolean next) {
      return SINGLE_RESULT;
    }
  },

  MULTIPLE_RESULTS() {
    @Override
    ResultSetState apply(boolean next) {
      return MULTIPLE_RESULTS;
    }
  };

  abstract ResultSetState apply(boolean next);
}
