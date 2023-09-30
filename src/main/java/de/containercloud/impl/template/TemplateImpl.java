package de.containercloud.impl.template;

import de.containercloud.api.template.Template;

public record TemplateImpl(String name, String internalPath, String externalPath) implements Template {

    @Override
    public String toString() {
        return name();
    }
}
