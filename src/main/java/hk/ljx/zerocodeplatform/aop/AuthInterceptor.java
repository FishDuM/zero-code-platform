package hk.ljx.zerocodeplatform.aop;

import hk.ljx.zerocodeplatform.annotation.AuthCheck;
import hk.ljx.zerocodeplatform.exception.ErrorCode;
import hk.ljx.zerocodeplatform.exception.ThrowUtils;
import hk.ljx.zerocodeplatform.model.entity.User;
import hk.ljx.zerocodeplatform.model.enums.UserRoleEnum;
import hk.ljx.zerocodeplatform.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static hk.ljx.zerocodeplatform.constant.UserConstant.USER_LOGIN_STATE;

@Aspect
@Component
public class AuthInterceptor {
    @Resource
    private UserService userService;

    /**
     * 权限校验
     * @param joinPoint 切面入参
     * @param authCheck 权限校验注解
     * @return 切面方法返回值
     * @throws Throwable 抛出异常
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        // 获取 request
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取当前登录用户
        User user = userService.getLoginUser(request);
        // 获取当前登录用户的角色
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        // 权限校验
        if (mustRoleEnum == null){
            joinPoint.proceed();
        }
        // 必须有该权限才通过
        // 获取当前用户的权限
        String userRole = user.getUserRole();
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(userRole);
        ThrowUtils.throwIf(userRoleEnum == null, ErrorCode.NO_AUTH_ERROR);
        // 要求必须有管理员权限
        ThrowUtils.throwIf(mustRole.equals(UserRoleEnum.ADMIN.getValue()) && !UserRoleEnum.ADMIN.equals(userRoleEnum), ErrorCode.NO_AUTH_ERROR);
        // 符合要求放行
        return joinPoint.proceed();
    }
}
