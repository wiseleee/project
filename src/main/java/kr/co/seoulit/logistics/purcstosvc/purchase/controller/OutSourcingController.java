package kr.co.seoulit.logistics.purcstosvc.purchase.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.seoulit.logistics.purcstosvc.purchase.service.PurchaseService;
import kr.co.seoulit.logistics.purcstosvc.purchase.to.OutSourcingTO;

@RestController
@RequestMapping("/purchase/*")
public class OutSourcingController {

	@Autowired
	private PurchaseService purchaseService;

	ModelMap map = null;

	@RequestMapping(value="/outsourcing/list" , method=RequestMethod.GET)
	public ModelMap searchOutSourcingList(@RequestParam("fromDate")String fromDate,
										  @RequestParam("toDate")String toDate,
										  @RequestParam("customerCode")String customerCode,
										  @RequestParam("itemCode")String itemCode,
										  @RequestParam("materialStatus")String materialStatus) {

		map = new ModelMap();
		
		try {
			ArrayList<OutSourcingTO> outSourcingList = purchaseService.searchOutSourcingList(fromDate,toDate,customerCode,itemCode,materialStatus);
			map.put("outSourcingList", outSourcingList);
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
