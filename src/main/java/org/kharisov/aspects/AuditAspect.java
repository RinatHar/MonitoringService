package org.kharisov.aspects;

import lombok.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.kharisov.annotations.Audit;
import org.kharisov.entities.UserRecord;
import org.kharisov.services.interfaces.AuditService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;

/**
 * Класс AuditAspect представляет собой аспект, который используется для аудита методов, аннотированных с помощью @Audit.
 * Он использует сервис AuditService для добавления записей аудита.
 */
@Aspect
@NoArgsConstructor
@AllArgsConstructor
public class AuditAspect {

    private AuditService auditService;

    @Pointcut("@annotation(org.kharisov.annotations.Audit)")
    public void annotatedByAudit() {}

    @AfterReturning(pointcut = "annotatedByAudit()", returning = "result")
    public void audit(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Audit auditAnnotation = method.getAnnotation(Audit.class);
        String action = auditAnnotation.action();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserRecord user = (UserRecord) authentication.getPrincipal();
//            auditService.addAuditRecord(user, action);
        }
    }
}