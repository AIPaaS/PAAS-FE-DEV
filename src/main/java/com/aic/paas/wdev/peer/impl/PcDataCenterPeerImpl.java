package com.aic.paas.wdev.peer.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.wdev.bean.CPcDataCenter;
import com.aic.paas.wdev.bean.PcDataCenter;
import com.aic.paas.wdev.peer.PcDataCenterPeer;
import com.aic.paas.wdev.rest.PcDataCenterSvc;

public class PcDataCenterPeerImpl implements PcDataCenterPeer{
	
	@Autowired
	PcDataCenterSvc pcDataCenterSvc;


	public List<PcDataCenter> queryList(CPcDataCenter cdt, String orders) {
		return pcDataCenterSvc.queryList(cdt, orders);
	}

}
