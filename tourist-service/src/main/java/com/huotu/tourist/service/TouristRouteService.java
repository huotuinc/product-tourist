package com.huotu.tourist.service;

import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristOrder;
import com.huotu.tourist.entity.TouristRoute;

import java.io.IOException;
import java.time.LocalDateTime;

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

    /**
     * 获取该订单的行程的剩余人数
     * @param order         订单
     * @return              剩余人数
     * @throws IOException
     */
    int getRemainPeopleByRoute(TouristOrder order) throws IOException;

    /**
     * 保存一个线路行程(包括新增和修改)
     * @param id        行程ID(null:新增,有数据：修改)
     * @param routeNo   行程号
     * @param good      线路商品(必须)
     * @param fromDate  出行时间(必须)
     * @param toDate    结束时间
     * @param maxPeople 总人数
     */
    TouristRoute saveTouristRoute(Long id, String routeNo, TouristGood good, LocalDateTime fromDate, LocalDateTime toDate
            , int maxPeople);

    /**
     * 判断一个行程是否已经售出
     * @param route     行程
     * @return
     */
    boolean judgeRouteIsSold(TouristRoute route);
}
