/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.aptina.unit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;

import junit.framework.ComparisonFailure;
import junit.framework.TestCase;

import org.seasar.aptina.commons.util.AssertionUtils;
import org.seasar.aptina.commons.util.DiagnosticUtils;
import org.seasar.aptina.commons.util.ElementUtils;
import org.seasar.aptina.commons.util.TypeMirrorUtils;

import static java.util.Arrays.*;

import static org.seasar.aptina.commons.util.AssertionUtils.*;
import static org.seasar.aptina.commons.util.CollectionUtils.*;
import static org.seasar.aptina.commons.util.IOUtils.*;

/**
 * {@link Processor} をテストするための抽象クラスです．
 * <p>
 * サブクラスのテストメソッドでは，コンパイルオプションやコンパイル対象などを設定して {@link #compile()} メソッドを呼び出します．
 * コンパイル後に生成されたソースの検証などを行うことができます．
 * </p>
 * <dl>
 * <dt>{@link #compile()} 前に以下のメソッドを呼び出すことができます ({@link #setUp()}から呼び出すこともできます)．
 * </dt>
 * <dd>
 * <ul>
 * <li>{@link #setLocale(Locale)}</li>
 * <li>{@link #setLocale(String)}</li>
 * <li>{@link #setCharset(Charset)}</li>
 * <li>{@link #setCharset(String)}</li>
 * <li>{@link #setOut(Writer)}</li>
 * <li>{@link #addSourcePath(File...)}</li>
 * <li>{@link #addSourcePath(String...)}</li>
 * <li>{@link #addOption(String...)}</li>
 * <li>{@link #addProcessor(Processor...)}</li>
 * <li>{@link #addCompilationUnit(Class)}</li>
 * <li>{@link #addCompilationUnit(String)}</li>
 * <li>{@link #addCompilationUnit(Class, CharSequence)}</li>
 * <li>{@link #addCompilationUnit(String, CharSequence)}</li>
 * </ul>
 * </dd>
 * <dt>{@link #compile()} 後に以下のメソッドを呼び出して情報を取得することができます．</dt>
 * <dd>
 * <ul>
 * <li>{@link #getCompiledResult()}</li>
 * <li>{@link #getDiagnostics()}</li>
 * <li>{@link #getDiagnostics(Class)}</li>
 * <li>{@link #getDiagnostics(String)}</li>
 * <li>{@link #getDiagnostics(javax.tools.Diagnostic.Kind)}</li>
 * <li>{@link #getDiagnostics(Class, javax.tools.Diagnostic.Kind)}</li>
 * <li>{@link #getDiagnostics(String, javax.tools.Diagnostic.Kind)}</li>
 * <li>{@link #getProcessingEnvironment()}</li>
 * <li>{@link #getElementUtils()}</li>
 * <li>{@link #getTypeUtils()}</li>
 * <li>{@link #getTypeElement(Class)}</li>
 * <li>{@link #getTypeElement(String)}</li>
 * <li>{@link #getFieldElement(TypeElement, Field)}</li>
 * <li>{@link #getFieldElement(TypeElement, String)}</li>
 * <li>{@link #getConstructorElement(TypeElement)}</li>
 * <li>{@link #getConstructorElement(TypeElement, Class...)}</li>
 * <li>{@link #getConstructorElement(TypeElement, String...)}</li>
 * <li>{@link #getMethodElement(TypeElement, String)}</li>
 * <li>{@link #getMethodElement(TypeElement, String, Class...)}</li>
 * <li>{@link #getMethodElement(TypeElement, String, String...)}</li>
 * <li>{@link #getTypeMirror(Class)}</li>
 * <li>{@link #getTypeMirror(String)}</li>
 * <li>{@link #getGeneratedSource(Class)}</li>
 * <li>{@link #getGeneratedSource(String)}</li>
 * </ul>
 * </dd>
 * <dt>{@link #compile()} 後に以下のメソッドを呼び出して生成されたソースの内容を検証することができます．</dt>
 * <dd>
 * <ul>
 * <li>{@link #assertEqualsGeneratedSource(CharSequence, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSource(CharSequence, String)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithFile(File, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithFile(File, String)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithFile(String, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithFile(String, String)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithResource(URL, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithResource(URL, String)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithResource(String, Class)}</li>
 * <li>{@link #assertEqualsGeneratedSourceWithResource(String, String)}</li>
 * </ul>
 * </dd>
 * </dl>
 * <p>
 * {@link #compile()} を呼び出した後に状態をリセットしてコンパイル前の状態に戻すには， {@link #reset()} を呼び出します．
 * </p>
 * 
 * <p>
 * 次のサンプルは， {@code src/test/java} フォルダにある {@code TestSource.java} をコンパイルすると，
 * {@code foo.bar.Baz} クラスのソースを生成する {@code TestProcessor} のテストクラスです．
 * </p>
 * 
 * <pre>
 * public class TestProcessorTest extends AptinaTestCase {
 *
 *     &#x40;Override
 *     protected void setUp() throws Exception {
 *         super.setUp();
 *         // ソースパスを追加
 *         addSourcePath("src/test/java");
 *     }
 *
 *     public void test() throws Exception {
 *         // テスト対象の Annotation Processor を生成して追加
 *         TestProcessor processor = new TestProcessor();
 *         addProcessor(processor);
 *
 *         // コンパイル対象を追加
 *         addCompilationUnit(TestSource.class);
 *
 *         // コンパイル実行
 *         compile();
 *
 *         // テスト対象の Annotation Processor が生成したソースを検証
 *         assertEqualsGeneratedSource("package foo.bar; public class Baz {}",
 *                 "foo.bar.Baz");
 *     }
 * 
 * }
 * </pre>
 * 
 * @author koichik
 */
