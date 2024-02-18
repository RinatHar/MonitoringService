package org.kharisov.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.kharisov.annotations.Audit;
import org.kharisov.configs.UserContextHolder;
import org.kharisov.domains.User;
import org.kharisov.services.interfaces.AuditService;
import org.kharisov.services.singletons.AuditServiceSingleton;

import java.lang.reflect.Method;

/**
 * Класс AuditAspect представляет собой аспект, который используется для аудита методов, аннотированных с помощью @Audit.
 * Он использует сервис AuditService для добавления записей аудита.
 */
@Aspect
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect() {
        auditService = AuditServiceSingleton.getInstance();
    }

    @Pointcut("@annotation(org.kharisov.annotations.Audit)")
    public void annotatedByAudit() {}

    @AfterReturning(pointcut = "annotatedByAudit()", returning = "result")
    public void audit(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Audit auditAnnotation = method.getAnnotation(Audit.class);
        String action = auditAnnotation.action();
        User user = UserContextHolder.getUser();
        auditService.addEntry(user, action);
    }
}
