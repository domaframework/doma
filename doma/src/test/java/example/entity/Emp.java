package example.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Version;
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;

@Entity
public interface Emp {

    @Id
    IntegerDomain id();

    StringDomain name();

    BigDecimalDomain salary();

    @Version
    IntegerDomain version();
}
