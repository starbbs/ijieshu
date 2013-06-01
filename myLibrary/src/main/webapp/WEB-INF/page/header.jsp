<%@ page contentType="text/html;charset=utf-8"%>
<link href="/css/header.css" rel="stylesheet" />
<div class="navbar nav-collapse navbar-fixed-top">
	<div class="navbar-inner" style="height:30px;">
		<div class="container">
			<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="brand" href="/user/index" style="color:#FF0000;">爱♥借书</a>
			<div class="nav-collapse collapse">
				<ul class="nav">                      
					<%if(userInfo != null){%>
						<li class="<%=myLibrary%>">
							<a  href="/user/mylibrary">藏书阁</a>
						</li>
						<li class="<%=myMessage%>">
							<a href="/user/myLend">代书板</a>
						</li>

						<li class="">
							<div class="input-append">
								<input class="span3 needFocus"  id="globalQuery" type="text" value="输入我想借的书名" data-placement="bottom" data-content="请输入您想借的书名">
								<button id="globalSearchBtn" type="submit" class="btn btn-primary">搜书</button>
							</div>
						</li>
						<li class=""><a id="nickLabel">欢迎馆君,<%=userInfo.getNick() %></a> </li>       
						<li class="dropdown">
	                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">	                        
	                        <img src="/img/edit.png"></img>
	                        </a>
	                        <ul class="dropdown-menu">
	                           <li><a href="javascript:void(0);" onclick="javascript:$('#passwordModal').modal('toggle');">修改密码</a></li>
	                            <li><a href="/user/loginOut">退出</a></li>
	                        </ul>
                      	</li>
					<%}else{ %>
						<li class="<%=myLibrary%>">
							<a href="/user/mylibrary">藏书阁</a>
						</li>
						<li class="haslogin hide <%=myMessage%>">
							<a href="/user/myLend">代书板</a>
						</li>
						     
						<li>
							<div class="input-append">
								<input class="span2 needFocus"  id="globalQuery" type="text" value="输入我想借的书名" data-placement="bottom" data-content="请输入您想借的书名">
								<button id="globalSearchBtn" type="submit" class="btn btn-primary">搜书</button>
							</div>
						</li>                                    
						<li id="nameInput"  class="haslogin hide" style="display:none;" >
							<span><input id="nick" title="点击修改昵称" type="text" value="" style="color:#3A5FCD;margin:4px 0 2px 0px;" /></span>
						</li>         
						<li class="dropdown haslogin hide">
	                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" style="color:#3A5FCD;">
	                        <span id="nickLabel"></span>
	                        <img src="/img/edit.png"></img>
							</a>
	                        <ul class="dropdown-menu">
	                           <li><a href="javascript:void(0);" onclick="javascript:$('#passwordModal').modal('toggle');">修改密码</a></li>
	                            <li><a href="/user/loginOut">退出</a></li>
	                        </ul>
                      	</li>
						<li class="navli last waithide">
							<form  id="login" name="login" action="/user/userLogin"  method="post">        	             
								<div class="input-append">
									<input id="loginPassport" class="span3 needFocus" type="email"  name="passport"  value="请输入邮箱账号" data-placement="bottom" data-content="请输入邮箱账号">			
									<input id="loginPassword" class="span2 needFocus" type="text" name="password" value="请输入密码"  data-placement="bottom" data-content="密码长度不能小于6位">
									<button id="loginBtn" type="submit" class="btn  btn-success">登录 | 注册</button>	
								</div>
								<span>
									<a href="javascript:void(0);" onclick="javascript:$('#passportModal').modal('toggle');">找回密码</a>																
								</span>
							</form>
						</li>             
					<%} %>
				</ul>
			</div>
		</div>
	</div>
</div>
	<div id="passportModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h4>找回密码</h4>
		</div>
		<div class="modal-body">
			<div class="input-append">
				<input class="span3 needFocus"  id="lostedPassport" type="text" value="输入您的账号邮箱"  data-placement="right" data-content="请输入邮箱账号">
				<button id="findPasswordBtn" type="submit" class="btn btn-primary">发送密码</button>
			</div>		
		</div>
	</div>
	<div id="passwordModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h4>修改密码</h4>
		</div>
		<div class="modal-body">
			<br/><br/><br/>
			<div class="input-append">
				<input class="span2 needFocus"  id="oldPassword" type="text" name="password" value="旧密码" data-placement="top" data-content="请输入邮箱账号">
				<input class="span2 needFocus"  id="newPassword" type="text" value="新密码" data-placement="top" data-content="请输入邮箱账号">
				<button id="editPasswordBtn" type="submit" class="btn btn-primary">修改</button>
			</div>
			<br/><br/><br/><br/>	
		</div>
	</div>
	<div id="loginModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3>登录    | 注册</h3>
		</div>
		<div class="modal-body">
			<form id="modalLogin"  name="login" action="/user/userLogin" method="post">
					<input class="js_passport span3 needFocus" type="email"
						name="passport" value="请输入邮箱账号"  data-placement="right" data-content="请输入邮箱账号">
					<br/> 
					<input class="js_password span3 needFocus" type="text" name="password" value="请输入密码"   data-placement="right" data-content="密码长度不能小于6位">
					<br/> 
					<button type="submit" class="loginBtn btn  btn-success">登录    | 注册</button>
					<span>PS:登录和注册功能合并为一个按钮啦,减少输入框!!</span>
			</form>
		</div>
	</div>			
<script type="text/javascript" src="/js/j.js"></script> 
<script type="text/javascript" src="/js/header.js"></script>
