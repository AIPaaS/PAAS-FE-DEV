package com.aic.paas.wdev.peer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.aic.paas.wdev.bean.BuildTaskRecord;
import com.aic.paas.wdev.bean.CPcBuildTask;
import com.aic.paas.wdev.bean.PcBuildTask;
import com.alibaba.dubbo.common.json.JSONObject;



public interface PcBuildTaskPeer {
	
	/**
	 * 保存获更新，判断主键ID[id]是否存在, 存在则更新, 不存在则插入
	 * @param record : PcBuildTask数据记录
	 * @return 当前回调BuildId[backBuildId]值
	 */
	public Long saveBuildTask(PcBuildTask pbt);
	
	
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
	
	/**
	 * 查询单条构建记录
	 * @param object
	 * @return
	 */
	public BuildTaskRecord queryTaskRecord(JSONObject object) throws IOException, URISyntaxException;
}
