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
						class="form-control" style="width: 120px;">
				</div>
				<div class="form-group pull-left">产品:</div>
				<div class="form-group pull-left">
					<select id="sel_productId" class="form-control"
						style="width: 120px;">
					</select>
				</div>
				<div class="form-group pull-left">工程:</div>
				<div class="form-group pull-left">
					<select id="sel_projectId" class="form-control"
						style="width: 120px;">
					</select>
				</div>
				<div class="form-group pull-left">是否生成镜像:</div>
				<div class="form-group pull-left">
					<select id="sel_isBuildImage" class="form-control"
						style="width: 120px;">
					</select>
				</div>
				<button id="btn_query" class="btn btn-primary">
					<i class="fa fa-search fa-lg"></i> 查询
				</button>
				<button id="btn_add" class="btn btn-primary">
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
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="div_title"></h4>
			</div>
			<div class="modal-body">
				<div class="diag_dos">
					<div class="diag_left">
						<ul>
							<li class="active"><a href="javascript:void(0)">crm-web-1.0</a></li>
							<li><a href="javascript:void(0)">crm-web-0.9</a></li>
							<li><a href="javascript:void(0)">crm-web-0.8</a></li>
							<li><a href="javascript:void(0)">crm-web-0.7</a></li>
							<li><a href="javascript:void(0)">crm-web-0.6</a></li>
							<li><a href="javascript:void(0)">crm-web-0.5</a></li>
							<li><a href="javascript:void(0)">crm-web-0.4</a></li>
							<li><a href="javascript:void(0)">crm-web-0.3</a></li>
							<li><a href="javascript:void(0)">crm-web-0.2</a></li>
							<li><a href="javascript:void(0)">crm-web-0.1</a></li>
						</ul>
					</div>
					<div class="diag_right">
						<div id="mon_scroll" class="scrollDiv">
							<ul>
								<li>jquery 滚动条 Scrollbar 设置浏览器默认滚动条样式</li>
								<li>jquery 图片切换 switchable 带左右按钮控制分页索引图片切换</li>
								<li>jquery powerFloat万能浮动框提示插件 支持图片、文字、ajax异步加载、表单验证等</li>
								<li>jquery 弹出层插件 ThickBox 多功能弹出层插件支持图片、ajax、内嵌内容弹等</li>
								<li>jquery 表单美化 jquery tzCheckbox 复选框美化 自定义默认复选框</li>
								<li>jquery HTML5 幻灯片插件 用 Canvas 制作类似百叶窗拍摄快门摄影拍摄效果</li>
								<li>jquery 弹出层插件 ThickBox 多功能弹出层插件支持图片、ajax、内嵌内容弹等</li>
								<li>jquery 表单美化 jquery tzCheckbox 复选框美化 自定义默认复选框</li>
								<li>jquery HTML5 幻灯片插件 用 Canvas 制作类似百叶窗拍摄快门摄影拍摄效果</li>
								<li>1111111111111111111</li>
								<li>2222222222222</li>
								<li>3333333333333333</li>
								<li>1111111111111111111</li>
								<li>2222222222222</li>
								<li>3333333333333333</li>
								<li>4444</li>
							</ul>
						</div>
						<div class="dos_list">
							<ul>
								<li>开始时间</li>
								<li>耗时</li>
								<li>Tag</li>
								<li>镜像</li>
								<li>产品\工程</li>
								<li>状态</li>
							</ul>

							<ul>
								<li>2016-02-30</li>
								<li>366s</li>
								<li>Tag</li>
								<li>镜像</li>
								<li>产品\工程</li>
								<li>状态</li>
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
		<td class="text-center"><a href="<%=ContextPath%>/dispatch/mc/1030501?id={{= row.def.id}}&pageNum={{= pageNum}}" class="table-link" title="编辑">{{= row.def.buildName}}</a></td>
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
		<td class="text-center">
			{{if !CU.isEmpty(row.lastBuildTask)}}
				{{= PU.getDropValue("V_PC_BUILD_TASK_STATUS",row.lastBuildTask.status,false)}}
			{{/if}}
		</td>
		<td class="text-center">
			<a href="###" class="table-link" title="构建">
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



			<a id="a_zd_build_{{= row.def.id}}" href="###" class="table-link" title="中段">               
				<span class="fa-stack"> 
					<i class="fa fa-square fa-stack-2x"></i>
					<i class="fa  fa-stack-1x fa-inverse"><font size="-2">中断</font></i>
				</span>
			</a>


		</td>
	</tr>
{{/each}}
</script>



<jsp:include page="/layout/jsp/footer.jsp"></jsp:include>
