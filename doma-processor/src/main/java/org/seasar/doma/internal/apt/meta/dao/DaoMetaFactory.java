/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.meta.dao;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import org.seasar.doma.DaoMethod;
import org.seasar.doma.Suppress;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.Annot;
import org.seasar.doma.internal.apt.annot.AnnotateWithAnnot;
import org.seasar.doma.internal.apt.annot.BatchModifyAnnot;
import org.seasar.doma.internal.apt.annot.DaoAnnot;
import org.seasar.doma.internal.apt.annot.ModifyAnnot;
import org.seasar.doma.internal.apt.annot.SqlAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.meta.query.ArrayCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.query.ArrayCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoFunctionQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoFunctionQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoMultiInsertQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoMultiInsertQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoProcedureQueryMeta;
import org.seasar.doma.internal.apt.meta.query.AutoProcedureQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.BlobCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.query.BlobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.ClobCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.query.ClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.DefaultQueryMeta;
import org.seasar.doma.internal.apt.meta.query.DefaultQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.NClobCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.query.NClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.QueryMeta;
import org.seasar.doma.internal.apt.meta.query.QueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.QueryMetaVisitor;
import org.seasar.doma.internal.apt.meta.query.SQLXMLCreateQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SQLXMLCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlFileBatchModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SqlFileBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlFileModifyQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SqlFileModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlFileScriptQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SqlFileScriptQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlFileSelectQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SqlFileSelectQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlProcessorQueryMeta;
import org.seasar.doma.internal.apt.meta.query.SqlProcessorQueryMetaFactory;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.message.Message;

public class DaoMetaFactory implements TypeElementMetaFactory<DaoMeta> {

  private final RoundContext ctx;

  private final List<BiFunction<TypeElement, ExecutableElement, QueryMetaFactory>> providers =
      new ArrayList<>(15);

