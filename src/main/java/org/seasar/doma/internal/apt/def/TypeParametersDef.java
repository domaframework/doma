package org.seasar.doma.internal.apt.def;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
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
    CsvFormatList csvFormatList =
        typeParameterNameMap
            .keySet()
            .stream()
            .map(Element::getSimpleName)
            .map(Name::toString)
            .collect(toCollection(CsvFormatList::new));
    return Collections.unmodifiableList(csvFormatList);
  }

  public List<String> getTypeParameters() {
    CsvFormatList csvFormatList = new CsvFormatList(typeParameterNameMap.values());
    return Collections.unmodifiableList(csvFormatList);
  }

  private class CsvFormatList extends ArrayList<String> {

    private CsvFormatList() {
      super();
    }

    private CsvFormatList(Collection<? extends String> collection) {
      super(collection);
    }

    @Override
    public String toString() {
      return this.stream().collect(joining(","));
    }
  }
}
