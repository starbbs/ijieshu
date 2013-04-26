<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="com.ibook.library.entity.UserInfo" %>
<%@ page import="com.ibook.library.vo.UserLibraryVo" %>
<%@ page import="com.ibook.library.entity.Book" %>
<%@ page import="java.util.List" %>
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
		<title>爱•借书</title>
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
		
		<link href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" rel="stylesheet"/>

	</head>

	<body>

		<%@include file="header.jsp" %>
		<div  class="container form-signin"  style="padding-top:40px;">
			<fieldset>
				<legend>我的书架		
					<div class="input-append">
						<input class="span4 " id="query" type="text" placeholder="书名/作者/ISBN">
						<button id="changeBtn" type="submit" class="btn btn-primary">图书上架</button>
					</div>
				</legend>	
				<div class="container-fluid">
					<%for(Book book : bookList){
					%>
					<div class="span2" style="width: 120px;">
						<a class="thumbnail">
							<img src="<%=book.getMediumImg()%>" alt="<%=book.getTitle()%>" />
						</a>
					</div>
					<%}%>
				</div>
			</fieldset>
		</div>
		<div  class="container form-signin">
			<fieldset>
				<legend>我的图书馆
					<div class="input-append" >
						<input class="span4 " id="library" type="text" placeholder="输入如【清华大学图书馆】加入图书馆啦！"/>
						<button id="libraryBtn" type="submit" class="btn btn-primary">加入图书馆</button>
					</div>
				</legend>
				<div class="container-fluid">
					<%for(UserLibraryVo userLibrary : list){
					%>
					<div class="span2">
						<a class=" close thumbnail" title="退出图书馆" onclick="javascript:quitLibrary(<%=userLibrary.getId()%>,<%=userLibrary.getLibraryId()%>)">									
							<h3><%=userLibrary.getLibraryName()%></h3>
							<p>馆藏图书： <%=userLibrary.getBookCount()%> 本</p>
							<p>馆君： <%=userLibrary.getUserCount()%> 位</p>
						</a>

					</div>
					<%}%>
				</div>
			</fieldset>
		</div>
		<div id="element_to_pop_up"></div>
		<%@include file="footer.html" %>

	</body>
	<script type="text/javascript" src="../../js/jquery-ui.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/bootstrap.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/popup.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/index.js"></script>
	<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/common.js"></script>
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
			$("#element_to_pop_up").html("");//清空
			$.each(data.books, function(i, item) {
				var url="'/user/addBook?identity="+item.id+"'";
				$("#element_to_pop_up").append(
					'<a id="'+item.id+'" href="javascript:addBook('+url+')" style="float: left;padding:20px;">'+
					'<img style="width:103px;height:142px;" src="'+ item.image+'" alt="' + item.title    + '" />'+
					'</a>');		            
				});
			$('#element_to_pop_up').bPopup();
			});
}


/** 添加图书馆逻辑 **/
$('#libraryBtn').click(function(){
		$('.libraryMsg').hide();
		var library=$('#library').val();
		if(null==library||""==library){
		$('.libraryMsg').html("请输入【图书馆的大名】");
		$('.libraryMsg').show();
		}else{
		addLibrary(library);		
		}
		return false;
		});

function addLibrary(library){
	if(0==iS.globalUserId){
		alert("登录后就可以加入属于自己的图书馆哟!");
		return;
	}
	var url ="/user/addLibrary?libraryName="+library;
	$.getJSON(url, function (data) {
			if(data.status==1){
			//图书馆添加成功
			$('.libraryMsg').html(data.msg);
			$('.libraryMsg').show();	
			location.href = "/user/myself";
			}else{
			if(data.status==1){
			//图书馆添加失败
			$('.libraryMsg').html(data.msg);
			$('.libraryMsg').show();
			}
			}
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
//搜索图书馆
$("#library").autocomplete(
		{
source : function(request, response) {
$.ajax({
url : "/library/queryLibrary",
dataType : "jsonp",
data : {
featureClass : "P",
style : "full",
maxRows : 12,
query : request.term
},
success : function(data) {
response($.map(data.array, function(item) {
		return {
label : item.name,
value : item.name
}
}));
}
});
},
minLength : 2,
		  select : function(event, ui) {
			  $(this).attr("value",ui.item.value);//填充内容
		  },
open : function() {
		   $(this).removeClass("ui-corner-all").addClass(
				   "ui-corner-top");
	   },
close : function() {
			$(this).removeClass("ui-corner-top").addClass(
					"ui-corner-all");
		}
});
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
