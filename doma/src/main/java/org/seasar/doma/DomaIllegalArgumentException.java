package org.seasar.doma;

import org.seasar.doma.message.DomaMessageCode;

/**
 * 事前条件をもつパラメータへの引数が不正な場合にスローされる例外です。
 * <p>
 * {@link IllegalArgumentException} とは別にこの例外を定義しているのは、 {@literal Doma}
 * のバグによる例外なのか、 {@literal Doma}のAPIの事前条件を満たしていないことによる例外なのかを判別しやすくするためです。
 * 
 * @author taedium
 * 
 */
public class DomaIllegalArgumentException extends DomaException {

    private static final long serialVersionUID = 1L;

    /** 不正な引数のパラメータの名前 */
    protected final String parameterName;

    /** 不正な引数であることの説明 */
    protected final String description;

    /**
     * インスタンスを構築します。
     * 
     * @param parameterName
     *            不正な引数のパラメータの名前
     * @param description
     *            不正な引数であることの説明
     */
    public DomaIllegalArgumentException(String parameterName, String description) {
        super(DomaMessageCode.DOMA0002, parameterName, description);
        this.parameterName = parameterName;
        this.description = description;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getDescription() {
        return description;
    }

}
