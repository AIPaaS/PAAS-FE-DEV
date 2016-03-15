package com.aic.paas.wdev.rest;
import java.util.List;

import com.aic.paas.wdev.bean.CPcImageRepository;
import com.aic.paas.wdev.bean.PcImageRepository;

public interface PcImageRepositorySvc {
	
	/**
	 * 不分页查询
	 * @param cdt : 条件对象
	 * @param orders : 排序字段, 多字段以逗号分隔
	 * @return 
	 */
	public List<PcImageRepository> queryList(CPcImageRepository cdt, String orders);
	
}