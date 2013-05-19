<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="com.ibook.library.entity.UserInfo" %>
<%@ page import="com.ibook.library.vo.BookVo" %>
<%@ page import="com.ibook.library.cst.Constants" %>
<%@ page import="com.ibook.library.util.Page" %>
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
	Page<BookVo> mypage=(Page<BookVo>)request.getAttribute("page");
	String query=(String)request.getAttribute("query");
	List<BookVo> bookList=mypage.getList();
	int start=(mypage.getTotlePages()-(mypage.getPageIndex()+4))>=0?mypage.getPageIndex():((mypage.getTotlePages()-4)>0?(mypage.getTotlePages()-4):1);
	int end=(start+4)>mypage.getTotlePages()?mypage.getTotlePages():(start+4);
	
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>爱•借书-藏书阁</title>
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

			<div  class="container-fluid">
				<%for(BookVo book : bookList){
				%>
				<div class="bookspan2" style="width: 120px;">
					<% if(book.getStatus()==Constants.BOOK_STATUS_FREE && book.getOwnerUserId()!=userId){	%>
					<a class="thumbnail by" rel="<%=book.getTitle()%>" bid="<%=book.getId()%>" title="借阅 《<%=book.getTitle()%>》">
						<img src="<%=book.getMediumImg()%>" alt="<%=book.getTitle()%>" />
					</a>
					<a title="豆瓣书评" href="<%=book.getAlt()%>" target="blank">
						豆瓣书评(可借阅)
					</a>		
					<%}else if(book.getStatus()!=Constants.BOOK_STATUS_FREE){ %>
					<a class="thumbnail bn"  rel="<%=book.getTitle()%>" bid="<%=book.getId()%>"  title="<%=book.getTitle()%>">
						<img src="<%=book.getMediumImg()%>" alt="<%=book.getTitle()%>" />
					</a>
					<a title="豆瓣书评" href="<%=book.getAlt()%>" target="blank">
						豆瓣书评(已外借)
					</a>				
					<%}else{ %>
					<a class="thumbnail my"  rel="<%=book.getTitle()%>" bid="<%=book.getId()%>"  title="<%=book.getTitle()%>">
						<img src="<%=book.getMediumImg()%>" alt="<%=book.getTitle()%>" />
					</a>
					<a title="豆瓣书评" href="<%=book.getAlt()%>" target="blank">
						豆瓣书评
					</a>				
					<%}%>		
				</div>
				<%}%>
			</div>
<div class="pagination pagination-centered">
  <ul>
    	<%if(start!=1) {%>
    		<li class=""><a href="/user/mylibrary?query=<%=query %>&pageIndex=<%=(start-1)>0?(start-1):1%>">&laquo;</a></li>
	    <%} %>
	    <%for(int i=start;i<=end;i++){ %>
	    	<li class="<%=mypage.getPageIndex()==i?"active":""%>"><a href="/user/mylibrary?query=<%=query %>&pageIndex=<%=i%>"><%=i%></a></li>
	    <%}%>
    	<%if(end!=mypage.getTotlePages()) {%>
    		<li class=""><a href="/user/mylibrary?query=<%=query %>&pageIndex=<%=(end+1>mypage.getTotlePages())?mypage.getTotlePages():end+1%>">&raquo;</a></li>
	    <%} %>	    
  </ul>
</div>			
		</div>

		<%@include file="footer.html" %>

	<div id="myModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>借阅图书申请</h3>
		</div>
		<div class="modal-body"></div>
	</div>
		
	</body>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/bootstrap.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/popup.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/index.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/common.js"></script>
</html>
