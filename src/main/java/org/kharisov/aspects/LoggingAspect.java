package org.kharisov.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.*;

/**
 * Класс LoggingAspect представляет собой аспект, который используется для логирования времени выполнения методов,
 * аннотированных с помощью @Loggable. Он использует Logger для записи информации о времени выполнения.
 */
@Aspect
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Логирует время выполнения метода. Этот метод выполняется вокруг метода, аннотированного @Loggable.
     *
     * @param joinPoint информация о выполненном методе.
     * @return результат выполнения метода.
     * @throws Throwable если во время выполнения метода возникает исключение.
     */
    @Around("@annotation(org.kharisov.annotations.Loggable)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        logger.info(joinPoint.getSignature() + " executed in " + executionTime + "ms");
        return proceed;
    }
}
