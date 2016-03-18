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
						&nbsp;&nbsp;镜像名:
					</div>
					<div class="form-group pull-left">
						<input type="text" name="imageFullName" id="imageFullName" class="form-control" style="width:120px;">
					</div>
					<div class="form-group pull-left">
						产品:
					</div>
					<div class="form-group pull-left">
						<select id="productId" class="form-control" style="width:120px;"></select>
					</div>
					<div class="form-group pull-left">
						工程:
					</div>
					<div class="form-group pull-left">
						<select id="projectId" class="form-control" style="width:120px;"></select>
					</div>
					<div class="form-group pull-left">环境:</div>
						<div class="form-group pull-left">
							<select id="status" class="form-control" style="width: 120px;"></select>
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
								<th class="text-center">build号</th>
								<th class="text-center">生成时间</th>
								<th class="text-center">镜像环境</th>
								<th class="text-center">数据中心</th>
								<th class="text-center">资源中心</th>
								<th class="text-center">操作</th>
							</tr>
						</thead>
						<tbody id="imageDeployTable">
							
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



<script id="imageDeployTable-tmpl" type="text/x-jquery-tmpl">
	{{each(i,row) data}}
		<tr>
			<td class="text-left">{{= row.image.imageFullName}}</td>
			<td class="text-center">{{= row.product.name}}</td>
			<td class="text-center">{{= row.project.name}}</td>
			<td class="text-center">{{= row.image.buildNo}}</td>
			<td class="text-center">{{= CU.toStringDateTime(row.image.buildTime)}}</td>
			<td class="text-center">{{html PU.getDropValue("V_PC_IMAGE_STATUS",row.image.status,true)}}</td>
			<td class="text-center">{{= PU.getDropValue("DV_DATA_CENTER_CODE",row.image.dataCenterId,false)}}</td>
			<td class="text-center">{{= PU.getDropValue("DV_RES_CENTER_CODE",row.image.resCenterId,false)}}</td>
			<td class="text-center">
				{{if row.image.status != 4}}
					<a id="a_deploy_image_{{= row.image.id}}" href="###" class="table-link" title="发布">
						<span class="fa-stack">
							<i class="fa fa-square fa-stack-2x"></i>
							<i class="fa fa-space-shuttle fa-stack-1x fa-inverse"></i>
						</span>
					</a>
				{{/if}}
				{{if !CU.isEmpty(row.deployingCount)&&row.deployingCount>0}}
					<a id="a_deploy_status_{{= row.image.id}}" href="<%=ContextPath%>/dispatch/mc/1030701?id={{= row.image.id}}"><img src="<%=ContextPath%>/layout/img/task_loading.gif" style="width:26px;height:26px;"></img></a>
				{{else}}
					<a id="a_deploy_status_{{= row.image.id}}" href="<%=ContextPath%>/dispatch/mc/1030701?id={{= row.image.id}}" class="table-link" title="发布状态">
						<span class="fa-stack">
							<i class="fa fa-square fa-stack-2x"></i>
							<i class="fa fa-cog fa-stack-1x fa-inverse"></i>
						</span>
					</a>
				{{/if}}
			</td>
		</tr>
{{/each}}
</script>


<div id="sel_forcenter_env1" style="width:300px;position:absolute;display:none;"></div>
<div id="sel_forcenter_env2" style="width:300px;position:absolute;display:none;"></div>
<div id="sel_forcenter_env3" style="width:300px;position:absolute;display:none;"></div>


<jsp:include page="/layout/jsp/footer.jsp"></jsp:include>
