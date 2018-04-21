package org.seasar.doma.internal.apt.meta.dao;

import static java.util.stream.Collectors.toList;
import static javax.lang.model.util.ElementFilter.constructorsIn;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import org.seasar.doma.DaoMethod;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.DaoAnnot;
import org.seasar.doma.internal.apt.annot.SuppressAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.meta.query.ArrayCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoFunctionQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.AutoProcedureQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.BlobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.ClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.DefaultQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.NClobCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.NonAbstractQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.QueryMeta;
import org.seasar.doma.internal.apt.meta.query.QueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SQLXMLCreateQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlProcessorQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlTemplateBatchModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlTemplateModifyQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.SqlTemplateSelectQueryMetaFactory;
import org.seasar.doma.internal.apt.meta.query.StaticScriptQueryMetaFactory;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.AbstractDao;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.message.Message;

public class DaoMetaFactory implements TypeElementMetaFactory<DaoMeta> {

  private static final String SINGLETON_CONFIG_FIELD_NAME = "INSTANCE";

  private final Context ctx;

  private final TypeElement daoElement;

  private final DaoAnnot daoAnnot;

  private final SuppressAnnot suppressAnnot;

  private boolean error;

  public DaoMetaFactory(Context ctx, TypeElement interfaceElement) {
    assertNotNull(ctx, interfaceElement);
    this.ctx = ctx;
    this.daoElement = interfaceElement;
    daoAnnot = ctx.getAnnots().newDaoAnnot(interfaceElement);
    if (daoAnnot == null) {
      throw new AptIllegalStateException("daoAnnot");
    }
    suppressAnnot = ctx.getAnnots().newSuppressAnnot(interfaceElement);
  }

  @Override
  public DaoMeta createTypeElementMeta() {
    validateTypeElement();
    validateName();

    var daoMeta = new DaoMeta(daoAnnot, daoElement);

    var strategy = createStrategy(daoMeta);
    strategy.doAnnotateWith(daoMeta);
    strategy.doParentDao(daoMeta);
    strategy.doConfig(daoMeta);
    strategy.doConstructors(daoMeta);
    strategy.doMethods(daoMeta);
    strategy.validateFiles(daoMeta);
    return error ? null : daoMeta;
  }

  private void validateTypeElement() {
    if (!isInterface(daoElement) && !isAbstractClass(daoElement)) {
      throw new AptException(Message.DOMA4014, daoElement, daoAnnot.getAnnotationMirror());
    }
    if (daoElement.getNestingKind().isNested()) {
      throw new AptException(Message.DOMA4017, daoElement);
    }
    if (!daoElement.getTypeParameters().isEmpty()) {
      throw new AptException(Message.DOMA4059, daoElement);
    }
  }

  private boolean isInterface(TypeElement typeElement) {
    return typeElement.getKind().isInterface();
  }

  private boolean isAbstractClass(TypeElement typeElement) {
    return typeElement.getKind().isClass()
        && typeElement.getModifiers().contains(Modifier.ABSTRACT);
  }

  private void validateName() {
    var name = daoElement.getSimpleName().toString();
    var suffix = ctx.getOptions().getDaoSuffix();
    if (name.endsWith(suffix)) {
      ctx.getNotifier().send(Kind.WARNING, Message.DOMA4026, daoElement, new Object[] {suffix});
    }
  }

  private Strategy createStrategy(DaoMeta daoMeta) {
    if (daoMeta.isInterface()) {
      return new InterfaceStrategy();
    }
    return new ClassStrategy();
  }

  private interface Strategy {
    void doAnnotateWith(DaoMeta daoMeta);

    void doParentDao(DaoMeta daoMeta);

    void doConfig(DaoMeta daoMeta);

    void doConstructors(DaoMeta daoMeta);

    void doMethods(DaoMeta daoMeta);

    void validateFiles(DaoMeta daoMeta);
  }

  private abstract class AbstractStrategy implements Strategy {

