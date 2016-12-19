package com.huotu.tourist.controller.distributionPlatform;

import com.huotu.tourist.entity.TouristBuyer;
import com.huotu.tourist.entity.TouristSupplier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lhx on 2016/12/17.
 */
@Controller
@RequestMapping("/distributionPlatform/")
public class DistributionPlatformController {


    @RequestMapping(value = "supplierList", method = RequestMethod.GET)
    public ResponseEntity supplierList(String name, HttpServletRequest request, Model model) {
        return null;
    }

    @RequestMapping(value = "createSupplier", method = RequestMethod.POST)
    public String createSupplier(TouristSupplier data, HttpServletRequest request, Model model) {
        return null;
    }

    @RequestMapping(value = "updateSupplier", method = RequestMethod.PUT)
    public String updateSupplier(TouristSupplier data, HttpServletRequest request, Model model) {
        return null;
    }

    @RequestMapping(value = "buyerList", method = RequestMethod.GET)
    public ResponseEntity buyerList(String buyerName, HttpServletRequest request, Model model) {
        return null;
    }

    @RequestMapping(value = "createBuyer", method = RequestMethod.POST)
    public String createBuyer(TouristBuyer data, HttpServletRequest request, Model model) {
        return null;
    }

    @RequestMapping(value = "updateBuyer", method = RequestMethod.PUT)
    public String updateBuyer(TouristBuyer data, HttpServletRequest request, Model model) {
        return null;
    }

}
