package com.aic.paas.wdev.rest;

import com.aic.paas.wdev.bean.PcBuildTask;

public interface PcBuildTaskSvc {


	/**
	 * 保存获更新，判断主键ID[id]是否存在, 存在则更新, 不存在则插入
	 * @param record : PcBuildTask数据记录
	 * @return 当前记录主键[id]值
	 */
	public Long saveOrUpdateBuildTask(PcBuildTask record);
}
