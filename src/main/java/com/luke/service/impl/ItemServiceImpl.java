package com.luke.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luke.bean.*;
import com.luke.common.pojo.EUTreeNode;
import com.luke.common.pojo.TaotaoResult;
import com.luke.common.utils.DateHelper;
import com.luke.common.utils.IDUtils;
import com.luke.mapper.TbItemCatMapper;
import com.luke.mapper.TbItemDescMapper;
import com.luke.mapper.TbItemMapper;
import com.luke.mapper.TbItemParamMapper;
import com.luke.service.ItemService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
public class ItemServiceImpl implements ItemService {

    @Resource
    private TbItemMapper itemMapper;

    @Resource
    private TbItemCatMapper itemCatMapper;

    @Resource
    private TbItemDescMapper itemDescMapper;

    @Resource
    private TbItemParamMapper itemParamMapper;

    @Override
    public Map getList(String page, String rows) {
        PageHelper.startPage(Integer.valueOf(page), Integer.valueOf(rows));
        TbItemExample itemExample = new TbItemExample();
        List<TbItem> itemList = itemMapper.selectByExample(itemExample);
//        List<TbItem> itemList1 =  mapper.selectByExample(itemExample);
//        System.out.println(itemList.size());
//        System.out.println(itemList1.size());
        PageInfo<TbItem> pageInfo = new PageInfo<>(itemList);
        Map map = new HashMap();
        map.put("total", pageInfo.getTotal());
        map.put("rows", pageInfo.getList());
        return map;
    }

    @Override
    public List<EUTreeNode> getItemCatList(Long parentId) {
        TbItemCatExample itemCatExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = itemCatExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list = itemCatMapper.selectByExample(itemCatExample);
        List<EUTreeNode> resultList = new ArrayList<>();
        for (TbItemCat tbItemCat : list) {
            EUTreeNode node = new EUTreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent() ? "closed" : "open");
            resultList.add(node);
        }
        return resultList;
    }

    @Override
    public Map picUpload(MultipartFile uploadFile, String uploadURL) {
        //图片名称生成策略---采用时间格式(精确到毫秒)并追加随机3位(10以内)数字
        //精确到毫秒
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String picName = df.format(new Date());
        //随机再生成3位 10以内的数
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            picName += r.nextInt(10);
        }
        //获取扩展名
        String originalFilename = uploadFile.getOriginalFilename();
        String ext = FilenameUtils.getExtension(originalFilename);
        //相对路径
        String path = "upload/" + picName + "." + ext;
        //全路径，该路径为后面创建的tomcat图片服务器的上传图片的web工程下存放图片的路径
        String url = uploadURL + path;
        //jersey 发送另一台Tomcat(可读写的)
        Client client = new Client();
        WebResource resource = client.resource(url);

        Map map = new HashMap();
        try {
            resource.put(String.class, uploadFile.getBytes());
            map.put("error", 0);
            map.put("url", url);
            return map;
        } catch (Exception e) {
            map.put("error", 0);
            map.put("message", "上传失败");
            return map;
        }
    }

    @Override
    public TaotaoResult save(TbItem item, String desc) {
        item.setId(IDUtils.genItemId());
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());

        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setCreated(item.getCreated());
        itemDesc.setUpdated(item.getUpdated());
        itemDesc.setItemDesc(desc);

        int insertItem = itemMapper.insert(item);
        int insertItemDesc = itemDescMapper.insert(itemDesc);
        if (insertItem == 1 && insertItemDesc == 1) {
            return TaotaoResult.ok();
        }
        return null;
    }

    @Override
    public Map getParamList(String page, String rows) {

        PageHelper.startPage(Integer.valueOf(page), Integer.valueOf(rows));
        TbItemParamExample itemParamExample = new TbItemParamExample();
        List<TbItemParam> itemParams = itemParamMapper.selectByExample(itemParamExample);

        PageInfo<TbItemParam> pageInfo = new PageInfo<>(itemParams);
        Map map = new HashMap();
        map.put("total", pageInfo.getTotal());
        map.put("rows", pageInfo.getList());

        return map;
    }

}
