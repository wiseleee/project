package kr.co.seoulit.logistics.busisvc.sales.controller.logisales.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.ContractDetailInMpsAvailableTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.ContractDetailTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.ContractInfoTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.EstimateTO;
import org.apache.ibatis.annotations.Mapper;

import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.ContractTO;

@Mapper
public interface ContractMapper {

	public ArrayList<EstimateTO> selectEstimateListInContractAvailable(HashMap<String, String> map);

	public ArrayList<ContractInfoTO> selectContractList(HashMap<String, String> map);
	
	public ArrayList<ContractInfoTO> selectDeliverableContractList(HashMap<String, String> ableSearchConditionInfo);
	
	public int selectContractCount(String contractDate);

	public void insertContract(ContractTO TO);

	public void updateContract(ContractTO TO);

	public void deleteContract(ContractTO TO);
	
	
	//ContractDetail
	public ArrayList<ContractDetailTO> selectContractDetailList(String contractNo);

	public ArrayList<ContractDetailTO> selectDeliverableContractDetailList(String contractNo);
	
	public int selectContractDetailCount(String contractNo);

	public ArrayList<ContractDetailInMpsAvailableTO> selectContractDetailListInMpsAvailable(
			HashMap<String, String> map);
//
	public ArrayList<ContractDetailInMpsAvailableTO> selectContractDetailListInProcessPlanAvailable(
			HashMap<String, String> map);
//
//	public void insertContractDetail(ContractDetailTO TO);
//
//	public void updateContractDetail(ContractDetailTO TO);

	public void changeMpsStatusOfContractDetail(HashMap<String, String> map);

	public void deleteContractDetail(ContractDetailTO TO);
	
	public void insertContractDetail(HashMap<String,String>  workingContractList);

	public void processPlan(HashMap<String, String> map);

}
