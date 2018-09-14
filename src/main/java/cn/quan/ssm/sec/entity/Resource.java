package cn.quan.ssm.sec.entity;

/**
 * @auther zhangsq on 2017-9-6.
 * 资源信息类
 */

public class Resource {

    private String url;//资源访问的地址

    private String role;//所需要的权限

    public Resource(String url, String role) {
        this.url = url;
        this.role = role;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
