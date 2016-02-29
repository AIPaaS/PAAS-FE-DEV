package com.aic.paas.wdev.peer.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.aic.paas.comm.util.SystemUtil;
import com.aic.paas.frame.cross.bean.CSysOp;
import com.aic.paas.frame.cross.bean.SysOp;
import com.aic.paas.frame.cross.bean.WsMerchent;
import com.aic.paas.frame.cross.integration.PaasWebSsoLoginUser;
import com.aic.paas.frame.cross.rest.MerchentSvc;
import com.aic.paas.wdev.bean.CPcProduct;
import com.aic.paas.wdev.bean.CPcProject;
import com.aic.paas.wdev.bean.ContType;
import com.aic.paas.wdev.bean.PcProduct;
import com.aic.paas.wdev.bean.PcProject;
import com.aic.paas.wdev.bean.ProjectInfo;
import com.aic.paas.wdev.bean.ProjectMgrInfo;
import com.aic.paas.wdev.bean.ProjectProdInfo;
import com.aic.paas.wdev.bean.RespType;
import com.aic.paas.wdev.bean.SourceType;
import com.aic.paas.wdev.peer.PcProjectPeer;
import com.aic.paas.wdev.peer.UserAuthentication;
import com.aic.paas.wdev.rest.PcProductSvc;
import com.aic.paas.wdev.rest.PcProjectSvc;
import com.aic.paas.wdev.rest.SysOpSvc;
import com.aic.paas.wdev.rest.VcMntRepositoryApplySvc;
import com.binary.core.lang.ArrayUtils;
import com.binary.core.util.BinaryUtils;
import com.binary.framework.bean.User;
import com.binary.framework.exception.ServiceException;
import com.binary.jdbc.Page;

public class PcProjectPeerImpl implements PcProjectPeer {
	
	
	@Autowired
	PcProjectSvc projectSvc;
	
	@Autowired
	PcProductSvc productSvc;
	
	@Autowired
	SysOpSvc sysOpSvc;
	
	@Autowired
	UserAuthentication userAuth;
	
	
	@Autowired
	VcMntRepositoryApplySvc mntRespApplySvc;
	
	
	@Autowired
	MerchentSvc merchentSvc;
	
	
	
	private List<ProjectMgrInfo> fillMgrInfo(List<ProjectInfo> ls) {
		List<ProjectMgrInfo> infos = new ArrayList<ProjectMgrInfo>();
		if(ls!=null && ls.size()>0) {
			Set<Long> opids = new HashSet<Long>();
			for(int i=0; i<ls.size(); i++) {
				ProjectInfo pif = ls.get(i);
				ProjectMgrInfo info = new ProjectMgrInfo();
				info.setProject(pif.getProject());
				info.setMgrIds(pif.getMgrIds());
				infos.add(info);
				
				Long[] mids = pif.getMgrIds();
				if(mids!=null && mids.length>0) {
					opids.addAll(ArrayUtils.toList(mids));
				}
			}
			
			if(opids.size() > 0) {
				CSysOp cdt = new CSysOp();
				cdt.setIds(opids.toArray(new Long[0]));
				List<SysOp> ops = sysOpSvc.queryList(cdt, null);
				
				if(ops.size() > 0) {
					Map<Long, SysOp> opmap = new HashMap<Long, SysOp>();
					for(int i=0; i<ops.size(); i++) {
						SysOp op = ops.get(i);
						opmap.put(op.getId(), op);
					}
					
					for(int i=0; i<infos.size(); i++) {
						ProjectMgrInfo info = infos.get(i);
						Long[] mgrIds = info.getMgrIds();
						if(mgrIds==null || mgrIds.length==0) continue ;
						
						List<SysOp> opls = new ArrayList<SysOp>();
						for(int j=0; j<mgrIds.length; j++) {
							SysOp op = opmap.get(mgrIds[j]);
							if(op != null) {
								opls.add(op);
							}
						}
						if(opls.size() > 0) {
							info.setMgrOps(opls);
						}
					}
				}
				
			}
		}
		
		return infos;
	}
	
	
	
