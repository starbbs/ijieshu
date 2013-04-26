<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="com.ibook.library.entity.UserInfo" %>
<%@ page import="com.ibook.library.vo.BookLogMessageVo" %>
<%@ page import="java.util.List" %>
<%
	String myMessage="active";
	String myLibrary="";
	String myUserInfo="";
	UserInfo userInfo=(UserInfo)request.getAttribute("userInfo");
	if(null==userInfo){
	ServletContext sc = getServletContext();
	RequestDispatcher rd = null;
	rd = sc.getRequestDispatcher("/user/index"); 
	rd.forward(request, response);
	return;
	}
	List<BookLogMessageVo> bookLogMessageVoList=(List<BookLogMessageVo>)request.getAttribute("BookLogMessageVoList");
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>爱•借书-代书板</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<link href="http://zjdesj.github.io/javascript/mycss/bootstrap.min.css" rel="stylesheet" />
		<link href="http://zjdesj.github.io/javascript/mycss/bootstrap-responsive.min.css" rel="stylesheet" />
		<link href="http://zjdesj.github.io/javascript/mycss/common.css" rel="stylesheet" />
		<link href="http://zjdesj.github.io/javascript/mycss/main.css" rel="stylesheet" />
		<script type="text/javascript">
			var iS = typeof iS == 'undefined' ? {}:iS;
		</script>
	</head>

	<body>
		<%@include file="header.jsp" %>

		<div class="container"  style="padding-top:40px;">

			<div class="form-signin container-fluid">
				<fieldset>
					<legend>我借的书</legend>	
					<div class="row-fluid">
						<%for(BookLogMessageVo bookLogMessageVo : bookLogMessageVoList){
						if(!bookLogMessageVo.isMyBook()){
						%>
						<div class="block span4">
							<div>
								<a class="thumbnail" style="float:left;width: 120px;" title="<%=bookLogMessageVo.getTitle()%>" ><img src="<%=bookLogMessageVo.getBookImg()%>" /></a>		
								<div>
									<h4>留言</h4>
									<p id="<%=bookLogMessageVo.getMsgId()%>"><%=bookLogMessageVo.getMsg()%></p>
								</div>
							</div>		
							<span><textarea id="<%=bookLogMessageVo.getMsgId()%>_text" style="width:300px;"  myid="<%=bookLogMessageVo.getMsgId()%>" rows="1"  placeholder="给对方回复,速速借到书^-^"></textarea></span>
							<div>
								<button  onclick="javascript:applyRequest(<%=bookLogMessageVo.getMsgId()%>);" type="submit" class="btn btn-primary">回复</button>		
							</div>
						</div>
						<%}}%>
					</div>

				</fieldset>
			</div>

			<div class="form-signin container-fluid">
				<fieldset>
					<legend>借出的书</legend>
					<div class="row-fluid">	
						<%for(BookLogMessageVo bookLogMessageVo : bookLogMessageVoList){
						if(bookLogMessageVo.isMyBook()){
						%>
						<div class="block span4">
							<div>
								<a class="thumbnail" style="float:left;width: 120px;" title="<%=bookLogMessageVo.getTitle()%>" ><img src="<%=bookLogMessageVo.getBookImg()%>" /></a>		
								<div>
									<h4>留言</h4>
									<p id="<%=bookLogMessageVo.getMsgId()%>"><%=bookLogMessageVo.getMsg()%></p>
								</div>
							</div>		
							<span><textarea id="<%=bookLogMessageVo.getMsgId()%>_text" style="width:300px;"  myid="<%=bookLogMessageVo.getMsgId()%>" rows="1" cols="60" placeholder="给对方回复,把书借出去吧^-^"></textarea></span>
							<div>
								<button  onclick="javascript:applyRequest(<%=bookLogMessageVo.getMsgId()%>);" type="submit" class="btn btn-primary">回复</button>		
								<button  onclick="javascript:revertRequest(<%=bookLogMessageVo.getLogId()%>);" type="submit" class="btn btn-primary">已归还</button>
							</div>
						</div>
						<%}}%>
					</div>
				</fieldset>
			</div>

		</div>
		<div id="element_to_pop_up"></div>
		<%@include file="footer.html" %>

	</body>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/bootstrap.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/popup.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/index.js"></script>
	<script type="text/javascript">
		function applyRequest(myid) {
			var myMsg = $('#'+myid+"_text").val();
			if (null == myMsg || "" == myMsg) {
				//$('.searchMsg').html("你不想给我发消息吗?");
				//$('.searchMsg').show();
			} else {
				$.post("/user/sendMessage", {
						"id" : myid,
						"msg" : myMsg
						}, function(data) {
						$('#'+myid).prepend("我我: "+myMsg+"<br/>");
						}, "json");
			}
			return false;
		}

		function revertRequest(myid) {
			$.post("/user/revertBook", {
					"id" : myid
					}, function(data) {
					alert(data.msg);
					location.href = "/user/myLend";
					}, "json");
			return false;
		}	
	</script>
</html>
