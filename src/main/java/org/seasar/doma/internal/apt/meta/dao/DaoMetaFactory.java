package org.seasar.doma.internal.apt.meta.dao;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import org.seasar.doma.DaoMethod;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.Suppress;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.*;
import org.seasar.doma.internal.apt.annot.*;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.meta.query.*;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.message.Message;

public class DaoMetaFactory implements TypeElementMetaFactory<DaoMeta> {

  protected static final String SINGLETON_CONFIG_FIELD_NAME = "INSTANCE";

  protected final Context ctx;

  protected final List<QueryMetaFactory> queryMetaFactories = new ArrayList<QueryMetaFactory>();

  public DaoMetaFactory(Context ctx, List<QueryMetaFactory> commandMetaFactories) {
    assertNotNull(ctx, commandMetaFactories);
    this.ctx = ctx;
    this.queryMetaFactories.addAll(commandMetaFactories);
  }

  @Override
  public DaoMeta createTypeElementMeta(TypeElement interfaceElement) {
    assertNotNull(interfaceElement);
    DaoAnnot daoAnnot = ctx.getAnnotations().newDaoAnnot(interfaceElement);
    if (daoAnnot == null) {
      throw new AptIllegalStateException("daoAnnot");
    }
    DaoMeta daoMeta = new DaoMeta(daoAnnot);
    doDaoElement(interfaceElement, daoMeta);
    doMethodElements(interfaceElement, daoMeta);
    validateFiles(interfaceElement, daoMeta);
    return daoMeta;
  }

  protected void doDaoElement(TypeElement interfaceElement, DaoMeta daoMeta) {
    validateInterface(interfaceElement, daoMeta);

    String name = interfaceElement.getSimpleName().toString();
    String suffix = ctx.getOptions().getDaoSuffix();
    if (name.endsWith(suffix)) {
      ctx.getReporter()
          .report(Kind.WARNING, Message.DOMA4026, interfaceElement, new Object[] {suffix});
    }
    daoMeta.setName(name);
    daoMeta.setTypeElement(interfaceElement);
    daoMeta.setType(interfaceElement.asType());
    doAnnotateWith(daoMeta);
    doParentDao(daoMeta);

    DaoAnnot daoAnnot = daoMeta.getDaoAnnot();
    if (daoAnnot.hasUserDefinedConfig()) {
      TypeElement configElement = ctx.getTypes().toTypeElement(daoAnnot.getConfigValue());
      if (configElement == null) {
        throw new AptIllegalStateException("failed to convert to TypeElement.");
      }
      validateUserDefinedConfig(configElement, daoMeta, daoAnnot);
    }
  }

  protected void validateUserDefinedConfig(
      TypeElement configElement, DaoMeta daoMeta, DaoAnnot daoAnnot) {
    SingletonConfig singletonConfig = configElement.getAnnotation(SingletonConfig.class);
    if (singletonConfig == null) {
      if (configElement.getModifiers().contains(Modifier.ABSTRACT)) {
        throw new AptException(
            Message.DOMA4163,
            daoMeta.getTypeElement(),
            daoAnnot.getAnnotationMirror(),
            daoAnnot.getConfig(),
            new Object[] {configElement.getQualifiedName()});
      }
      ExecutableElement constructor = ctx.getElements().getNoArgConstructor(configElement);
      if (constructor == null || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
        Optional<VariableElement> field =
            ElementFilter.fieldsIn(configElement.getEnclosedElements())
                .stream()
                .filter(e -> e.getSimpleName().contentEquals(SINGLETON_CONFIG_FIELD_NAME))
                .filter(
                    e ->
                        e.getModifiers()
                            .containsAll(
                                EnumSet.of(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)))
                .filter(e -> ctx.getTypes().isAssignableWithErasure(e.asType(), Config.class))
                .findFirst();
        if (field.isPresent()) {
          daoMeta.setSingletonFieldName(SINGLETON_CONFIG_FIELD_NAME);
        } else {
          throw new AptException(
              Message.DOMA4164,
              daoMeta.getTypeElement(),
              daoAnnot.getAnnotationMirror(),
              daoAnnot.getConfig(),
              new Object[] {configElement.getQualifiedName()});
        }
      }
    } else {
      String methodName = singletonConfig.method();
      boolean present =
          ElementFilter.methodsIn(configElement.getEnclosedElements())
              .stream()
              .filter(
                  m -> m.getModifiers().containsAll(EnumSet.of(Modifier.STATIC, Modifier.PUBLIC)))
              .filter(m -> ctx.getTypes().isAssignableWithErasure(m.getReturnType(), Config.class))
              .filter(m -> m.getParameters().isEmpty())
              .filter(m -> m.getSimpleName().toString().equals(methodName))
              .findAny()
              .isPresent();
      if (present) {
        daoMeta.setSingletonMethodName(methodName);
      } else {
        throw new AptException(
            Message.DOMA4255,
            daoMeta.getTypeElement(),
            daoAnnot.getAnnotationMirror(),
            daoAnnot.getConfig(),
            new Object[] {configElement.getQualifiedName(), methodName});
      }
    }
  }

