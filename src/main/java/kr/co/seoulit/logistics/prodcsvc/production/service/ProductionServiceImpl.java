package kr.co.seoulit.logistics.prodcsvc.production.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.mapper.ContractMapper;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.ContractDetailInMpsAvailableTO;
import kr.co.seoulit.logistics.busisvc.sales.mapper.SalesPlanMapper;
import kr.co.seoulit.logistics.prodcsvc.production.mapper.MpsMapper;
import kr.co.seoulit.logistics.prodcsvc.production.mapper.MrpMapper;
import kr.co.seoulit.logistics.prodcsvc.production.to.MpsTO;
import kr.co.seoulit.logistics.prodcsvc.production.to.MrpGatheringTO;
import kr.co.seoulit.logistics.prodcsvc.production.to.MrpTO;
import kr.co.seoulit.logistics.prodcsvc.production.to.SalesPlanInMpsAvailableTO;

@Service
public class ProductionServiceImpl implements ProductionService {
	
	@Autowired
	private MpsMapper mpsMapper;
	@Autowired
	private ContractMapper contractMapper;
	@Autowired
	private SalesPlanMapper salesPlanMapper;
	@Autowired
	private MrpMapper mrpMapper;

	@Override
	public ArrayList<MpsTO> getMpsList(String startDate, String endDate, String includeMrpApply) {

		ArrayList<MpsTO> mpsTOList = null;
		
		HashMap<String, String> map = new HashMap<>();

		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("includeMrpApply", includeMrpApply);
		
		mpsTOList = mpsMapper.selectMpsList(map);

		return mpsTOList;
	}

	@Override
	public ArrayList<ContractDetailInMpsAvailableTO> getContractDetailListInMpsAvailable(String searchCondition,
			String startDate, String endDate) {

		ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList = null;
		
		HashMap<String, String> map = new HashMap<>();
		
		map.put("searchCondition", searchCondition);
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		contractDetailInMpsAvailableList = contractMapper.selectContractDetailListInMpsAvailable(map);
		
		return contractDetailInMpsAvailableList;

	}

	public ArrayList<ContractDetailInMpsAvailableTO> getContractDetailListInProcessPlanAvailable(String searchCondition,
			String startDate, String endDate) {

		ArrayList<ContractDetailInMpsAvailableTO> contractDetailInProcessPlanAvailableList = null;

		HashMap<String, String> map = new HashMap<>();

		map.put("searchCondition", searchCondition);
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		contractDetailInProcessPlanAvailableList = contractMapper.selectContractDetailListInProcessPlanAvailable(map);

		return contractDetailInProcessPlanAvailableList;

	}



	@Override
	public ArrayList<SalesPlanInMpsAvailableTO> getSalesPlanListInMpsAvailable(String searchCondition,
			String startDate, String endDate) {

		ArrayList<SalesPlanInMpsAvailableTO> salesPlanInMpsAvailableList = null;
		
		HashMap<String, String> map = new HashMap<>();

		map.put("searchCondition", searchCondition);
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		salesPlanInMpsAvailableList = salesPlanMapper.selectSalesPlanListInMpsAvailable(map);

		return salesPlanInMpsAvailableList;

	}

	@Override
	public HashMap<String, Object> convertContractDetailToMps(
			ArrayList<ContractDetailInMpsAvailableTO> contractDetailInMpsAvailableList) {
		
		HashMap<String, Object> resultMap = null;

		ArrayList<MpsTO> mpsTOList = new ArrayList<>();

		MpsTO newMpsBean = null;
//mps를 담은 arraylist를 내보냄
		for (ContractDetailInMpsAvailableTO bean : contractDetailInMpsAvailableList) {

			newMpsBean = new MpsTO();

			newMpsBean.setStatus("INSERT");

			newMpsBean.setMpsPlanClassification(bean.getPlanClassification());
			newMpsBean.setContractDetailNo(bean.getContractDetailNo());
			newMpsBean.setItemCode(bean.getItemCode());
			newMpsBean.setItemName(bean.getItemName());
			newMpsBean.setUnitOfMps(bean.getUnitOfContract());
			newMpsBean.setMpsPlanDate(bean.getMpsPlanDate());
			newMpsBean.setMpsPlanAmount(bean.getProductionRequirement());
			newMpsBean.setDueDateOfMps(bean.getDueDateOfContract());
			newMpsBean.setScheduledEndDate(bean.getScheduledEndDate());
			newMpsBean.setDescription(bean.getDescription());

			mpsTOList.add(newMpsBean);

		}

		resultMap = batchMpsListProcess(mpsTOList); //batchMpsListProcess 메소드 호출

		return resultMap;

	}

