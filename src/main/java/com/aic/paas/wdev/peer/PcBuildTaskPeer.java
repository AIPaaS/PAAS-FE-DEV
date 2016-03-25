package com.aic.paas.wdev.peer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.aic.paas.wdev.bean.BuildTaskRecord;
import com.aic.paas.wdev.bean.CPcBuildTask;
import com.aic.paas.wdev.bean.PcBuildTask;
import com.aic.paas.wdev.util.bean.PcBuildTaskCallBack;



public interface PcBuildTaskPeer {
	
	/**
	 * 保存获更新，判断主键ID[id]是否存在, 存在则更新, 不存在则插入
	 * @param record : PcBuildTask数据记录
	 * @param buildName : 构建名称
	 * @param imageFullName :  镜像全名
	 * @return 当前回调BuildId[backBuildId]值
	 */
	public Long saveBuildTask(PcBuildTask pbt,String buildName,String imageFullName);
	
	
	/**
	 * 查询构建历史任务，只显示最近的10条
	 * @param pageNum
	 * @param pageSize
	 * @param cdt
	 * @param orders
	 * @return
	 */
	public List<PcBuildTask> queryBuildTaskList(Integer pageNum, Integer pageSize, CPcBuildTask cdt, String orders);
	
	
	
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
	
	public String updatePcBuildTaskApi(String namespace, String back_build_id, String  repo_name);

	/**
	 * 查询单条构建记录
	 * @param repo_name
	 * @param build_id
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public BuildTaskRecord queryTaskRecord(String repo_name,String build_id) throws IOException, URISyntaxException;
	
	/**
	 * aic.tsd_hyh  2016.03.18
	 * 根据状态 buildId 去查 数据状态为2，3的List
	 * @param CPcBuildTask
	 * * @param orders
	 * @return
	 */
	public List<PcBuildTask> selectTaskListByCdt(CPcBuildTask cdt,String orders);
	/**
	 * @param pbtc PcBuildTaskCallBack
	 * @param imgRespId 镜像库Id
	 * @return taskUserId 构建人的Id
	 */
	public String updateBuildTaskByCallBack(PcBuildTaskCallBack pbtc,String imgRespId);
	/**
	 * @param taskUserId 构建人的Id
	 * @return 构建人的邮箱地址 
	 */
	public String queryEmailAdressByTaskUserId(String taskUserId);
	/**
	 * @param namespace 
	 * @param repo_name
	 * @param build_id
	 * @return 构建过程的详细记录
	 */
	public BuildTaskRecord queryTaskRecordToEmail(String namespace,String repo_name, String build_id) ;
	
	/**
	 * 查询最近构建记录的状态
	 * @param buildDefIds
	 * @return
	 * @throws InterruptedException 
	 */
	public PcBuildTask searchBuildtaskStatus(Long[] buildDefIds) throws InterruptedException;
}
