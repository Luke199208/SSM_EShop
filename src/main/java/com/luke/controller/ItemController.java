package com.luke.controller;

import com.luke.bean.TbItem;
import com.luke.common.pojo.EUTreeNode;
import com.luke.common.pojo.TaotaoResult;
import com.luke.service.ItemService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
@RequestMapping("/item")
public class ItemController {

    @Resource
    private ItemService itemServiceImpl;

    @Value(value = "${uploadURL}")
    private String uploadURL;

    @GetMapping("/list.do")
    @ResponseBody
    public Map<String, Object> getList(String page , String rows){
        System.out.println("enter getList()");
        Map map =  itemServiceImpl.getList(page,rows);
        return map;
    }

    @PostMapping(value = "/itemCatList.do")
    @ResponseBody
    public List<EUTreeNode> getItemCatList(@RequestParam(value = "id",defaultValue = "0")Long parentId){
        System.out.println("enter getItemCatList()");
        List<EUTreeNode> treeNodes = itemServiceImpl.getItemCatList(parentId);
        return treeNodes;
    }

    @RequestMapping("/picUpload.do")
    @ResponseBody
    public Map picUpload(@RequestParam(required = false)MultipartFile uploadFile ){
        Map map = itemServiceImpl.picUpload(uploadFile,uploadURL);
        return map;
    }

    @PostMapping("/save.do")
    @ResponseBody
    public TaotaoResult save(TbItem item , String desc){
        System.out.println("enter save()");
        TaotaoResult result = itemServiceImpl.save(item,desc);
        return result;
    }

    @GetMapping("/getParamList.do")
    @ResponseBody
    public Map<String, Object> getParamList(String page , String rows){
        System.out.println("enter getParamList()");
        Map map =  itemServiceImpl.getParamList(page,rows);
        for (Object o : map.keySet()) {
            System.out.println(map.get(o));
        }
        return map;
    }
}