	@Override
	public HashMap<String, Object> convertSalesPlanToMps(
			ArrayList<SalesPlanInMpsAvailableTO> salesPlanInMpsAvailableList) {

		HashMap<String, Object> resultMap = null;

		ArrayList<MpsTO> mpsTOList = new ArrayList<>();

		MpsTO newMpsBean = null;

		for (SalesPlanInMpsAvailableTO bean : salesPlanInMpsAvailableList) {

			newMpsBean = new MpsTO();

			newMpsBean.setStatus("INSERT");

			newMpsBean.setMpsPlanClassification(bean.getPlanClassification());
			newMpsBean.setSalesPlanNo(bean.getSalesPlanNo());
			newMpsBean.setItemCode(bean.getItemCode());
			newMpsBean.setItemName(bean.getItemName());
			newMpsBean.setUnitOfMps(bean.getUnitOfSales());
			newMpsBean.setMpsPlanDate(bean.getMpsPlanDate());
			newMpsBean.setMpsPlanAmount(bean.getSalesAmount());
			newMpsBean.setDueDateOfMps(bean.getDueDateOfSales());
			newMpsBean.setScheduledEndDate(bean.getScheduledEndDate());
			newMpsBean.setDescription(bean.getDescription());

			mpsTOList.add(newMpsBean);

		}

		resultMap = batchMpsListProcess(mpsTOList);

		return resultMap;
//DB에 안갔다오는지?
	}

	@Override
	public HashMap<String, Object> batchMpsListProcess(ArrayList<MpsTO> mpsTOList) {

		HashMap<String, Object> resultMap = new HashMap<>();
		
		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();

		for (MpsTO bean : mpsTOList) {

			String status = bean.getStatus();
							
			switch (status) {

			case "INSERT":

				String newMpsNo = getNewMpsNo(bean.getMpsPlanDate());
				//MPS번호생성
				bean.setMpsNo(newMpsNo);

				mpsMapper.insertMps(bean);

				insertList.add(newMpsNo);

				if (bean.getContractDetailNo() != null) {

					changeMpsStatusInContractDetail(bean.getContractDetailNo(), "Y");
				//MPS TO수주상세번호가 존재하면 수주상세테이블에서 해당 번호의 MPS적용상태를 Y로 변경
				} else if (bean.getSalesPlanNo() != null) {

					changeMpsStatusInSalesPlan(bean.getSalesPlanNo(), "Y");

				}

				break;

			case "UPDATE":

				mpsMapper.updateMps(bean);

				updateList.add(bean.getMpsNo());

				break;

			case "DELETE":

				mpsMapper.deleteMps(bean);

				deleteList.add(bean.getMpsNo());

				break;

			}

		}

		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;

	}

	@Override
	public ArrayList<MrpTO> searchMrpList(String mrpGatheringStatusCondition) {

		ArrayList<MrpTO> mrpList = null;

		mrpList = mrpMapper.selectMrpList(mrpGatheringStatusCondition);

		return mrpList;

	}

	@Override
	public ArrayList<MrpTO> selectMrpListAsDate(String dateSearchCondtion, String startDate, String endDate) {

		ArrayList<MrpTO> mrpList = null;
		
		HashMap<String, String> map = new HashMap<>();

		map.put("dateSearchCondtion", dateSearchCondtion);
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		mrpList = mrpMapper.selectMrpListAsDate(map);

		return mrpList;
	}

	@Override
	public ArrayList<MrpTO> searchMrpListAsMrpGatheringNo(String mrpGatheringNo) {

		ArrayList<MrpTO> mrpList = null;

		mrpList = mrpMapper.selectMrpListAsMrpGatheringNo(mrpGatheringNo);

		return mrpList;
	}