public abstract class AptinaTestCase extends TestCase {

    Locale locale;

    Charset charset;

    Writer out;

    final List<String> options = newArrayList();

    final List<File> sourcePaths = newArrayList();

    final List<Processor> processors = newArrayList();
    {
        processors.add(new AptinaUnitProcessor());
    }

    final List<CompilationUnit> compilationUnits = newArrayList();

    JavaCompiler javaCompiler;

    DiagnosticCollector<JavaFileObject> diagnostics;

    StandardJavaFileManager standardJavaFileManager;

    JavaFileManager testingJavaFileManager;

    ProcessingEnvironment processingEnvironment;

    Boolean compiledResult;

    /**
     * インスタンスを構築します．
     */
    protected AptinaTestCase() {
    }

    /**
     * インスタンスを構築します．
     * 
     * @param name
     *            名前
     */
    protected AptinaTestCase(final String name) {
        super(name);
    }

    @Override
    protected void tearDown() throws Exception {
        if (testingJavaFileManager != null) {
            try {
                testingJavaFileManager.close();
            } catch (final Exception ignore) {
            }
        }
        super.tearDown();
    }

    /**
     * ロケールを返します．
     * 
     * @return ロケールまたは {@code null}
     */
    protected Locale getLocale() {
        return locale;
    }

    /**
     * ロケールを設定します．
     * <p>
     * 設定されなかった場合はプラットフォームデフォルトのロケールが使われます．
     * </p>
     * 
     * @param locale
     *            ロケール
     * @see Locale#getDefault()
     */
    protected void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * ロケールを設定します．
     * <p>
     * 設定されなかった場合はプラットフォームデフォルトのロケールが使われます．
     * </p>
     * 
     * @param locale
     *            ロケール
     * @see Locale#getDefault()
     */
    protected void setLocale(final String locale) {
        assertNotEmpty("locale", locale);
        setLocale(new Locale(locale));
    }

    /**
     * 文字セットを返します．
     * 
     * @return 文字セットまたは {@code null}
     */
    protected Charset getCharset() {
        return charset;
    }

    /**
     * 文字セットを設定します．
     * <p>
     * 設定されなかった場合はプラットフォームデフォルトの文字セットが使われます．
     * </p>
     * 
     * @param charset
     *            文字セット
     * @see Charset#defaultCharset()
     */
    protected void setCharset(final Charset charset) {
        this.charset = charset;
    }

    /**
     * 文字セットを設定します．
     * <p>
     * 設定されなかった場合はプラットフォームデフォルトの文字セットが使われます．
     * </p>
     * 
     * @param charset
     *            文字セット
     * @see Charset#defaultCharset()
     */
    protected void setCharset(final String charset) {
        assertNotEmpty("charset", charset);
        setCharset(Charset.forName(charset));
    }

    /**
     * コンパイラがメッセージを出力する{@link Writer}を設定します．
     * <p>
     * 設定されなかった場合は標準エラーが使われます．
     * </p>
     * 
     * @param out
     *            コンパイラがメッセージを出力する{@link Writer}
     */
    protected void setOut(final Writer out) {
        this.out = out;
    }

    /**
     * コンパイル時に参照するソースパスを追加します．
     * 
     * @param sourcePaths
     *            コンパイル時に参照するソースパスの並び
     */
    protected void addSourcePath(final File... sourcePaths) {
        assertNotEmpty("sourcePaths", sourcePaths);
        this.sourcePaths.addAll(asList(sourcePaths));
    }

    /**
     * コンパイル時に参照するソースパスを追加します．
     * 
     * @param sourcePaths
     *            コンパイル時に参照するソースパスの並び
     */
    protected void addSourcePath(final String... sourcePaths) {
        assertNotEmpty("sourcePaths", sourcePaths);
        for (final String path : sourcePaths) {
            this.sourcePaths.add(new File(path));
        }
    }

