package org.kharisov.loggingstarter.annotations;

import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnEnableLoggingCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getBeanFactory().getBeanNamesForAnnotation(EnableLogging.class).length > 0;
    }
}
