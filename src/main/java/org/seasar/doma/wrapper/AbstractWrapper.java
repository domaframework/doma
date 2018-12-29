package org.seasar.doma.wrapper;

/**
 * {@link Wrapper} の骨格実装です。
 *
 * @author taedium
 * @param <BASIC> 基本型
 */
public abstract class AbstractWrapper<BASIC> implements Wrapper<BASIC> {

  /** 基本型のクラス */
  protected Class<BASIC> basicClass;

  /** 値 */
  protected BASIC value;

  /**
   * クラスを指定してインスタンスを構築します。
   *
   * @param basicClass 基本型のクラス
   */
  protected AbstractWrapper(Class<BASIC> basicClass) {
    this.basicClass = basicClass;
  }

  /**
   * クラスと値を指定してインスタンスを構築します。
   *
   * @param basicClass 基本型のクラス
   * @param value 値
   */
  protected AbstractWrapper(Class<BASIC> basicClass, BASIC value) {
    this(basicClass);
    doSet(value);
  }

  @Override
  public final void set(BASIC value) {
    doSet(value);
  }

  /**
   * 値を設定します。
   *
   * @param value 値
   */
  protected void doSet(BASIC value) {
    this.value = value;
  }

  @Override
  public final BASIC get() {
    return doGet();
  }

  /**
   * 値を返します。
   *
   * @return 値
   */
  protected BASIC doGet() {
    return value;
  }

  @Override
  public final BASIC getCopy() {
    return doGetCopy();
  }

  /**
   * 値のコピーを返します。
   *
   * @return 値のコピーを返します。
   */
  protected BASIC doGetCopy() {
    return doGet();
  }

  @Override
  public BASIC getDefault() {
    return null;
  }

  @Override
  public final boolean hasEqualValue(Object otherValue) {
    return doHasEqualValue(otherValue);
  }

  /**
   * 等しい値を持っているかどうか判定します。
   *
   * @param otherValue 値
   * @return 等しい値を持っている場合 {@code true}
   */
  protected boolean doHasEqualValue(Object otherValue) {
    BASIC value = doGet();
    if (value == null) {
      return otherValue == null;
    }
    return value.equals(otherValue);
  }

  @Override
  public Class<BASIC> getBasicClass() {
    return basicClass;
  }
}
