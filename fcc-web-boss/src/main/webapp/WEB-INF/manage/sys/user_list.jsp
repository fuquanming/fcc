<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/head/base.jsp" %>
<%@ include file="/head/meta.jsp" %>
<%@ include file="/head/easyui.jsp" %>
<script type="text/javascript" charset="UTF-8">
	var datagrid;
	var userForm;
	$(function() {

		userForm = $('#userForm').form();

		$('[name=organId]').combotree({
			url : 'manage/sys/organ/tree.do?random=' + Math.random(),
			animate : false,
			lines : !Tool.isLessThanIe8(),
			checkbox : true,
			multiple : false,
			onLoadSuccess : function(node, data) {
				var t = $(this);
				if (data) {
					$(data).each(function(index, d) {
						if (this.state == 'closed') {
							t.tree('expandAll');
						}
					});
				}
			}
		});

		datagrid = $('#datagrid').datagrid({
			<c:if test="${empty param.lazy}">url : '<%=basePath%>manage/sys/user/datagrid.do?random=' + Math.random(),</c:if>
			toolbar : '#toolbar',
			title : '',
			iconCls : 'icon-save',
			pagination : true,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 ],
			fit : true,
			fitColumns : true,
			nowrap : false,
			border : false,
			idField : 'userId',
			frozenColumns : [ [ {
				field : 'id',
				width : 50,
				checkbox : true
			}, {
				field : 'userId',
				title : '帐号',
				width : 100
			} ] ],
			columns : [ [ {
				field : 'userName',
				title : '用户名称',
				width : 100
			} , {
				field : 'mobile',
				title : '手机',
				width : 150
			} , {
				field : 'email',
				title : 'Email',
				width : 150
			} , {
				field : 'regDate',
				title : '注册时间',
				width : 150,
				formatter : function(value, rowData, rowIndex) {
					var date = new Date(value);
					var month = date.getMonth() + 1;
					var day = date.getDate();
					var hour = date.getHours();
					var minute = date.getMinutes();
					var second = date.getSeconds();
                	return date.getFullYear() + '-' + (month < 10 ? "0" : "") + month + '-' + (day < 10 ? "0" : "") + day 
                	+ " " + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "") + second;
				}
			} ,{
				field : 'userStatus',
				title : '状态',
				width : 150,
				formatter : function(value, rowData, rowIndex) {
					var temp = value;
					if (value == '${userStatusLock}') {
						temp = '锁定';
					} else {
						temp = '正常'
					}
					return temp;
				}
			} , {
				field : 'roleNames',
				title : '角色',
				width : 150
			}] ],
			onRowContextMenu : function(e, rowIndex, rowData) {
				e.preventDefault();
				$(this).datagrid('unselectAll');
				$(this).datagrid('selectRow', rowIndex);
				$('#menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			},
			onLoadError : function() {
				window.location.href = overUrl;
			},
			onLoadSuccess : function(data) {
				if (data.msg && data.msg != '') {
					$.messager.show({
						msg : '数据库异常！',
						title : '提示'
					});
				}
			}
		});

	});

	function view() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length != 1 && rows.length != 0) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].userName);
			}
			$.messager.show({
				msg : '只能择一条记录进行查看！您已经选择了【' + names.join(',') + '】' + rows.length + '条记录',
				title : '提示'
			});
		} else if (rows.length == 1) {
			window.location.href = "<%=basePath%>manage/sys/user/toView.do?id=" + rows[0].userId;
		}
	}

	function add() {
		window.location.href = "<%=basePath%>manage/sys/user/toAdd.do";
	}

	function edit() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length != 1 && rows.length != 0) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].userName);
			}
			$.messager.show({
				msg : '只能择一条记录进行编辑！您已经选择了【' + names.join(',') + '】' + rows.length + '条记录',
				title : '提示'
			});
		} else if (rows.length == 1) {
			window.location.href = "<%=basePath%>manage/sys/user/toEdit.do?id=" + rows[0].userId;
		}
	}
	
	function resetPassword() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要操作当前所选记录？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].userId);
					}
					var idsVal = ids.join(',');
					userForm.find('[name=ids]').val(idsVal);
					userForm.form('submit', {
						url : '<%=basePath%>manage/sys/user/resetPassword.do',
						onSubmit : function() {
							return true;
						},
						success : function(data) {
							try {
								var d = $.parseJSON(data);
								$.messager.show({
									msg : d.msg,
									title : '提示'
								});
								if (d.success) {
									// 动态删除datagrid
									var rows = datagrid.datagrid('getSelections');
									while (rows.length > 0) {
										var rowIndex = datagrid.datagrid('getRowIndex', rows[0]);
										datagrid.datagrid('deleteRow',rowIndex);
										rows = datagrid.datagrid('getSelections');
									}
									//datagrid.datagrid('reload');
									searchFun();
								}
							} catch(e) {
								window.location.href = overUrl;
							}
						}
					});
				}
			});
		} else {
			$.messager.alert('提示', '请选择要操作的记录！', 'error');
		}
	}

	function del() {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要删除当前所选记录？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].userId);
					}
					var idsVal = ids.join(',');
					userForm.find('[name=ids]').val(idsVal);
					userForm.form('submit', {
						url : '<%=basePath%>manage/sys/user/delete.do',
						onSubmit : function() {
							return true;
						},
						success : function(data) {
							try {
								var d = $.parseJSON(data);
								$.messager.show({
									msg : d.msg,
									title : '提示'
								});
								if (d.success) {
									// 动态删除datagrid
									var rows = datagrid.datagrid('getSelections');
									while (rows.length > 0) {
										var rowIndex = datagrid.datagrid('getRowIndex', rows[0]);
										datagrid.datagrid('deleteRow',rowIndex);
										rows = datagrid.datagrid('getSelections');
									}
									//datagrid.datagrid('reload');
									searchFun();
								}
							} catch(e) {
								window.location.href = overUrl;
							}
						}
					});
				}
			});
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}
	
	function editStatus(userStatus) {
		var ids = [];
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要操作当前所选记录？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].userId);
					}
					var idsVal = ids.join(',');
					userForm.find('[name=ids]').val(idsVal);
					userForm.find('[name=userStatus]').val(userStatus);
					userForm.form('submit', {
						url : '<%=basePath%>manage/sys/user/lock.do',
						onSubmit : function() {
							return true;
						},
						success : function(data) {
							try {
								var d = $.parseJSON(data);
								$.messager.show({
									msg : d.msg,
									title : '提示'
								});
								if (d.success) {
									// 动态删除datagrid
									var rows = datagrid.datagrid('getSelections');
									while (rows.length > 0) {
										var rowIndex = datagrid.datagrid('getRowIndex', rows[0]);
										datagrid.datagrid('deleteRow',rowIndex);
										rows = datagrid.datagrid('getSelections');
									}
									//datagrid.datagrid('reload');
									searchFun();
								}
							} catch(e) {
								window.location.href = overUrl;
							}
						}
					});
				}
			});
		} else {
			$.messager.alert('提示', '请选择要操作的记录！', 'error');
		}
	}

	function searchFun() {
		<c:if test="${not empty param.lazy}">datagrid = $('#datagrid').datagrid({url : '<%=basePath%>manage/sys/user/datagrid.do?random=' + Math.random()});</c:if>
		datagrid.datagrid('load', {
			userId : $('#toolbar input[name=userId]').val(),
			organId : $('#toolbar input[name=organId]').val()
			<c:if test="${not empty userList}">
			,createUser : $('#toolbar select[name=createUser]').val()
			</c:if>
		});
	}
	function clearFun() {
		$('#toolbar input').val('');
		datagrid.datagrid('load', {});
		<c:if test="${not empty userList}">
		$('#toolbar select').val('');
		</c:if>
	}
