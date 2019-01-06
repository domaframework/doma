package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.query.Query;

public abstract class AbstractQueryMeta implements QueryMeta {

  private final String name;

  private final ExecutableElement executableElement;

  private final TypeElement daoElement;

  private List<String> typeParameterNames = new ArrayList<>();

  private List<String> thrownTypeNames = new ArrayList<>();

  protected QueryKind queryKind;

  private LinkedHashMap<String, TypeMirror> bindableParameterTypeMap = new LinkedHashMap<>();

  private QueryReturnMeta returnMeta;

  private List<QueryParameterMeta> parameterMetas = new ArrayList<>();

  private List<String> fileNames = new ArrayList<>();

  protected AbstractQueryMeta(ExecutableElement method, TypeElement dao) {
    assertNotNull(method);
    this.name = method.getSimpleName().toString();
    this.executableElement = method;
    this.daoElement = dao;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public ExecutableElement getMethodElement() {
    return executableElement;
  }

  @Override
  public TypeElement getDaoElement() {
    return daoElement;
  }

  public void addTypeParameterName(String typeParameterName) {
    typeParameterNames.add(typeParameterName);
  }

  @Override
  public List<String> getTypeParameterNames() {
    return typeParameterNames;
  }

  public void addThrownTypeName(String thrownTypeName) {
    thrownTypeNames.add(thrownTypeName);
  }

  @Override
  public List<String> getThrownTypeNames() {
    return thrownTypeNames;
  }

  public Class<? extends Query> getQueryClass() {
    if (queryKind == null) {
      return null;
    }
    return queryKind.getQueryClass();
  }

  @SuppressWarnings("rawtypes")
  public Class<?> getCommandClass() {
    if (queryKind == null) {
      return null;
    }
    return (Class<? extends Command>) queryKind.getCommandClass();
  }

  @Override
  public QueryKind getQueryKind() {
    return queryKind;
  }

  public void setQueryKind(QueryKind queryKind) {
    this.queryKind = queryKind;
  }

  @Override
  public LinkedHashMap<String, TypeMirror> getBindableParameterTypeMap() {
    return bindableParameterTypeMap;
  }

  public void addBindableParameterCtType(
      final String parameterName, CtType bindableParameterCtType) {
    bindableParameterCtType.accept(new BindableParameterCtTypeVisitor(parameterName), null);
  }

  @Override
  public QueryReturnMeta getReturnMeta() {
    return returnMeta;
  }

  public void setReturnMeta(QueryReturnMeta returnMeta) {
    this.returnMeta = returnMeta;
  }

  @Override
  public List<QueryParameterMeta> getParameterMetas() {
    return parameterMetas;
  }

  public void addParameterMeta(QueryParameterMeta queryParameterMeta) {
    this.parameterMetas.add(queryParameterMeta);
  }

  @Override
  public List<String> getFileNames() {
    return fileNames;
  }

  public void addFileName(String fileName) {
    this.fileNames.add(fileName);
  }

  @Override
  public boolean isVarArgs() {
    return this.executableElement.isVarArgs();
  }

  protected class BindableParameterCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    protected final String parameterName;

    protected BindableParameterCtTypeVisitor(String parameterName) {
      this.parameterName = parameterName;
    }

    @Override
    protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
      bindableParameterTypeMap.put(parameterName, ctType.getType());
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      return ctType.getElementCtType().accept(this, p);
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p) throws RuntimeException {
      return ctType.getElementCtType().accept(this, p);
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p) throws RuntimeException {
      return ctType.getElementCtType().accept(this, p);
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, p);
    }
  }
}
