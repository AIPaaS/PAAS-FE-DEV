<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.binary.framework.web.SessionKey,com.binary.framework.bean.User"%>

<%
String ContextPath = request.getContextPath();
User user = (User)session.getAttribute(SessionKey.SYSTEM_USER);

if(user == null) {
	response.sendRedirect(ContextPath+"/index.jsp");
	return ;
}

Long userId = user.getId();
%>

<!doctype html>
<html>
<head>
<title>EBDP-SDAS-CONSOLE Main</title>
<script type="text/javascript">
var ContextPath = "<%=ContextPath%>";

function forward2ModuCode(mc) {
	var url = ContextPath + "/dispatch/mc/"+mc;
	forward(url);
}

function forward(url) {
	document.getElementById("if_page").src = url + "?PARENT_SID=<%=userId%>&ParentLeftWidth=220&ParentHeaderHeight=50&d="+new Date().getTime();
}


</script>
</head>

<body>

<table>
<tr>
	<td colspan="2">
	&nbsp;
	</td>
</tr>
<tr>
	<td width="300" valign="top">
		<table>
			<tr>
				<td>
					<span style="padding-left:20px;">研发管理</span><br>
					<span style="padding-left:60px;"><a href="###" onclick="forward2ModuCode('10301')">创建产品</a></span><br>
					<span style="padding-left:60px;"><a href="###" onclick="forward2ModuCode('10302')">产品管理</a></span><br>
					<span style="padding-left:60px;"><a href="###" onclick="forward2ModuCode('10303')">创建工程</a></span><br>
					<span style="padding-left:60px;"><a href="###" onclick="forward2ModuCode('10304')">工程管理</a></span><br>
					<span style="padding-left:60px;"><a href="###" onclick="forward2ModuCode('10308')">镜像定义</a></span><br>
					<span style="padding-left:60px;"><a href="###" onclick="forward2ModuCode('10305')">构建管理</a></span><br>
					<span style="padding-left:60px;"><a href="###" onclick="forward2ModuCode('10306')">镜像管理</a></span><br>
					<span style="padding-left:60px;"><a href="###" onclick="forward2ModuCode('10307')">发布管理</a></span><br>
				</td>
			</tr>
		</table>
	</td>
	<td align="center">
		<iframe id="if_page" frameborder='1' width='900' height='600' src="###"></iframe>
	</td>
</tr>
</table>

<br>
<br>

</body>

</html>
