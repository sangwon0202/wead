package sangwon.wead.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import sangwon.wead.exception.ClientFaultException;
import sangwon.wead.exception.NeedLoginException;
import sangwon.wead.util.RequestProvider;


@Aspect
@Component
public class LoginCheckAspect {


    @Before("@annotation(sangwon.wead.aspect.annotation.CheckLogin)")
    public void checkLogin(JoinPoint joinPoint) {
        if(RequestProvider.getUserIdFromSession() == null) throw new ClientFaultException();
    }

    @Before("@annotation(sangwon.wead.aspect.annotation.CheckLogout)")
    public void checkLogout(JoinPoint joinPoint) {
        if(RequestProvider.getUserIdFromSession() != null) throw new ClientFaultException();
    }

    @Before("@annotation(sangwon.wead.aspect.annotation.NeedLogin)")
    public void needLogin(JoinPoint joinPoint) {
        if(RequestProvider.getUserIdFromSession() == null) throw new NeedLoginException();
    }
}
