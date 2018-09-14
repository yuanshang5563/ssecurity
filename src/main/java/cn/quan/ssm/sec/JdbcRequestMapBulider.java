package cn.quan.ssm.sec;

import cn.quan.ssm.sec.entity.Resource;
import cn.quan.ssm.sec.entity.ResourceMapping;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @auther zhangsq on 2017-9-6.
 * 查询资源和角色，并构建RequestMap
 */

public class JdbcRequestMapBulider extends JdbcDaoSupport{

    //查询数据的sql语句，该属性在配置bean的时候传入即可。
    private String resourceQuery = "";

    public String getResourceQuery() {
        return resourceQuery;
    }

    public void setResourceQuery(String resourceQuery) {
        this.resourceQuery = resourceQuery;
    }

    //查询资源的方法
    public List<Resource> findResourceData(){
        ResourceMapping resourceMapping = new ResourceMapping(getDataSource(),resourceQuery);

        return resourceMapping.execute();
    }

    /**
     * 拼接RequestMap
     * buildRequestMap方法用来最后拼接成LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>共security使用。
     * @return
     */
    public LinkedHashMap<RequestMatcher,Collection<ConfigAttribute>> buildRequestMap(){
        LinkedHashMap<RequestMatcher,Collection<ConfigAttribute>> requestMap =
                new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
        List<Resource> resourcesList = this.findResourceData();//查询资源信息
        for(Resource resource : resourcesList){
            RequestMatcher requestMatcher = this.getRequestMatcher(resource.getUrl());
            List<ConfigAttribute> list = new ArrayList<ConfigAttribute>();
            list.add(new SecurityConfig(resource.getRole()));
            requestMap.put(requestMatcher,list);
        }
        return requestMap;
    }

    //通过一个字符串地址构建一个AntPathRequestMatcher对象
    //getRequestMatcher方法就是用来创建RequestMatcher对象的
    protected RequestMatcher getRequestMatcher(String url){
        return new AntPathRequestMatcher(url);
    }



}
