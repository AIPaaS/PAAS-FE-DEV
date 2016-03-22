<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.binary.core.util.BinaryUtils"%>

<%
String ContextPath = request.getContextPath();
%>

<jsp:include page="/layout/jsp/head.jsp"></jsp:include>


<!-- 正文 -->
<div class="main-box">
	<header class="main-box-header clearfix"> </header>
	<div class="main-box-body clearfix">
		<form class="form-horizontal" role="form" id="form_imageDef">
		
		<div class="form-group">
				<label for="contactEmail" class="col-lg-2 control-label">是否外部镜像:</label>
				<div class="col-lg-1">
					<input type="checkbox" name="isExternal" id="isExternal">
				</div>
				<div class="col-lg-11">
					<span></span>
				</div>
			</div>
			<div class="form-group" id="div_productId">
				<label for="productId" class="col-lg-2 control-label">所属产品<font color="red">*</font>:</label>
				<div class="col-lg-5">
					<select id="productId" name="productId" class="form-control">
					</select>
				</div>
				<div class="col-lg-5">
					<span></span>
				</div>
			</div>
			<div class="form-group" id="div_projectId">
				<label for="projectId" class="col-lg-2 control-label">所属工程<font color="red">*</font>:</label>
				<div class="col-lg-5">
					<select id="projectId" name="projectId" class="form-control">
					</select>
				</div>
				<div class="col-lg-5">
					<span></span>
				</div>
			</div>
			<!-- **************************************************************************** -->
		
			<div class="form-group">
				<label for="dirName" class="col-lg-2 control-label">目录名<font color="red">*</font>:</label>
				
				
				
				<div class="col-lg-5" >
				    <span id="imageLockName" class="num_tit" style="margin-right:5px;"></span> 
					<input type="text" style="width:60%;"  name="dirName" class="form-control" id="dirName" required pattern="([0-9]|[a-z]|[_]|[/]){1,200}" placeholder="必填">
				</div>
				
				<div class="col-lg-5">
					<span>1-200位字母、数字、下划线或斜线的组合</span>
				</div>
			</div>
			
			
			<div class="form-group">
				<label for="imageName" class="col-lg-2 control-label">镜像名<font color="red">*</font>:</label>
				<div class="col-lg-5">
					<input type="text" name="imageName" class="form-control" id="imageName" required pattern="([0-9]|[a-zA-Z]|[_]|[-]){1,100}" placeholder="必填">
				</div>
				<div class="col-lg-5">
					<span>1-100位字母、数字或下划线的组合</span>
				</div>
			</div>
			
			
			
			<div class="form-group">
				<label for="versionNo" class="col-lg-2 control-label">版本号<font color="red">*</font>:</label>
				<div class="col-lg-5">
					<input type="text" name="versionNo" class="form-control" id="versionNo" required pattern="[0-9]+[.][0-9]+" maxlength="30" placeholder="必填">
				</div>
				<div class="col-lg-5">
					<span>版本号为数字和小数点组合，例如：1.1</span>
				</div>
			</div>

			<div class="form-group">
				<div class="col-lg-offset-2 col-lg-10">
					<button type="submit" id="btn_submit" class="btn btn-success">提交</button>
				</div>
			</div>
		</form>
	</div>
</div>

<jsp:include page="/layout/jsp/footer.jsp"></jsp:include>
