package org.seasar.doma.internal.apt.def;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeParameterElement;

public class TypeParametersDef {

  private Map<TypeParameterElement, String> typeParameterNameMap;

  public TypeParametersDef(Map<TypeParameterElement, String> typeParameterNameMap) {
    assertNotNull(typeParameterNameMap);
    this.typeParameterNameMap = typeParameterNameMap;
  }

  public List<String> getTypeVariables() {
    List<String> csvFormatList =
        typeParameterNameMap
            .keySet()
            .stream()
            .map(Element::getSimpleName)
            .map(Name::toString)
            .collect(toList());
    return Collections.unmodifiableList(csvFormatList);
  }

  public List<String> getTypeParameters() {
    return Collections.unmodifiableList(new ArrayList<>(typeParameterNameMap.values()));
  }
}