</script>
</head>
<body class="easyui-layout" fit="true">
<div region="center" border="false">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto;display: none;">
    <fieldset>
    <legend>筛选</legend>
    <form id="userForm" name="userForm" method="post">
  	<input name="ids" type="hidden" value=""/>
  	<input name="userStatus" type="hidden" value=""/>
    <table class="tableForm">
      <tr>
        <th>帐号</th>
        <td><input name="userId" style="width: 305px;" /></td>
		<c:if test="${not empty userList}">
        <th>创建者</th>
        <td>
        <select name="createUser" id="createUser" style="width: 305px;">
        <option value="">--请选择--</option>
        <c:forEach items="${userList}" var="user">
        <option value="${user.userId }">${user.userId }</option>
        </c:forEach>
        </select>
        </td>
        </c:if>
      </tr>
      <tr>
        <th>组织机构</th>
        <td ><input name="organId" style="width: 310px;" /></td>
        <td colspan="2">
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="searchFun();" href="javascript:void(0);">查找</a>
        <a class="easyui-linkbutton" iconCls="icon-search" plain="true" onClick="clearFun();" href="javascript:void(0);">清空</a>
        </td>
      </tr>
    </table>
    </form>
    </fieldset>
    <div> 
    <a class="easyui-linkbutton" iconCls="icon-search" onClick="view();" plain="true" href="javascript:void(0);">查看</a> 
    <fcc:permission moduleId="${rightModuleId}" operateId="add">
    <a class="easyui-linkbutton" iconCls="icon-add" onClick="add();" plain="true" href="javascript:void(0);">新增</a> 
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="edit">
    <a class="easyui-linkbutton" iconCls="icon-edit" onClick="edit();" plain="true" href="javascript:void(0);">修改</a>
    <a class="easyui-linkbutton" iconCls="icon-edit" onClick="resetPassword();" plain="true" href="javascript:void(0);">重置密码</a>
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="delete">
    <a class="easyui-linkbutton" iconCls="icon-remove" onClick="del();" plain="true" href="javascript:void(0);">删除</a>  
    </fcc:permission>
    <fcc:permission moduleId="${rightModuleId}" operateId="lock">
    <a class="easyui-linkbutton" iconCls="icon-edit" onClick="editStatus('${userStatusLock }');" plain="true" href="javascript:void(0);">锁定</a>  
    <a class="easyui-linkbutton" iconCls="icon-edit" onClick="editStatus('${userStatusActivation }');" plain="true" href="javascript:void(0);">解锁</a>  
    </fcc:permission>
    <a class="easyui-linkbutton" iconCls="icon-undo" onClick="datagrid.datagrid('unselectAll');" plain="true" href="javascript:void(0);">取消选中</a> </div>
  </div>
  <table id="datagrid">
  </table>
</div>
</body>
</html>
