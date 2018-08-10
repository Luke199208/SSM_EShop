package com.luke.service.impl;

import com.luke.bean.Admin;
import com.luke.bean.AdminExample;
import com.luke.mapper.AdminMapper;
import com.luke.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/***
 * com.luke.service.impl
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
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AdminMapper mapper;

    @Override
    public boolean findAdminByCodePasswd(Map map) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria =adminExample.createCriteria();
        criteria.andCodeEqualTo((String) map.get("code"));
        criteria.andPasswordEqualTo((String) map.get("password"));
        List<Admin> adminList = mapper.selectByExample(adminExample);
        return adminList.size()>0;
    }
}
