package sangwon.wead.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class TimeAspect {

    @Around("@annotation(sangwon.wead.aspect.annotation.Time)")
    public void time(ProceedingJoinPoint pointcut) throws Throwable {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        pointcut.proceed();
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("실행시간 = {}ms", totalTimeMillis);
    }

}
