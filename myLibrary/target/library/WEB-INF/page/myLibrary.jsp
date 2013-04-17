<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="com.ibook.library.entity.UserInfo" %>
<%@ page import="com.ibook.library.vo.BookVo" %>
<%@ page import="com.ibook.library.cst.Constants" %>
<%@ page import="java.util.List" %>
<%
String myMessage="";
String myLibrary="active";
String myUserInfo="";
int userId=0;
UserInfo userInfo=(UserInfo)request.getAttribute("userInfo");
if(null!=userInfo){
    userId=userInfo.getId();
}
List<BookVo> bookList=(List<BookVo>)request.getAttribute("bookList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>爱•借书-藏书阁</title>
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
.thumbnail > img{width: 102px;height: 145px;}
.row-fluid{margin-top: 10px;}
.row-fluid div{vertical-align: top;}
.row-fluid [class*="span"]{margin-right: 10px\0;*margin-right: 10px;}      
</style>
</head>

<body>
<%@include file="header.jsp"%>

 <div class="container form-signin" style="padding-top:70px;">

	<div  class="container-fluid">
	<%for(BookVo book : bookList){
	%>
	<div class="span2" style="width: 120px;">
		<% if(book.getStatus()==Constants.BOOK_STATUS_FREE && book.getOwnerUserId()!=userId){	%>
		<a class="thumbnail"  title="借阅 《<%=book.getTitle()%>》" href="javascript:borrowRequest('<%=book.getId()%>','<%=book.getTitle()%>')">
			<img src="<%=book.getMediumImg()%>" alt="<%=book.getTitle()%>" />
		</a>
		<a title="豆瓣书评" href="<%=book.getAlt()%>" target="blank">
			豆瓣书评(可借阅)
		</a>		
		<%}else if(book.getStatus()!=Constants.BOOK_STATUS_FREE){ %>
		<a class="thumbnail"  title="<%=book.getTitle()%>">
			<img src="<%=book.getMediumImg()%>" alt="<%=book.getTitle()%>" />
		</a>
		<a title="豆瓣书评" href="<%=book.getAlt()%>" target="blank">
			豆瓣书评(已外借)
		</a>				
		<%}else{ %>
		<a class="thumbnail"  title="<%=book.getTitle()%>">
			<img src="<%=book.getMediumImg()%>" alt="<%=book.getTitle()%>" />
		</a>
		<a title="豆瓣书评" href="<%=book.getAlt()%>" target="blank">
			豆瓣书评
		</a>				
		<%}%>		
	</div>
	<%}%>
	</div>
</div>
	
	 <div id="element_to_pop_up"></div>
<%@include file="footer.html" %>
	 
</body>
<script type="text/javascript">
var globalUserId=<%=userId%>;	
	function borrowRequestSubmit(myid){
		var myMsg=$('#borrowMsg').val();
		if(null==myMsg||""==myMsg){
			//$('.searchMsg').html("你不想给我发消息吗?");
			//$('.searchMsg').show();
		}else{
			$.post("/user/borrowBook", { "id": myid,"msg":myMsg},
					   function(data){
						 //$('#element_to_pop_up').hide();
					     alert(data.msg); // John
					     location.href = "/user/mylibrary";
					   }, "json");
		}
		return false;
	}
	
	function borrowRequest(id,title){
		if(0==globalUserId){
			alert("登录后就可以借阅 《"+title+"》 哟!");
			return;
		}
		$("#element_to_pop_up").html("");//清空
		$("#element_to_pop_up").append(
        		'<a style="float: left;padding:20px;">'+
                '<textarea id="borrowMsg" style="width:700px;"  myid="'+id+'" rows="6" cols="600" placeholder="欢迎您借阅俺的  《'+title+'》  ,给我留言唷^-^"></textarea>'+
                '<button id="borrowBtn" onclick="javascript:borrowRequestSubmit('+id+');" type="button" class="btn btn-primary">求借</button>'+
                '</a>');
		$('#element_to_pop_up').bPopup();
	}

</script>
</html>