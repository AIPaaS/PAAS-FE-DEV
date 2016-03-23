<%@ page contentType="text/html; charset=utf-8"%>

<%
String ContextPath = request.getContextPath();
%>

<jsp:include page="/layout/jsp/head.jsp"></jsp:include>




<div class="row">
	<div class="col-lg-12">
		<div class="main-box clearfix">
			<div class="filter-block pull-left">
				<div class="form-group pull-left">
					<div class="form-group pull-left">
						&nbsp;镜像名:
					</div>
					<div class="form-group pull-left">
						<input type="text" name="imageFullName" id="imageFullName" class="form-control" style="width:130px;">
					</div>
					<div class="form-group pull-left">
						产品:
					</div>
					<div class="form-group pull-left">
						<select id="sel_productId" class="form-control" style="width:160px;">
						</select>
					</div>
					<div class="form-group pull-left">
						工程:
					</div>
					<div class="form-group pull-left">
						<select id="sel_projectId" class="form-control" style="width:160px; background-color:#f5f5f5" disabled="false">
						</select>
					</div>
					<div class="form-group pull-left">
						是否外部镜像:
					</div>
					<div class="form-group pull-left">
						<select id="sel_isExternal" class="form-control" style="width:70px;">
						</select>
					</div>
				</div>
				<button id="btn_query" class="btn btn-primary"><i class="fa fa-search fa-lg"></i> 查询</button>
			</div>
		</div>
	</div>
</div>

<!-- 正文 -->

<div class="row">
	<div class="col-lg-12">
		<div class="main-box clearfix">
			<div class="main-box-body clearfix">
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th class="text-left">镜像名</th>
								<th class="text-center">对应产品</th>
								<th class="text-center">对应工程</th>
								<th class="text-center">是否外部镜像</th>
								<th class="text-center">最近build号</th>
								<th class="text-center">最近生成时间</th>
								<th class="text-center">操作</th>
							</tr>
						</thead>
						<tbody id="imageTable">
							
						</tbody>
					</table>
				</div>
				<div class="row-fluid">
					<div class="col-lg-6">
						<label>
							每页
								<select name="selPageSize"  class="pagination" id="grid_pageSize" >
									<option value="10">10</option>
									<option value="15">15</option>
									<option value="20" selected>20</option>
									<option value="25">25</option>
									<option value="30">30</option>
									<option value="40">40</option>
									<option value="50">50</option>
								</select>
							条记录
						</label>
					</div>
					<div class="col-lg-6">
						<div class="pagination pull-right" id="pagination_box">
							<ul id="ul_pagination" class="pagination-sm"></ul>
						</div>
					</div>
				</div>
				
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="uploadModal"  data-backdrop='static'>
	<div class="modal-dialog" style="width: 470px;">
		<div class="modal-content">
			<div class="modal-body" style="height: 242px;">
				<div class="spinner" style="display:none ;">
						<div class="rect1"></div>
						<div class="rect2"></div>
						<div class="rect3"></div>
						<div class="rect4"></div>
						<div class="rect5"></div>
					</div>
				<div class="upload_info" style="display:none ;">
						上传成功
				</div>
				<form id="uploadForm" enctype="multipart/form-data"
					class="form-horizontal"
					style="width: 408px; margin: 20px 5px 10px;" onsubmit="return false">
					<div class="input-group" style="width:270px;margin:0 auto 20px;">
						<span class="input-group-addon" style="border: 0px;">Tag
							标记:</span> <input type="text" list="tagList" id="tag" name="tag"
							style="margin-left: 5px; width:268px;border:1px solid #ccc;height:30px;">
					</div>
					<div class="input-group" style="width:270px;margin:0 auto 10px;">
						<span class="input-group-addon" style="border: 0px;">上传文件：</span>
						<div class="box">
							<input type="text" name="copyFile" class="textbox" id="copyFile" /> <a
								href="javascript:void(0);" class="link" >浏览</a> <input
								type="file" class="uploadFile" name="file" id="imageFile" onchange="getFile(this, 'copyFile')"/>
						</div>
					</div>
					<datalist id="tagList">
					</datalist>
					<span class="error">只支持.tar或tar.gz格式，不超过2G</span>
					<p class="btn_upload">
						<button class="btn btn-default new_cancle" id="cancleUpload">取消</button>
						<button class="btn btn-primary new_upload" id="upLoad">上传</button>
					</p>
					<span class="notice">导出示例：docker
						export -o registry.test.com/hello-world extname.tar</span>
				</form>


			</div>
		</div>
	</div>
</div>




<!-- jia【镜像管理】导出功能屏蔽掉 backup -->
<!-- <a href="###" class="table-link" title="导出">
					<span class="fa-stack">
						<i class="fa fa-square fa-stack-2x"></i>
						<i class="fa fa-share-square-o fa-stack-1x fa-inverse"></i>
					</span>
</a> -->
<script id="imageTable-tmpl" type="text/x-jquery-tmpl">
	{{each(i,row) data}}
		<tr>
			<td class="text-left">{{= row.def.imageFullName}}</td>
			<td class="text-center">
				{{if !CU.isEmpty(row.product)}}
					{{= row.product.name}}
				{{/if}}
			</td>
			<td class="text-center">
				{{if !CU.isEmpty(row.project)}}
					{{= row.project.name}}
				{{/if}}
			</td>			
			<td class="text-center">{{html PU.getDropValue("V_IMAGE_IS_EXTERNAL",row.def.isExternal,true)}}</td>
			<td class="text-center">
				{{if !CU.isEmpty(row.lastImage)}}
					{{= row.lastImage.buildNo}}
				{{/if}}
			</td>
			<td class="text-center">
				{{if !CU.isEmpty(row.lastImage)}}
					{{= CU.toStringDateTime(row.lastImage.buildTime)}}
				{{/if}}
			</td>
			<td class="text-center">
				<a id="a_upload_image_{{= row.def.id}}" href="###" class="table-link" title="上传">
					<span class="fa-stack">
						<i class="fa fa-square fa-stack-2x"></i>
						<i class="fa fa-upload fa-stack-1x fa-inverse"></i>
					</span>
				</a>
				<!--<a href="###" class="table-link" title="导出">
					<span class="fa-stack">
						<i class="fa fa-square fa-stack-2x"></i>
						<i class="fa fa-share-square-o fa-stack-1x fa-inverse"></i>
					</span>
				</a>-->
				<a href="<%=ContextPath%>/dispatch/mc/1030601?defId={{= row.def.id}}&pageNum={{= pageNum}}" class="table-link" title="查看历史版本">
					<span class="fa-stack">
						<i class="fa fa-square fa-stack-2x"></i>
						<i class="fa fa-history fa-stack-1x fa-inverse"></i>
					</span>
				</a>
				{{if !CU.isEmpty(row.lastImage)}}
					<a id="a_remove_image_{{= row.lastImage.id}}" href="###" class="table-link danger" title="删除">
						<span class="fa-stack">
							<i class="fa fa-square fa-stack-2x"></i>
							<i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>
						</span>
					</a>
				{{/if}}
				
			</td>
		</tr>
{{/each}}
</script>




<jsp:include page="/layout/jsp/footer.jsp"></jsp:include>
