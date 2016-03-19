package com.aic.paas.wdev.peer.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.wdev.bean.CPcImage;
import com.aic.paas.wdev.bean.CPcImageDef;
import com.aic.paas.wdev.bean.CPcImageDeploy;
import com.aic.paas.wdev.bean.ImageStatus;
import com.aic.paas.wdev.bean.PcImage;
import com.aic.paas.wdev.bean.PcImageDef;
import com.aic.paas.wdev.bean.PcImageDefInfo;
import com.aic.paas.wdev.bean.PcImageDeploy;
import com.aic.paas.wdev.bean.PcImageInfo;
import com.aic.paas.wdev.peer.PcImageDefPeer;
import com.aic.paas.wdev.peer.PcImagePeer;
import com.aic.paas.wdev.peer.UserAuthentication;
import com.aic.paas.wdev.rest.PcImageDefSvc;
import com.aic.paas.wdev.rest.PcImageSvc;
import com.binary.core.http.HttpUtils;
import com.binary.core.util.BinaryUtils;
import com.binary.framework.exception.ServiceException;
import com.binary.jdbc.Page;

public class PcImageDefPeerImpl implements PcImageDefPeer {
	
	
	@Autowired
	PcImageDefSvc imageDefSvc;


	@Override
	public List<PcImageDef> selectTaskListByCdt(CPcImageDef cid, String orders) {
		if(cid==null) return null;
		return imageDefSvc.selectTaskListByCdt(cid, orders);
	}


	@Override
	public PcImageDef selectDefByFullName(Long mntId, String imageFullName) {
		return imageDefSvc.selectDefByFullName(mntId, imageFullName);
	}



	
	
	
	












}