	@Override
	public ArrayList<MrpGatheringTO> searchMrpGatheringList(String dateSearchCondtion, String startDate,
			String endDate) {

		ArrayList<MrpGatheringTO> mrpGatheringList = null;
		
		HashMap<String, String> map = new HashMap<>();

		map.put("dateSearchCondtion", dateSearchCondtion);
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		mrpGatheringList = mrpMapper.selectMrpGatheringList(map);

		for(MrpGatheringTO bean : mrpGatheringList)    {
	            
	    	bean.setMrpTOList(  mrpMapper.selectMrpListAsMrpGatheringNo( bean.getMrpGatheringNo()) );
	         
		}

		return mrpGatheringList;
	}

	@Override
	public HashMap<String, Object> openMrp(ArrayList<String> mpsNoArr) {
		//클릭한 mps값은 배열
		String mpsNoList = mpsNoArr.toString().replace("[", "").replace("]", "");
		//ArrayList -> String으로 다시바꿈
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        
        HashMap<String, String> map = new HashMap<String, String>();
        
        map.put("mpsNoList", mpsNoList);
        
        mrpMapper.openMrp(map);
        
        System.out.println(map);

		resultMap.put("result", map.get("RESULT")); //프로시저의 out객체라 get으로 가져옴
		resultMap.put("errorCode",map.get("ERROR_CODE"));
		resultMap.put("errorMsg", map.get("ERROR_MSG"));
		
		return resultMap;
	}
	
	@Override
	public HashMap<String, Object> registerMrp(String mrpRegisterDate, ArrayList<String> mpsList) {

	      HashMap<String, Object> resultMap = new HashMap<String, Object>();
	      
	      HashMap<String, Object> map = new HashMap<String, Object>();
	      
	      map.put("mrpRegisterDate", mrpRegisterDate);
		
		 mrpMapper.insertMrpList(map);
		
	      resultMap.put("result", map.get("RESULT"));
		  resultMap.put("errorCode", map.get("ERROR_CODE"));
		  resultMap.put("errorMsg", map.get("ERROR_MSG"));

		for (String mpsNo : mpsList) {
			
			HashMap<String, String> mpsMap = new HashMap<>();

			mpsMap.put("mpsNo", mpsNo);
			mpsMap.put("mrpStatus", "Y");

			mpsMapper.changeMrpApplyStatus(mpsMap);
	            
		}

		return resultMap;
	}

