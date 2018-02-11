package org.smart4j.chapter2.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    String pkg() default "";
    String cls() default "";
    Class<? extends Annotation> annotation() default Aspect.class;
}
