<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="com.ibook.library.entity.UserInfo" %>
<%@ page import="com.ibook.library.vo.BookVo" %>
<%@ page import="com.ibook.library.cst.Constants" %>
<%@ page import="java.util.List" %>
<%
	String myMessage="";
	String myLibrary="";
	String myUserInfo="";
	int userId=0;
	UserInfo userInfo=(UserInfo)request.getAttribute("userInfo");
	Integer bookCount=(Integer)request.getAttribute("bookCount");
	Integer libraryCount=(Integer)request.getAttribute("libraryCount");
	Integer userCount=(Integer)request.getAttribute("userCount");
	if(null!=userInfo){
		userId=userInfo.getId();
	}
	List<BookVo> bookList=(List<BookVo>)request.getAttribute("bookList");
%>
<!DOCTYPE html> 
<html>
	<head>
		<title>爱•借书-首页</title>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<link href="http://zjdesj.github.io/javascript/mycss/bootstrap.min.css" rel="stylesheet" />
		<link href="http://zjdesj.github.io/javascript/mycss/bootstrap-responsive.min.css" rel="stylesheet" />
		<link href="http://zjdesj.github.io/javascript/mycss/common.css" rel="stylesheet" />
		<link href="http://zjdesj.github.io/javascript/mycss/main.css" rel="stylesheet" />

		<script type="text/javascript">
			var iS = typeof iS == 'undefined' ? {}:iS;
			iS.globalUserId=<%=userId%>;
		</script>
	</head>

	<body>
		<%@include file="header.jsp"%>

		<div class="container form-signin" style="padding-top:70px;">
			<div class="hero-unit" style="padding: 20px;">
				<h2>爱•借书—基于地理位置的虚拟图书馆</h2>
				<p>我们的愿景是共享大家附近的书籍  <span class='r'>目前为止<span>图书<%=bookCount%>本</span><span>馆君<%=userCount%>位 </span><span>图书馆<%=libraryCount%>座</span></span></p>
			</div>
			<div id="mapContainer" style="width:100%;height:400px;"></div> 
			<div  class="container-fluid" style="padding-top:40px;">
				<fieldset>
					<legend>新书上架</legend>
					<%for(BookVo book : bookList){
					%>
					<div class="span2" style="width: 120px;">
						<% if(book.getStatus()==Constants.BOOK_STATUS_FREE){	%>
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
						<%}%>		
					</div>
					<%}%>
				</fieldset>
			</div>
		</div>

		<div id="element_to_pop_up"></div>
		<%@include file="footer.html" %>

	</body>
		
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/bootstrap.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/popup.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=0Ef9e97e70efac4cffa7359a61e9e8ad"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/MarkerTool_min.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/map.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/index.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/common.js"></script>
</html>
