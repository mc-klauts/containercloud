package de.containercloud.impl.template;

import de.containercloud.api.template.Template;

public class TemplateImpl implements Template {
    @Override
    public String path() {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String toString() {
        return name();
    }
}
