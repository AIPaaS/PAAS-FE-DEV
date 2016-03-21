<%@ page contentType="text/html; charset=utf-8"%>

<%
String ContextPath = request.getContextPath();
%>

<jsp:include page="/layout/jsp/head.jsp"></jsp:include>





<!-- 正文 -->

<div class="row">
	<div class="col-lg-12">
		<div class="main-box clearfix">
			<div class="main-box-body clearfix">
				<div class="table-responsive">
					<header class="main-box-header clearfix">
					<h4 class="pull-left">镜像 <font color="blue"><span id="span_title"></span></font> 发布记录</h4>
					</header>
					<br>
					<div class="filter-block pull-left">
						<div class="form-group pull-left">
							<div class="form-group pull-left">
								&nbsp;&nbsp;发布日期:
							</div>
							<div class="form-group pull-left">
								<input type="text" class="form-control" id="maskedDate" style="width:160px;">
							</div>
							<div class="form-group pull-left">
								资源中心:
							</div>
							<div class="form-group pull-left">
								<input type="text" name="forcenter" id="forcenter" class="form-control" style="width:120px;" readOnly >
							</div>
						</div>
						<button id="btn_query" class="btn btn-primary"><i class="fa fa-search fa-lg"></i> 查询</button>
					</div>
					<table class="table">
						<thead>
							<tr>
								<th class="text-center">发布开始时间</th>
								<th class="text-center">发布结束时间</th>
								<th class="text-center">发布之前环境</th>
								<th class="text-center">发布之后环境</th>
								<th class="text-center">发布数据中心</th>
								<th class="text-center">发布资源中心</th>
								<th class="text-center">发布人</th>
								<th class="text-center">发布状态</th>
							</tr>
						</thead>
						<tbody id="deployFlowTable">
							
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

<div id="sel_forcenter" style="width:300px;position:absolute;display:none;"></div>

<script id="deployFlowTable-tmpl" type="text/x-jquery-tmpl">
	{{each(i,row) data}}
		<tr>
			<td class="text-center">{{= CU.toStringDateTime(row.depStartTime)}}</td>
			<td class="text-center">{{= CU.toStringDateTime(row.depEndTime)}}</td>
			<td class="text-center">{{html PU.getDropValue("V_PC_IMAGE_STATUS",row.imageBefStatus,true)}}</td>
			<td class="text-center">{{html PU.getDropValue("V_PC_IMAGE_STATUS",row.imageAftStatus,true)}}</td>
			<td class="text-center">{{= PU.getDropValue("DV_DATA_CENTER_CODE",row.dataCenterId,false)}}</td>
			<td class="text-center">{{= PU.getDropValue("DV_RES_CENTER_CODE",row.resCenterId,false)}}</td>
			<td class="text-center">{{= row.depor}}</td>
			<td class="text-center">
				{{if row.depStatus==1 || row.depStatus==2}}
					<img src="<%=ContextPath%>/layout/img/task_loading.gif" style="width:26px;height:26px;"></img>
				{{else}}
					{{html PU.getDropValue("V_PC_IMAGE_DEPLOY_DEP_STATUS",row.depStatus,true)}}
				{{/if}}
			</td>
		</tr>
{{/each}}
</script>




<jsp:include page="/layout/jsp/footer.jsp"></jsp:include>
