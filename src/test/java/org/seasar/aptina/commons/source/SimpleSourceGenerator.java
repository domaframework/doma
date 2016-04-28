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
package org.seasar.aptina.commons.source;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.Formatter;
import java.util.Locale;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import org.seasar.aptina.commons.message.EnumMessageCode;
import org.seasar.aptina.commons.message.EnumMessageFormatter;
import org.seasar.aptina.commons.util.VersionUtils;

import static java.util.Arrays.*;

import static org.seasar.aptina.commons.util.IOUtils.*;
import static org.seasar.aptina.commons.util.VersionUtils.*;

/**
 * {@code printf} でソースを生成する単純なジェネレータのための抽象クラスです．
 * 
 * @author koichik
 * @param <T>
 *            パターンを定義した列挙の型
 */
public abstract class SimpleSourceGenerator<T extends Enum<T> & EnumMessageCode> {

    /** インデント用の空白 */
    protected static final char[] SPACES = new char[100];
    static {
        fill(SPACES, ' ');
    }

    /** {@link ProcessingEnvironment} */
    protected final ProcessingEnvironment env;

    /** ソースの文字列バッファ */
    protected final StringBuilder buf = new StringBuilder(1024);

    /** フォーマッタ */
    protected final Formatter formatter;

    /** 列挙に定義されたパターンを使用するフォーマッタ */
    protected final EnumMessageFormatter<T> enumFormatter;

    /** インデントの深さ */
    protected int depth;

    /**
     * インスタンスを構築します．
     * 
     * @param env
     *            {@link ProcessingEnvironment}
     * @param enumClass
     *            パターンを定義した列挙のクラス
     */
    public SimpleSourceGenerator(final ProcessingEnvironment env,
            final Class<T> enumClass) {
        this.env = env;
        final Locale locale = env.getLocale();
        formatter = new Formatter(buf, locale);
        enumFormatter = new EnumMessageFormatter<T>(enumClass, buf, locale);
    }

    /**
     * 生成されたソースを返します．
     * 
     * @return 生成されたソース
     */
    @Override
    public String toString() {
        return new String(buf);
    }

    /**
     * 生成されたソースを破棄し，状態をリセットします．
     */
    protected void reset() {
        buf.setLength(0);
    }

    /**
     * 生成されたソースを {@link Filer} に出力します．
     * 
     * @param className
     *            生成されたクラスの完全限定名
     * @param originalElement
     *            生成されたクラスの元になった {@link Element}
     * @throws IOException
     *             入出力例外が発生した場合
     */
    protected void write(final String className,
            final TypeElement originalElement) throws IOException {
        final Filer filer = env.getFiler();
        final JavaFileObject sourceFile = filer.createSourceFile(
            className,
            originalElement);
        final Writer writer = new BufferedWriter(sourceFile.openWriter());
        try {
            writer.write(new String(buf));
        } finally {
            closeSilently(writer);
        }
    }

    /**
     * インデントの階層を下げます．
     */
    protected void enter() {
        ++depth;
    }

    /**
     * インデントの階層を上げます．
     */
    protected void leave() {
        --depth;
    }

    /**
     * インデントを出力します．
     */
    protected void indent() {
        indent(depth * 4);
    }

    /**
     * インデントを出力します．
     * 
     * @param spaces
     *            インデントするスペースの数
     */
    protected void indent(final int spaces) {
        for (int i = spaces; i > 0; i -= SPACES.length) {
            buf.append(SPACES, 0, i > SPACES.length ? SPACES.length : i);
        }
    }

    /**
     * フォーマットした文字列を出力します．
     * <p>
     * 先頭にはインデントが付けられます．
     * </p>
     * 
     * @param format
     *            パターン文字列
     * @param args
     *            パターンから参照される引数の並び
     */
    protected void printf(final String format, final Object... args) {
        indent();
        formatter.format(format, args);
    }

    /**
     * 列挙で示されるパターンを使ってフォーマットした文字列を出力します．
     * <p>
     * 先頭にはインデントが付けられます．
     * </p>
     * 
     * @param code
     *            パターンを示す列挙
     * @param args
     *            パターンから参照される引数の並び
     */
    protected void printf(final T code, final Object... args) {
        indent();
        enumFormatter.format(code, args);
    }

    /**
     * Javadoc コメントを出力します．
     * <p>
     * 先頭にはインデントが付けられます．
     * </p>
     * <p>
     * 引数で渡されたコメントは {@link Elements#getDocComment(Element)}
     * が返す文字列と同じ形式の文字列として扱われます． この形式は， Javadoc コメントから各行の先頭の {@literal "*"}
     * までを除去したものです． 最初の行は空白 ({@literal "/**"} の後に改行があるため) で， 最初と最後を除いた各行の先頭は空白 (
     * {@literal "/*"} の後の空白) で始まります．
     * </p>
     * 
     * @param comment
     *            コメント
     */
    protected void printJavadoc(final String comment) {
        if (comment == null || comment.isEmpty()) {
            return;
        }
        printf("/**%n");
        final BufferedReader reader = new BufferedReader(new StringReader(
            comment));
        try {
            String line = reader.readLine();
            if (!line.isEmpty()) {
                printf(" *%1$s%n", line);
            }
            while ((line = reader.readLine()) != null) {
                printf(" *%1$s%n", line);
            }
        } catch (final IOException ignore) {
        }
        printf(" */%n");
    }

    /**
     * 列挙で指定されたパターンを使って組み立てたメッセージを Javadoc コメントを出力します．
     * <p>
     * 先頭にはインデントが付けられます．
     * </p>
     * <p>
     * 組み立てられたコメントは {@link Elements#getDocComment(Element)}
     * が返す文字列と同じ形式の文字列として扱われます． この形式は， Javadoc コメントから各行の先頭の {@literal "*"}
     * までを除去したものです． 最初の行は空白 ({@literal "/**"} の後に改行があるため) で， 最初と最後を除いた各行の先頭は空白 (
     * {@literal "/*"} の後の空白) で始まります．
     * </p>
     * 
     * @param code
     *            パターンを示す列挙
     * @param args
     *            パターンから参照される引数の並び
     */
    protected void printJavadoc(final T code, final Object... args) {
        printJavadoc(enumFormatter.getMessage(code, args));
    }

    /**
     * {@link Generated} アノテーションを出力します．
     * <p>
     * アノテーションの属性には引数で渡された項目に加えて，
     * {@link VersionUtils#getVersion(String, String, String)}
     * で取得したバージョン情報が出力されます． バージョン情報が取得できなかった場合は {@literal "DEV"} が出力されます．
     * </p>
     * 
     * @param productName
     *            プロダクト名
     * @param groupId
     *            グループ ID
     * @param artifactId
     *            アーティファクト ID
     */
    protected void printGeneratedAnnotation(final String productName,
            final String groupId, final String artifactId) {
        printf(
            "@javax.annotation.Generated({\"%1$s\", \"%2$s\", \"%3$s\", \"%4$s\"})%n",
            productName,
            groupId,
            artifactId,
            getVersion(groupId, artifactId, "DEV"));
    }

}
