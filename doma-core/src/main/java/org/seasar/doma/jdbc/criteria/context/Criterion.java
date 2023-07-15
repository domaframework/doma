package org.seasar.doma.jdbc.criteria.context;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.option.LikeOption;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.criteria.tuple.Tuple3;

public interface Criterion {
  void accept(Visitor visitor);

  class Eq implements Criterion {
    public final Operand.Prop left;
    public final Operand right;

    public Eq(Operand.Prop left, Operand right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class Ne implements Criterion {
    public final Operand.Prop left;
    public final Operand right;

    public Ne(Operand.Prop left, Operand right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class Gt implements Criterion {
    public final Operand.Prop left;
    public final Operand right;

    public Gt(Operand.Prop left, Operand right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class Ge implements Criterion {
    public final Operand.Prop left;
    public final Operand right;

    public Ge(Operand.Prop left, Operand right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class Lt implements Criterion {
    public final Operand.Prop left;
    public final Operand right;

    public Lt(Operand.Prop left, Operand right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class Le implements Criterion {
    public final Operand.Prop left;
    public final Operand right;

    public Le(Operand.Prop left, Operand right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class IsNull implements Criterion {
    public final Operand.Prop prop;

    public IsNull(Operand.Prop prop) {
      this.prop = Objects.requireNonNull(prop);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class IsNotNull implements Criterion {
    public final Operand.Prop prop;

    public IsNotNull(Operand.Prop prop) {
      this.prop = Objects.requireNonNull(prop);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class Like implements Criterion {
    public final Operand.Prop left;
    public final CharSequence right;
    public final LikeOption option;

    public Like(Operand.Prop left, CharSequence right, LikeOption option) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
      this.option = Objects.requireNonNull(option);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class NotLike implements Criterion {
    public final Operand.Prop left;
    public final CharSequence right;
    public final LikeOption option;

    public NotLike(Operand.Prop left, CharSequence right, LikeOption option) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
      this.option = Objects.requireNonNull(option);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class Between implements Criterion {
    public final Operand.Prop prop;
    public final Operand.Param start;
    public final Operand.Param end;

    public Between(Operand.Prop prop, Operand.Param start, Operand.Param end) {
      this.prop = Objects.requireNonNull(prop);
      this.start = Objects.requireNonNull(start);
      this.end = Objects.requireNonNull(end);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class In implements Criterion {
    public final Operand.Prop left;
    public final List<Operand.Param> right;

    public In(Operand.Prop left, List<Operand.Param> right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class NotIn implements Criterion {
    public final Operand.Prop left;
    public final List<Operand.Param> right;

    public NotIn(Operand.Prop left, List<Operand.Param> right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class InSubQuery implements Criterion {
    public final Operand.Prop left;
    public final SelectContext right;

    public InSubQuery(Operand.Prop left, SelectContext right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class NotInSubQuery implements Criterion {
    public final Operand.Prop left;
    public final SelectContext right;

    public NotInSubQuery(Operand.Prop left, SelectContext right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class InTuple2 implements Criterion {
    public final Tuple2<Operand.Prop, Operand.Prop> left;
    public final List<Tuple2<Operand.Param, Operand.Param>> right;

    public InTuple2(
        Tuple2<Operand.Prop, Operand.Prop> left, List<Tuple2<Operand.Param, Operand.Param>> right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class NotInTuple2 implements Criterion {
    public final Tuple2<Operand.Prop, Operand.Prop> left;
    public final List<Tuple2<Operand.Param, Operand.Param>> right;

    public NotInTuple2(
        Tuple2<Operand.Prop, Operand.Prop> left, List<Tuple2<Operand.Param, Operand.Param>> right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class InTuple2SubQuery implements Criterion {
    public final Tuple2<Operand.Prop, Operand.Prop> left;
    public final SelectContext right;

    public InTuple2SubQuery(Tuple2<Operand.Prop, Operand.Prop> left, SelectContext right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class NotInTuple2SubQuery implements Criterion {
    public final Tuple2<Operand.Prop, Operand.Prop> left;
    public final SelectContext right;

    public NotInTuple2SubQuery(Tuple2<Operand.Prop, Operand.Prop> left, SelectContext right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class InTuple3 implements Criterion {
    public final Tuple3<Operand.Prop, Operand.Prop, Operand.Prop> left;
    public final List<Tuple3<Operand.Param, Operand.Param, Operand.Param>> right;

    public InTuple3(
        Tuple3<Operand.Prop, Operand.Prop, Operand.Prop> left,
        List<Tuple3<Operand.Param, Operand.Param, Operand.Param>> right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class NotInTuple3 implements Criterion {
    public final Tuple3<Operand.Prop, Operand.Prop, Operand.Prop> left;
    public final List<Tuple3<Operand.Param, Operand.Param, Operand.Param>> right;

    public NotInTuple3(
        Tuple3<Operand.Prop, Operand.Prop, Operand.Prop> left,
        List<Tuple3<Operand.Param, Operand.Param, Operand.Param>> right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class InTuple3SubQuery implements Criterion {
    public final Tuple3<Operand.Prop, Operand.Prop, Operand.Prop> left;
    public final SelectContext right;

    public InTuple3SubQuery(
        Tuple3<Operand.Prop, Operand.Prop, Operand.Prop> left, SelectContext right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class NotInTuple3SubQuery implements Criterion {
    public final Tuple3<Operand.Prop, Operand.Prop, Operand.Prop> left;
    public final SelectContext right;

    public NotInTuple3SubQuery(
        Tuple3<Operand.Prop, Operand.Prop, Operand.Prop> left, SelectContext right) {
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class Exists implements Criterion {
    public final SelectContext context;

    public Exists(SelectContext context) {
      this.context = Objects.requireNonNull(context);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class NotExists implements Criterion {
    public final SelectContext context;

    public NotExists(SelectContext context) {
      this.context = Objects.requireNonNull(context);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class And implements Criterion {
    public final List<Criterion> criterionList;

    public And(List<Criterion> criterionList) {
      Objects.requireNonNull(criterionList);
      this.criterionList = Collections.unmodifiableList(criterionList);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class Or implements Criterion {
    public final List<Criterion> criterionList;

    public Or(List<Criterion> criterionList) {
      Objects.requireNonNull(criterionList);
      this.criterionList = Collections.unmodifiableList(criterionList);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class Not implements Criterion {
    public final List<Criterion> criterionList;

    public Not(List<Criterion> criterionList) {
      Objects.requireNonNull(criterionList);
      this.criterionList = Collections.unmodifiableList(criterionList);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  interface Visitor {
    void visit(Eq criterion);

    void visit(Ne criterion);

    void visit(Gt criterion);

    void visit(Ge criterion);

    void visit(Lt criterion);

    void visit(Le criterion);

    void visit(IsNull criterion);

    void visit(IsNotNull criterion);

    void visit(Like criterion);

    void visit(NotLike criterion);

    void visit(Between criterion);

    void visit(In criterion);

    void visit(NotIn criterion);

    void visit(InSubQuery criterion);

    void visit(NotInSubQuery criterion);

    void visit(InTuple2 criterion);

    void visit(NotInTuple2 criterion);

    void visit(InTuple2SubQuery criterion);

    void visit(NotInTuple2SubQuery criterion);

    void visit(InTuple3 criterion);

    void visit(NotInTuple3 criterion);

    void visit(InTuple3SubQuery criterion);

    void visit(NotInTuple3SubQuery criterion);

    void visit(Exists criterion);

    void visit(NotExists criterion);

    void visit(And criterion);

    void visit(Or criterion);

    void visit(Not criterion);
  }
}
