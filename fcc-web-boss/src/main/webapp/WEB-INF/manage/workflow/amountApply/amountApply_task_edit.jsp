<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<input name="readonly" type="hidden" value="${readonly}" />
<input name="dataId" type="hidden" value="${data.amountApplyId}" />
<tr>    
    <th>借款人ID（会员）：</th>
    <td>
    <c:choose>
    <c:when test="${readonly == true}">
${data.userId}    </c:when>
    <c:otherwise>
    <input id="userId" name="userId" type="text" value="${data.userId}" class="easyui-validatebox easyui-textbox" maxlength="19" />
    </c:otherwise>
    </c:choose>     
    </td>
</tr>   
<tr>    
    <th>会员账号名称：</th>
    <td>
    <c:choose>
    <c:when test="${readonly == true}">
${data.userName}    </c:when>
    <c:otherwise>
    <input id="userName" name="userName" type="text" value="${data.userName}" class="easyui-validatebox easyui-textbox" maxlength="20" />
    </c:otherwise>
    </c:choose>     
    </td>
</tr>   
<tr>    
    <th>发起人申请的额度：</th>
    <td>
    <c:choose>
    <c:when test="${readonly == true}">
${data.primaryAmount}    </c:when>
    <c:otherwise>
    <input id="primaryAmount" name="primaryAmount" type="text" value="${data.primaryAmount}" class="easyui-validatebox easyui-textbox" maxlength="11" />
    </c:otherwise>
    </c:choose>     
    </td>
</tr>   
<tr>    
    <th>申请备注：</th>
    <td>
    <c:choose>
    <c:when test="${readonly == true}">
${data.applyRemark}    </c:when>
    <c:otherwise>
    <textarea rows="5" cols="40" id="applyRemark" name="applyRemark" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 4000]">${data.applyRemark}</textarea>
    </c:otherwise>
    </c:choose>     
    </td>
</tr>   
<tr>    
    <th>申请时间：</th>
    <td>
    <c:choose>
    <c:when test="${readonly == true}">
    <fmt:formatDate value="${data.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
    </c:when>
    <c:otherwise>
    <input id="applyTimeString" name="applyTimeString" class="easyui-datetimebox"  value="<fmt:formatDate value="${data.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
    </c:otherwise>
    </c:choose>     
    </td>
</tr>   
<c:if test="${memberFlg == true}">
<tr>    
    <th>建议金额：</th>      
    <td>
    <input id="memberAmount" name="memberAmount" type="text" value="" class="easyui-validatebox easyui-textbox" maxlength="10" />
    </td>
</tr>
</c:if>