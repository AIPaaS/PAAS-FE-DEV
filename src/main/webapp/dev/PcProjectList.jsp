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
						&nbsp;&nbsp;&nbsp;所属产品:
					</div>
					<div class="form-group pull-left">
						<select id="sel_productId" class="form-control" style="width:160px;">
						</select>
					</div>
					<div class="form-group pull-left">
						工程代码:
					</div>
					<div class="form-group pull-left">
						<input type="text" name="code" id="code" class="form-control">
					</div>
					<div class="form-group pull-left">
						工程名称:
					</div>
					<div class="form-group pull-left">
						<input type="text" name="name" id="name" class="form-control">
					</div>
				</div>
				<button id="btn_query" class="btn btn-primary"><i class="fa fa-search fa-lg"></i> 查询</button>
				<button id="btn_add" class="btn btn-primary"> <i class="fa fa-plus-circle fa-lg"></i> 添加</button>
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
								<th class="text-center">工程代码</th>
								<th class="text-center">工程名称</th>
								<th class="text-center">所属产品</th>
								<th class="text-center">所属机房</th>
								<th class="text-center">工程管理员</th>
								<th class="text-center">状态</th>
								<th class="text-center">描述</th>
							</tr>
						</thead>
						<tbody id="projectTable">
							
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



<script id="projectTable-tmpl" type="text/x-jquery-tmpl">
{{each(i,row) data}}
		<tr>
			<td class="text-center"><a href="<%=ContextPath%>/dispatch/mc/1030301?id={{= row.project.id}}&pageNum={{= pageNum}}">{{= row.project.code}}</a></td>
			<td class="text-center">{{= row.project.name}}</td>
			<td class="text-center">{{= PU.getDropValue("DV_MGR_PRODUCT_CODE",row.project.productId,false)}}</td>
			<td class="text-center">{{= PU.getDropValue("DV_COMP_ROOM_CODE",row.project.compRoomId,false)}}</td>
			<td class="text-center">
				{{each(j,sysOp) row.mgrOps}}
					{{if j>0}}
						,
					{{/if}}
					{{= sysOp.opName}}
				{{/each}}	
			</td>
			<td class="text-center">{{html PU.getDropValue("V_EDM_PRODUCT_STATUS",row.project.status,true)}}</td>
			<td class="text-center">{{= row.project.remark}}</td>
		</tr>
{{/each}}
</script>




<jsp:include page="/layout/jsp/footer.jsp"></jsp:include>