    /**
     * コンパイラオプションを追加します．
     * 
     * @param options
     *            形式のコンパイラオプションの並び
     */
    protected void addOption(final String... options) {
        assertNotEmpty("options", options);
        this.options.addAll(asList(options));
    }

    /**
     * 注釈を処理する{@link Processor}を追加します．
     * 
     * @param processors
     *            注釈を処理する{@link Processor}の並び
     */
    protected void addProcessor(final Processor... processors) {
        assertNotEmpty("processors", processors);
        this.processors.addAll(asList(processors));
    }

    /**
     * コンパイル対象のクラスを追加します．
     * <p>
     * 指定されたクラスのソースはソースパス上に存在していなければなりません．
     * </p>
     * 
     * @param clazz
     *            コンパイル対象クラス
     */
    protected void addCompilationUnit(final Class<?> clazz) {
        AssertionUtils.assertNotNull("clazz", clazz);
        addCompilationUnit(clazz.getName());
    }

    /**
     * コンパイル対象のクラスを追加します．
     * <p>
     * 指定されたクラスのソースはソースパス上に存在していなければなりません．
     * </p>
     * 
     * @param className
     *            コンパイル対象クラスの完全限定名
     */
    protected void addCompilationUnit(final String className) {
        assertNotEmpty("className", className);
        compilationUnits.add(new FileCompilationUnit(className));
    }

    /**
     * コンパイル対象のクラスをソースとともに追加します．
     * 
     * @param clazz
     *            コンパイル対象クラス
     * @param source
     *            ソース
     */
    protected void addCompilationUnit(final Class<?> clazz,
            final CharSequence source) {
        AssertionUtils.assertNotNull("clazz", clazz);
        assertNotEmpty("source", source);
        addCompilationUnit(clazz.getName(), source);
    }

    /**
     * コンパイル対象のクラスをソースとともに追加します．
     * 
     * @param className
     *            コンパイル対象クラスの完全限定名
     * @param source
     *            ソース
     */
    protected void addCompilationUnit(final String className,
            final CharSequence source) {
        assertNotEmpty("className", className);
        assertNotEmpty("source", source);
        compilationUnits.add(new InMemoryCompilationUnit(className, source
            .toString()));
    }

    /**
     * コンパイルを実行します．
     * 
     * @throws IOException
     *             入出力例外が発生した場合
     */
    protected void compile() throws IOException {
        javaCompiler = ToolProvider.getSystemJavaCompiler();
        diagnostics = new DiagnosticCollector<JavaFileObject>();
        final DiagnosticListener<JavaFileObject> listener = new LoggingDiagnosticListener(
            diagnostics);

        standardJavaFileManager = javaCompiler.getStandardFileManager(
            listener,
            locale,
            charset);
        standardJavaFileManager.setLocation(
            StandardLocation.SOURCE_PATH,
            sourcePaths);
        testingJavaFileManager = new TestingJavaFileManager(
            standardJavaFileManager,
            charset);

        final CompilationTask task = javaCompiler.getTask(
            out,
            testingJavaFileManager,
            listener,
            options,
            null,
            getCompilationUnits());
        task.setProcessors(processors);
        compiledResult = task.call();
        compilationUnits.clear();
    }

    /**
     * コンパイラの実行結果を返します．
     * 
     * @return コンパイラの実行結果
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @see CompilationTask#call()
     */
    protected Boolean getCompiledResult() throws IllegalStateException {
        assertCompiled();
        return compiledResult;
    }