  public DaoMetaFactory(RoundContext ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
    providers.add((dao, method) -> new SqlFileSelectQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new AutoModifyQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new AutoMultiInsertQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new AutoBatchModifyQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new AutoFunctionQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new AutoProcedureQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new SqlFileModifyQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new SqlFileBatchModifyQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new SqlFileScriptQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new DefaultQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new ArrayCreateQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new BlobCreateQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new ClobCreateQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new NClobCreateQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new SQLXMLCreateQueryMetaFactory(ctx, dao, method));
    providers.add((dao, method) -> new SqlProcessorQueryMetaFactory(ctx, dao, method));
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

  private void doDaoElement(TypeElement interfaceElement, DaoMeta daoMeta) {
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
  }

  private void validateInterface(TypeElement interfaceElement, DaoMeta daoMeta) {
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

  private void doAnnotateWith(DaoMeta daoMeta) {
    List<AnnotateWithAnnot> annotateWithAnnots =
        ctx.getAnnotations().newAnnotateWithAnnots(daoMeta.getTypeElement());
    daoMeta.setAnnotateWithAnnots(annotateWithAnnots);
  }

  private void doParentDao(DaoMeta daoMeta) {
    List<TypeElement> interfaces =
        daoMeta.getTypeElement().getInterfaces().stream()
            .map(type -> ctx.getMoreTypes().toTypeElement(type))
            .peek(
                element -> {
                  if (element == null) {
                    throw new AptIllegalStateException("failed to convert to TypeElement.");
                  }
                })
            .toList();
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
      List<ExecutableElement> methods =
          ElementFilter.methodsIn(typeElement.getEnclosedElements()).stream()
              .filter(it -> it.getModifiers().contains(Modifier.PUBLIC))
              .filter(it -> !it.getModifiers().contains(Modifier.STATIC))
              .collect(toList());
      ParentDaoMeta parentDaoMeta = new ParentDaoMeta(daoAnnot, typeElement, methods);
      daoMeta.setParentDaoMeta(parentDaoMeta);
    }
  }

  private ExecutableElement findNonDefaultMethod(TypeElement interfaceElement) {
    Optional<ExecutableElement> method =
        ElementFilter.methodsIn(interfaceElement.getEnclosedElements()).stream()
            .filter(m -> !m.isDefault())
            .filter(m -> !ctx.getMoreElements().isVirtualDefaultMethod(interfaceElement, m))
            .findAny();
    if (method.isPresent()) {
      return method.get();
    }
    for (TypeMirror typeMirror : interfaceElement.getInterfaces()) {
      TypeElement i = ctx.getMoreTypes().toTypeElement(typeMirror);
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

  private void doMethodElements(TypeElement interfaceElement, DaoMeta daoMeta) {
    for (ExecutableElement methodElement :
        ElementFilter.methodsIn(interfaceElement.getEnclosedElements())) {
      try {
        doMethodElement(interfaceElement, daoMeta, methodElement);
      } catch (AptException e) {
        ctx.getReporter().report(e);
        daoMeta.setError(true);
      }
    }
  }

  private void doMethodElement(
      TypeElement interfaceElement, DaoMeta daoMeta, ExecutableElement methodElement) {
    Set<Modifier> modifiers = methodElement.getModifiers();
    if (modifiers.contains(Modifier.STATIC) || modifiers.contains(Modifier.PRIVATE)) {
      return;
    }
    validateMethod(interfaceElement, methodElement);
    QueryMeta queryMeta = createQueryMeta(daoMeta, methodElement);
    validateQueryMeta(queryMeta, methodElement);
    daoMeta.addQueryMeta(queryMeta);

    ParentDaoMeta parentDaoMeta = daoMeta.getParentDaoMeta();
    if (parentDaoMeta != null) {
      parentDaoMeta
          .getMethods()
          .removeIf(it -> ctx.getMoreElements().overrides(methodElement, it, interfaceElement));
    }
  }

  private void validateMethod(TypeElement interfaceElement, ExecutableElement methodElement) {
    TypeElement foundAnnotationTypeElement = null;
    for (AnnotationMirror annotation : methodElement.getAnnotationMirrors()) {
      DeclaredType declaredType = annotation.getAnnotationType();
      TypeElement typeElement = ctx.getMoreTypes().toTypeElement(declaredType);
      if (typeElement.getAnnotation(DaoMethod.class) != null) {
        if (foundAnnotationTypeElement != null) {
          throw new AptException(
              Message.DOMA4087,
              methodElement,
              new Object[] {
                foundAnnotationTypeElement.getQualifiedName(), typeElement.getQualifiedName()
              });
        }
        if (methodElement.isDefault()
            || ctx.getMoreElements().isVirtualDefaultMethod(interfaceElement, methodElement)) {
          throw new AptException(
              Message.DOMA4252, methodElement, new Object[] {typeElement.getQualifiedName()});
        }
        foundAnnotationTypeElement = typeElement;
      }
    }
  }

  private QueryMeta createQueryMeta(DaoMeta daoMeta, ExecutableElement methodElement) {
    for (BiFunction<TypeElement, ExecutableElement, QueryMetaFactory> provider : providers) {
      QueryMetaFactory factory = provider.apply(daoMeta.getTypeElement(), methodElement);
      QueryMeta queryMeta = factory.createQueryMeta();
      if (queryMeta != null) {
        return queryMeta;
      }
    }
    throw new AptException(Message.DOMA4005, methodElement, new Object[] {});
  }

  private void validateQueryMeta(QueryMeta queryMeta, ExecutableElement methodElement) {
    SqlAnnot sqlAnnot = ctx.getAnnotations().newSqlAnnot(methodElement);
    if (sqlAnnot != null) {
      queryMeta.accept(new SqlAnnotationCombinationValidator(methodElement, sqlAnnot));
    }
  }

  private void validateFiles(TypeElement interfaceElement, DaoMeta daoMeta) {
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

  private Set<String> getFileNames(String dirPath) {
    File dir = getDir(dirPath);
    if (dir == null) {
      return Collections.emptySet();
    }
    String[] fileNames =
        dir.list(
            (dir1, name) ->
                name.endsWith(Constants.SQL_PATH_SUFFIX)
                    || name.endsWith(Constants.SCRIPT_PATH_SUFFIX));
    if (fileNames == null) {
      return Collections.emptySet();
    }
    return new HashSet<>(Arrays.asList(fileNames));
  }

  private File getDir(String dirPath) {
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

  private FileObject getFileObject(String path) {
    try {
      return ctx.getResources().getResource(path);
    } catch (Exception ignored) {
      // Ignore, in case the Filer implementation doesn't support
      // directory path.
      return null;
    }
  }

  private boolean isSuppressed(Suppress suppress, Message message) {
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

    private final ExecutableElement methodElement;

    private final SqlAnnot sqlAnnot;

    private SqlAnnotationCombinationValidator(ExecutableElement methodElement, SqlAnnot sqlAnnot) {
      assertNotNull(methodElement, sqlAnnot);
      this.methodElement = methodElement;
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
    public Void visitAutoMultiInsertQueryMeta(AutoMultiInsertQueryMeta m) {
      return handleConflict(m.getMultiInsertAnnot());
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
          methodElement,
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
          Message.DOMA4445, methodElement, annotationMirror, annotationValue, new Object[] {});
    }

    @Override
    public Void visitDefaultQueryMeta(DefaultQueryMeta m) {
      throw new AptException(
          Message.DOMA4446, methodElement, sqlAnnot.getAnnotationMirror(), new Object[] {});
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
