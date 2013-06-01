<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="com.ibook.library.entity.UserInfo"%>
<%@ page import="com.ibook.library.vo.UserLibraryVo"%>
<%@ page import="com.ibook.library.entity.Book"%>
<%@ page import="com.ibook.library.cst.Constants"%>
<%@ page import="java.util.List"%>
<%
    String myMessage="";
	String myLibrary="";
	String myUserInfo="active";
	int userId=0;
	UserInfo userInfo=(UserInfo)request.getAttribute("userInfo");
	if(null==userInfo){
		ServletContext sc = getServletContext();
		RequestDispatcher rd = null;
		rd = sc.getRequestDispatcher("/user/index"); 
		rd.forward(request, response);
		return;
	}else{
		userId=userInfo.getId();
	}

	List<UserLibraryVo> list=(List<UserLibraryVo>)request.getAttribute("libraryList");
	List<Book> bookList=(List<Book>)request.getAttribute("bookList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>爱♥借书</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta content="爱借书，借书，虚拟，图书馆，地理位置，借阅，图书" name="keywords">
<meta content="爱借书(IJieShu.com)基于地理位置的虚拟图书馆,我们的愿景是线上共享附近的书籍,线下互相借阅,做全球最大的图书馆" name="description">
<meta name="author" content="袋鼠">

<link href="/css/bootstrap.css" rel="stylesheet" />
<link href="/css/bootstrap-ie6.css" rel="stylesheet" />
<link href="/css/bootstrap-responsive.css"
	rel="stylesheet" />
<link href="/css/common.css" rel="stylesheet" />
<link href="/css/main.css" rel="stylesheet" />
<script type="text/javascript">
	var iS = typeof iS == 'undefined' ? {} : iS;
	iS.globalUserId =<%=userId%>;
	iS.action=function(){
		window.location.reload();
	};
</script>

<link
	href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css"
	rel="stylesheet" />

</head>

<body>
<div class="out_container">
	<%@include file="header.jsp"%>
	<div class="container form-signin" style="padding-top: 40px;">
		<fieldset>
			<legend>我的书架 </legend>
			<div class="input-append">
				<input class="span4 " id="query" type="text"
					placeholder="书名/作者/ISBN">
				<button id="changeBtn" type="submit" class="btn btn-primary">图书上架</button>
			</div>
			<div class="bookcontainer">
				<%
				    for (Book book : bookList) {
				%>
				<div class="bookspan2 _book">
					<%if(book.getStatus()==Constants.BOOK_STATUS_FREE){%>					
					<div class="_pinglun hide"
						style="position: absolute; top: 0; left: 0; width: 118px;">
						<div>
							<div class="r">
								<a title="图书下架"
									href="javascript:void(0);" onclick="javascript:iS.delBook('<%=book.getId()%>');">
									<img alt="图书下架" src="/img/close.png"/></a>
							</div>
						</div>
					</div>	
					<%} %>			
					<div class="_pinglun hide"
						style="opacity: 0.8; position: absolute; bottom: 0; left: 0; width: 118px; background-color: rgb(233, 238, 242);">

						<div>
							<div class="l">
								<a class="label label-info l" title="书名作为我的昵称"
									href="javascript:void(0);" onclick="javascript:iS.updateNick('<%=book.getTitle()%>');">书名作为我的昵称</a>
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
		</fieldset>
	</div>
	<div class="container form-signin">
		<fieldset>
			<legend>我的图书馆 </legend>
			<ul class="sns_tj_list o">
				<%
				    for(UserLibraryVo userLibrary : list){
				%>
                    <li>
                    	<div class="sns_tj_p">
                            <div class="sns_tj_info">
                            	<p><a  class="ba"><%=userLibrary.getLibraryName()%></a></p>
                                <p>馆藏：<a  class="ba"><%=userLibrary.getBookCount()%></a>本</p>
                                <p>馆君：<a  class="ba"><%=userLibrary.getUserCount()%></a>位</p>
                            </div>
                            <h4 class="top_gz sns_tj_gz" onclick="javascript:quitLibrary(<%=userLibrary.getId()%>,<%=userLibrary.getLibraryId()%>)"><strong>×</strong></h4>
                        </div>
                    </li>
				<%
				    }
				%>
			</ul>
			<div id="mapContainer" class="google-maps" style="width: 100%; height: 400px;"></div>			
		</fieldset>
	</div>
	<%@include file="footer.html"%>
	<div id="myModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>图书上架</h3>
		</div>
		<div class="modal-body container-fluid"></div>
	</div>
</div>
</body>
<script type="text/javascript"
	src="/js/bootstrap.js"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=1.5&ak=9097a620e2f941e69ad38ce70415caaa"></script>
<script type="text/javascript"
	src="/js/MarkerTool_min.js"></script>
<script type="text/javascript"
	src="/js/map.js"></script>
<script type="text/javascript"
	src="/js/common.js"></script>
	<script type="text/javascript" src="/js/index.js"></script>
<script type="text/javascript">
		/** 搜索图书逻辑 **/
		$('#changeBtn').click(function(){
				$('.searchMsg').hide();
				var query=$('#query').val();
				if(null==query||""==query){
				$('.searchMsg').html("请输入【书名/作者/ISBN】");
				$('.searchMsg').show();
				}else{
				searchBook(query);		
				}
				return false;
				});

function searchBook(query){
	var url ="/book/search?query="+encodeURI(query);
	$.getJSON(url, function (data) {
		$(".modal-body").html("");
			$.each(data.books, function(i, item) {
				var url="'/user/addBook?identity="+item.id+"'";
				 $(".modal-body").append(
					'<div class="bookspan2" style="width: 120px;">'+
						'<a id="'+item.id+'" class="thumbnail"  href="javascript:addBook('+url+')">'+
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
			location.href = "/user/myself";
			} else {
			if (data.status == 1) {
			//图书馆添加失败
			$('.libraryMsg').html(data.msg);
			$('.libraryMsg').show();
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
			alert(data.msg);;
			}
			}, "json");
}
</script>
</html>
