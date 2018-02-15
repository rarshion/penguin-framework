package org.penguin.framework.core.impl.support;

import org.penguin.framework.core.impl.support.ClassTemplate;

public abstract class SupperClassTemplate extends ClassTemplate {
    protected final Class<?> superClass;

    protected SupperClassTemplate(String packageName, Class<?> superClass) {
        super(packageName);
        this.superClass = superClass;
    }
}
