package com.aic.paas.wdev.peer;

import java.util.List;

import com.aic.paas.wdev.bean.CPcImage;
import com.aic.paas.wdev.bean.CPcImageDef;
import com.aic.paas.wdev.bean.CPcImageDeploy;
import com.aic.paas.wdev.bean.PcImage;
import com.aic.paas.wdev.bean.PcImageDef;
import com.aic.paas.wdev.bean.PcImageDefInfo;
import com.aic.paas.wdev.bean.PcImageDeploy;
import com.aic.paas.wdev.bean.PcImageInfo;
import com.binary.jdbc.Page;

public interface PcImageDefPeer {
	
	
	
	/**
	 * aic.tsd_hyh  2016.03.19
	 * 根据状态 CPcImageDef 去查 PC_IMAGE_DEF表
	 * @param CPcBuildTask
	 * @param orders
	 * @return
	 */
	public List<PcImageDef> selectTaskListByCdt(CPcImageDef cid,String orders);

}
