package framework.annotation;

import framework.RequestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
public @interface PostMapping {
    String name() default "";
    String[] value() default {};
    String[] path() default {};
    String[] params() default {};
    String[] headers() default {};
    String[] consumes() default {};
    String[] produces() default {};
}
