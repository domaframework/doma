package org.seasar.doma.domain;

import org.seasar.doma.message.DomaMessageCode;

/**
 * {@link Domain} に関するリフレクション処理に失敗した場合にスローされる例外です。
 * 
 * @author taedium
 * 
 */
public class DomainReflectionException extends DomainException {

    private static final long serialVersionUID = 1L;

    /**
     * リフレクション処理に失敗した原因を指定してインスタンスを構築します。
     * 
     * @param cause
     *            リフレクション処理に失敗した原因
     */
    public DomainReflectionException(Throwable cause) {
        super(DomaMessageCode.DOMA1004, cause, cause);
    }

}
