/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.mirror.ColumnMirror;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

/** @author nakamura-to */
public class EmbeddablePropertyMeta {

  protected final TypeMirror type;

  protected final String typeName;

  protected final String boxedTypeName;

  protected final String boxedClassName;

  protected String name;

  protected ColumnMirror columnMirror;

  protected CtType ctType;

  public EmbeddablePropertyMeta(VariableElement fieldElement, ProcessingEnvironment env) {
    assertNotNull(fieldElement, env);
    this.type = fieldElement.asType();
    this.typeName = TypeMirrorUtil.getTypeName(type, env);
    this.boxedTypeName = TypeMirrorUtil.getBoxedTypeName(type, env);
    this.boxedClassName = TypeMirrorUtil.getBoxedClassName(type, env);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ColumnMirror getColumnMirror() {
    return columnMirror;
  }

  public void setColumnMirror(ColumnMirror columnMirror) {
    this.columnMirror = columnMirror;
  }

  public String getColumnName() {
    return columnMirror != null ? columnMirror.getNameValue() : "";
  }

  public boolean isColumnInsertable() {
    return columnMirror != null ? columnMirror.getInsertableValue() : true;
  }

  public boolean isColumnUpdatable() {
    return columnMirror != null ? columnMirror.getUpdatableValue() : true;
  }

  public boolean isColumnQuoteRequired() {
    return columnMirror != null ? columnMirror.getQuoteValue() : false;
  }

  public CtType getCtType() {
    return ctType;
  }

  public void setCtType(CtType ctType) {
    this.ctType = ctType;
  }

  public TypeMirror getType() {
    return type;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getBoxedTypeName() {
    return boxedTypeName;
  }

  public String getBoxedClassName() {
    return boxedClassName;
  }
}
