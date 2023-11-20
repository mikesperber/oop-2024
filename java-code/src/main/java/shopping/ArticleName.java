package shopping;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record ArticleName(String name) {
}
