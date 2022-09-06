package kr.co.seoulit.logistics.prodcsvc.production.controller;

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

import kr.co.seoulit.logistics.prodcsvc.production.service.ProductionService;
import kr.co.seoulit.logistics.prodcsvc.production.to.MrpGatheringTO;
import kr.co.seoulit.logistics.prodcsvc.production.to.MrpTO;

@RestController
@RequestMapping("/production/*")
public class MrpController {

	@Autowired
	private ProductionService productionService;
	
	ModelMap map = null;

	private static Gson gson = new GsonBuilder().serializeNulls().create();	
	
	@RequestMapping(value="/mrp/list", method=RequestMethod.GET)
	public ModelMap getMrpList(@RequestParam("mrpGatheringStatusCondition")String mrpGatheringStatusCondition,
							   @RequestParam("dateSearchCondition")String dateSearchCondition,
							   @RequestParam("mrpStartDate")String mrpStartDate,
							   @RequestParam("mrpEndDate")String mrpEndDate,
							   @RequestParam("mrpGatheringNo")String mrpGatheringNo) {

		map = new ModelMap();
		try {
			ArrayList<MrpTO> mrpList = null;
			
			if(mrpGatheringStatusCondition != null ) {
				mrpList = productionService.searchMrpList(mrpGatheringStatusCondition);
			} else if (dateSearchCondition != null) {
				mrpList = productionService.selectMrpListAsDate(dateSearchCondition, mrpStartDate, mrpEndDate);
			} else if (mrpGatheringNo != null) {
				mrpList = productionService.searchMrpListAsMrpGatheringNo(mrpGatheringNo);
			}
			map.put("gridRowJson", mrpList);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
	}
	
	
	@RequestMapping(value="/mrp/open", method=RequestMethod.GET)
	public ModelMap openMrp(@RequestParam("mpsNoList")String mpsNoListStr) {

		map = new ModelMap();
		try {
			ArrayList<String> mpsNoArr = gson.fromJson(mpsNoListStr,
					new TypeToken<ArrayList<String>>() { }.getType());
			HashMap<String, Object> mrpMap = productionService.openMrp(mpsNoArr);
			
			map.put("gridRowJson", mrpMap.get("result"));
			map.put("errorCode", mrpMap.get("errorCode"));
			map.put("errorMsg", mrpMap.get("errorMsg"));
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
	}

	
	@RequestMapping(value="/mrp", method=RequestMethod.POST)
	public ModelMap registerMrp(@RequestParam("batchList")String batchList,
								@RequestParam("mrpRegisterDate")String mrpRegisterDate) {

		map = new ModelMap();
		try {
			ArrayList<String> mpsList = gson.fromJson(batchList, 
					new TypeToken<ArrayList<String>>() { }.getType());
			HashMap<String, Object> resultMap = productionService.registerMrp(mrpRegisterDate, mpsList);	 
			
			map.put("result", resultMap.get("result"));
			map.put("errorCode", resultMap.get("errorCode"));
			map.put("errorMsg", resultMap.get("errorMsg"));
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
	}
	
	
	
	@RequestMapping(value="/mrp/gathering-list", method=RequestMethod.GET)
	public ModelMap getMrpGatheringList(@RequestParam("mrpNoList")String mrpNoList) {

		map = new ModelMap();
		try {
			ArrayList<String> mrpNoArr = gson.fromJson(mrpNoList,
					new TypeToken<ArrayList<String>>() { }.getType());
			ArrayList<MrpGatheringTO> mrpGatheringList = productionService.getMrpGathering(mrpNoArr);
			
			map.put("gridRowJson", mrpGatheringList);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
	}
	
	

	@RequestMapping(value="/mrp/gathering", method=RequestMethod.POST)
	public ModelMap registerMrpGathering(@RequestParam("mrpGatheringRegisterDate")String mrpGatheringRegisterDate,
										 @RequestParam("mrpNoList")String mrpNoList,
										 @RequestParam("mrpNoAndItemCodeList")String mrpNoAndItemCodeList) {

		map = new ModelMap();
		try {
			ArrayList<String> mrpNoArr = gson.fromJson(mrpNoList,
					new TypeToken<ArrayList<String>>() { }.getType());
			HashMap<String, String> mrpNoAndItemCodeMap =  gson.fromJson(mrpNoAndItemCodeList, //mprNO : ItemCode 
	              new TypeToken<HashMap<String, String>>() { }.getType());
			HashMap<String, Object> resultMap  = productionService.registerMrpGathering(mrpGatheringRegisterDate, mrpNoArr,mrpNoAndItemCodeMap);	 
			
			map.put("result", resultMap);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
	}
	

	@RequestMapping(value="/mrp/mrpgathering/list", method=RequestMethod.GET)
	public ModelMap searchMrpGathering(@RequestParam("searchDateCondition")String searchDateCondition,
									   @RequestParam("mrpGatheringStartDate")String startDate,
									   @RequestParam("mrpGatheringEndDate")String endDate) {

		map = new ModelMap();
		try {
			ArrayList<MrpGatheringTO> mrpGatheringList = 
					productionService.searchMrpGatheringList(searchDateCondition, startDate, endDate);
				
			map.put("gridRowJson", mrpGatheringList);
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
