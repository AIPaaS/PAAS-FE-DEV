package com.aic.paas.wdev.rest;

import java.util.List;

import com.aic.paas.wdev.bean.CPcBuildTask;
import com.aic.paas.wdev.bean.PcBuildTask;
import com.aic.paas.wdev.util.bean.PcBuildTaskCallBack;

public interface PcBuildTaskSvc {



	/**
	 * 保存获更新，判断主键ID[id]是否存在, 存在则更新, 不存在则插入
	 * @param record : PcBuildTask数据记录
	 * @param mntCode : 租户code
	 * @return 当前回调BuildId[backBuildId]值
	 */
	public Long saveBuildTask(PcBuildTask record ,String mntCode);
	
	/**
	 * 查询历史构建记录 只查询最近的10个
	 * @param pageNum
	 * @param pageSize
	 * @param cdt
	 * @param orders
	 * @return
	 */
	public List<PcBuildTask> queryPcBuildTaskListForPage(Integer pageNum,Integer pageSize,CPcBuildTask cdt,String orders);

	
	/**
	 * aic.tsd_hyh  2016.03.11
	 * 根据状态 buildId 去查 数据状态为2，3的List
	 * @param buildDefId
	 * * @param statuss
	 * @return
	 */
	public List<PcBuildTask> selectTaskListByStatueId(Long buildDefId , Integer[] statuss);
	
	
	
	/**
	 * aic.tsd_hyh  2016.03.11
	 * 根据条件去修改表
	 * @param  PcBuildTask record ;//更新的映射对象
	 * @param CPcBuildTask cdt ;//条件对象
	 * @return
	 */
	public int updatePcBuildTaskCdt(PcBuildTask record ,CPcBuildTask cdt);
	
	/**
	 * 不分页查询
	 * @param pbtc : 构建任务回调对象
	 * @param imgRespId : 所属镜像库Id
	 * @return 
	 */
	public String updateBuildTaskByCallBack(PcBuildTaskCallBack pbtc);
	/**
	 * 不分页查询
	 * @param pbtc : 构建任务回调对象
	 * @param imgRespId : 所属镜像库Id
	 * @return 
	 */
	public String updateBuildTaskByCallBack(PcBuildTaskCallBack pbtc,String imgRespId);
}