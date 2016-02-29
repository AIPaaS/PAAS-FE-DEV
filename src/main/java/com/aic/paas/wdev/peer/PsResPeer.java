package com.aic.paas.wdev.peer;

import java.util.List;

import com.aic.paas.wdev.bean.CPcCompRoom;
import com.aic.paas.wdev.bean.CPcDataCenter;
import com.aic.paas.wdev.bean.CPcNetZone;
import com.aic.paas.wdev.bean.CPcResCenter;
import com.aic.paas.wdev.bean.PcCompRoom;
import com.aic.paas.wdev.bean.PcDataCenter;
import com.aic.paas.wdev.bean.PcNetZone;
import com.aic.paas.wdev.bean.PcResCenter;

public interface PsResPeer {
	

	/**
	 * 获取服务器机房列表
	 * @param cdt : 条件对象
	 * @param orders : 排序字段, 多字段以逗号分隔
	 * @return 
	 */
	public List<PcCompRoom> queryCompRoomList(CPcCompRoom cdt, String orders);
	
	
	/**
	 * 获取数据中心列表
	 * @param cdt : 条件对象
	 * @param orders : 排序字段, 多字段以逗号分隔
	 * @return 
	 */
	public List<PcDataCenter> queryDataCenterList(CPcDataCenter cdt, String orders);
	
	
	
	
	
	/**
	 * 获取资源中心列表
	 * @param cdt : 条件对象
	 * @param orders : 排序字段, 多字段以逗号分隔
	 * @return 
	 */
	public List<PcResCenter> queryResCenterList(CPcResCenter cdt, String orders);
	
	
	
	
	/**
	 * 获取网络区域列表
	 * @param cdt : 条件对象
	 * @param orders : 排序字段, 多字段以逗号分隔
	 * @return 
	 */
	public List<PcNetZone> queryNetZoneList(CPcNetZone cdt, String orders);
	
	

}
