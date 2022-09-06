package kr.co.seoulit.logistics.busisvc.sales.controller.logisales.controller;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.service.LogisalesService;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.EstimateDetailTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.EstimateTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping("/logisales/*")
public class EstimateController {

	@Autowired
	private LogisalesService logisalesService;

	ModelMap map=null;
	
	// GSON 라이브러리
	private static Gson gson = new GsonBuilder().serializeNulls().create(); // 속성값이 null 인 속성도 json 변환

	@RequestMapping(value="/estimate/list", method=RequestMethod.GET)
	public ModelMap searchEstimateInfo(
			@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate,
			@RequestParam("dateSearchCondition") String dateSearchCondition
		) {
		map = new ModelMap();
		
		try {
			ArrayList<EstimateTO> estimateTOList = logisalesService.getEstimateList(dateSearchCondition, startDate, endDate);

			map.put("gridRowJson", estimateTOList);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
		
	}

	@RequestMapping(value="/estimatedetail/list", method=RequestMethod.GET)
	public ModelMap searchEstimateDetailInfo(@RequestParam("estimateNo") String estimateNo) {
		
		map = new ModelMap();
		
		try {
			ArrayList<EstimateDetailTO> estimateDetailTOList = logisalesService.getEstimateDetailList(estimateNo);

			map.put("gridRowJson", estimateDetailTOList);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
	}

	@RequestMapping(value="/estimate/new", method=RequestMethod.POST)
	public ModelMap addNewEstimate(
			@RequestParam("estimateDate") String estimateDate,
			@RequestParam("newEstimateInfo") String newEstimateInfo
		) {
		EstimateTO newEstimateTO = gson.fromJson(newEstimateInfo, EstimateTO.class);
		
		map = new ModelMap();
		
		try {
			HashMap<String, Object> resultList = logisalesService.addNewEstimate(estimateDate, newEstimateTO);
			
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
	@RequestMapping(value="/estimate", method=RequestMethod.DELETE)
	public ModelMap deleteEstimateInfo(
			@RequestParam("estimateNo") String estimateNo,
			@RequestParam("status") String status
		) {

		map = new ModelMap();

		try {
			HashMap<String, Object> resultList = logisalesService.removeEstimate(estimateNo, status);
		
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

	@RequestMapping(value="/estimatedetail/batch", method=RequestMethod.POST)
	public ModelMap batchListProcess(@RequestParam("batchList") String batchList) {
		
		//String estimateNo = request.getParameter("estimateNo");
		ArrayList<EstimateDetailTO> estimateDetailTOList = gson.fromJson(batchList,
				new TypeToken<ArrayList<EstimateDetailTO>>() {}.getType());
		
		map = new ModelMap();
		
		try {
			HashMap<String, Object> resultList = logisalesService.batchEstimateDetailListProcess(estimateDetailTOList,estimateDetailTOList.get(0).getEstimateNo());

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
