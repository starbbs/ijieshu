<%@ page contentType="text/html;charset=utf-8"%>
<link href="http://zjdesj.github.io/javascript/mycss/header.css" rel="stylesheet" />
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
					<%if(userInfo != null){%>
						<li>
							<a href="/user/myself">馆君</a>
						</li>
						<li>
							<a href="/user/mylibrary">藏书阁</a>
						</li>
						<li>
							<a href="/user/myLend">代书板</a>
						</li>

						<li class="navli">
							<div class="input-append">
								<input class="span3 needFocus"  id="globalQuery" type="text" value="搜索您想要的书">
								<button id="globalSearchBtn" type="submit" class="btn btn-primary">搜书</button>
							</div>
						</li>
						<li id="nameLabel" class="" >
							<a id="nickLabel" title="点击修改昵称" style="color:#3A5FCD;">欢迎馆君,<%=userInfo.getNick()%></a>
						</li>              
						<li id="nameInput" style="display:none;" >
							<span><input id="nick" title="点击修改昵称" type="text"  value="<%=userInfo.getNick()%>" style="color:#3A5FCD;margin:4px 0 2px 0px;" /></span>
						</li>         
						<li class="">
							<a href="/user/loginOut">退出</a>
						</li>
					<%}else{ %>
						<li class="haslogin hide">
							<a href="/user/myself">馆君</a>
						</li>
						<li class="">
							<a href="/user/mylibrary">藏书阁</a>
						</li>
						<li class="haslogin hide">
							<a href="/user/myLend">代书板</a>
						</li>
						     
						<li class="navli">
							<div class="input-append">
								<input class="span2 needFocus"  id="globalQuery" type="text" value="搜索您想要的书">
								<button id="globalSearchBtn" type="submit" class="btn btn-primary">搜书</button>
							</div>
						</li>                         

						<li id="nameLabel" class="haslogin hide" >
						<a id="nickLabel" title="点击修改昵称" style="color:#3A5FCD;">欢迎馆君：<em></em></a>
						</li>              
						<li id="nameInput"  class="haslogin hide" style="display:none;" >
							<span><input id="nick" title="点击修改昵称" type="text" value="" style="color:#3A5FCD;margin:4px 0 2px 0px;" /></span>
						</li>         
						<li class="haslogin hide">
							<a href="/user/loginOut">退出</a>
						</li>

						<li class="navli last waithide">
							<form  id="login" name="login" action="/user/userLogin"  method="post">        	             
								<div class="input-append">
									<input id="loginPassport" class="span3 needFocus" type="email"  name="passport"  value="请输入用户名">			
									<input id="loginPassword" class="span2 needFocus" type="text" name="password" value="请输入密码">
									<button id="loginBtn" type="submit" class="btn  btn-success">登录 | 注册</button>
								</div>
							</form>
						</li>             
					<%} %>
				</ul>
			</div>
		</div>
	</div>
</div>
<div id="search_to_pop_up"></div>
<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/j.js"></script> 
<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/header.js"></script>
