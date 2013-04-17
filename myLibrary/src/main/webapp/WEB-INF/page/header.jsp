<%@ page contentType="text/html;charset=utf-8"%>
<style>
#search_to_pop_up { 
    background-color:#fff;
    border-radius:15px;
    color:#000;
    display:none; 
    padding:20px;
    width:750px;
    min-height: 180px;
}

#borrow_to_pop_up { 
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
</style>
    <div class="navbar nav-collapse navbar-fixed-top">
      <div class="navbar-inner" style="height:30px;">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="/user/index" style="color:#FF0000;">爱•借书</a>
          <div class="nav-collapse collapse">
            <ul class="nav">                      
              <%if(null!=userInfo){%>
              <li class="<%=myUserInfo%>">
                <a href="/user/myself">馆君</a>
              </li>
              <li class="<%=myLibrary%>">
                <a href="/user/mylibrary">藏书阁</a>
              </li>
              <li class="<%=myMessage%>">
                <a href="/user/myLend">代书板</a>
              </li>
               <li class="">
                	<div class="input-append">
						<input class="span3"  id="globalQuery" type="text" placeholder="搜索您想要的书">
						<button id="globalSearchBtn" onclick="javascript:searchGlobalBook($('#globalQuery').val());" type="submit" class="btn btn-primary">搜书</button>
					</div>
              </li>
			  <li id="nameLabel" class="" >
                	<a id="nickLabel" title="点击修改昵称" style="color:#3A5FCD;" onclick="$(nameInput).show();$(nameLabel).hide();$(nick).focus();">欢迎馆君,<%=userInfo.getNick()%></a>
              </li>              
              <li id="nameInput" style="display:none;" >
                	<span><input id="nick" title="点击修改昵称" type="text" onblur="inputBlur();" value="<%=userInfo.getNick()%>" style="color:#3A5FCD;margin:4px 0 2px 0px;" /></span>
              </li>         
              <li class="">
                <a href="/user/loginOut">退出</a>
              </li>
              <%}else{ %>
              <li class="<%=myLibrary%>">
                <a href="/user/mylibrary">藏书阁</a>
              </li>     
               <li class="">
                	<div class="input-append">
						<input class="span2"  id="globalQuery" type="text" placeholder="搜索您想要的书">
						<button id="globalSearchBtn" onclick="javascript:searchGlobalBook($('#globalQuery').val());" type="submit" class="btn btn-primary">搜书</button>
					</div>
              </li>                         
              <li class="">
              	<form  id="login" name="login" action="/user/userLogin"  method="post">        	             
                	<div class="input-append">
					<input id="loginPassport" class="span3" type="email" required name="passport" placeholder="账号">			
					<input id="loginPassword" class="span2" type="password" required name="password" placeholder="密码">
					<button id="loginBtn" type="submit" class="btn  btn-success">登录 | 注册</button>
					</div>
				</form>
              </li>             
              <!-- <li class="">
              <form  id="register" name="register" action="/user/userRegister"  method="post">
                <div class="input-append">               	
					<input id="registerPassport" class="span3" type="email" required name="passport" placeholder="账号" title="账号">			 
					<input id="registerPassword" class="span2" type="password" required name="password" placeholder="密码" title="密码">
					<button id="regBtn" type="submit" class="btn  btn-primary">注册</button>				
				</div>
				</form>
              </li> -->          
              <%} %>
            </ul>
          </div>
        </div>
      </div>
    </div>
 	 <div id="search_to_pop_up"></div>
    
<script type="text/javascript">

/** 搜索图书逻辑 **/
function searchGlobalBook(query){
	location.href = "/user/mylibrary.jieshu?query=" + encodeURI(query);
}

function borrowGlobalRequest(id,title){
	if(0==globalUserId){
		alert("请先登录就可以借阅《"+title+"》哟!");
		return;
	}
	$("#borrow_to_pop_up").html("");//清空
	$("#borrow_to_pop_up").append(
    		'<a style="float: left;padding:20px;">'+
            '<textarea id="borrowMsg" style="width:700px;"  myid="'+id+'" rows="6" cols="600" placeholder="欢迎您借阅俺的  《'+title+'》  ,给我留言唷^-^"></textarea>'+
            '<button id="borrowBtn" onclick="javascript:borrowRequestSubmit('+id+');" type="button" class="btn btn-primary">求借</button>'+
            '</a>');
	$('#borrow_to_pop_up').bPopup();
}

function inputBlur(){
	//alert($("#nick").val());
	$("#nameInput").hide();
	$("#nameLabel").show();
	$.post("/user/updateNick", {
		"nick" : $("#nick").val()
	}, function(data) {
		if(data.status==1){
			$("#nickLabel").html("欢迎馆君,"+$("#nick").val());
		}else{
			alert(data.msg);
		}
	}, "json");		
}

$(document).ready(function() {
	
	var loginOptions = {
		//target:        '.alert',   // target element(s) to be updated with server response 
		beforeSubmit : loginRequest, // pre-submit callback 
		success : loginResponse, // post-submit callback     
		dataType : 'json', // 'xml', 'script', or 'json' (expected server response type) 
		clearForm : true, // clear all form fields after successful submit 
		resetForm : true
	// reset the form after successful submit      
	};
	// bind to the form's submit event 
	$('#login').submit(function() {
		$(this).ajaxSubmit(loginOptions);
		return false;
	});

	var registerOptions = {
		//target:        '.alert',   // target element(s) to be updated with server response 
		beforeSubmit : registerRequest, // pre-submit callback 
		success : registerResponse, // post-submit callback     
		dataType : 'json', // 'xml', 'script', or 'json' (expected server response type) 
		clearForm : true, // clear all form fields after successful submit 
		resetForm : true
	// reset the form after successful submit      
	};
	// bind to the form's submit event 
	$('#register').submit(function() {
		$(this).ajaxSubmit(registerOptions);
		return false;
	});
});

// pre-submit callback 
function loginRequest(formData, jqForm, options) {
	var queryString = $.param(formData);
	// var formElement = jqForm[0]; 
	//$('.loginMsg').hide();
	//alert('About to submit: \n\n' + queryString);  
	return true;
}

// post-submit callback 
function loginResponse(responseText, statusText, xhr, $form) {
	if(responseText.msg.status!=-1){
		location.href = "/user/myself";
	}else{
		$('.loginMsg').html(responseText.msg);
		$('.loginMsg').show();
	}		
	//$('#loginBtn').popover(responseText.msg);
}

// pre-submit callback 
function registerRequest(formData, jqForm, options) {
	var queryString = $.param(formData);
	// var formElement = jqForm[0]; 
	//$('.registerMsg').hide();
	//alert('About to submit: \n\n' + queryString);  
	return true;
}

// post-submit callback 
function registerResponse(responseText, statusText, xhr, $form) {
	if(responseText.msg.status!=-1){
		location.href = "/user/myself";
	}else{
		//$('.registerMsg').html(responseText.msg);
		//$('.registerMsg').show();
	}
	//$('#regBtn').popover(responseText.msg);
}
</script>