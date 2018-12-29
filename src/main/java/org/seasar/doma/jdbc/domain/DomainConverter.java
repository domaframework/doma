package org.seasar.doma.jdbc.domain;

import org.seasar.doma.DomainConverters;
import org.seasar.doma.ExternalDomain;

/**
 * 任意の型の値を基本型の値と相互に変換します。つまり 、任意の型をドメインクラスとして扱うことを可能にします。
 *
 * <p>通常、このインタフェースの実装クラスには {@link ExternalDomain} を注釈します。また、 実装クラスは {@link DomainConverters}
 * に登録して使用します。
 *
 * <p>1番目の型パラメータは、 次の制約を満たす必要があります。
 *
 * <ul>
 *   <li>トップレベルのクラスである。
 *   <li>パッケージに属する。
 * </ul>
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;ExtenalDomain
 * public class SalaryConverter implements DomainConverter&lt;Salary, BigDecimal&gt; {
 *
 *     public BigDecimal fromDomainToValue(Salary domain) {
 *         return domain.getValue();
 *     }
 *
 *     public Salary fromValueToDomain(BigDecimal value) {
 *         return new Salary(value);
 *     }
 * }
 * </pre>
 *
 * @author taedium
 * @since 1.25.0
 * @see ExternalDomain
 * @see DomainConverters
 * @param <DOMAIN> ドメイン型
 * @param <BASIC> 基本型
 */
public interface DomainConverter<DOMAIN, BASIC> {

  /**
   * ドメインから値へ変換します。
   *
   * @param domain ドメイン
   * @return 値
   */
  BASIC fromDomainToValue(DOMAIN domain);

  /**
   * 値からドメインへ変換します。
   *
   * @param value 値
   * @return ドメイン
   */
  DOMAIN fromValueToDomain(BASIC value);
}
