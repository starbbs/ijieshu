$(function(){
    var map = new BMap.Map("mapContainer"); // 创建地图实例
    var myCity = new BMap.LocalCity();
    var mkrTool=myCity.get(mapInital);
    var gc = new BMap.Geocoder();
    function mapInital(result) {
        var cityName = result.name;
        map.setCenter(cityName);
        var point = new BMap.Point(result.lng, result.lat); // 创建点坐标
        map.centerAndZoom(point, result.level); // 初始化地图，设置中心点坐标和地图级别
        map.enableScrollWheelZoom();
        getLibrary();
        mkrTool = new BMapLib.MarkerTool(map, {
            autoClose : true
        });
        mkrTool.addEventListener("markend", function(evt) {
            var mkr = evt.marker;
            mkr.openInfoWindow(infoWin);
            // alert("您标注的位置" + evt.marker.getPosition().lng + ", "
            // +evt.marker.getPosition().lat);
            $("#txtLongitude").val(evt.marker.getPosition().lng);
            $("#txtLatitude").val(evt.marker.getPosition().lat);
            var pt = evt.marker.getPosition();
            gc.getLocation(pt, function(rs) {
                var addComp = rs.addressComponents;
                var address="";
                if(addComp.province==addComp.city){
                    address=address+addComp.province+" "+addComp.district+" "+addComp.street+" "+addComp.streetNumber;
                }else{
                    address=address+addComp.province+" "+addComp.city+" "+addComp.district+" "+addComp.street+" "+addComp.streetNumber;
                }

                $("#txtCitys").val(addComp.province+"=="+addComp.city+"=="+addComp.district+"=="+addComp.street+"=="+addComp.streetNumber);
                $("#txtAddr").val(address);
            });
        });
    }

    // 定义一个控件类,即function
    function ZoomControl() {
        // 默认停靠位置和偏移量
        this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
        this.defaultOffset = new BMap.Size(10, 10);
    }

    // 通过JavaScript的prototype属性继承于BMap.Control
    ZoomControl.prototype = new BMap.Control();

    // 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
    // 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
    ZoomControl.prototype.initialize = function(map) {
        // 创建一个DOM元素
        var div = document.createElement("div");
        // 添加文字说明
        //div.appendChild(document.createTextNode("创建我的图书馆"));
        $(div).html('<button  class="btn btn-primary">创建我的图书馆</button>');
        // 设置样式
        //	div.style.cursor = "pointer";
        //	div.style.border = "1px solid gray";
        //	div.style.backgroundColor = "blue";
        // 绑定事件,点击一次放大两级
        div.onclick = function(e) {
            if(0==globalUserId){
                alert("登录后就可以创建属于自己的图书馆哟!");
                return;
            }
            addLibraryPoint();
        }
        // 添加DOM元素到地图中
        map.getContainer().appendChild(div);
        // 将DOM元素返回
        return div;
    }
    // 创建控件
    var myZoomCtrl = new ZoomControl();
    // 添加到地图当中
    map.addControl(myZoomCtrl);

    // 拼接infowindow内容字串
    var html = [];
    html.push('<span style="font-size:12px">创建图书馆: </span><br/>');
    html.push('<table border="0" cellpadding="1" cellspacing="1" >');
    html.push('  <tr>');
    html.push('      <td align="left" class="common">名 称：</td>');
    html.push('      <td colspan="2"><input type="text" maxlength="50" size="18"  id="txtName"></td>');
    html.push('	     <td valign="top"><span class="star">*</span></td>');
    html.push('  </tr>');
    html.push('  <tr>');
    html.push('      <td  align="left" class="common">地 址：</td>');
    html.push('      <td  colspan="2"><input type="text" maxlength="50" size="18"  id="txtAddr"></td>');
    html.push('	     <td valign="top"><span class="star">*</span></td>');
    html.push('  </tr>');
    html.push('  <tr>');
    html.push('      <td align="left" class="common">描 述：</td>');
    html.push('      <td colspan="2"><textarea rows="2" cols="15"  id="areaDesc"></textarea></td>');
    html.push('	     <td valign="top"></td>');
    html.push('  </tr>');
    html.push('  <tr>');
    html.push('	     <td  align="center" colspan="3">');
    html.push('          <input type="button" name="btnOK" class="btn btn-primary" onclick="fnOK()" value="确定">&nbsp;&nbsp;');
    html.push('	     </td>');
    html.push('  </tr>');
    html.push('	     <input type="hidden"  id="txtLongitude"></td>');
    html.push('	     <input type="hidden"  id="txtLatitude"></td>');
    html.push('	     <input type="hidden"  id="txtCitys"></td>');
    html.push('</table>');



    var infoWin = new BMap.InfoWindow(html.join(""), {
        offset : new BMap.Size(0, -10)
    });
    var curMkr = null; // 记录当前添加的Mkr

    // 选择样式
    function addLibraryPoint() {
        mkrTool.open(); // 打开工具
        var icon=new BMap.Icon(" http://api.map.baidu.com/images/marker_red_sprite.png",new BMap.Size(35, 45));
        // --
        // BMapLib.MarkerTool.SYS_ICONS[23]
        mkrTool.setIcon(icon);
    }

    //提交数据
    function fnOK(){
        var name =$("#txtName").val();
        var addr =$("#txtAddr").val();
        var desc =$("#areaDesc").val();
        var longitude =$("#txtLongitude").val();
        var latitude =$("#txtLatitude").val();
        var citys =$("#txtCitys").val();

        if(!name || !addr){
            alert("名称和地址不能为空,悟空你又调皮了-_-");    
            return;
        }
        //在此用户可将数据提交到后台数据库中
        $.post("/user/addLibrary", {
            "libraryName" : name,
            "libraryAddr" : addr,
            "libraryDesc" : desc,
            "longitude" : longitude,
            "latitude" : latitude,
            "citys":citys
        }, function(data) {
            if(data.status==1){
                location.href = "/user/myself";
            }else{
                alert(data.msg);;
            }
        }, "json");
    }

    function getLibrary(){
        var url="/library/queryLibrary";
        $.getJSON(url, function (data) {
            $.each(data.array, function(i, library) {
                var name=library.name;
                var longitude=library.longitude;
                var latitude=library.latitude;
                var bookCount=library.bookCount;
                var userCount=library.userCount;
                var point = new BMap.Point(longitude,latitude);
                var marker = new BMap.Marker(point);
                var icon=new BMap.Icon(" http://api.map.baidu.com/images/marker_red_sprite.png",new BMap.Size(35, 45));
                marker.setIcon(icon);
                map.addOverlay(marker);					
                var sContent =
                "<div>"+
                "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>"+library.name+"</h4>" +
                "<p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'>藏书："+library.bookCount+"本</p>" +
                "<p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'>馆君："+library.userCount+"位</p>" +
                "<p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'>简介："+library.desc+"</p>" + 
                "<p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'>地址："+library.addr+"</p>" +
                "<input  type='button'id name='btnOK' class='btn btn-primary' onclick='addMyLibrary(\""+library.name+"\")' value='加入图书馆'/>"+
                "</div>";
            //					marker.openInfoWindow(new BMap.InfoWindow(sContent),point);
            var infoWindow = new BMap.InfoWindow(sContent);  // 创建信息窗口对象
            var label = new BMap.Label(library.name, { point: point, offset: new BMap.Size(0, -20) });       //定义一个文字标签
            label.setStyle({ backgroundColor:"#104E8B",color : "white", fontSize : "15px",borderStyle:"none",fontWeight:"bold"});
            marker.setLabel(label);

            marker.addEventListener("click", function(){          
                this.openInfoWindow(infoWindow);
            });
            });
        });
    }	

    function addMyLibrary(library){
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
});

