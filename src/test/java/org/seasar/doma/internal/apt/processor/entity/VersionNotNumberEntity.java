package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Version;

/**
 * @author taedium
 * 
 */
@Entity
public class VersionNotNumberEntity {

    @Version
    String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
