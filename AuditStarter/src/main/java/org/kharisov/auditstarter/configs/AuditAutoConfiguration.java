package org.kharisov.auditstarter.configs;

import org.kharisov.auditshared.services.interfaces.AuditService;
import org.kharisov.auditstarter.aspects.AuditAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.*;

@Configuration
@ConditionalOnClass(AuditService.class)
public class AuditAutoConfiguration {

    @Bean
    public AuditAspect auditAspect(AuditService auditService) {
        return new AuditAspect(auditService);
    }
}

