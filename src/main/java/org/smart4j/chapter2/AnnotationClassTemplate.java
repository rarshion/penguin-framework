package org.smart4j.chapter2;

import java.lang.annotation.Annotation;

public abstract class AnnotationClassTemplate extends ClassTemplate {

    protected final Class<? extends Annotation> annotationClass;

    protected AnnotationClassTemplate(String packageName, Class<? extends Annotation> annotationClass){
        super(packageName);
        this.annotationClass = annotationClass;
    }
}
