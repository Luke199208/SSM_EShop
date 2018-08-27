package com.luke.service.impl;

import com.luke.bean.Admin;
import com.luke.bean.AdminExample;
import com.luke.mapper.AdminMapper;
import com.luke.mapper.ResourceMapper;
import com.luke.mapper.RoleMapper;
import com.luke.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/***
 * com.luke.service.impl
 * dllo
 * 18/8/21
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
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private ResourceMapper resourceMapper;


    @Override
    public Admin findAdmin(String accountName) {

        System.out.println("enter findAdmin");

        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andCodeEqualTo(accountName);
        List admins = adminMapper.selectByExample(adminExample);

//        Admin admin = new Admin();
//        admin.setCode("admin1");
//        admin.setPassword("123456");
//        admin.setName("liSi");
//        admin.setEmail("648518411@qq.com");

        System.out.println("before findAdmin return");
        System.out.println(admins.get(0));
        return (Admin) admins.get(0);
    }

    @Override
    public List getRoles(String adminCode) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andCodeEqualTo(adminCode);
        List<Admin> admins = adminMapper.selectByExample(adminExample);
        List roles = roleMapper.findRoles(String.valueOf(admins.get(0).getAdminId()));
        return roles;
    }

    @Override
    public List getResources(String id) {
        List<com.luke.bean.Resource> resourceList = resourceMapper.getResources(id);
        return resourceList;
    }
}
