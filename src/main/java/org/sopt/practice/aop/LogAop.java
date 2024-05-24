package org.sopt.practice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

// Lombok 어노테이션을 사용하여 로거 인스턴스를 자동으로 생성
@Slf4j
// 이 클래스를 Aspect로 선언하여 AOP 기능을 구현
@Aspect
@Component
public class LogAop {

    // Pointcut 정의: org.sopt.practice.controller 패키지 내의 모든 클래스와 모든 메소드를 대상으로 함
    @Pointcut("execution(* org.sopt.practice.controller.*.*(..))")
    public void controllerPointcut() {}

    // Before 어드바이스: 메소드 실행 전에 실행되며, 지정된 Pointcut에 해당하는 메소드에서 작동
    @Before("controllerPointcut()")
    public void logMethodCall(JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        // 메소드 진입 시 메소드 이름을 로깅
        log.info("===== Entering method: {} =====", method.getName());
        // 메소드 파라미터를 로깅
        logParameters(joinPoint);
    }

    // AfterReturning 어드바이스: 메소드가 값을 반환한 후 실행되며, 지정된 Pointcut에 해당하는 메소드에서 작동
    @AfterReturning(value = "controllerPointcut()", returning = "returnObj")
    public void logMethodReturn(JoinPoint joinPoint, Object returnObj) {
        Method method = getMethod(joinPoint);
        // 메소드 반환 시 메소드 이름을 로깅
        log.info("===== Returning method: {} =====", method.getName());
        // 반환된 값의 타입과 값을 로깅
        logReturnValue(returnObj);
    }

    // JoinPoint에서 메소드 정보를 추출하는 메소드
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    // 메소드 파라미터를 로깅하는 메소드
    private void logParameters(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        if (args.length == 0) {
            log.info("No parameters");
        } else {
            for (int i = 0; i < args.length; i++) {
                log.info("Parameter name: {}, type: {}, value: {}",
                        parameterNames[i],
                        args[i].getClass().getSimpleName(),
                        args[i]);
            }
        }
    }

    // 반환된 객체를 로깅하는 메소드
    private void logReturnValue(Object returnObj) {
        if (returnObj != null) {
            log.info("Return type: {}, value: {}", returnObj.getClass().getSimpleName(), returnObj);
        } else {
            log.info("Return value is null");
        }
    }
}

