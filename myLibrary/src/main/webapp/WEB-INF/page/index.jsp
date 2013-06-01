<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="com.ibook.library.entity.UserInfo"%>
<%@ page import="com.ibook.library.entity.Book"%>
<%@ page import="com.ibook.library.vo.BookVo"%>
<%@ page import="com.ibook.library.cst.Constants"%>
<%@ page import="java.util.List"%>
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
	List<Book> myBookList=(List<Book>)request.getAttribute("myBookList");
%>
<!DOCTYPE html>
<html>
<head>
<title>爱♥借书-首页</title>
<meta charset="utf-8">
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
	if (typeof iS == 'undefined') {
		var iS = {};
	}
	iS.globalUserId =
<%=userId%>
	;
	iS.action = function() {
		window.location.reload();
	};
</script>
</head>

<body>
	<div class="out_container">
		<%@include file="header.jsp"%>

		<div class="container form-signin">
			<div class="hero-unit" style="padding: 20px;">
				<h3>爱借书(I JieShu)—基于地理位置的虚拟图书馆</h3>
				<p>
				新人指南:
				①<a href="#mapContainer" class="label label-success">创建|加入</a>图书馆(在地图上标注)
				②<a href="#js_tabbable" class="label label-success">上传</a>图书(与图书馆内的其他馆君分享图书)
				③<a href="/user/mylibrary" class="label label-success">借阅</a>图书(借阅您加入的图书馆内的图书)
				</p>
			</div>
			<div id="mapContainer"
				class="js_mapcontainer google-maps img-polaroid"
				style="height: 400px;"></div>
			<div id="js_tabbable" class="tabbable" style="padding: 20px;">
				<ul class="nav nav-pills">
					<li  class="active"><a href="#tab1" data-toggle="tab">新书速递</a></li>				
					<li><a href="#tab2"  data-toggle="tab">我的书架</a></li>														
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="tab1">
						<div class="bookcontainer">
						<%if(bookList.size()<=0){ %>
						<div class="shujia">							
							<h4>赶快来上传图书或加入您附近的图书馆吧！</h4>
							<p>加入图书馆，共享无限书籍.....~~<a href="#mapContainer" class="label label-success">加入图书馆</a></p>
							<p>上传图书，分享书香...~~<a href="#tab2" data-toggle="tab" class="label label-success">上传图书</a></p>																
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
										<a user_id="<%=book.getOwnerUserId()%>"
											bid="<%=book.getId()%>" rel="<%=book.getTitle()%>"
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
								<a class=" by" rel="<%=book.getTitle()%>"
									bid="<%=book.getId()%>" title="《<%=book.getTitle()%>》"> <img
									src="<%=book.getMediumImg()%>" width="109px" height="135px"
									alt="<%=book.getTitle()%>" />
								</a>
							</div>
							<%
							    }
							%>
						</div>
					</div>
					<div class="tab-pane" id="tab2">
					<%if(myBookList.size()>0){ %>
						<div class="input-append">
							<input class="span4 " id="query" type="text" placeholder="请输入 书名/作者/ISBN" data-placement="top" data-content="输入 书名/作者/ISBN 再点图书上架哟!">
							<button    id="upBookBtn" type="submit" class="btn btn-success">图书上架</button>
						</div>
					<%} %>
						<div class="bookcontainer">
							<%
							    for (Book book : myBookList) {
							%>
							<div class="bookspan2 _book">
								<%
								    if (book.getStatus() == Constants.BOOK_STATUS_FREE) {
								%>
								<div class="_pinglun hide"
									style="position: absolute; top: 0; left: 0; width: 118px;">
									<div>
										<div class="r">
											<a title="图书下架" href="javascript:void(0);"
												onclick="javascript:iS.delBook('<%=book.getId()%>');"> <img
												alt="图书下架" src="/img/close.png" /></a>
										</div>
									</div>
								</div>
								<%
								    }
								%>
								<div class="_pinglun hide"
									style="opacity: 0.8; position: absolute; bottom: 0; left: 0; width: 118px; background-color: rgb(233, 238, 242);">

									<div>
										<div class="l">
											<a class="label label-info l" title="书名作为我的昵称"
												href="javascript:void(0);"
												onclick="javascript:iS.updateNick('<%=book.getTitle()%>');">书名作为我的昵称</a>
										</div>
										<%
										    if (book.getStatus() != Constants.BOOK_STATUS_FREE) {
										%>
										<span class="label label-success r">已借出</span>
										<%
										    }
										%>
									</div>
								</div>
								<a class=" by" rel="<%=book.getTitle()%>"
									bid="<%=book.getId()%>" title="《<%=book.getTitle()%>》"> <img
									src="<%=book.getMediumImg()%>" width="109px" height="135px"
									alt="<%=book.getTitle()%>" />
								</a>
							</div>
							<%
							    }
							%>
						</div>
						<%if(myBookList.size()<=0){ %>
						<div class="shujia">							
							<h4>赶快来上传图书吧！</h4>
							<p>上传图书，与爱书之人共享图书.....~~
							<span class="input-append">
								<input class="span3" id="query" type="text" placeholder="请输入 书名/作者/ISBN"  data-placement="top" data-content="输入 书名/作者/ISBN 再点图书上架哟!">
								<button id="upBookBtn" type="submit" class="btn btn-success">图书上架</button>
							</span>
							</p>													
						</div>
					<%} %>
					</div>
				</div>
			</div>

		</div>
		<%@include file="footer.html"%>

		<div id="myModal" class="modal hide fade">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h3>图书上架</h3>
			</div>
			<div class="modal-body"></div>
		</div>
		
	</div>
