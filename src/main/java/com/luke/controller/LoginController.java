package com.luke.controller;

import com.luke.bean.Admin;
import com.luke.service.LoginService;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * com.luke.controller
 * dllo
 * 18/8/7
 *             ,%%%%%%%%,
 *           ,%%/\%%%%/\%%
 *          ,%%%\c "" J/%%%
 * %.       %%%%/ 0  0 \%%%
 * `%%.     %%%%    _  |%%%
 *  `%%     `%%%%(__Y__)%%'
 *  //       ;%%%%`\-/%%%'
 * ((       /  `%%%%%%%'
 *  \\    .'     '%%%'|    攻
 *   \\  /       \  | |    城
 *    \\/         ) | |    湿
 *     \         /_ | |__
 *     (___________))))))) 
 *
 *       我湿一吼  BUG无有                        
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Resource
    private LoginService loginServiceImpl;

    @PostMapping(value = "/toLogin.do")
    @ResponseBody
    public boolean login(@RequestBody Map map) {
        System.out.println(map);
        System.out.println("enter login");
        // boolean flag = loginServiceImpl.findAdminByCodePasswd(map);

        //生成用户名和密码的token
        UsernamePasswordToken token = new UsernamePasswordToken((String) map.get("code"),(String)map.get("password"));

        //获取当前用户的subject对象
        Subject currentAdmin = SecurityUtils.getSubject();
        try {
            //传递token给shiro,调用doGetAuthenticationInfo这个方法(即:登录验证)
            currentAdmin.login(token);

           // System.out.println("ROLES:"+getRoleList().get(0));

            return true;
        }catch (AuthenticationException e){
            //登录失败
            return false;
        }


    }

    @RequestMapping(value = "queryAuth.do",method = RequestMethod.POST)
    @ResponseBody
    public List<String> queryAuth(HttpServletRequest request) {
        System.out.println("查询登录用户角色");
        Subject subject= SecurityUtils.getSubject();
        List<String> roleList=new ArrayList<String>();
        if(subject.hasRole("commodityManagement")){
            roleList.add("commodityManagement");
        }
        if(subject.hasRole("contentManagement")){
            roleList.add("contentManagement");
        }
        if(subject.hasRole("authorityManagement")){
            roleList.add("authorityManagement");
        }
        return roleList;
    }

    @RequestMapping(value = "queryPermission.do",method = RequestMethod.POST)
    @ResponseBody
    public List<String> queryPermission(HttpServletRequest request) {
        System.out.println("查询登录用户权限");
        Subject subject= SecurityUtils.getSubject();
        List<String> rolePermission=new ArrayList<String>();
        if(subject.isPermitted("commodityManagement:add")){
            rolePermission.add("commodityManagement:add");
        }
        if(subject.isPermitted("commodityManagement:query")){
            rolePermission.add("commodityManagement:query");
        }
        if(subject.isPermitted("commodityManagement:param")){
            rolePermission.add("commodityManagement:param");
        }

        if(subject.isPermitted("contentManagement:category")){
            rolePermission.add("contentManagement:category");
        }
        if(subject.hasRole("contentManagement:content")){
            rolePermission.add("contentManagement:content");
        }

        if(subject.hasRole("authorityManagement:add")){
            rolePermission.add("authorityManagement:add");
        }
        if(subject.isPermitted("authorityManagement:query")){
            rolePermission.add("authorityManagement:query");
        }
        if(subject.isPermitted("authorityManagement:del")){
            rolePermission.add("authorityManagement:del");
        }

        return rolePermission;
    }
//    List<String> getRoleList(){
//        List<String> list = new ArrayList<>();
//        Subject currentUser = SecurityUtils.getSubject();
//        System.out.println(currentUser.isPermitted("commodityManagement:add"));
//        System.out.println(currentUser.isPermitted("ContentManagement:category"));
//        if (currentUser.hasRole("commodityManagement")){
//            list.add("commodityManagement");
//        }
//        return list;
//     }
//
//    @RequiresPermissions("oper:add")//执行需要”oper:add”权限
//    @RequestMapping("/add.do")
//    public String add(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        //userService.add();
//        System.out.println("addaddadd");
//        return "a";
//    }
//    @RequiresPermissions("oper:delBtn")//执行需要”oper:del”权限
//    @RequestMapping("/del.do")
//    public void del(HttpServletRequest request,HttpServletResponse response) throws IOException{
//        System.out.println("bbbbb");
//    }
//    @RequiresRoles("admin")
//    @RequestMapping("/query.do")
//    public void query(HttpServletRequest request,HttpServletResponse response) throws IOException{
//        System.out.println("CCCCC");
//
//    }
//
//    private String loginUser(Admin admin) {
//        if (isRelogin(admin)) return "SUCC"; // 如果已经登陆，无需重新登录
//
//        return shiroLogin(admin); // 调用shiro的登陆验证
//    }
//    private String shiroLogin(Admin admin) {
//        // 组装token，包括客户公司名称、简称、客户编号、用户名称；密码
//        System.out.println(admin.getCode());
//        System.out.println(admin.getPassword());
//        UsernamePasswordToken token = new UsernamePasswordToken(admin.getCode(), admin.getPassword().toCharArray(), null);
//        token.setRememberMe(true);
//
//        // shiro登陆验证
//        try {
//            SecurityUtils.getSubject().login(token);
//        } catch (UnknownAccountException ex) {
//            return "用户不存在或者密码错误！";
//        } catch (IncorrectCredentialsException ex) {
//            return "用户不存在或者密码错误！";
//        } catch (AuthenticationException ex) {
//            return ex.getMessage(); // 自定义报错信息
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return "内部错误，请重试！";
//        }
//        return "SUCC";
//    }
//
//    private boolean isRelogin(Admin admin) {
//        Subject us = SecurityUtils.getSubject();
//        if (us.isAuthenticated()) {
//            return true; // 参数未改变，无需重新登录，默认为已经登录成功
//        }
//        return false; // 需要重新登陆
//    }
}