	private Long[] getProductIds(CPcProject cdt) {
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		Long mntId = user.getMerchent().getId();
		
		CPcProduct productcdt = new CPcProduct();
		productcdt.setMntId(mntId);
		if(cdt != null) {
			productcdt.setId(cdt.getProductId());
			productcdt.setIds(cdt.getProductIds());
		}
		List<PcProduct> ls = productSvc.queryList(productcdt, null);
		if(ls.size() == 0) return null;
		
		Long[] productIds = new Long[ls.size()];
		for(int i=0; i<ls.size(); i++) {
			PcProduct p = ls.get(i);
			productIds[i] = p.getId();
		}
		
		return productIds;
	}
	
	

	@Override
	public Page<ProjectMgrInfo> queryInfoPage(Integer pageNum, Integer pageSize, CPcProject cdt, String orders) {
		Long[] productIds = getProductIds(cdt);
		if(BinaryUtils.isEmpty(productIds)) {
			return new Page<ProjectMgrInfo>(1, pageSize, 0, 0, new ArrayList<ProjectMgrInfo>());
		}
		
		if(cdt == null) cdt = new CPcProject();
		cdt.setProductIds(productIds);
		Page<ProjectInfo> page = projectSvc.queryInfoPage(pageNum, pageSize, cdt, orders);
		List<ProjectInfo> ls = page.getData();
		List<ProjectMgrInfo> infols = fillMgrInfo(ls);
		return new Page<ProjectMgrInfo>(page.getPageNum(), page.getPageSize(), page.getTotalRows(), page.getTotalPages(), infols);
	}

	
	
	@Override
	public List<ProjectMgrInfo> queryInfoList(CPcProject cdt, String orders) {
		Long[] productIds = getProductIds(cdt);
		if(BinaryUtils.isEmpty(productIds)) {
			return new ArrayList<ProjectMgrInfo>();
		}
		
		if(cdt == null) cdt = new CPcProject();
		cdt.setProductIds(productIds);
		List<ProjectInfo> ls = projectSvc.queryInfoList(cdt, null);
		return fillMgrInfo(ls);
	}

	
	
	@Override
	public ProjectMgrInfo queryInfoById(Long id) {
		BinaryUtils.checkEmpty(id, "id");
		ProjectInfo info = projectSvc.queryInfoById(id);
		if(info == null) return null;
		
		List<ProjectInfo> ls = new ArrayList<ProjectInfo>();
		ls.add(info);
		return fillMgrInfo(ls).get(0);
	}

	
	
	@Override
	public Page<PcProject> queryMgrPage(Integer pageNum, Integer pageSize, CPcProject cdt, String orders) {
		User user = SystemUtil.getLoginUser();
		return projectSvc.queryMgrPage(pageNum, pageSize, user.getId(), cdt, orders);
	}

	
	
	
	@Override
	public List<PcProject> queryMgrList(CPcProject cdt, String orders) {
		User user = SystemUtil.getLoginUser();
		return projectSvc.queryMgrList(user.getId(), cdt, orders);
	}

	
	
	private List<ProjectProdInfo> fileProdInfo(List<PcProject> ls) {
		List<ProjectProdInfo> infos = new ArrayList<ProjectProdInfo>();
		if(ls!=null && ls.size()>0) {
			Set<Long> pids = new HashSet<Long>();
			
			for(int i=0; i<ls.size(); i++) {
				PcProject p = ls.get(i);
				ProjectProdInfo info = new ProjectProdInfo();
				info.setProject(p);
				infos.add(info);
				
				pids.add(p.getProductId());
			}
			
			if(pids.size() > 0) {
				CPcProduct cdt = new CPcProduct();
				cdt.setIds(pids.toArray(new Long[0]));
				List<PcProduct> ops = productSvc.queryList(cdt, null);
				
				if(ops.size() > 0) {
					Map<Long, PcProduct> opmap = new HashMap<Long, PcProduct>();
					for(int i=0; i<ops.size(); i++) {
						PcProduct op = ops.get(i);
						opmap.put(op.getId(), op);
					}
					
					for(int i=0; i<infos.size(); i++) {
						ProjectProdInfo info = infos.get(i);
						Long productId = info.getProject().getProductId();
						if(productId == null) continue ;
						info.setProduct(opmap.get(productId));
					}
				}
				
			}
		}
		
		return infos;
	}
	
	
	
	
	@Override
	public Page<ProjectProdInfo> queryMgrProdInfoPage(Integer pageNum, Integer pageSize, CPcProject cdt, String orders) {
		Page<PcProject> page = queryMgrPage(pageNum, pageSize, cdt, orders);
		List<PcProject> ls = page.getData();
		List<ProjectProdInfo> infols = fileProdInfo(ls);
		return new Page<ProjectProdInfo>(page.getPageNum(), page.getPageSize(), page.getTotalRows(), page.getTotalPages(), infols);
	}

	
	
	
	@Override
	public List<ProjectProdInfo> queryMgrProdInfoList(CPcProject cdt, String orders) {
		List<PcProject> ls = queryMgrList(cdt, orders);
		return fileProdInfo(ls);
	}
	
	
	
