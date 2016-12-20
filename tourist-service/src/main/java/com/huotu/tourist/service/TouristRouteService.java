package com.huotu.tourist.service;

import com.huotu.tourist.entity.TouristRoute;

import java.io.IOException;

/**
 * 线路行程路线服务
 * Created by slt on 2016/12/19.
 */
public interface TouristRouteService {

    /**
     * 获取所给线路行程的剩余人数
     * @param route     线路行程
     * @return
     * @throws IOException IO异常，获取人数出错
     */
    int getRemainPeopleByRoute(TouristRoute route) throws IOException;

//    /**
//     * TouristRoute与TouristRouteModel的转换
//     * @param routes    线路行程
//     * @return
//     */
//    List<TouristRouteModel> touristRouteModelConver(List<TouristRoute> routes);

}
