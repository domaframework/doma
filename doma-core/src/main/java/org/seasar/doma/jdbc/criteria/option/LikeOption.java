package org.seasar.doma.jdbc.criteria.option;

import java.util.Objects;

public interface LikeOption {

  char DEFAULT_ESCAPE_CHAR = '$';

  static LikeOption none() {
    return None.INSTANCE;
  }

  static LikeOption escape() {
    return new Escape(DEFAULT_ESCAPE_CHAR);
  }

  static LikeOption escape(char escapeChar) {
    return new Escape(escapeChar);
  }

  static LikeOption prefix() {
    return new Prefix(DEFAULT_ESCAPE_CHAR);
  }

  static LikeOption prefix(char escapeChar) {
    return new Prefix(escapeChar);
  }

  static LikeOption infix() {
    return new Infix(DEFAULT_ESCAPE_CHAR);
  }

  static LikeOption infix(char escapeChar) {
    return new Infix(escapeChar);
  }

  static LikeOption suffix() {
    return new Suffix(DEFAULT_ESCAPE_CHAR);
  }

  static LikeOption suffix(char escapeChar) {
    return new Suffix(escapeChar);
  }

  void accept(Visitor visitor);

  class None implements LikeOption {
    private static final None INSTANCE = new None();

    private None() {}

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  class Escape implements LikeOption {
    public final char escapeChar;

    public Escape(char escapeChar) {
      this.escapeChar = escapeChar;
    }

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  class Prefix implements LikeOption {
    public final char escapeChar;

    public Prefix(char escapeChar) {
      this.escapeChar = escapeChar;
    }

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  class Infix implements LikeOption {
    public final char escapeChar;

    public Infix(char escapeChar) {
      this.escapeChar = escapeChar;
    }

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  class Suffix implements LikeOption {
    public final char escapeChar;

    public Suffix(char escapeChar) {
      this.escapeChar = escapeChar;
    }

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  interface Visitor {
    void visit(None none);

    void visit(Escape escape);

    void visit(Prefix prefix);

    void visit(Infix infix);

    void visit(Suffix suffix);
  }
}
