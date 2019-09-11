package com.github.grpcx.processor;

import com.github.grpcx.GrpcServer;
import com.github.grpcx.annotation.EnableGrpcx;
import org.reflections.util.Utils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.Lifecycle;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ServiceImplScanner2 implements  ImportBeanDefinitionRegistrar, EnvironmentAware, BeanClassLoaderAware, ResourceLoaderAware {

    private  Set<String>  servicePackageNames;
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
         servicePackageNames = getPackagesToScan(importingClassMetadata);

        GrpcServer.serviceImplPackageNames=servicePackageNames;
//        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
//
//        // this check is needed in Spring 3.1
//        if (resourceLoader != null) {
//            scanner.setResourceLoader(resourceLoader);
//        }
//        scanner.setAnnotationClass(DB.class);
//        scanner.registerFilters();
//        var scans= new String[packagesToScan.size()];
//
//        scanner.doScan(packagesToScan.toArray(scans));
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        var a= metadata.getAnnotationAttributes(EnableGrpcx.class.getName());
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(a);
        String[] redisConfigKeys = attributes.getStringArray("serviceImplPackageName");
        // Appends value array attributes
        Set<String> packagesToScan = new LinkedHashSet<String>(Arrays.asList(redisConfigKeys));

        if (packagesToScan.isEmpty()) {
            return Collections.singleton(ClassUtils.getPackageName(metadata.getClassName()));
        }
        return packagesToScan;
    }
    private Environment env;
    @Override
    public void setEnvironment(Environment environment) {
        env=environment;
    }


    private ClassLoader classLoader;
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader=classLoader;
    }

    private ResourceLoader resourceLoader;
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader= resourceLoader;
    }

  ////////////


}
