package com.huotu.tourist.service.impl;

import com.huotu.tourist.entity.TouristGood;
import com.huotu.tourist.entity.TouristRoute;
import com.huotu.tourist.repository.TouristRouteRepository;
import com.huotu.tourist.repository.TravelerRepository;
import com.huotu.tourist.service.TouristRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by lhx on 2017/1/3.
 */
@Service
public class TouristRouteServiceImpl implements TouristRouteService {
    @Autowired
    TouristRouteRepository touristRouteRepository;
    @Autowired
    TravelerRepository travelerRepository;

    @Override
    public int getRemainPeopleByRoute(TouristRoute route) throws IOException {
        int count = travelerRepository.countByRoute(route);
        return route.getMaxPeople() - count;
    }

    @Override
    public TouristRoute saveTouristRoute(Long id, String routeNo, TouristGood good, LocalDateTime fromDate, LocalDateTime
            toDate, int maxPeople) {
        TouristRoute touristRoute;
        if (id == null) {
            touristRoute = new TouristRoute();
        } else {
            touristRoute = touristRouteRepository.getOne(id);
        }
        touristRoute.setRouteNo(routeNo);
        touristRoute.setGood(good);
        touristRoute.setFromDate(fromDate);
        touristRoute.setToDate(toDate);
        touristRoute.setMaxPeople(maxPeople);
        return touristRouteRepository.saveAndFlush(touristRoute);
    }

    @Override
    public boolean judgeRouteIsSold(TouristRoute route) {
        return travelerRepository.countByRoute(route) > 0;
    }
}
