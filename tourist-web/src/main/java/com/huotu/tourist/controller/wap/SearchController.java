package com.huotu.tourist.controller.wap;

import com.huotu.tourist.common.TouristCheckStateEnum;
import com.huotu.tourist.entity.ActivityType;
import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.repository.ActivityTypeRepository;
import com.huotu.tourist.repository.TouristOrderRepository;
import com.huotu.tourist.service.ActivityTypeService;
import com.huotu.tourist.service.TouristGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

/**
 * 商品逻辑层
 * Created by slt on 2017/1/12.
 */
@Controller
@RequestMapping(value = "/wap/")
public class SearchController {

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private TouristOrderRepository touristOrderRepository;

    @Autowired
    private ActivityTypeService activityTypeService;

    @Autowired
    private TouristGoodService touristGoodService;

    private String viewWapPath="/view/wap/";


    /**
     * 进入查询页面
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/showSearchView")
    public String showSearchView(Model model) throws IOException{

        List<ActivityType> hotTypes=activityTypeService.getHotTypes();

        model.addAttribute("types",hotTypes);
        return viewWapPath+"search.html";
    }

    /**
     * 进入搜索商品的页面
     * @param name
     * @param aTypeId
     * @param model
     * @return
     */
    @RequestMapping("/showSearchGoods")
    public String showSearchGoods(String name, Long aTypeId,Model model){

        model.addAttribute("name",name);
        model.addAttribute("aTypeId",aTypeId);
        return viewWapPath+"searchGoods.html";
    }


    /**
     * 搜索出来的商品列表
     * @param lastId    最后一个商品的ID
     * @param model     活动类型ID
     * @param name      线路商品名称
     * @return  列表
     */
    @RequestMapping(value = {"/searchGoods"})
    public String searchGoods(Long lastId, String name, Long aTypeId, Model model) {

        ActivityType activityType=aTypeId==null?null:activityTypeRepository.findOne(aTypeId);
        List<TouristGood> goods=touristGoodService.touristGoodList(null,name,null,null,activityType,
                TouristCheckStateEnum.CheckFinish, null, new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "id")), lastId)
                .getContent();
        model.addAttribute("list",goods);
        model.addAttribute("name",name);
        model.addAttribute("aTypeId",aTypeId);
        return viewWapPath+"searchGoodsListDiv.html";
    }


}
