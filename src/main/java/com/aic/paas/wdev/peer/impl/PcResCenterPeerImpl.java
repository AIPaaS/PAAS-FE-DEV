package com.aic.paas.wdev.peer.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.wdev.bean.CPcResCenter;
import com.aic.paas.wdev.bean.PcResCenter;
import com.aic.paas.wdev.peer.PcResCenterPeer;
import com.aic.paas.wdev.rest.PcResCenterSvc;

public class PcResCenterPeerImpl implements PcResCenterPeer{
	
	@Autowired
	PcResCenterSvc pcResCenterSvc;

	public List<PcResCenter> queryList(CPcResCenter cdt, String orders) {
		return pcResCenterSvc.queryList(cdt, orders);
	}

}
