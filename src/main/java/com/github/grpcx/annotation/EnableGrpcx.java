package com.github.grpcx.annotation;


import com.github.grpcx.processor.ServiceImplScanner;
import com.github.grpcx.processor.ServiceImplScanner2;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author ming
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ServiceImplScanner2.class})
public @interface EnableGrpcx {

    @AliasFor("serviceImplPackageName")
    String[] value() default {};

    @AliasFor("value")
    String[] serviceImplPackageName() default {};
}
