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
                    <li class="navli">
                    <div class="input-append">
                        <input class="span3"  id="globalQuery" type="text" placeholder="搜索您想要的书">
                        <button id="globalSearchBtn" onclick="javascript:iS.searchGlobalBook($('#globalQuery').val());" type="submit" class="btn btn-primary">搜书</button>
                    </div>
                    </li>
                    <li id="nameLabel" class="" >
                    <a id="nickLabel" title="点击修改昵称" style="color:#3A5FCD;" onclick="$(nameInput).show();$(nameLabel).hide();$(nick).focus();">欢迎馆君,<%=userInfo.getNick()%></a>
                    </li>              
                    <li id="nameInput" style="display:none;" >
                    <span><input id="nick" title="点击修改昵称" type="text" onblur="iS.inputBlur();" value="<%=userInfo.getNick()%>" style="color:#3A5FCD;margin:4px 0 2px 0px;" /></span>
                    </li>         
                    <li class="">
                    <a href="/user/loginOut">退出</a>
                    </li>
                    <%}else{ %>
                    <li class="<%=myLibrary%>">
                    <a href="/user/mylibrary">藏书阁</a>
                    </li>     
                    <li class="navli">
                    <div class="input-append">
                        <input class="span2"  id="globalQuery" type="text" placeholder="搜索您想要的书">
                        <button id="globalSearchBtn" onclick="javascript:iS.searchGlobalBook($('#globalQuery').val());" type="submit" class="btn btn-primary">搜书</button>
                    </div>
                    </li>                         
                    <li class="navli last">
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
<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/j.js"></script> 
<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/jquery.form.js"></script> 
<script type="text/javascript" src="http://zjdesj.github.io/javascript/myjs/header.js"></script>
