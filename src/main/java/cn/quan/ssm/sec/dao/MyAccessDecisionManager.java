package cn.quan.ssm.sec.dao;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * @auther zhangsq on 2017-9-6.
 */

public class MyAccessDecisionManager implements AccessDecisionManager {

    /**
     * 该方法决定该权限是否有权限访问该资源，其实object就是一个资源的地址，authentication是当前用户的对应权限，
     * 如果没登陆就为游客，登陆了就是该用户对应的权限
     * @param authentication
     * @param object
     * @param configAttributes
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        if(null == configAttributes){
            return;
        }
        //所请求的资源拥有的权限(一个资源对多个权限)
        Iterator<ConfigAttribute> iterable = configAttributes.iterator();
        while (iterable.hasNext()){
            ConfigAttribute configAttribute = iterable.next();
            //访问所请求的资源所需要的权限
            String needPermission = configAttribute.getAttribute();
            System.out.println("访问"+object.toString()+"需要的权限是："+needPermission);

            //用户所拥有的权限authentication
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority ga : authorities) {
                if (needPermission.equals(ga.getAuthority())) {
                    return;
                }
            }
        }
        //没有权限
        throw new AccessDeniedException("没有权限访问！");
    }

    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    public boolean supports(Class<?> aClass) {
        return true;
    }
}
