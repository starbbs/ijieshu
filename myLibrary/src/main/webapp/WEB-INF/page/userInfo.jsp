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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<title>爱•借书</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<script type="text/javascript" src="../../js/bootstrap.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript" src="../../js/jquery-1.9.1.js"></script> 
<script type="text/javascript" src="../../js/jquery.bpopup.min.js"></script>
<script type="text/javascript" src="../../js/jquery-ui.js"></script>
<script type="text/javascript" src="../../js/bootstrap-collapse.js"></script>
<script type="text/javascript" src="../../js/bootstrap-alert.js"></script>
<link href="../../css/bootstrap.css" rel="stylesheet" />
<link href="../../css/bootstrap-responsive.css" rel="stylesheet" />
<link href="../../css/common.css" rel="stylesheet" />
<link href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" rel="stylesheet"/>
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


</style>

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

<script type="text/javascript">
var globalUserId=<%=userId%>;
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
		if(0==globalUserId){
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