	@Override
	public HashMap<String, Object> batchMrpListProcess(ArrayList<MrpTO> mrpTOList) {
		
		HashMap<String, Object> resultMap = new HashMap<>();

		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();

		for (MrpTO bean : mrpTOList) {

			String status = bean.getStatus();

			switch (status) {

				case "INSERT":

	               mrpMapper.insertMrp(bean);

	               insertList.add(bean.getMrpNo());

	               break;

	            case "UPDATE":

	               mrpMapper.updateMrp(bean);
	               
	               updateList.add(bean.getMrpNo());

	               break;

	            case "DELETE":

	               mrpMapper.deleteMrp(bean);

	               deleteList.add(bean.getMrpNo());

	               break;

	            }

	         }

		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public ArrayList<MrpGatheringTO> getMrpGathering(ArrayList<String> mrpNoArr) {
		
		ArrayList<MrpGatheringTO> mrpGatheringList = null;
		//mrp번호 배열 [mrp번호,mrp번호...] => "mrp번호,mrp번호..." 형식의 문자열로 변환
		String mrpNoList = mrpNoArr.toString().replace("[", "").replace("]", "");
		mrpGatheringList = mrpMapper.getMrpGathering(mrpNoList);

		return mrpGatheringList;
	}
	
	@Override
	public HashMap<String, Object> registerMrpGathering(String mrpGatheringRegisterDate,ArrayList<String> mrpNoArr,HashMap<String, String> mrpNoAndItemCodeMap) {
		
		HashMap<String, Object> resultMap = null;
	    int seq=0;
	    ArrayList<MrpGatheringTO> mrpGatheringList = null;
		int i=1;
	    List<MrpGatheringTO> list= mrpMapper.selectMrpGatheringCount(mrpGatheringRegisterDate);//해당 소요량 취합결과 등록날짜에 이미 등록된 취합결과 검색하여 없으면 1, 있으면 최대값+1 리턴
		//소요량 취합일자로 새로운 소요량 취합번호 확인
		TreeSet<Integer> intSet = new TreeSet<>();
		for(MrpGatheringTO bean : list) {
			String mrpGatheringNo = bean.getMrpGatheringNo();
			int no = Integer.parseInt(mrpGatheringNo.substring(mrpGatheringNo.length() - 2, mrpGatheringNo.length()));//substring 첫번째인자값:시작부분 지정, 두번째값:끝부분지정
			intSet.add(no); //MrpGathering 일련번호에서 마지막 2자리 가져오기
		}
		if (!intSet.isEmpty()) {
			i=intSet.pollLast() + 1;//가장높은번호+1
		}

	    HashMap<String, String> itemCodeAndMrpGatheringNoMap = new HashMap<>();

	    StringBuffer newMrpGatheringNo = new StringBuffer();
	    newMrpGatheringNo.append("MG"); 
	    newMrpGatheringNo.append(mrpGatheringRegisterDate.replace("-", ""));//(오늘날짜-xx)
	    newMrpGatheringNo.append("-");
	         
	    seq=mrpMapper.getMGSeqNo();
	         
	    mrpGatheringList = getMrpGathering(mrpNoArr); //소요량 취합결과를 ArrayList<MrpGatheringTO>로 리턴(행 모두를 가져옴)

	    for (MrpGatheringTO bean : mrpGatheringList) { 
	    	bean.setMrpGatheringNo(newMrpGatheringNo.toString() + String.format("%03d", i++));//MrpGathering 빈 객체에 MRP_MrpGathering_NO부여
	    	bean.setStatus("INSERT");//MrpGathering 빈 객체에 상태를 INSERT로 부여
	    	bean.setMrpGatheringSeq(seq);//MrpGathering 빈 객체에 MRP_GATHERING_SEQ부여

	    	itemCodeAndMrpGatheringNoMap.put(bean.getItemCode(), bean.getMrpGatheringNo());
			//MrpGathering 빈 객체에 {itemCode : mrpGatheringNo}
			//ex)itemCodeAndMrpGatheringNoMap = {a:001, b:002, c:003...} mrpGathering을 한줄한줄 돌린다
	    }

	    resultMap = batchMrpGatheringListProcess(mrpGatheringList);//MRP_GATHERING테이블에 INSERT함(ArrayList<MrpGatheringTO>)의 모든 빈을 insert
									//insert처리한 MrpGatheringTO객체->ArrayList에 MrpGatheringNO를 담고, Map에 {itemCode : mrpGatheringNo}를 담음
	    TreeSet<String> mrpGatheringNoSet = new TreeSet<>();

	    @SuppressWarnings("unchecked")
	    HashMap<String, String> mrpGatheringNoList = (HashMap<String, String>) resultMap.get("INSERT_MAP");//key(ItemCode):value(소요량취합번호) map을 받아옴
	    for (String mrpGatheringNo : mrpGatheringNoList.values()) {
	    	
	    	mrpGatheringNoSet.add(mrpGatheringNo);//mrpGatheringNo 만을 TreeSet<String>에 담음
	            //mrpGatheringNoSet={001,002,003...}
	    }

	    resultMap.put("firstMrpGatheringNo", mrpGatheringNoSet.pollFirst());//최초 mrpGathering 번호를 결과 Map에 저장
	    resultMap.put("lastMrpGatheringNo", mrpGatheringNoSet.pollLast());//마지막 mrpGathering 번호를 결과 Map에 저장

	    for (String mrpNo : mrpNoAndItemCodeMap.keySet()) {//mrpNoAndItemCodeMap는 세번째 파라미터, 소요량 취합되지 않은 mrp의 {itemCode : mrpGatheringNo}
	    	String itemCode = mrpNoAndItemCodeMap.get(mrpNo);
	    	String mrpGatheringNo = itemCodeAndMrpGatheringNoMap.get(itemCode);//mrpno-itemcode-mrpgatheringno순으로 찾음
	    	//mrpno는 달라도 mrpGatheringNo는 같다
	    	HashMap<String, String> map = new HashMap<>();

	    	map.put("mrpNo", mrpNo);
	    	map.put("mrpGatheringNo", mrpGatheringNo);
	    	map.put("mrpGatheringStatus", "Y");
	    	
	    	mrpMapper.changeMrpGatheringStatus(map);//mrp테이블에서 해당 mrpno의 mrpgatheringno저장
	    }//changeMrpGatheringStatus={0000001,0000002,0000003...}
	         
	    String mrpNoList = mrpNoArr.toString().replace("[", "").replace("]", "");
		//mrp적용상태를 Y로 변경한 MRP번호들을 결과
	    resultMap.put("changeMrpGatheringStatus", mrpNoList);
		//changeMrpGatheringStatus=mrpno 모달창에 xx번~xx번까지 소요량 취합되었습니다. 때문에 구하는것
	    StringBuffer sb = new StringBuffer();
	 		
	    for(String mrpGatheringNo : mrpGatheringNoList.values()) { //mrpGatheringNoList={001,002,003...}
	    	sb.append(mrpGatheringNo);
	    	sb.append(",");
	    }
	    sb.delete(sb.toString().length()-1, sb.toString().length());//마지막에 붙은 ,가 잘려짐
	    
	    HashMap<String, String> parameter = new HashMap<>();
	    parameter.put("mrpGatheringNo", sb.toString());
	    mrpMapper.updateMrpGatheringContract(parameter);

		return resultMap;
	}
	public String getNewMpsNo(String mpsPlanDate) {
		StringBuffer newEstimateNo = null;
		List<MpsTO> mpsTOlist = mpsMapper.selectMpsCount(mpsPlanDate);
		TreeSet<Integer> intSet = new TreeSet<>();
		for (MpsTO bean : mpsTOlist) {
			String mpsNo = bean.getMpsNo();
			// MPS 일련번호에서 마지막 2자리만 가져오기
			int no = Integer.parseInt(mpsNo.substring(mpsNo.length() - 2, mpsNo.length()));
			intSet.add(no);	
		}
		int i=1;
		if (!intSet.isEmpty()) {
			i=intSet.pollLast() + 1;
		}

		newEstimateNo = new StringBuffer();
		newEstimateNo.append("PS");
		newEstimateNo.append(mpsPlanDate.replace("-", ""));
		newEstimateNo.append(String.format("%02d", i)); //PS2020042401 맨뒤2자리는 오늘 등록한 순서대로 번호

		return newEstimateNo.toString();
	}

	public void changeMpsStatusInContractDetail(String contractDetailNo, String mpsStatus) {

		HashMap<String, String> map = new HashMap<>();

		map.put("contractDetailNo", contractDetailNo);
		map.put("mpsStatus", mpsStatus);
		
		contractMapper.changeMpsStatusOfContractDetail(map);

	}

	public void changeMpsStatusInSalesPlan(String salesPlanNo, String mpsStatus) {

		HashMap<String, String> map = new HashMap<>();

		map.put("salesPlanNo", salesPlanNo);
		map.put("mpsStatus", mpsStatus);
		
		salesPlanMapper.changeMpsStatusOfSalesPlan(map);

	}

	public HashMap<String, Object> batchMrpGatheringListProcess(ArrayList<MrpGatheringTO> mrpGatheringTOList) {

		HashMap<String, Object> resultMap = new HashMap<>();

		 HashMap<String, String> insertListMap = new HashMap<>(); 
		 ArrayList<String> insertList = new ArrayList<>();
		 ArrayList<String> updateList = new ArrayList<>();
		 ArrayList<String> deleteList = new ArrayList<>();

		 for (MrpGatheringTO bean : mrpGatheringTOList) { //소요량 취합결과 그리드에 뿌려진 데이터값
		            
			 String status = bean.getStatus();
		            
			 switch (status) {

			 	case "INSERT":

			 			mrpMapper.insertMrpGathering(bean);
		               
			 			insertList.add(bean.getMrpGatheringNo());
						//소요량 취합번호 추가
			 			insertListMap.put(bean.getItemCode(), bean.getMrpGatheringNo());
						//map에 key(ItemCode), value(MrpGatheringNo)
			 			break;

			 	case "UPDATE":

			 		mrpMapper.updateMrpGathering(bean);

			 		updateList.add(bean.getMrpGatheringNo());

			 		break;

			 	case "DELETE":

			 		mrpMapper.deleteMrpGathering(bean);

			 		deleteList.add(bean.getMrpGatheringNo());

			 		break;

			 }

		 }

		 resultMap.put("INSERT_MAP", insertListMap); //key(ItemCode) : value(getMrpGatheringNo)
		 resultMap.put("INSERT", insertList); //소요량취합번호
		 resultMap.put("UPDATE", updateList);
		 resultMap.put("DELETE", deleteList);

		 return resultMap;
		   }
}
