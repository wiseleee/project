package kr.co.seoulit.logistics.busisvc.sales.controller.logisales.service;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.ContractDetailTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.ContractInfoTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.EstimateDetailTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.EstimateTO;
import org.springframework.ui.ModelMap;

public interface LogisalesService {

	
	// EstimateApplicationServiceImpl
	public ArrayList<EstimateTO> getEstimateList(String dateSearchCondition, String startDate, String endDate);

	public ArrayList<EstimateDetailTO> getEstimateDetailList(String estimateNo);
	
	public HashMap<String, Object> addNewEstimate(String estimateDate, EstimateTO newEstimateTO);
	
	public HashMap<String, Object> removeEstimate(String estimateNo, String status);

	public HashMap<String, Object> batchEstimateDetailListProcess(ArrayList<EstimateDetailTO> estimateDetailTOList,String estimateNo);	
	
	
	// ContractApplicationServiceImpl
	public ArrayList<ContractInfoTO> getContractList(String searchCondition, String startDate, String endDate,
                                                     String customerCode);
		
	public ArrayList<ContractDetailTO> getContractDetailList(String estimateNo);
	
	public ArrayList<EstimateTO> getEstimateListInContractAvailable(String startDate, String endDate);

	public ModelMap addNewContract(HashMap<String,String[]>  workingContractList);

	public HashMap<String, Object> batchContractDetailListProcess(ArrayList<ContractDetailTO> contractDetailTOList);
	
	public void changeContractStatusInEstimate(String estimateNo , String contractStatus);

	public void processPlan(HashMap<String,String[]> processMap);
}
