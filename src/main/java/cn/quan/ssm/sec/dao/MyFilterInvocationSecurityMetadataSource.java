package cn.quan.ssm.sec.dao;

import cn.quan.ssm.sec.JdbcRequestMapBulider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * security的运行过程，security实现控制的功能其实就是通过一系列的拦截器来实现的，当用户登陆的时候，
 * 会被AuthenticationProcessingFilter拦截，调用AuthenticationManager的实现类，
 * 同时AuthenticationManager会调用ProviderManager来获取用户验证信息，其中不同的Provider调用的服务不同，
 * 因为这些信息可以是在数据库上，可以是在LDAP服务器上，可以是xml配置文件上等，这个例子中就是为数据库；
 * 如果验证通过后会将用户的权限信息放到spring的全局缓存SecurityContextHolder中，以备后面访问资源时使用。
 * 当访问资源，访问url时，会通过AbstractSecurityInterceptor拦截器拦截，
 * 其中会调用FilterInvocationSecurityMetadataSource的方法来获取被拦截url所需的全部权限，
 * 其中FilterInvocationSecurityMetadataSource的常用的实现类为DefaultFilterInvocationSecurityMetadataSource，
 * 这个类中有个很关键的东西就是requestMap，也就是我们上面所得到的数据，在调用授权管理器AccessDecisionManager，
 * 这个授权管理器会通过spring的全局缓存SecurityContextHolder获取用户的权限信息，
 * 还会获取被拦截的url和被拦截url所需的全部权限，然后根据所配的策略，如果权限足够，则返回，
 * 权限不够则报错并调用权限不足页面。
 * 根据源码debug跟踪得出，其实资源权限关系就放在DefaultFilterInvocationSecurityMetadataSource的requestMap，中的，
 * 这个requestMap就是我们JdbcRequestMapBulider.buildRequestMap()方法所需要的数据类型，
 * 因此，顺气自然就想到了我们自定义一个类继承FilterInvocationSecurityMetadataSource接口，
 * 将数据查出的数据放到requestMap中去。
 * 制定类MyFilterInvocationSecurityMetadataSource继承FilterInvocationSecurityMetadataSource和InitializingBean接口。
 * @auther zhangsq on 2017-9-6.
 */

public class MyFilterInvocationSecurityMetadataSource
        implements FilterInvocationSecurityMetadataSource,InitializingBean {

    private final static List<ConfigAttribute> NULL_CONFIG_ATTRIBUTE = null;

    //资源权限集合
    private Map<RequestMatcher,Collection<ConfigAttribute>> requestMap;

    //查找数据库权限和资源的关系
    private JdbcRequestMapBulider bulider;



    /**
     * 更具访问资源的地址查找所需要的权限
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        final HttpServletRequest request = ((FilterInvocation) object) .getRequest();
        Collection<ConfigAttribute> atts = NULL_CONFIG_ATTRIBUTE;
        for(Map.Entry<RequestMatcher,Collection<ConfigAttribute>> entry : requestMap.entrySet()){
            if(entry.getKey().matches(request)){
                atts = entry.getValue();
                break;
            }

        }
        return atts;
    }

    /**
     * 获取所有的权限
     * @return
     */
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> attributes = new HashSet<ConfigAttribute>();
        for(Map.Entry<RequestMatcher,Collection<ConfigAttribute>> entry : requestMap.entrySet()){
            attributes.addAll(entry.getValue());
        }
        System.out.println("总共有这些权限："+attributes.toString());
        return attributes;
    }

    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }

    //绑定requestMap
    protected Map<RequestMatcher, Collection<ConfigAttribute>> bindRequestMap() {

        return bulider.buildRequestMap();
    }

    public void afterPropertiesSet() throws Exception {
        this.requestMap = this.bindRequestMap();
    }

    public void refreshResuorceMap() {
        this.requestMap = this.bindRequestMap();
    }

    public JdbcRequestMapBulider getBulider() {
        return bulider;
    }

    public void setBulider(JdbcRequestMapBulider bulider) {
        this.bulider = bulider;
    }
}
