<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="com.ibook.library.entity.UserInfo"%>
<%@ page import="com.ibook.library.vo.BookVo"%>
<%@ page import="com.ibook.library.cst.Constants"%>
<%@ page import="com.ibook.library.util.Page"%>
<%@ page import="java.util.List"%>
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
<title>爱♥借书-藏书阁</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta content="爱借书，借书，虚拟，图书馆，地理位置，借阅，图书" name="keywords">
<meta content="爱借书(IJieShu.com)基于地理位置的虚拟图书馆,我们的愿景是线上共享附近的书籍,线下互相借阅,做全球最大的图书馆" name="description">
<meta name="author" content="袋鼠">

<link href="/css/bootstrap.css" rel="stylesheet" />
<link href="/css/bootstrap-ie6.css" rel="stylesheet" />
<link href="/css/bootstrap-responsive.css" rel="stylesheet" />
<link href="/css/common.css" rel="stylesheet" />
<link href="/css/main.css" rel="stylesheet" />
<script type="text/javascript">
	var iS = typeof iS == 'undefined' ? {} : iS;
	iS.globalUserId =<%=userId%>;
	iS.action=function(){
		window.location.reload();
	};
</script>
</head>

<body>
	<div class="out_container">
		<%@include file="header.jsp"%>

		<div class="container form-signin" style="padding-top: 70px;">

			<div class="bookcontainer">
				<%if(bookList.size()<=0){ %>
						<div class="shujia">							
							<h4>赶快加入附近的图书馆吧！</h4>
							<p>加入图书馆，与爱书之人共享图书.....~~
							<a  href="/user/index" class="label label-success">浏览图书馆</a>
							</p>													
						</div>
				<%} %>			
				<%
				    for (BookVo book : bookList) {
				%>
				<div class="bookspan2 _book">
					<div class="_pinglun hide"
						style="opacity: 0.8; position: absolute; bottom: 0; left: 0; width: 118px; background-color: rgb(233, 238, 242);">

						<div>
							<div class="l">
								<a class="label label-info l" title="查看豆瓣书评"
									href="<%=book.getAlt()%>" target="blank"> 豆瓣书评</a>
							</div>
							<%
							    if (book.getStatus() == Constants.BOOK_STATUS_FREE && book.getOwnerUserId() != userId) {
							%>
							<a user_id="<%=book.getOwnerUserId()%>" bid="<%=book.getId()%>" rel="<%=book.getTitle()%>"
								class="_lend label label-info r">借阅</a>
							<%
							    } else if (book.getStatus() != Constants.BOOK_STATUS_FREE) {
							%>
							<span class="label label-success r">已借出</span>
							<%
							    }
							%>
						</div>
					</div>
					<a class=" by" rel="<%=book.getTitle()%>" bid="<%=book.getId()%>"
						title="《<%=book.getTitle()%>》"> <img
						src="<%=book.getMediumImg()%>" width="109px" height="135px"
						alt="<%=book.getTitle()%>" />
					</a>
				</div>
				<%
				    }
				%>
			</div>
			<div class="pagination pagination-centered">
				<ul>
					<%
					    if (start != 1) {
					%>
					<li class=""><a
						href="/user/mylibrary?query=<%=query%>&pageIndex=<%=(start - 1) > 0 ? (start - 1) : 1%>">&laquo;</a></li>
					<%
					    }
					%>
					<%
					    for (int i = start; i <= end; i++) {
					%>
					<li class="<%=mypage.getPageIndex() == i ? "active" : ""%>"><a
						href="/user/mylibrary?query=<%=query%>&pageIndex=<%=i%>"><%=i%></a></li>
					<%
					    }
					%>
					<%
					    if (end != mypage.getTotlePages()) {
					%>
					<li class=""><a
						href="/user/mylibrary?query=<%=query%>&pageIndex=<%=(end + 1 > mypage.getTotlePages()) ? mypage.getTotlePages() : end + 1%>">&raquo;</a></li>
					<%
					    }
					%>
				</ul>
			</div>
		</div>

		<%@include file="footer.html"%>

		<div id="myModal" class="modal hide fade">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h3>借阅图书申请</h3>
			</div>
			<div class="modal-body"></div>
		</div>
	</div>
</body>
<script type="text/javascript" src="/js/bootstrap.js"></script>
<script type="text/javascript" src="/js/popup.js"></script>
<script type="text/javascript" src="/js/index.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
</html>
