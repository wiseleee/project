package kr.co.seoulit.logistics.purcstosvc.purchase.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import kr.co.seoulit.logistics.purcstosvc.purchase.service.PurchaseService;
import kr.co.seoulit.logistics.purcstosvc.purchase.to.OrderInfoTO;

@RestController
@RequestMapping("/purchase/*")
public class OrderController {
	
	@Autowired
	private PurchaseService purchaseService;

	ModelMap map = null;

	private static Gson gson = new GsonBuilder().serializeNulls().create();
	
	@RequestMapping(value="/order" , method=RequestMethod.GET)
	public ModelMap checkOrderInfo(@RequestParam("orderNoList")String orderNoListStr) {

		map = new ModelMap();
		try {
			ArrayList<String> orderNoArr = gson.fromJson(orderNoListStr,
					new TypeToken<ArrayList<String>>(){}.getType());
			
			map = purchaseService.checkOrderInfo(orderNoArr);
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		} 
		return map;
	}
	
	@RequestMapping(value="/order/list" , method=RequestMethod.GET)
	public ModelMap getOrderList(@RequestParam("startDate")String startDate,@RequestParam("endDate")String endDate) {

		map = new ModelMap();
		try {
			HashMap<String, Object> resultMap = purchaseService.getOrderList(startDate, endDate);
			map.put("gridRowJson",resultMap.get("gridRowJson"));
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		} 
		return map;
	}

	@RequestMapping(value="/order/dialog" , method=RequestMethod.GET)
	public ModelMap openOrderDialog(@RequestParam("mrpGatheringNoList")String mrpNoListStr) {
		

		
		HashMap<String,Object> resultMap = new HashMap<>();
		ModelMap map = new ModelMap();
		
		try {

			resultMap = purchaseService.getOrderDialogInfo(mrpNoListStr);
			
			map.put("gridRowJson",resultMap.get("gridRowJson"));
			map.put("errorCode", resultMap.get("errorCode"));
			map.put("errorMsg", resultMap.get("errorMsg"));
			
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		} 
		return map;
	}

	@RequestMapping(value="/order/info" , method=RequestMethod.GET)
	public ModelMap showOrderInfo(@RequestParam("startDate")String startDate,@RequestParam("endDate")String endDate) {

		map = new ModelMap();
		try {
			ArrayList<OrderInfoTO> orderInfoList = purchaseService.getOrderInfoList(startDate,endDate);
			map.put("gridRowJson", orderInfoList);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공!");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		} 
		return map;
	}
	
	@RequestMapping(value="/order/delivery" , method=RequestMethod.GET)
	public ModelMap searchOrderInfoListOnDelivery(HttpServletRequest request, HttpServletResponse response) {
		map = new ModelMap();
		try {
			ArrayList<OrderInfoTO> orderInfoListOnDelivery = purchaseService.getOrderInfoListOnDelivery();
			map.put("gridRowJson", orderInfoListOnDelivery);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공!");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		} 
		return map;
	}

	@RequestMapping(value="/order" , method=RequestMethod.POST)
	public ModelMap order(@RequestParam("mrpGatheringNoList")String mrpGatheringNoListStr) {

		map = new ModelMap();
		try {
			ArrayList<String> mrpGaNoArr = gson.fromJson(mrpGatheringNoListStr , new TypeToken<ArrayList<String>>() {
				}.getType());
			
			map = purchaseService.order(mrpGaNoArr);
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		} 
		return map;
	}

	@RequestMapping(value="/order/option" , method=RequestMethod.POST)
	public ModelMap optionOrder(@RequestParam("itemCode")String itemCode,@RequestParam("itemAmount")String itemAmount) {
		HashMap<String, Object> resultMap = new HashMap<>();

		map = new ModelMap();
		try {
			resultMap = purchaseService.optionOrder(itemCode, itemAmount);
			map.put("gridRowJson",resultMap);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공!");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		} 
		return map;
	}

}
