package org.kharisov.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * Класс LoggingAspect представляет собой аспект,
 * который используется для логирования времени выполнения методов, аннотированных с помощью @Loggable.
 */
@Aspect
public class LoggingAspect {
    @Pointcut("@annotation(org.kharisov.annotations.Loggable)")
    public void annotatedByLoggable() {}

    @Around("annotatedByLoggable()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        System.out.println(joinPoint.getSignature() + " executed in " + executionTime + "ms");

        return proceed;
    }
}
