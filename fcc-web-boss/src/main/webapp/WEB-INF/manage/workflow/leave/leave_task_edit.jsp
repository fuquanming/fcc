<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<input name="readonly" type="hidden" value="${readonly}" />
<input name="dataId" type="hidden" value="${data.leaveId}" />
<tr>    
    <th>开始时间：</th>      
    <td>
    <c:choose>
    <c:when test="${readonly == true }">
    <fmt:formatDate value="${data.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
    </c:when>
    <c:otherwise>
    <input id="startTimeString" name="startTimeString" class="easyui-datetimebox"  value="<fmt:formatDate value="${data.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
    </c:otherwise>
    </c:choose>
    </td>
</tr>   
<tr>    
    <th>结束时间：</th>      
    <td>
    <c:choose>
    <c:when test="${readonly == true }">
    <fmt:formatDate value="${data.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
    </c:when>
    <c:otherwise>
    <input id="endTimeString" name="endTimeString" class="easyui-datetimebox"  value="<fmt:formatDate value="${data.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
    </c:otherwise>
    </c:choose>
    </td>
</tr>   
<tr>    
    <th>内容：</th>        
    <td>
    <c:choose>
    <c:when test="${readonly == true }">
    ${data.content}
    </c:when>
    <c:otherwise>
    <textarea rows="5" cols="40" id="content" name="content" class="easyui-validatebox textbox eaayui-textarea" validType="length[0, 500]">${data.content}</textarea>
    </c:otherwise>
    </c:choose>
    </td>
</tr>
