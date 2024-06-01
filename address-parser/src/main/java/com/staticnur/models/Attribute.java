package com.staticnur.models;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Attribute {
    private final Map<String, String> attributes;

    public Attribute() {
        this.attributes = new LinkedHashMap<>();
    }

    public String getAttributesByKey(String key) {
        return attributes.getOrDefault(key, "");
    }

    public void addAttribute(String attributeName, String attributeValue) {
        attributes.put(attributeName, attributeValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute that = (Attribute) o;
        return Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes);
    }
}