  protected void validateInterface(TypeElement interfaceElement, DaoMeta daoMeta) {
    if (!interfaceElement.getKind().isInterface()) {
      DaoAnnot daoAnnot = daoMeta.getDaoAnnot();
      throw new AptException(
          Message.DOMA4014, interfaceElement, daoAnnot.getAnnotationMirror(), new Object[] {});
    }
    if (interfaceElement.getNestingKind().isNested()) {
      throw new AptException(Message.DOMA4017, interfaceElement, new Object[] {});
    }
    if (!interfaceElement.getTypeParameters().isEmpty()) {
      throw new AptException(Message.DOMA4059, interfaceElement, new Object[] {});
    }
  }

  protected void doAnnotateWith(DaoMeta daoMeta) {
    AnnotateWithAnnot annotateWithAnnot =
        ctx.getAnnotations().newAnnotateWithAnnot(daoMeta.getTypeElement());
    if (annotateWithAnnot != null) {
      daoMeta.setAnnotateWithAnnot(annotateWithAnnot);
    }
  }

  protected void doParentDao(DaoMeta daoMeta) {
    List<TypeElement> interfaces =
        daoMeta
            .getTypeElement()
            .getInterfaces()
            .stream()
            .map(type -> ctx.getTypes().toTypeElement(type))
            .peek(
                element -> {
                  if (element == null) {
                    throw new AptIllegalStateException("failed to convert to TypeElement.");
                  }
                })
            .collect(toList());
    for (TypeElement typeElement : interfaces) {
      DaoAnnot daoAnnot = ctx.getAnnotations().newDaoAnnot(typeElement);
      if (daoAnnot == null) {
        ExecutableElement nonDefaultMethod = findNonDefaultMethod(typeElement);
        if (nonDefaultMethod == null) {
          continue;
        }
        throw new AptException(
            Message.DOMA4440,
            daoMeta.getTypeElement(),
            new Object[] {nonDefaultMethod.getSimpleName()});
      }
      if (daoMeta.getParentDaoMeta() != null) {
        throw new AptException(Message.DOMA4188, daoMeta.getTypeElement(), new Object[] {});
      }
      ParentDaoMeta parentDaoMeta = new ParentDaoMeta(daoAnnot, typeElement);
      daoMeta.setParentDaoMeta(parentDaoMeta);
    }
  }

  protected ExecutableElement findNonDefaultMethod(TypeElement interfaceElement) {
    Optional<ExecutableElement> method =
        ElementFilter.methodsIn(interfaceElement.getEnclosedElements())
            .stream()
            .filter(m -> !m.isDefault())
            .findAny();
    if (method.isPresent()) {
      return method.get();
    }
    for (TypeMirror typeMirror : interfaceElement.getInterfaces()) {
      TypeElement i = ctx.getTypes().toTypeElement(typeMirror);
      if (i == null) {
        throw new AptIllegalStateException("failed to convert to TypeElement.");
      }
      ExecutableElement m = findNonDefaultMethod(i);
      if (m != null) {
        return m;
      }
    }
    return null;
  }

