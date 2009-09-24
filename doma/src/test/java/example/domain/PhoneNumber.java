package example.domain;

import org.seasar.doma.Domain;

@Domain(valueType = String.class, accessorMethod = "getValue")
public class PhoneNumber {

    private final String value;

    public PhoneNumber(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
