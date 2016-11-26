<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="UTF-8">
function logout(b) {
	$.get('logout.do', function() {
		location.replace(loginUrl);
	});
}
function reloadTab() {
	var currentTab = $('#centerTabs').tabs('getSelected');
	var url = $(currentTab.panel('options')).attr('href');
	$('#centerTabs').tabs('update', {
		tab : currentTab,
		options : {
			href : url
		}
	});
	currentTab.panel('refresh');
}
var northTabs;
$(function() {
	return;
	northTabs = $('#northTabs').tabs({
        border : false,
        fit : true
    });
	/* northTabs.tabs('add', {
        title : '首页',
        iconCls : ''
    }); */
	northTabs.tabs({
        plain: true,
        narrow: true
        //pill: $('#pill').is(':checked'),
        //justified: true
    })
    $.ajax({
        url : 'manage/sys/user/userMenu.do?random=' + Math.random(),
        cache : false,
        dataType : "json",
        success : function(d) {
            try {
                datas = d;
                $.each(datas, function(i, item) {
                    // 一级菜单
                    var openFlag = (i == 0) ? true : false;
                    /* $('#menuDiv').accordion('add', {
                        title : item.text,
                        content : '<ul id="tree_' + item.id +'" style="margin-top: 5px;"></ul>',
                        selected : openFlag
                    }); */
                    //alert(item.text)
                    northTabs.tabs('add', {
                        title : item.text,
                        iconCls : '',
                        selected : openFlag
                    });
                })
            } catch(e) {
                alert(e)
                //window.location.href = overUrl;
            }
        }
    });
})
</script>

    <!-- <div class="easyui-layout" style="width:100%;height:54px;">
        <div id="p" data-options="region:'west'" title="" style="width:199px; border-right-style: none;">
            <p>width: 30%</p>
        </div>
        <div data-options="region:'center'" title="" style="border-left-style: none;">
        <div id="northTabs" style="margin-top: 25px; margin-left: 2px;"></div>
        </div>
        <div data-options="region:'east'" title="" style="width: 175px; border-left-style: none;">
            
        </div>
    </div>  -->

<div style="position: absolute; right: 0px; bottom: 0px; ">
    <a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_kzmbMenu" iconCls="icon-help">控制面板</a>
    <a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_zxMenu" iconCls="icon-back">注销</a>
</div>
<div id="layout_north_kzmbMenu" style="width: 100px; ">
    <%-- 
    <div onclick="userInfo();">个人信息</div>
    --%>
    <div onclick="reloadTab();">刷新</div>
    <!----> <div class="menu-sep"></div>
    <div>
        <span>更换主题</span>
        <div style="width: 100px;">
            <div onclick="sy.changeTheme('default');">default</div>
            <div onclick="sy.changeTheme('black');">black</div>
            <div onclick="sy.changeTheme('bootstrap');">bootstrap</div>
            <div onclick="sy.changeTheme('gray');">gray</div>
        </div>
    </div> 
    
</div>
<div id="layout_north_zxMenu" style="width: 100px; display: none;">
    <%--
    <div onclick="loginAndRegDialog.dialog('open');">锁定窗口</div>
    <div class="menu-sep"></div>
    <div onclick="logout();" style="display: none;">重新登录</div>
    --%>
    <div onclick="logout(true);">退出系统</div>
</div>
            