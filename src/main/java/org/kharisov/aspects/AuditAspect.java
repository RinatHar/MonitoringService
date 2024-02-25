package org.kharisov.aspects;

import lombok.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.kharisov.annotations.Audit;
import org.kharisov.services.interfaces.AuditService;

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

    /**
     * Определение точки среза для методов, аннотированных @Audit.
     */
    @Pointcut("@annotation(org.kharisov.annotations.Audit)")
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

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            UserRecord user = (UserRecord) authentication.getPrincipal();
//            auditService.addAuditRecord(user, action);
//        }
    }
}