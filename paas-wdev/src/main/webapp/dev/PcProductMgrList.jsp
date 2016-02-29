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
						&nbsp;&nbsp;&nbsp;产品代码:
					</div>
					<div class="form-group pull-left">
						<input type="text" name="code" id="code" class="form-control">
					</div>
					<div class="form-group pull-left">
						产品名称:
					</div>
					<div class="form-group pull-left">
						<input type="text" name="name" id="name" class="form-control">
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
								<th class="text-center">产品代码</th>
								<th class="text-center">产品名称</th>
								<th class="text-center">文档库</th>
								<th class="text-center">操作</th>
							</tr>
						</thead>
						<tbody id="productMgrTable">
							
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



<script id="productMgrTable-tmpl" type="text/x-jquery-tmpl">
	{{each(i,row) data}}
		<tr>
			<td class="text-center">{{= row.code}}</td>
			<td class="text-center">{{= row.name}}</td>
			<td class="text-center">
				{{if row.respDocStatus==1}}
					<a href="###" id="a_apply_doc_open_{{= row.id}}" data-type="select" data-pk="{{= row.id}}" data-title="文档库" class="editable">申请开通</a>
				{{else row.respDocStatus==2}}
					[{{= PU.getDropValue("V_PC_PRODUCT_RESP_DOC_TYPE",row.respDocType,false)}}]<font color="blue"> 待开通</font>
				{{else row.respDocStatus==3}}
					[{{= PU.getDropValue("V_PC_PRODUCT_RESP_DOC_TYPE",row.respDocType,false)}}]{{= row.respDocUrl}}
				{{/if}}
			</td>
			<td class="text-center">
				<a href="###" id="a_permission_mgr_{{= row.id}}" class="table-link" title="文档库权限管理">
					<span class="fa-stack">
						<i class="fa fa-square fa-stack-2x"></i>
						<i class="fa fa-flash fa-stack-1x fa-inverse"></i>
					</span>
				</a>
			</td>
		</tr>
{{/each}}
</script>




<jsp:include page="/layout/jsp/footer.jsp"></jsp:include>
