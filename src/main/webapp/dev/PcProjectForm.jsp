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
		<form class="form-horizontal" role="form" id="form_project">
			<div class="form-group">
				<label for="productId" class="col-lg-2 control-label">所属产品<font color="red">*</font>:</label>
				<div class="col-lg-5">
					<select id="productId" name="productId" required class="form-control">
					</select>
				</div>
				<div class="col-lg-5">
					<span></span>
				</div>
			</div>
			<div class="form-group">
				<label for="compRoomId" class="col-lg-2 control-label">所属机房<font color="red">*</font>:</label>
				<div class="col-lg-5">
					<select id="compRoomId" name="compRoomId" required class="form-control">
					</select>
				</div>
				<div class="col-lg-5">
					<span></span>
				</div>
			</div>
			
			<div class="form-group">
				<label for="code" class="col-lg-2 control-label">工程代码<font color="red">*</font>:</label>
				<div class="col-lg-5">
					<input type="text" name="code" class="form-control" id="code" required pattern="([0-9]|[a-z]|[_]){1,40}" placeholder="必填">
				</div>
				<div class="col-lg-5">
					<span>1-40位   小写字母、数字或下划线的组合</span>
				</div>
			</div>			
			<div class="form-group">
				<label for="name" class="col-lg-2 control-label">工程名称<font color="red">*</font>:</label>
				<div class="col-lg-5">
					<input type="text" name="name" class="form-control" id="name" required pattern=".{1,20}" placeholder="必填">
				</div>
				<div class="col-lg-5">
					<span>1-20位</span>
				</div>
			</div>
			<div class="form-group">
				<label for="mgrIds" class="col-lg-2 control-label">工程管理员<font color="red">*</font>:</label>
				<div class="col-lg-5">
					<input type="text" name="mgrIds" class="form-control" readonly id="mgrIds" placeholder="">
				</div>
				<div class="col-lg-5">
					<span><a id="btn_select_mgr" href="###" title="选择用户"><i class="fa fa-search fa-lg"></i></a></span>
				</div>
			</div>
			<div class="form-group">
				<label class="col-lg-2 control-label">工程状态:</label>
				<div class="col-lg-5">
					<input type="radio" id="status1" name="status" placeholder="" checked><label for="status1">&nbsp;正常</label>&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" id="status0" name="status" placeholder=""><label for="status0">&nbsp;停用</label>
				</div>
				<div class="col-lg-5">
					<span></span>
				</div>
			</div>
			<div class="form-group">
				<label for="remark" class="col-lg-2 control-label">工程描述:</label>
				<div class="col-lg-5">
					<textarea name="remark" rows="3" cols="3" class="form-control" id="remark" maxlength="100"></textarea>
				</div>
				<div class="col-lg-5">
					<span>1-100位</span>
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
