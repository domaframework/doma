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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner6;

import static java.util.Arrays.*;

/**
 * コンパイル対象の {@link Element} 階層をコンソールに出力する {@link Processor} です．
 * <p>
 * {@link AptinaTestCase} のサブクラスで，
 * {@link AptinaTestCase#addProcessor(Processor...)} にこのクラスを加えることで， コンパイル対象の
 * {@link Element} 階層を確認することができます．
 * </p>
 * 
 * <p>
 * 次のサンプルは， {@code src/test/java} フォルダにある {@code TestSource.java} をコンパイルし，
 * {@code PrintingProcessor} で {@link Element} 階層をコンソールに出力するテストクラスです．
 * </p>
 * 
 * <pre>
 * public class XxxProcessorTest extends AptinaTestCase {
 *
 *     &#x40;Override
 *     protected void setUp() throws Exception {
 *         super.setUp();
 *         // ソースパスを追加
 *         addSourcePath("src/test/java");
 *     }
 *
 *     public void test() throws Exception {
 *         // PrintingProcessor を生成して追加
 *         addProcessor(new PrintingProcessor());
 *
 *         // コンパイル対象を追加
 *         addCompilationUnit(TestSource.class);
 *
 *         // コンパイル実行
 *         compile();
 *     }
 * }
 * </pre>
 * 
 * @author koichik
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("*")
public class PrintingProcessor extends AbstractProcessor {

    /** インデント用の空白 (100 文字) */
    protected static final char[] SPACES = new char[100];
    static {
        fill(SPACES, ' ');
    }

    /** 出力先 */
    protected PrintWriter out;

    /** 階層の深さ */
    protected int depth;

    /**
     * インスタンスを構築します．
     */
    public PrintingProcessor() {
        this(new PrintWriter(System.out));
    }

    /**
     * インスタンスを構築します．
     * 
     * @param out
     *            出力先
     */
    public PrintingProcessor(final PrintWriter out) {
        this.out = out;
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        for (final Element e : roundEnv.getRootElements()) {
            new PrintingVisitor().scan(e);
        }
        return false;
    }

    /**
     * {@link Element} の階層をトラバースしてコンソールに出力する {@link ElementVisitor} です．
     * 
     * @author koichik
     */
    class PrintingVisitor extends ElementScanner6<Void, Void> {

        @Override
        public Void visitPackage(final PackageElement e, final Void p) {
            enter(e);
            super.visitPackage(e, p);
            leave(e);
            return null;
        }

        @Override
        public Void visitType(final TypeElement e, final Void p) {
            enter(e);
            super.visitType(e, p);
            leave(e);
            return null;
        }

        @Override
        public Void visitVariable(final VariableElement e, final Void p) {
            enter(e);
            super.visitVariable(e, p);
            leave(e);
            return null;
        }

        @Override
        public Void visitExecutable(final ExecutableElement e, final Void p) {
            enter(e);
            super.visitExecutable(e, p);
            leave(e);
            return null;
        }

        @Override
        public Void visitTypeParameter(final TypeParameterElement e,
                final Void p) {
            enter(e);
            super.visitTypeParameter(e, p);
            leave(e);
            return null;
        }

        void enter(final Element e) {
            printEnterMessage(e);
            out.flush();
            ++depth;
        }

        void leave(final Element e) {
            --depth;
            printLeaveMessage(e);
            out.flush();
        }

    }

    /**
     * {@link Element}が開始されたことを出力します．
     * 
     * @param e
     *            {@link Element}
     */
    protected void printEnterMessage(final Element e) {
        final String comment = processingEnv.getElementUtils().getDocComment(e);
        if (comment != null) {
            indent(depth * 2);
            out.println("/**");
            final BufferedReader reader = new BufferedReader(new StringReader(
                comment));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    indent(depth * 2);
                    out.print(" * ");
                    out.println(line);
                }
            } catch (final IOException ignore) {
            }
            indent(depth * 2);
            out.println(" */");
        }
        indent(depth * 2);
        out.print(e.getKind().name());
        out.print(' ');
        out.print(e);
        out.println(" {");
    }

    /**
     * {@link Element}が終了したことを出力します．
     * 
     * @param e
     *            {@link Element}
     */
    protected void printLeaveMessage(final Element e) {
        indent(depth * 2);
        out.println("}");
    }

    /**
     * インデントを出力します．
     * 
     * @param depth
     *            インデントの深さ
     */
    protected void indent(final int depth) {
        for (int i = depth; i > 0; i -= SPACES.length) {
            out.write(SPACES, 0, i > SPACES.length ? SPACES.length : i);
        }
    }

}