    @Override
    public void doConfig(DaoMeta daoMeta) {
      if (!daoAnnot.hasUserDefinedConfig()) {
        return;
      }
      var configElement = ctx.getTypes().toTypeElement(daoAnnot.getConfigValue());
      if (configElement == null) {
        throw new AptIllegalStateException("failed to convert to TypeElement.");
      }
      var configMeta = createConfigMeta(configElement);
      daoMeta.setConfigMeta(configMeta);
    }

    private ConfigMeta createConfigMeta(TypeElement configElement) {
      var singletonConfig = configElement.getAnnotation(SingletonConfig.class);
      if (singletonConfig == null) {
        if (configElement.getModifiers().contains(Modifier.ABSTRACT)) {
          throw new AptException(
              Message.DOMA4163,
              daoElement,
              daoAnnot.getAnnotationMirror(),
              daoAnnot.getConfig(),
              new Object[] {configElement.getQualifiedName()});
        }
        var constructor = ctx.getElements().getNoArgConstructor(configElement);
        if (constructor != null && constructor.getModifiers().contains(Modifier.PUBLIC)) {
          return ConfigMeta.byConstructor(configElement.asType());
        }
        return ElementFilter.fieldsIn(configElement.getEnclosedElements())
            .stream()
            .filter(e -> e.getSimpleName().contentEquals(SINGLETON_CONFIG_FIELD_NAME))
            .filter(
                e ->
                    e.getModifiers()
                        .containsAll(EnumSet.of(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)))
            .filter(e -> ctx.getTypes().isAssignable(e.asType(), Config.class))
            .findAny()
            .map(f -> ConfigMeta.byField(configElement.asType(), f))
            .orElseThrow(
                () -> {
                  throw new AptException(
                      Message.DOMA4164,
                      daoElement,
                      daoAnnot.getAnnotationMirror(),
                      daoAnnot.getConfig(),
                      new Object[] {configElement.getQualifiedName()});
                });
      }
      var methodName = singletonConfig.method();
      return ElementFilter.methodsIn(configElement.getEnclosedElements())
          .stream()
          .filter(m -> m.getModifiers().containsAll(EnumSet.of(Modifier.STATIC, Modifier.PUBLIC)))
          .filter(m -> ctx.getTypes().isAssignable(m.getReturnType(), Config.class))
          .filter(m -> m.getParameters().isEmpty())
          .filter(m -> m.getSimpleName().toString().equals(methodName))
          .findAny()
          .map(m -> ConfigMeta.byMethod(configElement.asType(), m))
          .orElseThrow(
              () -> {
                throw new AptException(
                    Message.DOMA4255,
                    daoElement,
                    daoAnnot.getAnnotationMirror(),
                    daoAnnot.getConfig(),
                    new Object[] {configElement.getQualifiedName(), methodName});
              });
    }

    @Override
    public void doAnnotateWith(DaoMeta daoMeta) {
      var annotateWithAnnot = ctx.getAnnots().newAnnotateWithAnnot(daoElement);
      if (annotateWithAnnot != null) {
        daoMeta.setAnnotateWithAnnot(annotateWithAnnot);
      }
    }

    @Override
    public abstract void doParentDao(DaoMeta daoMeta);

    @Override
    public abstract void doConstructors(DaoMeta daoMeta);

    @Override
    public void doMethods(DaoMeta daoMeta) {
      for (var methodElement : ElementFilter.methodsIn(daoElement.getEnclosedElements())) {
        try {
          doMethod(methodElement, daoMeta);
        } catch (AptException e) {
          ctx.getNotifier().send(e);
          error = true;
        }
      }
    }

    private void doMethod(ExecutableElement methodElement, DaoMeta daoMeta) {
      var modifiers = methodElement.getModifiers();
      if (modifiers.contains(Modifier.STATIC)
          || modifiers.contains(Modifier.PRIVATE)
          || modifiers.contains(Modifier.FINAL)) {
        return;
      }
      validateMethod(methodElement, daoMeta);
      var queryMeta = createQueryMeta(methodElement, daoMeta);
      daoMeta.addQueryMeta(queryMeta);
    }

