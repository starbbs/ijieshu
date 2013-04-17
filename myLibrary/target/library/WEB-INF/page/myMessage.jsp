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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>爱•借书-代书板</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<script type="text/javascript" src="../../js/bootstrap.js"></script>
<script type="text/javascript" src="../../js/jquery-1.9.1.js"></script> 
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/bootstrap.js"></script>
<script type="text/javascript" src="../../js/bootstrap-collapse.js"></script>
<link href="../../css/bootstrap.css" rel="stylesheet" />
<link href="../../css/bootstrap-responsive.css" rel="stylesheet" />
<link href="../../css/common.css" rel="stylesheet" />

<script type="text/javascript" src="../../js/jquery.bpopup.min.js"></script>

<style>
.center {
	width: 450px;
	height: 150px;
	float: left;
}
#element_to_pop_up { 
    background-color:#fff;
    border-radius:15px;
    color:#000;
    display:none; 
    padding:20px;
    width:750px;
    min-height: 180px;
}
.b-close{
    cursor:pointer;
    position:absolute;
    right:10px;
    top:5px;
}
      .form-signin {
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      
      .block {
      	max-width: 400px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 0px solid #e5e5e5;

      }
.thumbnail > img{width: 102px;height: 145px;}
.row-fluid{margin-top: 10px;}
.row-fluid div{vertical-align: top;}
.row-fluid [class*="span"]{margin-right: 10px\0;*margin-right: 10px;}      
</style>
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