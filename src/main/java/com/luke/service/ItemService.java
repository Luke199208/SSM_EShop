package com.luke.service;

import com.luke.bean.TbItem;
import com.luke.common.pojo.EUTreeNode;
import com.luke.common.pojo.TaotaoResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/***
 * com.luke.service
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
public interface ItemService {

    Map getList(String page , String rows);

    List<EUTreeNode> getItemCatList(Long parentId);

    Map picUpload(MultipartFile uploadFile,String uploadURL);

    TaotaoResult save(TbItem item, String desc);

    Map getParamList(String page, String rows);
}