	@Override
	public List<PcProject> queryProjectList(Long productId, CPcProject cdt, String orders) {
		BinaryUtils.checkEmpty(productId, "productId");
		userAuth.verifyUserProductAuth(productId);
		
		if(cdt == null) cdt = new CPcProject();
		cdt.setProductId(productId);
		return projectSvc.queryList(cdt, orders);
	}
	
	
	@Override
	public Long saveOrUpdate(PcProject record) {
		BinaryUtils.checkEmpty(record, "record");
		BinaryUtils.checkEmpty(record.getProductId(), "record.productId");
		
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		Long mntId = user.getMerchent().getId();
		WsMerchent mnt = merchentSvc.queryById(mntId);
		if(mnt == null) {
			throw new ServiceException(" not found mnt by id '"+mntId+"'! ");
		}
		
		boolean isadd = record.getId() == null;
		if(isadd) {
			BinaryUtils.checkEmpty(record.getCode(), "record.code");
			BinaryUtils.checkEmpty(record.getName(), "record.name");
			BinaryUtils.checkEmpty(record.getStatus(), "record.status");
			userAuth.verifyUserProductAuth(record.getProductId());
		}else {
			if(record.getCode() != null) BinaryUtils.checkEmpty(record.getCode(), "record.code");
			if(record.getName() != null) BinaryUtils.checkEmpty(record.getName(), "record.name");
			if(record.getStatus() != null) BinaryUtils.checkEmpty(record.getStatus(), "record.status");
			userAuth.verifyUserProjectAuth(record.getId());
		}
		return projectSvc.saveOrUpdate(record, mnt);
	}

	
	
	@Override
	public int removeById(Long id) {
		BinaryUtils.checkEmpty(id, "id");
		userAuth.verifyUserProjectAuth(id);
		return projectSvc.removeById(id);
	}
	
	
	

	@Override
	public void setProjectMgrs(Long projectId, Long[] mgrIds) {
		BinaryUtils.checkEmpty(projectId, "projectId");
		userAuth.verifyUserProjectAuth(projectId);
		projectSvc.setProjectMgrs(projectId, mgrIds);
	}



	@Override
	public void applyRespDoc(Long projectId, Integer respType) {
		BinaryUtils.checkEmpty(projectId, "projectId");
		BinaryUtils.checkEmpty(respType, "respType");
		userAuth.verifyUserProjectAuth(projectId);
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		Long mntId = user.getMerchent().getId();
		mntRespApplySvc.createApply(mntId, RespType.valueOf(respType), ContType.DOC, SourceType.PROJECT, projectId, null);
	}


	

	@Override
	public void applyRespCode(Long projectId, Integer respType) {
		BinaryUtils.checkEmpty(projectId, "projectId");
		BinaryUtils.checkEmpty(respType, "respType");
		userAuth.verifyUserProjectAuth(projectId);
		PaasWebSsoLoginUser user = (PaasWebSsoLoginUser)SystemUtil.getLoginUser();
		Long mntId = user.getMerchent().getId();
		mntRespApplySvc.createApply(mntId, RespType.valueOf(respType), ContType.CODE, SourceType.PROJECT, projectId, null);
	}



	
	
	

}
