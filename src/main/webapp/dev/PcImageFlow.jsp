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
					<h4 class="pull-left">镜像 <font color="blue"><span id="span_title"></span></font> 历史版本&nbsp;&nbsp;<a id="a_backToImageList" href="###"><i class="fa fa-rotate-left"></i> 返回</a></h4>
				
					</header>
					<br>
					<div class="filter-block pull-left">
						<div class="form-group pull-left">
							<div class="form-group pull-left">
								&nbsp;&nbsp;生成日期:
							</div>
							<div class="form-group pull-left">
								<input type="text" class="form-control" id="maskedDate" style="width:160px;">
							</div>
							<div class="form-group pull-left">
								build号:
							</div>
							<div class="form-group pull-left">
								<input type="text" name="buildNo" id="buildNo" class="form-control" style="width:160px;">
							</div>
						</div>
						<button id="btn_query" class="btn btn-primary"><i class="fa fa-search fa-lg"></i> 查询</button>
					</div>
					<table class="table">
						<thead>
							<tr>
								<th class="text-left">镜像名</th>
								<th class="text-center">生成时间</th>
								<th class="text-center">build号</th>
								<th class="text-center">生成镜像人员</th>
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



<script id="imageTable-tmpl" type="text/x-jquery-tmpl">
	{{each(i,row) data}}
		<tr>
			<td class="text-left">{{= row.image.imageFullName}}</td>
			<td class="text-center">
				{{= CU.toStringDateTime(row.image.buildTime)}}
			</td>
			<td class="text-center">{{= row.image.buildNo}}</td>
			<td class="text-center">{{= row.image.buildUser}}</td>
			<td class="text-center">
				<a id="a_remove_image_{{= row.image.id}}" href="###" class="table-link danger" title="删除">
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
