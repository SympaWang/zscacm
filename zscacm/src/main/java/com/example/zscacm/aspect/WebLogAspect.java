package com.example.zscacm.aspect;

import com.example.zscacm.utils.IpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 切面处理，记录入参，出参，异常信息
 *
 * @author chp
 * @date 2022-06-08
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    /**
     * aop切点
     */
    @Pointcut("execution(* com.example.zscacm.controller.*.*(..))")
    public void webLog() {
    }

    /**
     * 切面处理--->>>事前处理
     *
     * @param joinPoint JoinPoint联机对象
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        log.info("请求METHOD，URL : {}，{}", request.getMethod(), request.getRequestURL());
        log.info("请求IP :{} ", IpUtil.getIpAddr(request));
        log.info("请求处理CLASS_METHOD : {}", joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName());
        log.info("请求参数：{} : " + Arrays.toString(joinPoint.getArgs()));
    }


    /**
     * 切面--->>>异常处理、
     *
     * @param ex    异常信息
     * @param point JoinPoint联机对象
     */
    @AfterThrowing(throwing = "ex", pointcut = "webLog()")
    public void doRecoveryActions(JoinPoint point, Throwable ex) {
        String className = point.getTarget().getClass().getSimpleName() + "[" + point.getSignature().getName() + "]";
        log.error("[Result] 异常" + className + "" + " 返回 " + ex);
    }

    /**
     * 切面--->>>事后处理
     *
     * @param res 返回值
     */
    @AfterReturning(returning = "res", pointcut = "webLog()")
    public void doAfterReturning(Object res) throws JsonProcessingException {
        //获取返回值
        log.info("返回值 : " + new ObjectMapper().writeValueAsString(res));
    }
}