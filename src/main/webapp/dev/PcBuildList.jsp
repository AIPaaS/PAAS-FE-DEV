<%@ page contentType="text/html; charset=utf-8"%>

<%
String ContextPath = request.getContextPath();
%>

<jsp:include page="/layout/jsp/head.jsp"></jsp:include>


<!-- 正文 -->
<div class="row">
	<div class="col-lg-12">
		<div class="main-box clearfix">
			<div class="filter-block pull-left">
				<div class="form-group pull-left">&nbsp;&nbsp;&nbsp;构建名:</div>
				<div class="form-group pull-left">
					<input type="text" name="buildName" id="buildName"
						class="form-control" style="width: 130px;">
				</div>
				<div class="form-group pull-left">产品:</div>
				<div class="form-group pull-left">
					<select id="sel_productId" class="form-control"
						style="width: 160px;">
					</select>
				</div>
				<div class="form-group pull-left">工程:</div>
				<div class="form-group pull-left">
					<select id="sel_projectId" class="form-control"
						style="width: 160px; background-color:#f5f5f5" disabled="false">
					</select>
				</div>
				<div class="form-group pull-left">是否生成镜像:</div>
				<div class="form-group pull-left">
					<select id="sel_isBuildImage" class="form-control"
						style="width: 70px;">
					</select>
				</div>
				<button id="btn_query" class="btn btn-primary btn_gj">
					<i class="fa fa-search fa-lg"></i> 查询
				</button>
				<button id="btn_add" class="btn btn-primary btn_gj">
					<i class="fa fa-plus-circle fa-lg"></i> 添加
				</button>
			</div>

		</div>
	</div>
</div>


<div class="row">
	<div class="col-lg-12">
		<div class="main-box clearfix">
			<div class="main-box-body clearfix">
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th class="text-center">构建名</th>
								<th class="text-center">对应产品</th>
								<th class="text-center">对应工程</th>
								<th class="text-center">是否生成镜像</th>
								<th class="text-left">镜像名</th>
								<th class="text-center">最近构建时间</th>
								<th class="text-center">最近构建版本</th>
								<th class="text-center">最近构建状态</th>
								<th class="text-center">操作</th>
							</tr>
						</thead>
						<tbody id="buildTable">

						</tbody>
					</table>
				</div>
				<div class="row-fluid">
					<div class="col-lg-6">
						<label> 每页 <select name="selPageSize" class="pagination"
							id="grid_pageSize">
								<option value="10">10</option>
								<option value="15">15</option>
								<option value="20" selected>20</option>
								<option value="25">25</option>
								<option value="30">30</option>
								<option value="40">40</option>
								<option value="50">50</option>
						</select> 条记录
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
<div class="modal fade" id="buildTask_modal" tabindex="-1" role="dialog"
	aria-hidden="true">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-body">
				<span class="close new_close" data-dismiss="modal" aria-hidden="true">&times</span>
				<div class="diag_dos">
					<div class="diag_left">
						<ul id="iso_list">
							
						</ul>
					</div>
					<div class="diag_right">
						<div id="mon_scroll" class="scrollDiv">
							<ul id="stdoutList" class="stdout">
								
							</ul>
						</div>
						<div class="dos_list">
							<ul>
								<li>开始时间</li>
								<li>Tag</li>
								<li>镜像</li>
								<li>产品|工程</li>
								<li>状态</li>
							</ul>

							<ul>
								<li id="start_time"></li>
								<li id="iso_tag"></li>
								<li id="iso_name"></li>
								<li id="prod_proj"></li>
								<li id="status"></li>
							</ul>
						</div>
					</div>

				</div>

			</div>
		</div>
	</div>
</div>

<script id="buildTable-tmpl" type="text/x-jquery-tmpl">
{{each(i,row) data}}
	<tr>
		<td class="text-center">
			<a id="a_build_task_gjname_{{= row.def.id}}" href="<%=ContextPath%>/dispatch/mc/1030501?id={{= row.def.id}}&pageNum={{= pageNum}}" class="table-link" title="编辑">{{= row.def.buildName}}</a>
		</td>

		<td class="text-center">
			{{if !CU.isEmpty(row.product) && row.def.isExternal!=1}}
				{{= row.product.name}}
			{{/if}}
		</td>
		<td class="text-center">
			{{if !CU.isEmpty(row.project) && row.def.isExternal!=1}}
				{{= row.project.name}}
			{{/if}}		
		</td>
		<td class="text-center">{{html PU.getDropValue("V_PC_BUILD_DEF_IS_BUILD_IMAGE",row.def.isBuildImage,true)}}</td>
		<td class="text-left">
			{{if !CU.isEmpty(row.imageDef)}}
				{{= row.imageDef.imageFullName}}
			{{/if}}
		</td>
		<td class="text-center">
			{{if !CU.isEmpty(row.lastBuildTask)}}
				{{= CU.toStringDateTime(row.lastBuildTask.taskStartTime)}}
			{{/if}}
		</td>
		<td class="text-center">
			{{if !CU.isEmpty(row.lastBuildTask)}}
				{{= row.lastBuildTask.codeVersion}}
			{{/if}}
		</td>
		<td class="text-center" id="td_build_task_msage_{{= row.def.id}}">
			{{if !CU.isEmpty(row.lastBuildTask)}}
				{{= PU.getDropValue("V_PC_BUILD_TASK_STATUS",row.lastBuildTask.status,false)}}
			{{/if}}
		</td>

		<td class="text-center">
		
		 <input type="hidden" id="thisBackBuildStatues_{{= row.def.id}}" />	
         <input type="hidden" id="thisBackBuildId_{{= row.def.id}}" />

			<a id="a_build_task_zd_{{= row.def.id}}" href="###" class="table-link" title="中断" style="display:none">     <!--hidden style="display:none" -->          
				<span class="fa-stack"> 
					<i class="fa fa-square fa-stack-2x"></i>
					<i class="fa fa-pause fa-stack-1x fa-inverse"></i>
				</span>
			</a>

			<a id="a_build_task_gj_{{= row.def.id}}" href="###" class="table-link" title="构建">
				<span class="fa-stack">
					<i class="fa fa-square fa-stack-2x"></i>
					<i class="fa fa-cog fa-stack-1x fa-inverse"></i>
				</span>
			</a>

			<a id="a_build_task_{{= row.def.id}}" href="###" class="table-link" title="历史任务">
				<span class="fa-stack">
					<i class="fa fa-square fa-stack-2x"></i>
					<i class="fa fa-history fa-stack-1x fa-inverse"></i>
				</span>
			</a>
			<a id="a_remove_build_{{= row.def.id}}" href="###" class="table-link danger" title="删除">
				<span class="fa-stack">
					<i class="fa fa-square fa-stack-2x"></i>
					<i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>
				</span>
			</a>

		</td>
	</tr>
{{/each}}
</script>
 



<jsp:include page="/layout/jsp/footer.jsp"></jsp:include>
