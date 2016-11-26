<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript" charset="UTF-8">
var tree;
var datas;
$(function() {
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
                    $('#menuDiv').accordion('add', {
                        title : item.text,
                        content : '<ul id="tree_' + item.id +'" style="margin-top: 5px;"></ul>',
                        selected : openFlag
                    });
                    
                    $('#tree_' + item.id).tree({
                        data : item.children,
                        animate : false,
                        lines : !Tool.isLessThanIe8(),
                        onClick : function(node) {
                            if (node.attributes && node.attributes.src && node.attributes.src != '') {
                                var href = node.attributes.src;
                                addTabFun({
                                    src : href,
                                    title : node.text
                                });
                            } else {
                            }
                        },
                        onLoadSuccess : function(node, data) {
                        },
                        onLoadError : function() {
                        }
                    });
                })
            } catch(e) {
                alert(e)
                //window.location.href = overUrl;
            }
        }
    });
});
function collapseAll() {
	var node = tree.tree('getSelected');
	if (node) {
		tree.tree('collapseAll', node.target);
	} else {
		tree.tree('collapseAll');
	}
}
function expandAll() {
	var node = tree.tree('getSelected');
	if (node) {
		tree.tree('expandAll', node.target);
	} else {
		tree.tree('expandAll');
	}
}

// 点击菜单
var tempObj;
// #E0ECFF
function toModule(href, text, obj) {
	if (tempObj != null) {
		tempObj.style.backgroundColor = '#fafafa';
		tempObj.style.color = '#000000';
	}
	obj.style.backgroundColor = '#0092DC';
	obj.style.color = '#FFFFFF';
	
	addTabFun({
		src : href,
		title : text
	});
	tempObj = obj;
}

$(function() {
	$('.easyui-accordion ul[name="menuUl"] li').hover(
		function() {
			var color = $(this).css('backgroundColor');
			color = color.toUpperCase();
			if (color.substr(0, 1) == '#') {
				if (color != '#0092DC') {
					$(this).css('backgroundColor', '#E0ECFF');
					$(this).css('color', '#000000');
				}
			} else {
				if (color != 'RGB(0, 146, 220)') {
					$(this).css('backgroundColor', '#E0ECFF');
					$(this).css('color', '#000000');
				}
			}
		}, 
		function() {
			var color = $(this).css('backgroundColor');
			color = color.toUpperCase();
			if (color.substr(0, 1) == '#') {
				if (color != '#0092DC') {
					$(this).css('backgroundColor', '#fafafa');
					$(this).css('color', '#000000');
				}
			} else {
				if (color != 'RGB(0, 146, 220)') {
					$(this).css('backgroundColor', '#fafafa');
					$(this).css('color', '#000000');
				}
			}
		}
	)

})

</script>
<style type="text/css">
	.drag-item{
		list-style-type:none;
		display:block;
		padding:5px;
		border:1px solid #ccc;
		margin:2px;
		width:80%;
		background:#fafafa;
		color:#444;
		text-align: center;
		cursor: pointer;
	}
</style>

<div class="easyui-panel" fit="true" border="false">
  <div id="menuDiv" class="easyui-accordion" fit="true" border="false">
    <%--
    <div title="系统菜单" iconCls="icon-tip">
        <div region="north" border="false" style="overflow: hidden;"> 
          <a href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-redo" onclick="expandAll();">展开</a><a href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-undo" onclick="collapseAll();">折叠</a><a href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-reload" onclick="tree.tree('reload');">刷新</a>
          <hr style="border-color: #fff;" />
        </div>
        <div region="center" border="false">
          <ul id="tree" style="margin-top: 5px;">
          </ul>
        </div>
    </div>
    --%>
   	<%-- 
	<div title="EasyUiDemo" href="easyuidemo.html"></div>
	--%>
	<%-- <c:forEach items="${sessionScope.USER_MENU}" var="module">
	<c:if test="${module.moduleLevel == '1'}">
	<div title="${module.moduleName }" iconCls="icon-tip">
		<!-- 只展示二级模块
		<ul style="margin:0;padding:0;margin-left:10px;" name="menuUl">
		<c:forEach items="${sessionScope.USER_MENU}" var="module2">
			<c:if test="${module2.moduleLevel == '2' && module2.parentId == module.moduleId}">
				<li class="drag-item" onclick="toModule('${module2.moduleDesc }', '${module2.moduleName }', this)">${module2.moduleName }</li>
			</c:if>
		</c:forEach>
		</ul>
		 -->
		 <ul id="tree_${module.moduleId }" style="margin-top: 5px;">
         </ul>
         <script type="text/javascript">
         $(function() {
			var tree_${module.moduleId } = $('#tree_${module.moduleId }').tree({
				url : 'manage/sys/user/userMenu.do?moduleId=${module.moduleId }&random=' + Math.random(),
				animate : false,
				lines : !Tool.isLessThanIe8(),
				onClick : function(node) {
					if (node.attributes && node.attributes.src && node.attributes.src != '') {
						var href = node.attributes.src;
						/*
						$.messager.progress({
							text : '请求数据中....',
							interval : 100
						});
						*/
						addTabFun({
							src : href,
							title : node.text
						});
					} else {
						/*
						addTabFun({
							src : 'test/err.jsp',
							title : '工程建设'
						});
						*/
					}
				},
				onLoadSuccess : function(node, data) {
					var t = $(this);
					if (data) {
						$(data).each(function(index, d) {
							if (this.state == 'closed') {
								t.tree('expandAll');
							}
						});
						try {
							if (data[0] && data[0].msg) {
								$.messager.show({
									msg : '数据库异常！',
									title : '提示'
								});
							}
						} catch(e){alert(e)}
					}
				},
				onLoadError : function() {
						window.location.href = overUrl;
				}
			});
		});
         </script>
	</div>
	</c:if>
	</c:forEach> --%>
	
  </div>
</div>
