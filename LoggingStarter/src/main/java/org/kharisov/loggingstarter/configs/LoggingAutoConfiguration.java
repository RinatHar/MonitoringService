package org.kharisov.loggingstarter.configs;

import org.kharisov.loggingstarter.annotations.OnEnableLoggingCondition;
import org.kharisov.loggingstarter.aspects.LoggingAspect;
import org.springframework.context.annotation.*;

@Configuration
public class LoggingAutoConfiguration {

    @Bean
    @Conditional(OnEnableLoggingCondition.class)
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}

