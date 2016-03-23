package com.aic.paas.wdev.peer;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.aic.paas.wdev.bean.CPcImage;
import com.aic.paas.wdev.bean.CPcImageDef;
import com.aic.paas.wdev.bean.CPcImageDeploy;
import com.aic.paas.wdev.bean.PcImage;
import com.aic.paas.wdev.bean.PcImageDef;
import com.aic.paas.wdev.bean.PcImageDefInfo;
import com.aic.paas.wdev.bean.PcImageDeploy;
import com.aic.paas.wdev.bean.PcImageInfo;
import com.binary.jdbc.Page;
import com.binary.json.JSONObject;

public interface PcImagePeer {

	
	/**
	 * 分页查询
	 * 
	 * @param pageNum
	 *            : 指定页码
	 * @param pageSize
	 *            : 指定页行数
	 * @param cdt
	 *            : 条件对象
	 * @param orders
	 *            : 排序字段, 多字段以逗号分隔
	 * @return
	 */
	public Page<PcImageDefInfo> queryDefInfoPage(Integer pageNum, Integer pageSize, CPcImageDef cdt, String orders);

	/**
	 * 不分页查询
	 * 
	 * @param cdt
	 *            : 条件对象
	 * @param orders
	 *            : 排序字段, 多字段以逗号分隔
	 * @return
	 */
	public List<PcImageDefInfo> queryDefInfoList(CPcImageDef cdt, String orders);

	/**
	 * 不分页查询
	 * 
	 * @param cdt
	 *            : 条件对象
	 * @param orders
	 *            : 排序字段, 多字段以逗号分隔
	 * @return
	 */
	public List<PcImageDef> queryDefList(CPcImageDef cdt, String orders);

	/**
	 * 跟据主键查询
	 * 
	 * @param id
	 *            : 主键ID
	 * @return 操作员表[SYS_OP]映射对象
	 */
	public PcImageDefInfo queryDefInfoById(Long id);

	/**
	 * 跟据主键查询
	 * 
	 * @param id
	 *            : 主键ID
	 * @return 操作员表[SYS_OP]映射对象
	 */
	public PcImageDef queryDefById(Long id);

	/**
	 * 保存获更新，判断主键ID[id]是否存在, 存在则更新, 不存在则插入
	 * 
	 * @param record
	 *            : PcImageDef数据记录
	 * @return 当前记录主键[id]值
	 */
	public Long saveOrUpdateDef(PcImageDef record);

	/**
	 * 跟据分类ID删除
	 * 
	 * @param id
	 * @return
	 */
	public int removeDefById(Long id);

	/**
	 * 不分页查询当前租户下的镜像定义
	 * 
	 * @param cdt
	 * @param orders
	 * @return
	 */
	public List<PcImage> queryImageList(CPcImage cdt, String orders);

	/**
	 * 根据主键查询
	 * 
	 * @return
	 */
	public PcImage queryImageById(Long imageId);

	/**
	 * 分页查询
	 * 
	 * @param pageNum
	 *            : 指定页码
	 * @param pageSize
	 *            : 指定页行数
	 * @param cdt
	 *            : 条件对象
	 * @param orders
	 *            : 排序字段, 多字段以逗号分隔
	 * @return
	 */
	public Page<PcImageInfo> queryImageInfoPage(Integer pageNum, Integer pageSize, CPcImage cdt, String orders);

	/**
	 * 不分页查询
	 * 
	 * @param cdt
	 *            : 条件对象
	 * @param orders
	 *            : 排序字段, 多字段以逗号分隔
	 * @return
	 */
	public List<PcImageInfo> queryImageInfoList(CPcImage cdt, String orders);

	/**
	 * 跟据主键查询
	 * 
	 * @param id
	 *            : 主键ID
	 * @return 操作员表[SYS_OP]映射对象
	 */
	public PcImageInfo queryImageInfoById(Long id);

	/**
	 * 跟据分类ID删除
	 * 
	 * @param id
	 * @return
	 */
	public int removeImageById(Long id);

	/**
	 * 将镜像发布至开发环境
	 * 
	 * @param imageId
	 * @param dataCenterId
	 * @param resCenterId
	 * @return null||"" 表示发布成功, 否则为发布出错信息
	 */
	public String deployImage2Dev(Long imageId, Long dataCenterId, Long resCenterId, String remark);

	/**
	 * 将镜像发布至测试环境
	 * 
	 * @param imageId
	 * @param dataCenterId
	 * @param resCenterId
	 * @return null||"" 表示发布成功, 否则为发布出错信息
	 */
	public String deployImage2Test(Long imageId, Long dataCenterId, Long resCenterId, String remark);

	/**
	 * 将镜像发布至生产环境
	 * 
	 * @param imageId
	 * @param dataCenterId
	 * @param resCenterId
	 * @return null||"" 表示发布成功, 否则为发布出错信息
	 */
	public String deployImage2Release(Long imageId, Long dataCenterId, Long resCenterId, String remark);

	/**
	 * 跟据镜像ID查询发布记录
	 * 
	 * @param cdt
	 *            : 条件对象
	 * @param orders
	 *            : 排序字段, 多字段以逗号分隔
	 * @return
	 */
	public Page<PcImageDeploy> queryDeployPageByImageId(Integer pageNum, Integer pageSize, Long imageId,
			CPcImageDeploy cdt, String orders);

	/**
	 * 上传镜像
	 * 
	 * @param defid
	 * @param image_name
	 * @param file
	 * @param tag
	 * @return
	 */
	public JSONObject upLoadImage(String defid, String image_name, String tag, String export_file_url);

	/**
	 * 根据镜像定义ID查询构建tag
	 * @param cdt
	 * @param orders
	 * @return
	 */
	public List<String> queryTagsByDefId(CPcImage cdt, String orders);
}
