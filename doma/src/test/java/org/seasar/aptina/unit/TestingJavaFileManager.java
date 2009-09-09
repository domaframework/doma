package org.seasar.aptina.unit;

import static org.seasar.aptina.commons.util.CollectionUtils.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.JavaFileObject.Kind;

class TestingJavaFileManager extends
        ForwardingJavaFileManager<StandardJavaFileManager> {

    final Map<String, InMemoryJavaFileObject> fileObjects = newHashMap();

    final Map<String, InMemoryJavaFileObject> javaFileObjects = newHashMap();

    final Charset charset;

    /**
     * インスタンスを構築します。
     * 
     * @param fileManager
     *            移譲先となる{@link StandardJavaFileManager}
     * @param charset
     *            文字セット
     */
    public TestingJavaFileManager(final StandardJavaFileManager fileManager,
            final Charset charset) {
        super(fileManager);
        this.charset = charset;
    }

    @Override
    public FileObject getFileForInput(final Location location,
            final String packageName, final String relativeName)
            throws IOException {
        final String key = packageName + "::" + relativeName;
        if (fileObjects.containsKey(key)) {
            return fileObjects.get(key);
        }
        return super.getFileForInput(location, packageName, relativeName);
    }

    @Override
    public FileObject getFileForOutput(final Location location,
            final String packageName, final String relativeName,
            final FileObject sibling) throws IOException {
        if (sibling == null) {
            return super.getFileForOutput(location, packageName, relativeName,
                    sibling);
        }
        final InMemoryJavaFileObject fileObject = new InMemoryJavaFileObject(
                toURI(location, packageName, relativeName), Kind.OTHER, charset);
        fileObjects.put(packageName + "::" + relativeName, fileObject);
        return fileObject;
    }

    @Override
    public JavaFileObject getJavaFileForInput(final Location location,
            final String className, final Kind kind) throws IOException {
        final String key = kind.name() + "::" + className;
        if (fileObjects.containsKey(key)) {
            return fileObjects.get(key);
        }
        return super.getJavaFileForInput(location, className, kind);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(final Location location,
            final String className, final Kind kind, final FileObject sibling)
            throws IOException {
        final String key = kind.name() + "::" + className;
        final InMemoryJavaFileObject fileObject = new InMemoryJavaFileObject(
                toURI(location, className), kind, charset);
        fileObjects.put(key, fileObject);
        return fileObject;
    }

    /**
     * 生成された {@link JavaFileObject} を返します．
     * 
     * @param location
     *            ロケーション
     * @param className
     *            クラスの完全限定名
     * @param kind
     *            ファイルの種類
     * @return 生成された {@link JavaFileObject}
     * @throws IOException
     *             入出力例外が発生した場合
     */
    public JavaFileObject getGeneratedJavaFile(final Location location,
            final String className, final Kind kind) throws IOException {
        final String key = kind.name() + "::" + className;
        if (fileObjects.containsKey(key)) {
            return fileObjects.get(key);
        }
        return null;
    }

    URI toURI(final Location location, final String packageName,
            final String relativeName) {
        try {
            return new URI(location.getName() + "/"
                    + packageName.replace('.', '/') + "/" + relativeName);
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    URI toURI(final Location location, final String className) {
        try {
            return new URI(location.getName() + "/"
                    + className.replace('.', '/') + ".java");
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
