package kr.co.seoulit.logistics.busisvc.sales.controller.logisales.to;

import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.BaseTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProcessPlanTO  extends BaseTO {
    private String contractDetailNo;

}