    private void validateMethod(ExecutableElement methodElement, DaoMeta daoMeta) {
      TypeElement foundAnnotationTypeElement = null;
      for (AnnotationMirror annotation : methodElement.getAnnotationMirrors()) {
        var declaredType = annotation.getAnnotationType();
        var typeElement = ctx.getTypes().toTypeElement(declaredType);
        if (typeElement.getAnnotation(DaoMethod.class) != null) {
          if (foundAnnotationTypeElement != null) {
            throw new AptException(
                Message.DOMA4087,
                methodElement,
                new Object[] {
                  foundAnnotationTypeElement.getQualifiedName(), typeElement.getQualifiedName()
                });
          }
          validateAnnotatedMethod(methodElement, typeElement);
          foundAnnotationTypeElement = typeElement;
        }
      }
    }

    protected abstract void validateAnnotatedMethod(
        ExecutableElement methodElement, TypeElement typeElement);

    private QueryMeta createQueryMeta(ExecutableElement methodElement, DaoMeta daoMeta) {
      for (var supplier : createQueryMetaFactories(methodElement)) {
        var factory = supplier.get();
        var queryMeta = factory.createQueryMeta();
        if (queryMeta != null) {
          return queryMeta;
        }
      }
      throw new AptException(Message.DOMA4005, methodElement, new Object[] {});
    }

    private List<Supplier<QueryMetaFactory>> createQueryMetaFactories(
        ExecutableElement methodElement) {
      return List.of(
          () -> new SqlTemplateSelectQueryMetaFactory(ctx, methodElement),
          () -> new AutoModifyQueryMetaFactory(ctx, methodElement),
          () -> new AutoBatchModifyQueryMetaFactory(ctx, methodElement),
          () -> new AutoFunctionQueryMetaFactory(ctx, methodElement),
          () -> new AutoProcedureQueryMetaFactory(ctx, methodElement),
          () -> new SqlTemplateModifyQueryMetaFactory(ctx, methodElement),
          () -> new SqlTemplateBatchModifyQueryMetaFactory(ctx, methodElement),
          () -> new StaticScriptQueryMetaFactory(ctx, methodElement),
          () -> new DefaultQueryMetaFactory(ctx, methodElement),
          () -> new NonAbstractQueryMetaFactory(ctx, methodElement),
          () -> new ArrayCreateQueryMetaFactory(ctx, methodElement),
          () -> new BlobCreateQueryMetaFactory(ctx, methodElement),
          () -> new ClobCreateQueryMetaFactory(ctx, methodElement),
          () -> new NClobCreateQueryMetaFactory(ctx, methodElement),
          () -> new SQLXMLCreateQueryMetaFactory(ctx, methodElement),
          () -> new SqlProcessorQueryMetaFactory(ctx, methodElement));
    }

    @Override
    public void validateFiles(DaoMeta daoMeta) {
      if (error) {
        return;
      }
      if (!ctx.getOptions().getSqlValidation()) {
        return;
      }
      var dirPath = SqlFileUtil.buildPath(daoElement.getQualifiedName().toString());
      var fileNames = getFileNames(dirPath);
      for (var queryMeta : daoMeta.getQueryMetas()) {
        for (var fileName : queryMeta.getFileNames()) {
          fileNames.remove(fileName);
        }
      }
      if (!isSuppressed(Message.DOMA4220)) {
        for (var fileName : fileNames) {
          ctx.getNotifier()
              .send(
                  Kind.WARNING,
                  Message.DOMA4220,
                  daoElement,
                  new Object[] {dirPath + "/" + fileName});
        }
      }
    }

    private Set<String> getFileNames(String dirPath) {
      var dir = getDir(dirPath);
      if (dir == null) {
        return Collections.emptySet();
      }
      var fileNames =
          dir.list(
              (__, name) ->
                  name.endsWith(Constants.SQL_PATH_SUFFIX)
                      || name.endsWith(Constants.SCRIPT_PATH_SUFFIX));
      if (fileNames == null) {
        return Collections.emptySet();
      }
      return new HashSet<>(Arrays.asList(fileNames));
    }

