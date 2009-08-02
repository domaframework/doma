/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.PatternSyntaxException;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link String} を値の型とするドメインの骨格実装です。
 * 
 * @author taedium
 * 
 * @param <D>
 *            ドメインの型
 */
public abstract class AbstractStringDomain<D extends AbstractStringDomain<D>>
        extends AbstractComparableDomain<String, D> implements CharSequence,
        SerializableDomain<String, D> {

    private static final long serialVersionUID = 1L;

    /**
     * デフォルトの値でインスタンス化します。
     */
    protected AbstractStringDomain() {
        this(null);
    }

    /**
     * 値を指定してインスタンス化します。
     * 
     * @param value
     *            値
     */
    protected AbstractStringDomain(String value) {
        super(String.class, value);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (AbstractStringDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractStringDomainVisitor<R, P, TH> v = AbstractStringDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractStringDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

    /**
     * ドメインの値が空文字の場合 {@code true} を返します。
     * 
     * @return 空文字の場合 {@code true}
     * @throws DomainIllegalStateException
     *             ドメインの値が {@code null} の場合
     */
    public boolean isEmpty() throws DomainIllegalStateException {
        if (value == null) {
            throw new DomainIllegalStateException("value == null");
        }
        return value.isEmpty();
    }

    /**
     * ドメインの値がプレフィックスで始まっている場合 {@code true} を返します。
     * 
     * @param prefix
     *            プレフィックス
     * @return プレフィックスで始まっている場合 {@code true}
     * @throws DomainIllegalStateException
     *             ドメインの値が {@code null} の場合
     */
    public boolean startsWith(String prefix) throws DomainIllegalStateException {
        if (value == null) {
            throw new DomainIllegalStateException("value == null");
        }
        return value.startsWith(prefix);
    }

    /**
     * ドメインの値がサフィックスで終わっている場合 {@code true} を返します。
     * 
     * @param suffix
     *            サフィックス
     * @return サフィックスで終わっている場合 {@code true}
     * @throws DomainIllegalStateException
     *             ドメインの値が {@code null} の場合
     */
    public boolean endsWith(String suffix) throws DomainIllegalStateException {
        if (value == null) {
            throw new DomainIllegalStateException("value == null");
        }
        return value.endsWith(suffix);
    }

    /**
     * ドメインの値の長さを返します。
     * 
     * @return 長さ
     * @throws DomainIllegalStateException
     *             ドメインの値が {@code null} の場合
     */
    public int length() throws DomainIllegalStateException {
        if (value == null) {
            throw new DomainIllegalStateException("value == null");
        }
        return value.length();
    }

    /**
     * ドメインの値が正規表現にマッチした場合に {@code true} を返します。
     * 
     * @param regex
     *            正規表現
     * @return 正規表現にマッチした場合に {@code true}
     * @throws DomainIllegalStateException
     *             ドメインの値が {@code null} の場合
     * @throws PatternSyntaxException
     *             正規表現の構文が無効な場合
     */
    public boolean matches(String regex) throws DomainIllegalStateException {
        if (value == null) {
            throw new DomainIllegalStateException("value == null");
        }
        return value.matches(regex);
    }

    /**
     * ドメインの値が文字シーケンスを含んでいる場合 {@code true} を返します。
     * 
     * @param s
     *            文字シーケンス
     * @return 文字シーケンス含んでいる場合 {@code true}
     * @throws DomainIllegalStateException
     *             ドメインの値が {@code null} の場合
     * @throws NullPointerException
     *             文字シーケンスが {@code null} の場合
     */
    public boolean contains(CharSequence s) throws DomainIllegalStateException {
        if (value == null) {
            throw new DomainIllegalStateException("value == null");
        }
        return value.contains(s);
    }

    /**
     * ドメインの値から、指定されたインデックス位置にある {@code char} 値を返します
     * 
     * @param index
     *            インデックス
     * @return 指定されたインデックス位置にある {@code char}
     * @throws DomainIllegalStateException
     *             ドメインの値が {@code null} の場合
     * @throws IndexOutOfBoundsException
     *             インデックスが負の値、またはドメインの値の長さ以上である場合
     */
    @Override
    public char charAt(int index) throws DomainIllegalStateException {
        if (value == null) {
            throw new DomainIllegalStateException("value == null");
        }
        return value.charAt(index);
    }

    /**
     * ドメインの値のサブシーケンスである新規文字シーケンスを返します
     * 
     * @throws DomainIllegalStateException
     *             ドメインの値が {@code null} の場合
     * @throws IndexOutOfBoundsException
     *             {@code start} または {@code end} が負の値の場合、{@code end} の値が
     *             ドメインの値の長さ より大きい場合、{@code start} の値が {@code end} よりも大きい場合
     */
    @Override
    public CharSequence subSequence(int start, int end)
            throws DomainIllegalStateException {
        if (value == null) {
            throw new DomainIllegalStateException("value == null");
        }
        return value.subSequence(start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        AbstractStringDomain<?> other = AbstractStringDomain.class.cast(o);
        if (value == null) {
            return other.value == null;
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : null;
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream inputStream) throws IOException,
            ClassNotFoundException {
        inputStream.defaultReadObject();
        valueClass = (Class<String>) inputStream.readObject();
        value = String.class.cast(inputStream.readObject());
        changed = inputStream.readBoolean();
    }

    private void writeObject(ObjectOutputStream outputStream)
            throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeObject(valueClass);
        outputStream.writeObject(value);
        outputStream.writeBoolean(changed);
    }
}
