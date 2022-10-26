package kr.co.seoulit.logistics.busisvc.sales.controller.logisales.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.mapper.ContractMapper;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.mapper.EstimateMapper;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.ContractDetailTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.ContractInfoTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.EstimateDetailTO;
import kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to.EstimateTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class LogisalesServiceImpl implements LogisalesService {
	
	@Autowired
	private ContractMapper contractMapper;
	@Autowired
	private EstimateMapper estimateMapper;


	@Override
	public ArrayList<EstimateTO> getEstimateList(String dateSearchCondition, String startDate, String endDate) {

		ArrayList<EstimateTO> estimateTOList = null;
		
		HashMap<String, String> map = new HashMap<>();
		
		map.put("dateSearchCondition", dateSearchCondition);
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		estimateTOList = estimateMapper.selectEstimateList(map);

		return estimateTOList;
	}

	@Override
	public ArrayList<EstimateDetailTO> getEstimateDetailList(String estimateNo) {

		ArrayList<EstimateDetailTO> estimateDetailTOList = null;

		estimateDetailTOList = estimateMapper.selectEstimateDetailList(estimateNo);

		return estimateDetailTOList;
	}

	@Override
	public ModelMap addNewEstimate(String estimateDate, EstimateTO newEstimateTO) {

		ModelMap resultMap = null;

		String newEstimateNo = getNewEstimateNo(estimateDate); //견적일련번호 생성

		newEstimateTO.setEstimateNo(newEstimateNo); //뷰단에서 보내온 견적 TO에 새로운 견적 일련번호 할당

		estimateMapper.insertEstimate(newEstimateTO); //db에 올림
			
		ArrayList<EstimateDetailTO> estimateDetailTOList = newEstimateTO.getEstimateDetailTOList(); //bean객체
			    //견적상세리스트
		for (EstimateDetailTO bean : estimateDetailTOList) {
			String newEstimateDetailNo = getNewEstimateDetailNo(newEstimateNo);
				//견적상세 일련번호
			bean.setEstimateNo(newEstimateNo);
				//앞에서 생성된 견적 일련번호 할당 ex)ES2022081812
			bean.setEstimateDetailNo(newEstimateDetailNo);
		}      //새롭게 생선된 견적상세 일련번호 할당 ex)ESES2022081812-01

		resultMap = batchEstimateDetailListProcess(estimateDetailTOList,newEstimateNo);

		resultMap.put("newEstimateNo", newEstimateNo);

		return resultMap;
	}

	public String getNewEstimateNo(String estimateDate) {

		StringBuffer newEstimateNo = null;

		int i = estimateMapper.selectEstimateCount(estimateDate);

		newEstimateNo = new StringBuffer();
		newEstimateNo.append("ES");
		newEstimateNo.append(estimateDate.replace("-", ""));
		newEstimateNo.append(String.format("%02d", i)); 
			
		return newEstimateNo.toString();
	}
	
	public String getNewEstimateDetailNo(String estimateNo) {

		StringBuffer newEstimateDetailNo = null;

		int i = estimateMapper.selectEstimateDetailSeq(estimateNo);

		newEstimateDetailNo = new StringBuffer();
		newEstimateDetailNo.append("ES");
		newEstimateDetailNo.append(estimateNo.replace("-", ""));
		newEstimateDetailNo.append("-"); 
		newEstimateDetailNo.append(String.format("%02d", i));		   

		return newEstimateDetailNo.toString();
	}

	@Override
	public ModelMap removeEstimate(String estimateNo, String status) {

		ModelMap resultMap = null;

		estimateMapper.deleteEstimate(estimateNo);
			
		ArrayList<EstimateDetailTO> estimateDetailTOList = getEstimateDetailList(estimateNo);
			
		for (EstimateDetailTO bean : estimateDetailTOList) {
				
			bean.setStatus(status);
				
		}
			
		resultMap = batchEstimateDetailListProcess(estimateDetailTOList,estimateNo);

		resultMap.put("removeEstimateNo", estimateNo);

		return resultMap;
	}

	@Override
	public ModelMap batchEstimateDetailListProcess(ArrayList<EstimateDetailTO> estimateDetailTOList,String estimateNo) {
		
		ModelMap resultMap = new ModelMap();
		
		ArrayList<EstimateDetailTO> list = new ArrayList<>();
		//추가,수정,삭제된 견적상세 일련번호를 담을 ArrayList
		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();
//첫번째 반복문 : insert만 처리 -> DELETE를 먼저하면 새로운 번호가 기존에 매겨졌던 번호로 매겨질수 있음.
		estimateMapper.initDetailSeq(); //시퀀스 초기화
		list = estimateMapper.selectEstimateDetailCount(estimateNo);
		TreeSet<Integer> intSet = new TreeSet<>();
		int cnt;

		for(EstimateDetailTO bean : list) {
			String estimateDetailNo = bean.getEstimateDetailNo();
			int no = Integer.parseInt(estimateDetailNo.split("-")[1]); //
			intSet.add(no); //행의갯수 찾음, -01 이렇게 붙는 갯수를 intSet에 담음
		}

		if (intSet.isEmpty()) { //아무것도 안들어가있으면 1을리턴
			cnt =  1;
		} else {
			cnt =  intSet.pollLast() + 1;
		}
		
		boolean isDelete=false;
		for (EstimateDetailTO bean : estimateDetailTOList) {

			String status = bean.getStatus();

			switch (status) {

			case "INSERT":
				if(cnt==1) {
					estimateMapper.insertEstimateDetail(bean);
				}else {
					ArrayList<EstimateDetailTO> newList = estimateMapper.selectEstimateDetailCount(estimateNo);
					int newCnt;
					for(EstimateDetailTO newbean : newList) {
						String estimateDetailNo = newbean.getEstimateDetailNo();
						int no = Integer.parseInt(estimateDetailNo.split("-")[1]);
						intSet.add(no);
					}

					if (intSet.isEmpty()) { //새로운 견적에 견적상세 등록한 경우
						newCnt =  1;
					} else { //기존견적에 견적상세 추가한경우
						newCnt =  intSet.pollLast() + 1;
					}
					StringBuffer newEstimateDetailNo = new StringBuffer();
					newEstimateDetailNo.append("ES");
					newEstimateDetailNo.append(estimateNo.replace("-", ""));
					newEstimateDetailNo.append("-"); 
					newEstimateDetailNo.append(String.format("%02d", newCnt));	
					bean.setEstimateDetailNo(newEstimateDetailNo.toString());
					estimateMapper.insertEstimateDetail(bean);
				}
				insertList.add(bean.getEstimateDetailNo());
				break;
					
			case "UPDATE":
				estimateMapper.updateEstimateDetail(bean);
				updateList.add(bean.getEstimateDetailNo());
				break;
					
			case "DELETE":
				estimateMapper.deleteEstimateDetail(bean);
				deleteList.add(bean.getEstimateDetailNo());
				isDelete=true;
				//기존의 값을 삭제했을 경우
				break;
			}
		}
		if(isDelete==true) {
			for (EstimateDetailTO bean : estimateDetailTOList) {
				int i = estimateMapper.selectEstimateDetailSeq(estimateNo);
				String newSeq = String.format("%02d", i);
				HashMap<String, String> map=new HashMap<>();
				map.put("estimateDetailNo", bean.getEstimateDetailNo());
				map.put("newSeq", newSeq);
				estimateMapper.reArrangeEstimateDetail(map);
			}
			estimateMapper.initDetailSeq();
		}
		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public ArrayList<ContractInfoTO> getContractList(String searchCondition, String startDate, String endDate, String customerCode) {
		ArrayList<ContractInfoTO> contractInfoTOList = null;
		
		HashMap<String, String> map = new HashMap<>();

		map.put("searchCondition", searchCondition);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("customerCode", customerCode);

		contractInfoTOList = contractMapper.selectContractList(map);
		
		for (ContractInfoTO bean : contractInfoTOList) {
			bean.setContractDetailTOList(contractMapper.selectContractDetailList(bean.getContractNo()));
		}
		return contractInfoTOList;
	}

	
	@Override
	public ArrayList<ContractDetailTO> getContractDetailList(String contractNo) {

		ArrayList<ContractDetailTO> contractDetailTOList = null;

		contractDetailTOList = contractMapper.selectContractDetailList(contractNo);

		return contractDetailTOList;
	}

	@Override
	public ArrayList<EstimateTO> getEstimateListInContractAvailable(String startDate, String endDate) {

		ArrayList<EstimateTO> estimateListInContractAvailable = null;
		
		HashMap<String, String> map = new HashMap<>();

		map.put("startDate", startDate);
		map.put("endDate", endDate);

		estimateListInContractAvailable = contractMapper.selectEstimateListInContractAvailable(map); //수주가능 견적조회

		for (EstimateTO bean : estimateListInContractAvailable) {

			bean.setEstimateDetailTOList(estimateMapper.selectEstimateDetailList(bean.getEstimateNo()));//ES2022011360
//하나의 견적상세리스트가 추가될때마다 견적상세TO가 추가됨, 해당견적에 견적상세 추가
		}

		return estimateListInContractAvailable;
	}

	@Override
	public ModelMap addNewContract(HashMap<String,String[]>  workingContractList) {

		ModelMap resultMap = new ModelMap();
		HashMap<String,String> setValue = null;
		StringBuffer str = null;

		setValue=new HashMap<String,String>();
		for(String key:workingContractList.keySet()) { //key로만 이루어진 배열을 for문안에 돌림
			str=new StringBuffer();

			// {수주상세번호,수주유형,수주등록자...)
			for(String value:workingContractList.get(key)) { //key에 해당되는 value값 들고옴
				if(key.equals("contractDate")) {
					String newContractNo=getNewContractNo(value);	 //수주번호 생성(날짜에 번호가 붙음)
					str.append(newContractNo+","); //key1 : [a,b,c] -> key : a,b,c
				}
				else str.append(value+",");
			}

			str.substring(0, str.length()-1);
			if(key.equals("contractDate")) 
				setValue.put("newContractNo", str.toString()); //HashMap<String,String[]>를 HashMap<String,String>으로 바꿈
					
			else 
			setValue.put(key, str.toString());
		}
		contractMapper.insertContractDetail(setValue);
		
		resultMap.put("gridRowJson", setValue.get("RESULT"));
		resultMap.put("errorCode", setValue.get("ERROR_CODE"));
		resultMap.put("errorMsg", setValue.get("ERROR_MSG"));

		return resultMap;
	}

	public String getNewContractNo(String contractDate) {
		
		StringBuffer newContractNo = null;

		int i = contractMapper.selectContractCount(contractDate);
		newContractNo = new StringBuffer();
		newContractNo.append("CO"); //CO
		newContractNo.append(contractDate.replace("-", "")); 
		newContractNo.append(String.format("%02d", i));

		return newContractNo.toString();
	}
	
	@Override
	public ModelMap batchContractDetailListProcess(ArrayList<ContractDetailTO> contractDetailTOList) {

		ModelMap resultMap = new ModelMap();

		ArrayList<String> insertList = new ArrayList<>();
		ArrayList<String> updateList = new ArrayList<>();
		ArrayList<String> deleteList = new ArrayList<>();
		
		for (ContractDetailTO bean : contractDetailTOList) {

			String status = bean.getStatus();

			switch (status) {

			case "INSERT":
				
				//contractMapper.insertContractDetail(bean);
				insertList.add(bean.getContractDetailNo());

				break;

			case "UPDATE":
				
				//contractMapper.updateContractDetail(bean);
				updateList.add(bean.getContractDetailNo());

				break;
					
			case "DELETE":

				contractMapper.deleteContractDetail(bean);
				deleteList.add(bean.getContractDetailNo());

				break;

			}

		}

		resultMap.put("INSERT", insertList);
		resultMap.put("UPDATE", updateList);
		resultMap.put("DELETE", deleteList);

		return resultMap;
	}

	@Override
	public void changeContractStatusInEstimate(String estimateNo, String contractStatus) {

		HashMap<String, String> map = new HashMap<>();

		map.put("estimateNo", estimateNo);
		map.put("contractStatus", contractStatus);
		
		estimateMapper.changeContractStatusOfEstimate(map);

	}

	public void processPlan(HashMap<String,String[]> processMap) {
		ModelMap resultMap = new ModelMap();
		HashMap<String, String> map = new HashMap<>();

		System.out.println("서비스임플 map : "+processMap);
		Set<String> keys = processMap.keySet();
		keys.forEach((key)->{
			System.out.println(processMap.get(key)); //get(key): 키값의 value를 가지고 올때
			for(String val:processMap.get(key)) {
				map.put(key,val);
			}
		});
		System.out.println("프로시저 변수값 map : "+map);
		contractMapper.processPlan(map);


	}
}
