package sangwon.wead.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Around("@annotation(sangwon.wead.aspect.annotation.Log)")
    public Object log(ProceedingJoinPoint pointcut) throws Throwable {
        for(Object object : pointcut.getArgs()) {
            log.info("argument: {}", object.toString());
        }

        Object result =  pointcut.proceed();
        log.info("return: {}", result.toString());
        return result;
    }

}
