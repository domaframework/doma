package org.seasar.doma.internal.apt.meta.dao;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

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
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
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
import org.seasar.doma.internal.apt.annot.AnnotateWithAnnot;
import org.seasar.doma.internal.apt.annot.DaoAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.meta.query.QueryMeta;
import org.seasar.doma.internal.apt.meta.query.QueryMetaFactory;
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
      ctx.getNotifier()
          .notify(
              Kind.WARNING,
              Message.DOMA4026,
              interfaceElement,
              new Object[] {suffix, interfaceElement.getQualifiedName()});
    }
    daoMeta.setName(name);
    daoMeta.setDaoElement(interfaceElement);
    daoMeta.setDaoType(interfaceElement.asType());
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
            daoMeta.getDaoElement(),
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
                .filter(e -> ctx.getTypes().isAssignable(e.asType(), Config.class))
                .findFirst();
        if (field.isPresent()) {
          daoMeta.setSingletonFieldName(SINGLETON_CONFIG_FIELD_NAME);
        } else {
          throw new AptException(
              Message.DOMA4164,
              daoMeta.getDaoElement(),
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
              .filter(m -> ctx.getTypes().isAssignable(m.getReturnType(), Config.class))
              .filter(m -> m.getParameters().isEmpty())
              .filter(m -> m.getSimpleName().toString().equals(methodName))
              .findAny()
              .isPresent();
      if (present) {
        daoMeta.setSingletonMethodName(methodName);
      } else {
        throw new AptException(
            Message.DOMA4255,
            daoMeta.getDaoElement(),
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
          Message.DOMA4014,
          interfaceElement,
          daoAnnot.getAnnotationMirror(),
          new Object[] {interfaceElement.getQualifiedName()});
    }
    if (interfaceElement.getNestingKind().isNested()) {
      throw new AptException(
          Message.DOMA4017, interfaceElement, new Object[] {interfaceElement.getQualifiedName()});
    }
    if (!interfaceElement.getTypeParameters().isEmpty()) {
      throw new AptException(
          Message.DOMA4059, interfaceElement, new Object[] {interfaceElement.getQualifiedName()});
    }
  }

  protected void doAnnotateWith(DaoMeta daoMeta) {
    AnnotateWithAnnot annotateWithAnnot =
        ctx.getAnnotations().newAnnotateWithAnnot(daoMeta.getDaoElement());
    if (annotateWithAnnot != null) {
      daoMeta.setAnnotateWithAnnot(annotateWithAnnot);
    }
  }

  protected void doParentDao(DaoMeta daoMeta) {
    List<TypeElement> interfaces =
        daoMeta
            .getDaoElement()
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
            daoMeta.getDaoElement(),
            new Object[] {
              nonDefaultMethod.getSimpleName(), daoMeta.getDaoElement().getQualifiedName()
            });
      }
      if (daoMeta.getParentDaoMeta() != null) {
        throw new AptException(
            Message.DOMA4188,
            daoMeta.getDaoElement(),
            new Object[] {daoMeta.getDaoElement().getQualifiedName()});
      }
      ParentDaoMeta parentDaoMeta = new ParentDaoMeta(daoAnnot);
      parentDaoMeta.setDaoType(typeElement.asType());
      parentDaoMeta.setDaoElement(typeElement);
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
        ctx.getNotifier().notify(e);
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
                foundAnnotationTypeElement.getQualifiedName(),
                typeElement.getQualifiedName(),
                daoMeta.getDaoElement().getQualifiedName(),
                methodElement.getSimpleName()
              });
        }
        if (methodElement.isDefault()) {
          throw new AptException(
              Message.DOMA4252,
              methodElement,
              new Object[] {
                typeElement.getQualifiedName(),
                daoMeta.getDaoElement().getQualifiedName(),
                methodElement.getSimpleName()
              });
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
    throw new AptException(
        Message.DOMA4005,
        method,
        new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
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
        ctx.getNotifier()
            .notify(
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
}
