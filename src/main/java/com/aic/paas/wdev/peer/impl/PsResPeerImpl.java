package com.aic.paas.wdev.peer.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.wdev.bean.CPcCompRoom;
import com.aic.paas.wdev.bean.CPcDataCenter;
import com.aic.paas.wdev.bean.CPcNetZone;
import com.aic.paas.wdev.bean.CPcResCenter;
import com.aic.paas.wdev.bean.PcCompRoom;
import com.aic.paas.wdev.bean.PcDataCenter;
import com.aic.paas.wdev.bean.PcNetZone;
import com.aic.paas.wdev.bean.PcResCenter;
import com.aic.paas.wdev.peer.PsResPeer;
import com.aic.paas.wdev.rest.PcCompRoomSvc;
import com.aic.paas.wdev.rest.PcDataCenterSvc;
import com.aic.paas.wdev.rest.PcNetZoneSvc;
import com.aic.paas.wdev.rest.PcResCenterSvc;

public class PsResPeerImpl implements PsResPeer {
	
	
	@Autowired
	PcDataCenterSvc dataCenterSvc;
	
	
	@Autowired
	PcResCenterSvc resCenterSvc;
	
	
	@Autowired
	PcNetZoneSvc netZoneSvc;
	
	@Autowired
	PcCompRoomSvc compRoomSvc;
	
	

	@Override
	public List<PcDataCenter> queryDataCenterList(CPcDataCenter cdt, String orders) {
		return dataCenterSvc.queryList(cdt, orders);
	}
	
	

	@Override
	public List<PcResCenter> queryResCenterList(CPcResCenter cdt, String orders) {
		return resCenterSvc.queryList(cdt, orders);
	}

	
	
	@Override
	public List<PcNetZone> queryNetZoneList(CPcNetZone cdt, String orders) {
		return netZoneSvc.queryList(cdt, orders);
	}



	@Override
	public List<PcCompRoom> queryCompRoomList(CPcCompRoom cdt, String orders) {
		return compRoomSvc.queryList(cdt, orders);
	}
	
	
	

}