    /**
     * コンパイル中に作成された {@link Diagnostic} のリストを返します．
     * 
     * @return コンパイル中に作成された {@link Diagnostic} のリスト
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics()
            throws IllegalStateException {
        assertCompiled();
        return diagnostics.getDiagnostics();
    }

    /**
     * 指定されたクラスに対する {@link Diagnostic} のリストを返します．
     * 
     * @param clazz
     *            取得するクラス
     * @return 指定されたクラスに対する {@link Diagnostic} のリスト
     */
    protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
            final Class<?> clazz) {
        assertCompiled();
        return DiagnosticUtils.getDiagnostics(getDiagnostics(), clazz);
    }

    /**
     * 指定されたクラスに対する {@link Diagnostic} のリストを返します．
     * 
     * @param className
     *            取得するクラス名
     * @return 指定されたクラスに対する {@link Diagnostic} のリスト
     */
    protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
            final String className) {
        assertCompiled();
        return DiagnosticUtils.getDiagnostics(getDiagnostics(), className);
    }

    /**
     * 指定された {@link javax.tools.Diagnostic.Kind} を持つ {@link Diagnostic}
     * のリストを返します．
     * 
     * @param kind
     *            取得する {@link javax.tools.Diagnostic.Kind}
     * @return 指定された{@link javax.tools.Diagnostic.Kind} を持つ {@link Diagnostic}
     *         のリスト
     */
    protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
            final javax.tools.Diagnostic.Kind kind) {
        assertCompiled();
        return DiagnosticUtils.getDiagnostics(getDiagnostics(), kind);
    }

    /**
     * 指定されたクラスに対する指定された {@link javax.tools.Diagnostic.Kind} を持つ
     * {@link Diagnostic} のリストを返します．
     * 
     * @param clazz
     *            取得するクラス
     * @param kind
     *            取得する {@link javax.tools.Diagnostic.Kind}
     * @return 指定されたクラスに対する指定された {@link javax.tools.Diagnostic.Kind} を持つ
     *         {@link Diagnostic} のリスト
     */
    protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
            final Class<?> clazz, final javax.tools.Diagnostic.Kind kind) {
        assertCompiled();
        return DiagnosticUtils.getDiagnostics(getDiagnostics(), clazz, kind);
    }

    /**
     * 指定されたクラスに対する指定された {@link javax.tools.Diagnostic.Kind} を持つ
     * {@link Diagnostic} のリストを返します．
     * 
     * @param className
     *            取得するクラス名
     * @param kind
     *            取得する {@link javax.tools.Diagnostic.Kind}
     * @return 指定されたクラスに対する指定された {@link javax.tools.Diagnostic.Kind} を持つ
     *         {@link Diagnostic} のリスト
     */
    protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
            final String className, final javax.tools.Diagnostic.Kind kind) {
        assertCompiled();
        return DiagnosticUtils
            .getDiagnostics(getDiagnostics(), className, kind);
    }

    /**
     * {@link ProcessingEnvironment} を返します．
     * 
     * @return {@link ProcessingEnvironment}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ProcessingEnvironment getProcessingEnvironment()
            throws IllegalStateException {
        assertCompiled();
        return processingEnvironment;
    }

    /**
     * {@link Elements} を返します．
     * 
     * @return {@link Elements}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @see ProcessingEnvironment#getElementUtils()
     */
    protected Elements getElementUtils() throws IllegalStateException {
        assertCompiled();
        return processingEnvironment.getElementUtils();
    }

    /**
     * {@link Types} を返します．
     * 
     * @return {@link Types}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @see ProcessingEnvironment#getTypeUtils()
     */
    protected Types getTypeUtils() throws IllegalStateException {
        assertCompiled();
        return processingEnvironment.getTypeUtils();
    }

    /**
     * クラスに対応する {@link TypeElement} を返します．
     * <p>
     * このメソッドが返す {@link TypeElement} およびその {@link Element#getEnclosedElements()}
     * が返す {@link Element} から， {@link Elements#getDocComment(Element)} を使って
     * Javadoc コメントを取得することはできません．
     * </p>
     * 
     * @param clazz
     *            クラス
     * @return クラスに対応する{@link TypeElement}， 存在しない場合は {@code null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected TypeElement getTypeElement(final Class<?> clazz)
            throws IllegalStateException {
        assertCompiled();
        return ElementUtils.getTypeElement(getElementUtils(), clazz.getName());
    }

    /**
     * クラスに対応する {@link TypeElement} を返します．
     * <p>
     * このメソッドが返す {@link TypeElement} およびその {@link Element#getEnclosedElements()}
     * が返す {@link Element} から， {@link Elements#getDocComment(Element)} を使って
     * Javadoc コメントを取得することはできません．
     * </p>
     * 
     * @param className
     *            クラスの完全限定名
     * @return クラスに対応する{@link TypeElement}， 存在しない場合は {@code null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected TypeElement getTypeElement(final String className)
            throws IllegalStateException {
        assertCompiled();
        return ElementUtils.getTypeElement(getElementUtils(), className);
    }

    /**
     * 型エレメントに定義されたフィールドの変数エレメントを返します．
     * 
     * @param typeElement
     *            型エレメント
     * @param field
     *            フィールド
     * @return 型エレメントに定義されたフィールドの変数エレメント． 存在しない場合は {@code null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected VariableElement getFieldElement(final TypeElement typeElement,
            final Field field) throws IllegalStateException {
        assertCompiled();
        return ElementUtils.getFieldElement(typeElement, field);
    }

    /**
     * 型エレメントに定義されたフィールドの変数エレメントを返します．
     * 
     * @param typeElement
     *            型エレメント
     * @param fieldName
     *            フィールド名
     * @return 型エレメントに定義されたフィールドの変数エレメント． 存在しない場合は {@code null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected VariableElement getFieldElement(final TypeElement typeElement,
            final String fieldName) throws IllegalStateException {
        assertCompiled();
        return ElementUtils.getFieldElement(typeElement, fieldName);
    }

    /**
     * 型エレメントに定義されたデフォルトコンストラクタの実行可能エレメントを返します．
     * 
     * @param typeElement
     *            型エレメント
     * @return 型エレメントに定義されたデフォルトコンストラクタの実行可能エレメント． 存在しない場合は {@code null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ExecutableElement getConstructorElement(
            final TypeElement typeElement) throws IllegalStateException {
        assertCompiled();
        return ElementUtils.getConstructorElement(typeElement);
    }

    /**
     * 型エレメントに定義されたコンストラクタの実行可能エレメントを返します．
     * <p>
     * 引数型が型引数を持つ場合は {@link #getConstructorElement(TypeElement, String...)}
     * を使用してください．
     * </p>
     * 
     * @param typeElement
     *            型エレメント
     * @param parameterTypes
     *            引数型の並び
     * @return 型エレメントに定義されたコンストラクタの実行可能エレメント． 存在しない場合は {@code null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ExecutableElement getConstructorElement(
            final TypeElement typeElement, final Class<?>... parameterTypes)
            throws IllegalStateException {
        assertCompiled();
        return ElementUtils.getConstructorElement(typeElement, parameterTypes);
    }

    /**
     * 型エレメントに定義されたコンストラクタの実行可能エレメントを返します．
     * <p>
     * 引数がの型が配列の場合は， 要素型の名前の後に {@code []} を連ねる形式と， {@code [[LString;}
     * のような形式のどちらでも指定することができます．
     * </p>
     * <p>
     * 引数型が型引数を持つ場合は {@code "java.util.List&lt;T&gt;"} のようにそのまま指定します．
     * </p>
     * 
     * @param typeElement
     *            型エレメント
     * @param parameterTypeNames
     *            引数の型名の並び
     * @return 型エレメントに定義されたコンストラクタの実行可能エレメント． 存在しない場合は {@code null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ExecutableElement getConstructorElement(
            final TypeElement typeElement, final String... parameterTypeNames)
            throws IllegalStateException {
        assertCompiled();
        return ElementUtils.getConstructorElement(
            typeElement,
            parameterTypeNames);
    }

    /**
     * 型エレメントに定義されたメソッドの実行可能エレメントを返します．
     * 
     * @param typeElement
     *            型エレメント
     * @param methodName
     *            メソッド名
     * @return 型エレメントに定義されたメソッドの実行可能エレメント．存在しない場合は {@code null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ExecutableElement getMethodElement(final TypeElement typeElement,
            final String methodName) throws IllegalStateException {
        assertCompiled();
        return ElementUtils.getMethodElement(typeElement, methodName);
    }

    /**
     * 型エレメントに定義されたメソッドの実行可能エレメントを返します．
     * <p>
     * 引数型が型引数を持つ場合は {@link #getMethodElement(TypeElement, String, String...)}
     * を使用してください．
     * </p>
     * 
     * @param typeElement
     *            型エレメント
     * @param methodName
     *            メソッド名
     * @param parameterTypes
     *            引数型の並び
     * @return 型エレメントに定義されたメソッドの実行可能エレメント． 存在しない場合は {@code null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ExecutableElement getMethodElement(final TypeElement typeElement,
            final String methodName, final Class<?>... parameterTypes)
            throws IllegalStateException {
        assertCompiled();
        return ElementUtils.getMethodElement(
            typeElement,
            methodName,
            parameterTypes);
    }

    /**
     * 型エレメントに定義されたメソッドの実行可能エレメントを返します．
     * <p>
     * 引数がの型が配列の場合は， 要素型の名前の後に {@code []} を連ねる形式と， {@code [[LString;}
     * のような形式のどちらでも指定することができます．
     * </p>
     * <p>
     * 引数型が型引数を持つ場合は {@code "java.util.List&lt;T&gt;"} のようにそのまま指定します．
     * </p>
     * 
     * @param typeElement
     *            型エレメント
     * @param methodName
     *            メソッド名
     * @param parameterTypeNames
     *            引数の型名の並び
     * @return 型エレメントに定義されたメソッドの実行可能エレメント． 存在しない場合は {@code null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected ExecutableElement getMethodElement(final TypeElement typeElement,
            final String methodName, final String... parameterTypeNames)
            throws IllegalStateException {
        assertCompiled();
        return ElementUtils.getMethodElement(
            typeElement,
            methodName,
            parameterTypeNames);
    }

    /**
     * クラスに対応する {@link TypeMirror} を返します．
     * 
     * @param clazz
     *            クラス
     * @return クラスに対応する{@link TypeMirror}， クラスが存在しない場合は {@code null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected TypeMirror getTypeMirror(final Class<?> clazz)
            throws IllegalStateException {
        assertCompiled();
        return TypeMirrorUtils.getTypeMirror(
            getTypeUtils(),
            getElementUtils(),
            clazz);
    }

    /**
     * クラスに対応する {@link TypeMirror} を返します．
     * <p>
     * 配列の場合は要素型の名前の後に {@code []} を連ねる形式と， {@code [[LString;} のような
     * 形式のどちらでも指定することができます．
     * </p>
     * 
     * @param className
     *            クラスの完全限定名
     * @return クラスに対応する{@link TypeMirror}， クラスが存在しない場合は {@code null}
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    protected TypeMirror getTypeMirror(final String className)
            throws IllegalStateException {
        assertCompiled();
        return TypeMirrorUtils.getTypeMirror(
            getTypeUtils(),
            getElementUtils(),
            className);
    }

    /**
     * {@link Processor} が生成したソースを返します．
     * 
     * @param clazz
     *            生成されたクラス
     * @return 生成されたソースの内容
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     */
    protected String getGeneratedSource(final Class<?> clazz)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException {
        assertNotNull("clazz", clazz);
        assertCompiled();
        return getGeneratedSource(clazz.getName());
    }

    /**
     * {@link Processor} が生成したソースを返します．
     * 
     * @param className
     *            生成されたクラスの完全限定名
     * @return 生成されたソースの内容
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     */
    protected String getGeneratedSource(final String className)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException {
        assertNotEmpty("className", className);
        assertCompiled();
        final JavaFileObject javaFileObject = testingJavaFileManager
            .getJavaFileForInput(
                StandardLocation.SOURCE_OUTPUT,
                className,
                Kind.SOURCE);
        if (javaFileObject == null) {
            throw new SourceNotGeneratedException(className);
        }
        final CharSequence content = javaFileObject.getCharContent(true);
        if (content == null) {
            throw new SourceNotGeneratedException(className);
        }
        return content.toString();
    }

    /**
     * 文字列を行単位で比較します．
     * 
     * @param expected
     *            期待される文字列
     * @param actual
     *            実際の文字列
     */
    protected void assertEqualsByLine(final String expected, final String actual) {
        if (expected == null || actual == null) {
            assertEquals(expected, actual);
            return;
        }
        final BufferedReader expectedReader = new BufferedReader(
            new StringReader(expected.toString()));
        final BufferedReader actualReader = new BufferedReader(
            new StringReader(actual));
        try {
            assertEqualsByLine(expectedReader, actualReader);
        } catch (final IOException ignore) {
            // unreachable
        } finally {
            closeSilently(expectedReader);
            closeSilently(actualReader);
        }
    }

    /**
     * 文字列を行単位で比較します．
     * 
     * @param expectedReader
     *            期待される文字列の入力ストリーム
     * @param actualReader
     *            実際の文字列の入力ストリーム
     * @throws IOException
     *             入出力例外が発生した場合
     */
    protected void assertEqualsByLine(final BufferedReader expectedReader,
            final BufferedReader actualReader) throws IOException {
        String expectedLine;
        String actualLine;
        int lineNo = 0;
        while ((expectedLine = expectedReader.readLine()) != null) {
            ++lineNo;
            actualLine = actualReader.readLine();
            assertEquals("line:" + lineNo, expectedLine, actualLine);
        }
        ++lineNo;
        assertEquals("line:" + lineNo, null, actualReader.readLine());
    }

    /**
     * {@link Processor} が生成したソースを文字列と比較・検証します．
     * 
     * @param expected
     *            生成されたソースに期待される内容の文字列
     * @param clazz
     *            生成されたクラス
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSource(final CharSequence expected,
            final Class<?> clazz) throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        assertNotEmpty("expected", expected);
        assertNotNull("clazz", clazz);
        assertCompiled();
        assertEqualsGeneratedSource(expected, clazz.getName());
    }

    /**
     * {@link Processor} が生成したソースを文字列と比較・検証します．
     * 
     * @param expected
     *            生成されたソースに期待される内容の文字列
     * @param className
     *            生成されたクラスの完全限定名
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSource(final CharSequence expected,
            final String className) throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        assertNotEmpty("className", className);
        assertCompiled();
        final String actual = getGeneratedSource(className);
        assertNotNull("actual", actual);
        assertEqualsByLine(
            expected == null ? null : expected.toString(),
            actual);
    }

    /**
     * {@link Processor} が生成したソースをファイルと比較・検証します．
     * 
     * @param expectedSourceFile
     *            生成されたソースに期待される内容を持つファイル
     * @param clazz
     *            生成されたクラス
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithFile(
            final File expectedSourceFile, final Class<?> clazz)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        assertNotNull("expectedSourceFile", expectedSourceFile);
        assertNotNull("clazz", clazz);
        assertCompiled();
        assertEqualsGeneratedSourceWithFile(expectedSourceFile, clazz.getName());
    }

    /**
     * {@link Processor} が生成したソースをファイルと比較・検証します．
     * 
     * @param expectedSourceFile
     *            生成されたソースに期待される内容を持つファイル
     * @param className
     *            クラスの完全限定名
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithFile(
            final File expectedSourceFile, final String className)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        assertNotNull("expectedSourceFile", expectedSourceFile);
        assertNotEmpty("className", className);
        assertCompiled();
        assertEqualsGeneratedSource(
            readString(expectedSourceFile, charset),
            className);
    }

    /**
     * {@link Processor} が生成したソースをファイルと比較・検証します．
     * 
     * @param expectedSourceFilePath
     *            生成されたソースに期待される内容を持つファイルのパス
     * @param clazz
     *            生成されたクラス
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithFile(
            final String expectedSourceFilePath, final Class<?> clazz)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        assertNotEmpty("expectedSourceFilePath", expectedSourceFilePath);
        assertNotNull("clazz", clazz);
        assertCompiled();
        assertEqualsGeneratedSourceWithFile(expectedSourceFilePath, clazz
            .getName());
    }

    /**
     * {@link Processor} が生成したソースをファイルと比較・検証します．
     * 
     * @param expectedSourceFilePath
     *            生成されたソースに期待される内容を持つファイルのパス
     * @param className
     *            生成されたクラスの完全限定名
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithFile(
            final String expectedSourceFilePath, final String className)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        assertNotEmpty("expectedSourceFilePath", expectedSourceFilePath);
        assertNotEmpty("className", className);
        assertCompiled();
        assertEqualsGeneratedSourceWithFile(
            new File(expectedSourceFilePath),
            className);
    }

    /**
     * {@link Processor} が生成したソースをクラスパス上のリソースと比較・検証します．
     * 
     * @param expectedResourceUrl
     *            生成されたソースに期待される内容を持つリソースのURL
     * @param clazz
     *            生成されたクラス
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithResource(
            final URL expectedResourceUrl, final Class<?> clazz)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        assertNotNull("expectedResourceUrl", expectedResourceUrl);
        assertNotNull("clazz", clazz);
        assertCompiled();
        assertEqualsGeneratedSourceWithResource(expectedResourceUrl, clazz
            .getName());
    }

    /**
     * {@link Processor} が生成したソースをクラスパス上のリソースと比較・検証します．
     * 
     * @param expectedResourceUrl
     *            生成されたソースに期待される内容を持つリソースのURL
     * @param className
     *            生成されたクラスの完全限定名
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithResource(
            final URL expectedResourceUrl, final String className)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        assertNotNull("expectedResourceUrl", expectedResourceUrl);
        assertNotEmpty("className", className);
        assertCompiled();
        assertEqualsGeneratedSource(
            readFromResource(expectedResourceUrl),
            className);
    }

    /**
     * {@link Processor} が生成したソースをクラスパス上のリソースと比較・検証します．
     * 
     * @param expectedResource
     *            生成されたソースに期待される内容を持つリソースのパス
     * @param clazz
     *            生成されたクラス
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithResource(
            final String expectedResource, final Class<?> clazz)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        assertNotEmpty("expectedResource", expectedResource);
        assertNotNull("clazz", clazz);
        assertCompiled();
        assertEqualsGeneratedSourceWithResource(
            clazz.getName(),
            expectedResource);
    }

    /**
     * {@link Processor} が生成したソースをクラスパス上のリソースと比較・検証します．
     * 
     * @param expectedResource
     *            生成されたソースに期待される内容を持つリソースのパス
     * @param className
     *            生成されたクラスの完全限定名
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     * @throws IOException
     *             入出力例外が発生した場合
     * @throws SourceNotGeneratedException
     *             ソースが生成されなかった場合
     * @throws ComparisonFailure
     *             生成されたソースが期待される内容と一致しなかった場合
     */
    protected void assertEqualsGeneratedSourceWithResource(
            final String expectedResource, final String className)
            throws IllegalStateException, IOException,
            SourceNotGeneratedException, ComparisonFailure {
        assertNotEmpty("expectedResource", expectedResource);
        assertNotEmpty("className", className);
        assertCompiled();
        final URL url = Thread
            .currentThread()
            .getContextClassLoader()
            .getResource(expectedResource);
        if (url == null) {
            throw new FileNotFoundException(expectedResource);
        }
        assertEqualsGeneratedSourceWithResource(url, className);
    }

    /**
     * 設定をリセットし，初期状態に戻します．
     * <p>
     * {@link #compile()} 呼び出し前に設定した内容も， {@link #compile()}
     * によって得られた状態も全てリセットされます．
     * </p>
     */
    protected void reset() {
        locale = null;
        charset = null;
        out = null;
        options.clear();
        sourcePaths.clear();
        processors.clear();
        processors.add(new AptinaUnitProcessor());
        compilationUnits.clear();
        javaCompiler = null;
        diagnostics = null;
        standardJavaFileManager = null;
        if (testingJavaFileManager != null) {
            try {
                testingJavaFileManager.close();
            } catch (final Exception ignore) {
            }
        }
        testingJavaFileManager = null;
        processingEnvironment = null;
        compiledResult = null;
    }

    /**
     * 追加されたコンパイル対象のリストを返します．
     * 
     * @return 追加されたコンパイル対象のリスト
     * @throws IOException
     *             入出力例外が発生した場合
     */
    List<JavaFileObject> getCompilationUnits() throws IOException {
        final List<JavaFileObject> result = new ArrayList<JavaFileObject>(
            compilationUnits.size());
        for (final CompilationUnit compilationUnit : compilationUnits) {
            result.add(compilationUnit.getJavaFileObject());
        }
        return result;
    }

    /**
     * {@link #compile()} が呼び出されていなければ例外をスローします．
     * 
     * @throws IllegalStateException
     *             {@link #compile()} が呼び出されていない場合
     */
    void assertCompiled() throws IllegalStateException {
        if (compiledResult == null) {
            throw new IllegalStateException("not compiled");
        }
    }

    /**
     * URL から読み込んだ内容を文字列で返します．
     * <p>
     * URLで表されるリソースの内容は， {@link #charset} で指定された文字セットでエンコード
     * (未設定時はプラットフォームデフォルトの文字セット) されていなければなりません．
     * </p>
     * 
     * @param url
     *            リソースのURL
     * @return URL から読み込んだ内容の文字列
     * @throws IOException
     *             入出力例外が発生した場合
     */
    String readFromResource(final URL url) throws IOException {
        final InputStream is = url.openStream();
        try {
            return readString(is, charset);
        } finally {
            closeSilently(is);
        }
    }

    /**
     * 発生した {@link Diagnostic} をコンソールに出力する {@link DiagnosticListener} です．
     * <p>
     * {@link Diagnostic} コンソールに出力した後，後続の {@link DiagnosticListener} へ通知します．
     * </p>
     * 
     * @author koichik
     */
    static class LoggingDiagnosticListener implements
            DiagnosticListener<JavaFileObject> {

        DiagnosticListener<JavaFileObject> listener;

        /**
         * インスタンスを構築します．
         * 
         * @param listener
         *            後続の {@link DiagnosticListener}
         */
        LoggingDiagnosticListener(
                final DiagnosticListener<JavaFileObject> listener) {
            this.listener = listener;
        }

        @Override
        public void report(final Diagnostic<? extends JavaFileObject> diagnostic) {
            System.out.println(diagnostic);
            listener.report(diagnostic);
        }

    }

    /**
     * コンパイル時に {@link Processor} に渡される @ ProcessingEnvironment} を取得するための
     * {@link Processor} です．
     * 
     * @author koichik
     */
    @SupportedSourceVersion(SourceVersion.RELEASE_6)
    @SupportedAnnotationTypes("*")
    class AptinaUnitProcessor extends AbstractProcessor {

        @Override
        public synchronized void init(
                final ProcessingEnvironment processingEnvironment) {
            super.init(processingEnvironment);
            AptinaTestCase.this.processingEnvironment = processingEnvironment;
        }

        @Override
        public boolean process(final Set<? extends TypeElement> annotations,
                final RoundEnvironment roundEnv) {
            return false;
        }

    }

    /**
     * コンパイル対象を表すインタフェースです．
     * 
     * @author koichik
     */
    interface CompilationUnit {

        /**
         * このコンパイル対象に対応する {@link JavaFileObject} を返します．
         * 
         * @return このコンパイル対象に対応する {@link JavaFileObject}
         * @throws IOException
         *             入出力例外が発生した場合
         */
        JavaFileObject getJavaFileObject() throws IOException;

    }

    /**
     * ソースパス上のファイルとして存在するコンパイル対象を表すクラスです．
     * 
     * @author koichik
     */
    class FileCompilationUnit implements CompilationUnit {

        String className;

        /**
         * インスタンスを構築します．
         * 
         * @param className
         *            クラス名
         */
        public FileCompilationUnit(final String className) {
            this.className = className;
        }

        @Override
        public JavaFileObject getJavaFileObject() throws IOException {
            return standardJavaFileManager.getJavaFileForInput(
                StandardLocation.SOURCE_PATH,
                className,
                Kind.SOURCE);
        }

    }

    /**
     * メモリ上に存在するコンパイル対象を表すクラスです．
     * 
     * @author koichik
     */
    class InMemoryCompilationUnit implements CompilationUnit {

        String className;

        String source;

        /**
         * インスタンスを構築します．
         * 
         * @param className
         *            クラス名
         * @param source
         *            ソース
         */
        public InMemoryCompilationUnit(final String className,
                final String source) {
            this.className = className;
            this.source = source;
        }

        @Override
        public JavaFileObject getJavaFileObject() throws IOException {
            final JavaFileObject javaFileObject = testingJavaFileManager
                .getJavaFileForOutput(
                    StandardLocation.SOURCE_OUTPUT,
                    className,
                    Kind.SOURCE,
                    null);
            final Writer writer = javaFileObject.openWriter();
            try {
                writer.write(source);
            } finally {
                closeSilently(writer);
            }
            return javaFileObject;
        }

    }

}
