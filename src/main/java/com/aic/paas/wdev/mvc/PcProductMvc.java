package com.aic.paas.wdev.mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aic.paas.frame.cross.bean.DropRecord;
import com.aic.paas.frame.util.ComponentUtil;
import com.aic.paas.wdev.bean.CPcProduct;
import com.aic.paas.wdev.bean.PcProduct;
import com.aic.paas.wdev.bean.ProductMgrInfo;
import com.aic.paas.wdev.peer.PcProductPeer;
import com.binary.core.lang.Conver;
import com.binary.core.util.BinaryUtils;
import com.binary.framework.util.ControllerUtils;
import com.binary.jdbc.Page;

@Controller
@RequestMapping("/dev/product")
public class PcProductMvc {
	
	@Autowired
	PcProductPeer productPeer;
	
	
	@RequestMapping("/getProductMgrDropList")
	public void getProductMgrDropList(HttpServletRequest request, HttpServletResponse response, Boolean addEmpty, Boolean addAttr) {
		CPcProduct cdt = new CPcProduct();
		cdt.setStatus(1);
		List<PcProduct> list = productPeer.queryMgrList(cdt, "CODE , ID");
		List<DropRecord> dropList = ComponentUtil.toDropList(list, "ID", "name", addAttr, addEmpty);
	
		ControllerUtils.returnJson(request, response, dropList);
	}
	
	@RequestMapping("/getProductDropList")
	public void getProductDropList(HttpServletRequest request, HttpServletResponse response, Boolean addEmpty, Boolean addAttr) {
		CPcProduct cdt = new CPcProduct();
		cdt.setStatus(1);
		List<PcProduct> list = productPeer.queryProductList(cdt, "CODE , ID");
		List<DropRecord> dropList = ComponentUtil.toDropList(list, "ID", "name", addAttr, addEmpty);
	
		ControllerUtils.returnJson(request, response, dropList);
	}
	
	@RequestMapping("/queryInfoPage")
	public void queryInfoPage(HttpServletRequest request, HttpServletResponse response,Integer pageNum, Integer pageSize, CPcProduct cdt, String orders){
		Page<ProductMgrInfo> page = productPeer.queryInfoPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	
	@RequestMapping("/queryInfoById")
	public void queryInfoById(HttpServletRequest request, HttpServletResponse response,Long id){
		ProductMgrInfo p = productPeer.queryInfoById(id);
		ControllerUtils.returnJson(request, response, p);
	}

	@RequestMapping("/queryMgrPage")
	public void queryMgrPage(HttpServletRequest request, HttpServletResponse response,Integer pageNum, Integer pageSize, CPcProduct cdt, String orders){
		cdt.setStatus(1);
		Page<PcProduct> page = productPeer.queryMgrPage(pageNum, pageSize, cdt, orders);
		ControllerUtils.returnJson(request, response, page);
	}
	
	@RequestMapping("/queryMgrList")
	public void queryMgrList(HttpServletRequest request, HttpServletResponse response, CPcProduct cdt, String orders){
		List<PcProduct> list = productPeer.queryMgrList(cdt, orders);
		ControllerUtils.returnJson(request, response, list);
	}
	
	@RequestMapping("/saveOrUpdate")
	public void saveOrUpdate(HttpServletRequest request, HttpServletResponse response,PcProduct record, String strMgrIds){
		Long productId = productPeer.saveOrUpdate(record);
		Long[] mgrIds = null;
		if(!BinaryUtils.isEmpty(strMgrIds)) {
			mgrIds = Conver.to(strMgrIds.trim().split(","), Long.class);
		}
		productPeer.setProductMgrs(productId, mgrIds);
		ControllerUtils.returnJson(request, response, productId);
	}
	
	
	@RequestMapping("/applyRespDoc")
	public void applyRespDoc(HttpServletRequest request, HttpServletResponse response,Long productId, Integer respType){
		productPeer.applyRespDoc(productId, respType);
		ControllerUtils.returnJson(request, response, null);
	}
	
	
	
}
