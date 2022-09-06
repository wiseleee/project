package kr.co.seoulit.logistics.purcstosvc.stock.controller;

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

import kr.co.seoulit.logistics.purcstosvc.stock.service.StockService;
import kr.co.seoulit.logistics.purcstosvc.stock.to.BomDeployTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.BomInfoTO;
import kr.co.seoulit.logistics.purcstosvc.stock.to.BomTO;

@RestController
@RequestMapping("/stock/*")
public class BomController {

	@Autowired
	private StockService stockService;
	
	ModelMap map = null;

	private static Gson gson = new GsonBuilder().serializeNulls().create();

	@RequestMapping(value="/bom/deploy", method=RequestMethod.GET)
	public ModelMap searchBomDeploy(@RequestParam("deployCondition")String deployCondition,
									@RequestParam("itemCode")String itemCode,
									@RequestParam("itemClassificationCondition")String itemClassificationCondition) {

		map= new ModelMap();
		try {
			ArrayList<BomDeployTO> bomDeployList = stockService.getBomDeployList(deployCondition, itemCode, itemClassificationCondition);

			map.put("gridRowJson", bomDeployList);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
	}

	@RequestMapping(value="/bom/info", method=RequestMethod.GET)
	public ModelMap searchBomInfo(@RequestParam("parentItemCode")String parentItemCode) {

		map= new ModelMap();
		try {
			ArrayList<BomInfoTO> bomInfoList = stockService.getBomInfoList(parentItemCode);

			map.put("gridRowJson", bomInfoList);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");

		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());

		}
		
		return map;
	}

	@RequestMapping(value="/bom/available", method=RequestMethod.GET)
	public ModelMap searchAllItemWithBomRegisterAvailable(HttpServletRequest request, HttpServletResponse response) {
		map= new ModelMap();
		try {
			ArrayList<BomInfoTO> allItemWithBomRegisterAvailable = stockService.getAllItemWithBomRegisterAvailable();

			map.put("gridRowJson", allItemWithBomRegisterAvailable);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		} 
		return map;
	}

	@RequestMapping(value="/bom/batch", method=RequestMethod.POST)
	public ModelMap batchBomListProcess(@RequestParam("batchList")String batchList) {

		map= new ModelMap();
		ArrayList<BomTO> batchBomList = gson.fromJson(batchList, new TypeToken<ArrayList<BomTO>>() {
		}.getType());
		try {
			HashMap<String, Object> resultList = stockService.batchBomListProcess(batchBomList);

			map.put("result", resultList);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		} 
		return map;
	}

}
