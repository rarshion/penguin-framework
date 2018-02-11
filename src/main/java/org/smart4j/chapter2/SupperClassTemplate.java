package org.smart4j.chapter2;

public abstract class SupperClassTemplate extends ClassTemplate {
    protected final Class<?> superClass;

    protected SupperClassTemplate(String packageName, Class<?> superClass) {
        super(packageName);
        this.superClass = superClass;
    }
}
