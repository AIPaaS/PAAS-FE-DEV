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
						&nbsp;&nbsp;&nbsp;&nbsp;工程代码:
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
								<th class="text-left">文档库</th>
								<th class="text-left">代码库</th>
							</tr>
						</thead>
						<tbody id="projectMgrTable">
							
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




<script id="projectMgrTable-tmpl" type="text/x-jquery-tmpl">
{{each(i,row) data}}
		<tr>
			<td class="text-center">{{= row.project.code}}</td>
			<td class="text-center">{{= row.project.name}}</td>
			<td class="text-center">{{= row.product.name}}</td>
			<td class="text-left">
				{{if row.project.respDocStatus==1}}
					<a href="###" id="a_apply_doc_open_{{= row.project.id}}" data-type="select" data-pk="{{= row.project.id}}" data-title="文档库" class="editable">申请开通</a>
				{{else row.project.respDocStatus==2}}
					[{{= PU.getDropValue("V_PC_PRODUCT_RESP_DOC_TYPE",row.project.respDocType,false)}}]<font color="blue"> 待开通</font>
				{{else row.project.respDocStatus==3}}
					[{{= PU.getDropValue("V_PC_PRODUCT_RESP_DOC_TYPE",row.project.respDocType,false)}}]{{= row.project.respDocUrl}}
				{{/if}}
			</td>
			<td class="text-left">
				{{if row.project.respCodeStatus==1}}
					<a href="###" id="a_apply_code_open_{{= row.project.id}}" data-type="select" data-pk="{{= row.project.id}}" data-title="代码库" class="editable">申请开通</a>
				{{else row.project.respCodeStatus==2}}
					[{{= PU.getDropValue("V_PC_PRODUCT_RESP_DOC_TYPE",row.project.respCodeType,false)}}]<font color="blue"> 待开通</font>
				{{else row.project.respCodeStatus==3}}
					[{{= PU.getDropValue("V_PC_PRODUCT_RESP_DOC_TYPE",row.project.respCodeType,false)}}]{{= row.project.respCodeUrl}}
				{{/if}}
			</td>
		</tr>
{{/each}}
</script>




<jsp:include page="/layout/jsp/footer.jsp"></jsp:include>
