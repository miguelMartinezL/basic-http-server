package framework.annotation;

public @interface ScanApp {
    String[] packages() default {};
}
