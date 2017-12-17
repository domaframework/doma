package example.holder;

import org.seasar.doma.Holder;

/**
 * @author taedium
 * 
 */
@Holder(valueType = String.class)
public class InternationalPhoneNumber extends PhoneNumber {

    public InternationalPhoneNumber(String value) {
        super(value);
    }

}
