<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="com.ibook.library.entity.UserInfo"%>
<%@ page import="com.ibook.library.vo.BookLogMessageVo"%>
<%@ page import="com.ibook.library.vo.UserMessageVo"%>
<%@ page import="com.ibook.library.cst.Constants"%>
<%@ page import="java.util.List"%>
<%
    String myMessage="active";
	String myLibrary="";
	String myUserInfo="";
	int userId=0;
	boolean isLendIn=false;
	boolean isLendOut=false;
	UserInfo userInfo=(UserInfo)request.getAttribute("userInfo");
	if(null==userInfo){
		ServletContext sc = getServletContext();
		RequestDispatcher rd = null;
		rd = sc.getRequestDispatcher("/user/index"); 
		rd.forward(request, response);
		return;
	}
	userId=userInfo.getId();
	List<BookLogMessageVo> bookLogMessageVoList=(List<BookLogMessageVo>)request.getAttribute("BookLogMessageVoList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>爱♥借书-代书板</title>
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

		<div class="container" style="padding-top: 40px;">
			<div class="tabbable" style="padding: 20px;">
				<ul class="nav nav-pills">
					<li class="active"><a href="#tab1" data-toggle="tab">图书借入</a></li>
					<li><a href="#tab2" data-toggle="tab">图书借出</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="tab1">
						<div class="_lendIn main">
							<div>
								<%
								    for(BookLogMessageVo bookLogMessageVo : bookLogMessageVoList){
																			if(!bookLogMessageVo.isMyBook()){
																			    isLendIn=true;
								%>
								<div class="sns_con">
									<div class="book shadow fix">
										<a href="#" title="" class="book_pic"> <img
											src="<%=bookLogMessageVo.getBookImg()%>" width="68"
											height="95" title="<%=bookLogMessageVo.getTitle()%>"
											alt="<%=bookLogMessageVo.getTitle()%>" />
										</a>
										<div class="book_info">
											<dl class="book_info_l">
												<dt class="book_info_dt">
													<a href="#" class="book_info_name"><%=bookLogMessageVo.getTitle()%></a>
												</dt>
												<dd class="book_info_dd">
													<span class="book_info_dh">书主人：</span>
													<p class="book_info_dp">
														<a href="#"><%=bookLogMessageVo.getOwnerNick()%></a>
													</p>
												</dd>
												<dd class="book_info_dd">
													<span class="book_info_dh">借阅者：</span>
													<p class="book_info2_nr">
														<span class="book_info2_a"><%=bookLogMessageVo.getBorrowNick()%></span>
														<span class="book_info2_a">靠谱(<span
															class="_reliable_<%=bookLogMessageVo.getBorrowUserId()%> _reliable_<%=bookLogMessageVo.getLogId()%>"><%=bookLogMessageVo.getBorrowReliableNum()%></span>)
														</span> | <span class="book_info2_a">不靠谱(<span
															class="_unreliable_<%=bookLogMessageVo.getBorrowUserId()%> _unreliable_<%=bookLogMessageVo.getLogId()%>"><%=bookLogMessageVo.getBorrowUnReliableNum()%></span>)
														</span>
													</p>
												</dd>
												<dd class="book_info_dd">
													<span class="book_info_dh">状态：</span>
													<p class="book_info_dp">
														<span
															class="book_status book_status_<%=bookLogMessageVo.getLogId()%>"><%=bookLogMessageVo.getStatus()%></span>
													</p>
												</dd>
											</dl>
										</div>
									</div>
									<p class="sns_info">
										<span class="r"> <a
											href="javascript:showBookComment('book_comment_<%=bookLogMessageVo.getMsgId()%>');">留言(<span
												class="comment_num_<%=bookLogMessageVo.getMsgId()%>"><%=bookLogMessageVo.getMessages().size()%></span>)
										</a>
										</span>
										<!-- <a href="#" class="sns_info_time">5分钟前</a> -->
									</p>
									<div id="book_comment_<%=bookLogMessageVo.getMsgId()%>"
										class="sns_pl shadow hide">
										<form action="#" method="post" class="sns_pl_form">
											<textarea id="<%=bookLogMessageVo.getMsgId()%>_text"
												class="sns_pl_area" myid="<%=bookLogMessageVo.getMsgId()%>"
												rows="1" placeholder="给对方回复,速速借到书^-^"></textarea>
											<p class="sns_pl_sub">
												<a
													onclick="javascript:applyRequest(<%=bookLogMessageVo.getMsgId()%>);"
													class="label label-info">回复</a>
											</p>
										</form>
										<ul class="sns_pl_list">
											<div class="fix_<%=bookLogMessageVo.getMsgId()%>"></div>
											<%
											    for(UserMessageVo userMessageVo:bookLogMessageVo.getMessages()){
											%>
											<li class="fix">
												<p class="pl_list_cz r">
													<a href="#">删除</a>
												</p>
												<div class="pl_list_con">
													<a href="#" class="pl_list_name"><%=userMessageVo.getFromUserNick()%>：</a>
													<p class="pl_list_com" id="<%=userMessageVo.getId()%>"><%=userMessageVo.getMsg()%></p>
												</div>
											</li>
											<%
											    }
											%>
										</ul>
									</div>
								</div>
								<%
								    }}
								%>
							</div>

							<%
							    if(!isLendIn){
							%>
							<div class="shujia">
								<h4>去借书，开始新的阅读之旅！</h4>
								<p>
									身体和灵魂必须一个在路上,先让心上路吧.....~~ <a href="/user/mylibrary"
										class="label label-success">借书去</a>
								</p>
							</div>
							<%
							    }
							%>


						</div>
					</div>
					<div class="tab-pane" id="tab2">
						<div class="_lendOut main">
							<div>
								<%
								    for(BookLogMessageVo bookLogMessageVo : bookLogMessageVoList){
																			if(bookLogMessageVo.isMyBook()){
																			    isLendOut=true;
								%>
								<div class="sns_con">
									<div class="book shadow fix">
										<a href="#" title="" class="book_pic"> <img
											src="<%=bookLogMessageVo.getBookImg()%>" width="68"
											height="96" title="<%=bookLogMessageVo.getTitle()%>"
											alt="<%=bookLogMessageVo.getTitle()%>" />
										</a>
										<div class="book_info">
											<dl class="book_info_l">
												<dt class="book_info_dt">
													<a href="#" class="book_info_name"><%=bookLogMessageVo.getTitle()%></a>
												</dt>
												<dd class="book_info_dd">
													<span class="book_info_dh">书主人：</span>
													<p class="book_info_dp">
														<span><%=bookLogMessageVo.getOwnerNick()%></span>
													</p>
												</dd>
												<dd class="book_info_dd">
													<span class="book_info_dh">借阅者：</span>
													<p class="book_info2_nr">
														<span class="book_info2_a"><%=bookLogMessageVo.getBorrowNick()%></span>
														<a class="book_info2_a"
															href="javascript:appraiseGood('<%=bookLogMessageVo.getLogId()%>','<%=bookLogMessageVo.getBorrowUserId()%>');">靠谱(<span
															class="_reliable_<%=bookLogMessageVo.getBorrowUserId()%> _reliable_<%=bookLogMessageVo.getLogId()%>"><%=bookLogMessageVo.getBorrowReliableNum()%></span>)
														</a> | <a class="book_info2_a"
															href="javascript:appraiseBad('<%=bookLogMessageVo.getLogId()%>','<%=bookLogMessageVo.getBorrowUserId()%>');">不靠谱(<span
															class="_unreliable_<%=bookLogMessageVo.getBorrowUserId()%> _unreliable_<%=bookLogMessageVo.getLogId()%>"><%=bookLogMessageVo.getBorrowUnReliableNum()%></span>)
														</a>
													</p>
												</dd>
												<dd class="book_info_dd">
													<span class="book_info_dh">状态：</span>
													<p class="book_info_dp">
														<span
															class="book_status book_status_<%=bookLogMessageVo.getLogId()%>"><%=bookLogMessageVo.getStatus()%></span>
													</p>
												</dd>
											</dl>
										</div>
									</div>
									<p class="sns_info">
										<span class="r"> <span
											class="log_apply_<%=bookLogMessageVo.getLogId()%> <%=bookLogMessageVo.getStatus()==Constants.BOOK_LOG_STATUS_APPLY?"":"hide"%>"><a
												href="javascript:approveBorrowBook('<%=bookLogMessageVo.getLogId()%>');">借阅</a>
												| <a
												href="javascript:rejectBorrowBook('<%=bookLogMessageVo.getLogId()%>');">拒绝</a>
												|</span> <span
											class="log_end_<%=bookLogMessageVo.getLogId()%> <%=bookLogMessageVo.getStatus()==Constants.BOOK_LOG_STATUS_BORROWED?"":"hide"%>"><a
												href="javascript:revertRequest('<%=bookLogMessageVo.getLogId()%>');">已归还</a>
												| <a
												href="javascript:presentBook('<%=bookLogMessageVo.getLogId()%>');">已赠送</a>
												| </span> <a
											href="javascript:showBookComment('book_comment_<%=bookLogMessageVo.getMsgId()%>');">留言(<span
												class="comment_num_<%=bookLogMessageVo.getMsgId()%>"><%=bookLogMessageVo.getMessages().size()%></span>)
										</a>
										</span>
										<!-- <a href="#" class="sns_info_time">5分钟前</a> -->
									</p>
									<div id="book_comment_<%=bookLogMessageVo.getMsgId()%>"
										class="sns_pl shadow hide">
										<form action="#" method="post" class="sns_pl_form">
											<textarea id="<%=bookLogMessageVo.getMsgId()%>_text"
												class="sns_pl_area" myid="<%=bookLogMessageVo.getMsgId()%>"
												rows="1" placeholder="给对方回复,速速借到书^-^"></textarea>
											<p class="sns_pl_sub">
												<a
													onclick="javascript:applyRequest(<%=bookLogMessageVo.getMsgId()%>);"
													class="label label-info">回复</a>
											</p>
										</form>
										<ul class="myout sns_pl_list">
											<div class="fix_<%=bookLogMessageVo.getMsgId()%>"></div>
											<%
											    for(UserMessageVo userMessageVo:bookLogMessageVo.getMessages()){
											%>
											<li class="fix">
												<p class="pl_list_cz r">
													<a href="#">删除</a>
												</p>
												<div class="pl_list_con">
													<a href="#" class="pl_list_name"><%=userMessageVo.getFromUserNick()%>：</a>
													<p class="pl_list_com"
														id="<%=bookLogMessageVo.getMsgId()%>"><%=userMessageVo.getMsg()%></p>
												</div>
											</li>
											<%
											    }
											%>
										</ul>
									</div>
								</div>
								<%
								    }}
								%>
								<%
								    if(!isLendOut){
								%>
								<div class="shujia">
									<h4>好期待借书者的到来！</h4>
									<p>
										给我的爱书寻找更多爱惜之人.....~~ <a href="/user/index#js_tabbable"
											class="label label-success">再上传点书</a>
									</p>
								</div>
								<%
								    }
								%>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div id="element_to_pop_up"></div>
		<%@include file="footer.html"%>
	</div>
</body>
<script type="text/javascript" src="/js/bootstrap.js"></script>
<script type="text/javascript" src="/js/popup.js"></script>
<script type="text/javascript" src="/js/index.js"></script>
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
					if(data.status==1){
						$(".fix_"+myid).after(
								'<li class="fix">'+
									'<p class="pl_list_cz r">'+
										'<a href="#">删除</a>'+
									'</p>'+
									'<div class="pl_list_con">'+
									'<a href="#" class="pl_list_name">'+data.nick+'：</a>'+
									'<p class="pl_list_com" id="'+data.msgId+'">'+myMsg+'</p>'+
									'</div>'+
								'</li>');	
						var num=$(".comment_num_"+myid).text();
						$(".comment_num_"+myid).text((parseInt(num)+1));
					}
				}, "json");
	}
}

	function presentBook(myid) {
		$.post("/user/presentBook", {
			"id" : myid
		}, function(data) {
			//alert(data.msg);
			if (data.status == 1) {
				$(".log_end_" + myid).hide();
				$(".book_status_" + myid).text("已赠送");
			}
			iS.showTip(data.msg);
			//location.href = "/user/myLend";
		}, "json");
	}

	function revertRequest(myid) {
		$.post("/user/revertBook", {
			"id" : myid
		}, function(data) {
			if (data.status == 1) {
				$(".log_end_" + myid).hide();
				$(".book_status_" + myid).text("已归还");
			}
			iS.showTip(data.msg);
		}, "json");
	}

	function approveBorrowBook(myid) {
		$.post("/user/approveBorrowBook", {
			"id" : myid
		}, function(data) {
			if (data.status == 1) {
				$(".log_apply_" + myid).hide();
				$(".log_end_" + myid).show();
				$(".book_status_" + myid).text("已借阅");
			}
			iS.showTip(data.msg);
		}, "json");
	}

	function rejectBorrowBook(myid) {
		$.post("/user/rejectBorrowBook", {
			"id" : myid
		}, function(data) {
			if (data.status == 1) {
				$(".log_apply_" + myid).hide();
				$(".book_status_" + myid).text("已拒绝");
			}
			iS.showTip(data.msg);
		}, "json");
	}

	function appraiseGood(myid,userId) {
		$.post("/user/appraiseGood", {
				"id" : myid
				}, function(data) {
					if(data.status==1){
						var num=$("._reliable_"+myid).text();
						$("._reliable_"+userId).text((parseInt(num)+1));						
					}else if(data.status==3){	
						var num=$("._reliable_"+myid).text();
						$("._reliable_"+userId).text((parseInt(num)+1));
						var _num=$("._unreliable_"+myid).text();
						$("._unreliable_"+userId).text((parseInt(_num)-1));
					}						
					iS.showTip(data.msg);
				}, "json");
	}
	
	function appraiseBad(myid,userId) {
		$.post("/user/appraiseBad", {
				"id" : myid
				}, function(data) {
					if(data.status==1){
						var num=$("._unreliable_"+myid).text();
						$("._unreliable_"+userId).text((parseInt(num)+1));
						iS.showTip(data.msg);
					}else if(data.status==3){	
						var num=$("._unreliable_"+myid).text();
						$("._unreliable_"+userId).text((parseInt(num)+1));
						var _num=$("._reliable_"+myid).text();
						$("._reliable_"+userId).text((parseInt(_num)-1));
					}	
					iS.showTip(data.msg);
				}, "json");
	}		
	
	function showBookComment(id){
		$("#"+id).toggle();
	}

	function getStatus(status){
		var statusStr="";
		status=parseInt(status);
		switch (status){
			case 0:
				statusStr="申请中";
				break;				
			case 1:
				statusStr="已借阅";
				break;
			case 2:
				statusStr="已拒绝";
				break;
			case 3:
				statusStr="已归还";
				break;
			case 4:
				statusStr="已赠送";
				break;						
		}
		return statusStr;
	}
	
	function showLend(type){
		if(1==type){
			//借出
			$("._lendIn").hide();
			$("._lendOut").show();
			$("._menuOut").addClass("on");
			$("._menuIn").removeClass("on");
		}else{
			//借入
			$("._lendIn").show();
			$("._lendOut").hide();
			$("._menuOut").removeClass("on");
			$("._menuIn").addClass("on");
			
		}
	}
	 var _bookStatus=$(".book_status");
     for(var i=0;i<_bookStatus.length;i++){
           $(_bookStatus[i]).text(getStatus($(_bookStatus[i]).text()));
     }	     
	

</script>
</html>

