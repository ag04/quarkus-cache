package com.ag04.quarkus.cache.interceptor;

import com.ag04.quarkus.cache.*;
import com.ag04.quarkus.cache.annotation.CacheKey;
import org.jboss.logging.Logger;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.Interceptor.Priority;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public abstract class BaseCacheInterceptor {
    private static final Logger log = Logger.getLogger(BaseCacheInterceptor.class);

    public static final int BASE_PRIORITY = Priority.PLATFORM_BEFORE;

    @Inject
    CacheManager cacheManager;
    
    @Inject
    Instance<CacheKeyGenerator> keyGenerator;

    protected Object getCacheKey(
        String cacheName, 
        Class<? extends CacheKeyGenerator> keyGeneratorClass, 
        InvocationContext context
    ) {
        //
        Object[] methodParameterValues = context.getParameters();

        if (keyGeneratorClass != UndefinedCacheKeyGenerator.class) {
            return generateKey(keyGeneratorClass, context.getMethod(), methodParameterValues);
        } else if (methodParameterValues == null || methodParameterValues.length == 0) {
            // If the intercepted method doesn't have any parameter, then the default cache key will be used.
            return getDefaultKey(cacheName);
        } else if (paramsAreAnnotated(context.getMethod().getParameterAnnotations())) {
               return new CompositeCacheKey(extractAnnotatedParamValues(context));
        } else if (methodParameterValues.length == 1) {
            // If the intercepted method has exactly one parameter, then this parameter will be used as the cache key.
            return methodParameterValues[0];
        } else {
            // If the intercepted method has two or more parameters, then a composite cache key built from all these parameters
            // will be used.
            return new CompositeCacheKey(methodParameterValues);
        }
    }

    private Object[] extractAnnotatedParamValues(InvocationContext context) {
        Annotation[][] allAnnotations = context.getMethod().getParameterAnnotations();
        Object[] parameters = context.getParameters();

        List<Object> annotatedParams = new ArrayList<>();
        for (int i = 0; i < allAnnotations.length; i++) {
            List<Annotation> annotations = Arrays.asList(allAnnotations[i]);
            boolean isParamAnnotated = annotations.stream().anyMatch(ann -> ann.annotationType().equals(CacheKey.class));
            if (isParamAnnotated) {
                annotatedParams.add(parameters[i]);
            }
        }

        return annotatedParams.toArray();
    }

    private boolean paramsAreAnnotated(Annotation[][] annotations) {
        return Arrays.stream(annotations).flatMap(Stream::of).anyMatch(p -> p.annotationType().equals(CacheKey.class));
    }

    private Object getDefaultKey(String cacheName) {
        return new DefaultCacheKey(cacheName);
    }

    private <T extends CacheKeyGenerator> Object generateKey(Class<T> keyGeneratorClass, Method method,
            Object[] methodParameterValues) {
        Instance<T> keyGenInstance = keyGenerator.select(keyGeneratorClass);
        if (keyGenInstance.isResolvable()) {
            log.tracef("Using cache key generator bean from Arc [class=%s]", keyGeneratorClass.getName());
            T keyGen = keyGenInstance.get();
            try {
                return keyGen.generate(method, methodParameterValues);
            } finally {
                keyGenerator.destroy(keyGen);
            }
        } else {
            try {
                log.tracef("Creating a new cache key generator instance [class=%s]", keyGeneratorClass.getName());
                return keyGeneratorClass.getConstructor().newInstance().generate(method, methodParameterValues);
            } catch (NoSuchMethodException e) {
                // This should never be thrown because the default constructor availability is checked at build time.
                throw new CacheException("No default constructor found in cache key generator [class="
                        + keyGeneratorClass.getName() + "]", e);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new CacheException("Cache key generator instantiation failed", e);
            }
        }
    }

}
