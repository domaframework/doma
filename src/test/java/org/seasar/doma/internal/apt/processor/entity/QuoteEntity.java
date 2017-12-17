package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

/**
 * @author taedium
 * 
 */
@Entity
@Table(quote = true)
public class QuoteEntity {

    @Id
    @Column(quote = true)
    Integer id;

    @Column(quote = true)
    String name;

    @Column(quote = true)
    Integer version;
}