    private File getDir(String dirPath) {
      var fileObject = getFileObject(dirPath);
      if (fileObject == null) {
        return null;
      }
      var uri = fileObject.toUri();
      if (!uri.isAbsolute()) {
        uri = new File(".").toURI().resolve(uri);
      }
      var dir = new File(uri);
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

    private boolean isSuppressed(Message message) {
      if (suppressAnnot != null) {
        return suppressAnnot.isSuppressed(message);
      }
      return false;
    }
  }

  private class InterfaceStrategy extends AbstractStrategy {

    @Override
    public void doParentDao(DaoMeta daoMeta) {
      var interfaces =
          daoElement
              .getInterfaces()
              .stream()
              .map(ctx.getTypes()::toTypeElement)
              .peek(
                  element -> {
                    if (element == null) {
                      throw new AptIllegalStateException("failed to convert to TypeElement.");
                    }
                  })
              .collect(toList());
      for (var typeElement : interfaces) {
        var daoAnnot = ctx.getAnnots().newDaoAnnot(typeElement);
        if (daoAnnot == null) {
          var nonDefaultMethod = findNonDefaultMethod(typeElement);
          if (nonDefaultMethod == null) {
            continue;
          }
          throw new AptException(
              Message.DOMA4440, daoElement, new Object[] {nonDefaultMethod.getSimpleName()});
        }
        if (daoMeta.getParentDaoMeta() != null) {
          throw new AptException(Message.DOMA4188, daoElement);
        }
        var parentDaoMeta = new ParentDaoMeta(daoAnnot, typeElement);
        daoMeta.setParentDaoMeta(parentDaoMeta);
      }
    }

    private ExecutableElement findNonDefaultMethod(TypeElement interfaceElement) {
      var method =
          ElementFilter.methodsIn(interfaceElement.getEnclosedElements())
              .stream()
              .filter(m -> !m.isDefault())
              .findAny();
      if (method.isPresent()) {
        return method.get();
      }
      for (TypeMirror typeMirror : interfaceElement.getInterfaces()) {
        var i = ctx.getTypes().toTypeElement(typeMirror);
        if (i == null) {
          throw new AptIllegalStateException("failed to convert to TypeElement.");
        }
        var m = findNonDefaultMethod(i);
        if (m != null) {
          return m;
        }
      }
      return null;
    }

    @Override
    public void doConstructors(DaoMeta daoMeta) {
      // do nothing
    }

    @Override
    protected void validateAnnotatedMethod(
        ExecutableElement methodElement, TypeElement typeElement) {
      if (methodElement.isDefault()) {
        throw new AptException(
            Message.DOMA4252, methodElement, new Object[] {typeElement.getQualifiedName()});
      }
    }
  }

  private class ClassStrategy extends AbstractStrategy {

    @Override
    public void doParentDao(DaoMeta daoMeta) {
      var superclass = daoElement.getSuperclass();
      if (!ctx.getTypes().isSameType(superclass, AbstractDao.class)) {
        throw new AptException(Message.DOMA4445, daoElement, new Object[] {});
      }
    }

    @Override
    public void doConstructors(DaoMeta daoMeta) {
      var constructors = constructorsIn(daoMeta.getDaoElement().getEnclosedElements());
      var hasValidConstructor =
          constructors
              .stream()
              .allMatch(
                  e -> {
                    if (e.getParameters().size() != 1) {
                      return false;
                    }
                    var parameter = e.getParameters().iterator().next();
                    return ctx.getTypes().isSameType(parameter.asType(), Config.class);
                  });
      if (!hasValidConstructor) {
        throw new AptException(Message.DOMA4446, daoElement, new Object[] {});
      }
    }

    @Override
    protected void validateAnnotatedMethod(
        ExecutableElement methodElement, TypeElement typeElement) {
      if (!methodElement.getModifiers().contains(Modifier.ABSTRACT)) {
        throw new AptException(
            Message.DOMA4447, methodElement, new Object[] {typeElement.getQualifiedName()});
      }
    }
  }
}
