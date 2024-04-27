package com.arms.util.aspect;

import com.arms.notification.slack.SlackNotificationService;
import com.arms.notification.slack.SlackProperty;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
// @Component
@Slf4j
public class SessionParamAdvice {

    private final SlackNotificationService slackNotificationService;
    private final String appName;

    public SessionParamAdvice(SlackNotificationService slackNotificationService
            , @Value("${spring.application.name}") String appName) {
        this.slackNotificationService = slackNotificationService;
        this.appName = appName;
    }

    @Around("execution(* com.arms..controller.*.*(..))")
    public Object sessionParam(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

        ServerWebExchange exchange = (ServerWebExchange) args[0];

        String methodName
                = Arrays.stream(joinPoint.getSignature().toLongString().split(" ")).skip(1).collect(Collectors.joining(" "));
        try{
            return joinPoint.proceed();
        }catch (Exception e){
            slackNotificationService.sendMessageToChannel(SlackProperty.Channel.middleproxy, e);

            return exchange.getSession()
                .flatMap(session -> {
                    slackNotificationService.sendMessageToChannel(SlackProperty.Channel.middleproxy, e);
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));

                    if(args.length>0){
                        for (Object arg : args) {
                            log.error("{} Error 발생\tmethodName : {}\tsession    : {}\tparameter   : {}\terrorMsg    : {}",appName,methodName,session.getId(),arg,errors);
                        }
                    }else{
                        log.error("{} Error 발생\tmethodName : {}\tsession    : {}\terrorMsg    : {}",appName,methodName,session.getId(),errors);
                    }

                    return Mono.error(e);
                });
        }

    }
}
