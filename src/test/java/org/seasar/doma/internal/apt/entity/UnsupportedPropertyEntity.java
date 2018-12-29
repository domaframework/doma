package org.seasar.doma.internal.apt.entity;

import java.util.Calendar;
import org.seasar.doma.Entity;

/** @author taedium */
@Entity
public class UnsupportedPropertyEntity {

  Calendar calendar;

  public Calendar getCalendar() {
    return calendar;
  }

  public void setCalendar(Calendar calendar) {
    this.calendar = calendar;
  }
}
