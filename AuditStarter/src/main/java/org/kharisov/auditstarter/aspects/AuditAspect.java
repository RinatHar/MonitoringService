package org.kharisov.auditstarter.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.kharisov.auditshared.annotations.Audit;
import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.auditshared.services.interfaces.AuditService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;


/**
 * Класс AuditAspect представляет собой аспект, который используется для аудита методов, аннотированных с помощью @Audit.
 * Он использует сервис AuditService для добавления записей аудита.
 */
@Aspect
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Определение точки среза для методов, аннотированных @Audit.
     */
    @Pointcut("@annotation(org.kharisov.auditshared.annotations.Audit)")
    public void annotatedByAudit() {}

    /**
     * Метод, выполняемый после возврата из методов, аннотированных @Audit.
     * Добавляет запись аудита для выполненного действия.
     *
     * @param joinPoint информация о выполненном методе.
     * @param result результат выполнения метода.
     */
    @AfterReturning(pointcut = "annotatedByAudit()", returning = "result")
    public void audit(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Audit auditAnnotation = method.getAnnotation(Audit.class);
        String action = auditAnnotation.action();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserRecord user = (UserRecord) authentication.getPrincipal();
            auditService.addAuditRecord(user, action);
        }
    }
}