</body>

<script type="text/javascript" src="/js/bootstrap.js"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=1.5&ak=9097a620e2f941e69ad38ce70415caaa"></script>
<script type="text/javascript" src="/js/MarkerTool_min.js"></script>
<script type="text/javascript" src="/js/map.js"></script>
<script type="text/javascript" src="/js/index.js"></script>
<script type="text/javascript">
		/** 搜索图书逻辑 **/
		$('#upBookBtn').click(function(){
				if(0==iS.globalUserId){
					$('#loginModal').modal('toggle');
					return false;
				}
				var query=$('#query').val();				
				if(null==query||""==query){
					//iS.showTip("输入 书名/作者/ISBN 再点图书上架哟!");
					$('#query').popover('show');
				}else{
					$('#query').popover('hide');
					searchBook(query);		
				}
				return false;
				});

function searchBook(query){
	var url ="/book/search?query="+encodeURI(query);
	$.getJSON(url, function (data) {
		$(".modal-header h3").html("图书上架");
		$(".modal-body").html("");
			$.each(data.books, function(i, item) {
				var url="'/user/addBook?identity="+item.id+"'";
				 $(".modal-body").append(
					'<div class="bookspan2 _book">'+
						'<a id="'+item.id+'"  href="javascript:addBook('+url+')">'+
							'<img  src="'+ item.image+'" alt="' + item.title    + '" />'+
						'</a>'+
					'</div>');		            
				});
			$('#myModal').modal('toggle');
			});
}

//添加图书
function addBook(href) {
	$.getJSON(href, function(data) {
			if (data.status == 1) {
			//图书馆添加成功
			$('.libraryMsg').html(data.msg);
			$('.libraryMsg').show();
			location.href = "/user/index";
			} else {
			if (data.status == 1) {
			//图书馆添加失败
			iS.showTip(data.msg);
			}
			}
			});
}

function quitLibrary(userLibraryId,libraryId,userId){
	if(!confirm("确认要退出该图书馆?")){
		return false;
	} 
	$.post("/user/quitLibrary", {
			"userLibraryId" : userLibraryId,
			"libraryId" : libraryId
			}, function(data) {
			if(data.status==1){
			location.href = "/user/myself";
			}else{
				iS.showTip(data.msg);;
			}
			}, "json");
}
</script>
</html>
