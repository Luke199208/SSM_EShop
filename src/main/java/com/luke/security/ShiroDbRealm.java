
package com.luke.security;

import com.luke.bean.Admin;
import com.luke.bean.Role;
import com.luke.service.AdminService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShiroDbRealm extends AuthorizingRealm {

    @Resource
    public AdminService adminService;

    /**
     * 获取授权信息
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        System.out.println("enter doGetAuthorizationInfo");

        //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String adminCode = principalCollection.getPrimaryPrincipal().toString();
        List<Role> roleList = adminService.getRoles(adminCode);
        Map<String, List<com.luke.bean.Resource>> resourceMap = new HashMap<>();
        for (Role role : roleList) {
            System.out.println(role);
            info.addRole(role.getRole_name());
            List<com.luke.bean.Resource> resources = adminService.getResources(role.getId());
            resourceMap.put(role.getRole_name(),resources);
        }
        for (String s : resourceMap.keySet()) {
            for (com.luke.bean.Resource resource : resourceMap.get(s)) {
                System.out.println(resource);
                info.addStringPermission(resource.getResource_name());
            }
        }
        //info.addRole("oper");//oper,admin,manager
        //info.addRole("admin");
        //List permissionList = userService.getPermissions();
        //info.addStringPermission("oper:add");
        //info.addStringPermission("oper:del");
        //info.addStringPermission("oper:add");
        return info;
    }

    /**
     * 登录验证
     *
     * 自动调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {

        System.out.println("enter doGetAuthenticationInfo");

        //获得令牌——基于用户名和密码的令牌
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        //从令牌中可以取出用户名
        String accountName = token.getUsername();

        //使用 登录页面输入的用户名 ,上数据库中查找用户
        Admin userInDB = adminService.findAdmin(accountName);
        System.out.println(userInDB+"   ----userindb");
        //让shiro框架去验证账号密码
        if (userInDB != null) {
            return new SimpleAuthenticationInfo(userInDB.getCode(), userInDB.getPassword(), getName());
        }
        return null;

    }


    //一定要写getset方法
    public AdminService getAdminService() {
        return adminService;
    }

    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

}