  protected void doMethodElements(TypeElement interfaceElement, DaoMeta daoMeta) {
    for (ExecutableElement methodElement :
        ElementFilter.methodsIn(interfaceElement.getEnclosedElements())) {
      try {
        doMethodElement(methodElement, daoMeta);
      } catch (AptException e) {
        ctx.getReporter().report(e);
        daoMeta.setError(true);
      }
    }
  }

  protected void doMethodElement(ExecutableElement methodElement, DaoMeta daoMeta) {
    Set<Modifier> modifiers = methodElement.getModifiers();
    if (modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.PRIVATE)) {
      return;
    }
    validateMethod(methodElement, daoMeta);
    QueryMeta queryMeta = createQueryMeta(methodElement, daoMeta);
    validateQueryMeta(queryMeta, methodElement);
    daoMeta.addQueryMeta(queryMeta);
  }

  protected void validateMethod(ExecutableElement methodElement, DaoMeta daoMeta) {
    TypeElement foundAnnotationTypeElement = null;
    for (AnnotationMirror annotation : methodElement.getAnnotationMirrors()) {
      DeclaredType declaredType = annotation.getAnnotationType();
      TypeElement typeElement = ctx.getTypes().toTypeElement(declaredType);
      if (typeElement.getAnnotation(DaoMethod.class) != null) {
        if (foundAnnotationTypeElement != null) {
          throw new AptException(
              Message.DOMA4087,
              methodElement,
              new Object[] {
                foundAnnotationTypeElement.getQualifiedName(), typeElement.getQualifiedName()
              });
        }
        if (methodElement.isDefault()) {
          throw new AptException(
              Message.DOMA4252, methodElement, new Object[] {typeElement.getQualifiedName()});
        }
        foundAnnotationTypeElement = typeElement;
      }
    }
  }

  protected QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    for (QueryMetaFactory factory : queryMetaFactories) {
      QueryMeta queryMeta = factory.createQueryMeta(method, daoMeta);
      if (queryMeta != null) {
        return queryMeta;
      }
    }
    throw new AptException(Message.DOMA4005, method, new Object[] {});
  }

  protected void validateQueryMeta(QueryMeta queryMeta, ExecutableElement method) {
    SqlAnnot sqlAnnot = ctx.getAnnotations().newSqlAnnot(method);
    if (sqlAnnot != null) {
      queryMeta.accept(new SqlAnnotationCombinationValidator(method, sqlAnnot));
    }
  }

  protected void validateFiles(TypeElement interfaceElement, DaoMeta daoMeta) {
    if (daoMeta.isError()) {
      return;
    }
    if (!ctx.getOptions().getSqlValidation()) {
      return;
    }
    String dirPath = SqlFileUtil.buildPath(interfaceElement.getQualifiedName().toString());
    Set<String> fileNames = getFileNames(dirPath);
    for (QueryMeta queryMeta : daoMeta.getQueryMetas()) {
      for (String fileName : queryMeta.getFileNames()) {
        fileNames.remove(fileName);
      }
    }
    Suppress suppress = interfaceElement.getAnnotation(Suppress.class);
    Message message = Message.DOMA4220;
    if (!isSuppressed(suppress, message)) {
      for (String fileName : fileNames) {
        ctx.getReporter()
            .report(
                Kind.WARNING, message, interfaceElement, new Object[] {dirPath + "/" + fileName});
      }
    }
  }

  protected Set<String> getFileNames(String dirPath) {
    File dir = getDir(dirPath);
    if (dir == null) {
      return Collections.emptySet();
    }
    String[] fileNames =
        dir.list(
            new FilenameFilter() {

              @Override
              public boolean accept(File dir, String name) {
                return name.endsWith(Constants.SQL_PATH_SUFFIX)
                    || name.endsWith(Constants.SCRIPT_PATH_SUFFIX);
              }
            });
    return new HashSet<String>(Arrays.asList(fileNames));
  }

  protected File getDir(String dirPath) {
    FileObject fileObject = getFileObject(dirPath);
    if (fileObject == null) {
      return null;
    }
    URI uri = fileObject.toUri();
    if (!uri.isAbsolute()) {
      uri = new File(".").toURI().resolve(uri);
    }
    File dir = new File(uri);
    if (dir.exists() && dir.isDirectory()) {
      return dir;
    }
    return null;
  }

  protected FileObject getFileObject(String path) {
    try {
      return ctx.getResources().getResource(path);
    } catch (Exception ignored) {
      // Ignore, in case the Filer implementation doesn't support
      // directory path.
      return null;
    }
  }

  protected boolean isSuppressed(Suppress suppress, Message message) {
    if (suppress != null) {
      for (Message suppressMessage : suppress.messages()) {
        if (suppressMessage == message) {
          return true;
        }
      }
    }
    return false;
  }

  private static class SqlAnnotationCombinationValidator implements QueryMetaVisitor<Void> {

    private final ExecutableElement method;

    private final SqlAnnot sqlAnnot;

    private SqlAnnotationCombinationValidator(ExecutableElement method, SqlAnnot sqlAnnot) {
      assertNotNull(method, sqlAnnot);
      this.method = method;
      this.sqlAnnot = sqlAnnot;
    }

    @Override
    public Void visitSqlFileSelectQueryMeta(SqlFileSelectQueryMeta m) {
      // always OK
      return null;
    }

    @Override
    public Void visitSqlProcessorQueryMeta(SqlProcessorQueryMeta m) {
      // always OK
      return null;
    }

    @Override
    public Void visitSqlFileScriptQueryMeta(SqlFileScriptQueryMeta m) {
      // always OK
      return null;
    }

    @Override
    public Void visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m) {
      return handleConflict(m.getFunctionAnnot());
    }

    @Override
    public Void visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m) {
      return handleConflict(m.getProcedureAnnot());
    }

    @Override
    public Void visitArrayCreateQueryMeta(ArrayCreateQueryMeta m) {
      return handleConflict(m.getArrayFactoryAnnot());
    }

    @Override
    public Void visitBlobCreateQueryMeta(BlobCreateQueryMeta m) {
      return handleConflict(m.getBlobFactoryAnnot());
    }

    @Override
    public Void visitClobCreateQueryMeta(ClobCreateQueryMeta m) {
      return handleConflict(m.getClobFactoryAnnot());
    }

    @Override
    public Void visitNClobCreateQueryMeta(NClobCreateQueryMeta m) {
      return handleConflict(m.getNClobFactoryAnnot());
    }

    @Override
    public Void visitSQLXMLCreateQueryMeta(SQLXMLCreateQueryMeta m) {
      return handleConflict(m.getSqlxmlFactoryAnnot());
    }

    private Void handleConflict(Annot annot) {
      assertNotNull(annot);
      throw new AptException(
          Message.DOMA4444,
          method,
          sqlAnnot.getAnnotationMirror(),
          new Object[] {annot.getAnnotationMirror()});
    }

    @Override
    public Void visitSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m) {
      ModifyAnnot annot = m.getModifyAnnot();
      if (annot.getSqlFileValue()) {
        handleConflictWithSqlFileElement(annot.getAnnotationMirror(), annot.getSqlFile());
      }
      return null;
    }

    @Override
    public Void visitSqlFileBatchModifyQueryMeta(SqlFileBatchModifyQueryMeta m) {
      BatchModifyAnnot annot = m.getBatchModifyAnnot();
      if (annot.getSqlFileValue()) {
        handleConflictWithSqlFileElement(annot.getAnnotationMirror(), annot.getSqlFile());
      }
      return null;
    }

    private Void handleConflictWithSqlFileElement(
        AnnotationMirror annotationMirror, AnnotationValue annotationValue) {
      assertNotNull(annotationMirror, annotationValue);
      throw new AptException(
          Message.DOMA4445, method, annotationMirror, annotationValue, new Object[] {});
    }

    @Override
    public Void visitDefaultQueryMeta(DefaultQueryMeta m) {
      throw new AptException(
          Message.DOMA4446, method, sqlAnnot.getAnnotationMirror(), new Object[] {});
    }

    @Override
    public Void visitAutoModifyQueryMeta(AutoModifyQueryMeta m) {
      assertUnreachable("visitAutoModifyQueryMeta");
      return null;
    }

    @Override
    public Void visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m) {
      assertUnreachable("visitAutoBatchModifyQueryMeta");
      return null;
    }
  }
}
