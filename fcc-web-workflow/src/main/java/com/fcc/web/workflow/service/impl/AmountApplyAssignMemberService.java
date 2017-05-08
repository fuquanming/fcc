package com.fcc.web.workflow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.web.service.TreeableService;
import com.fcc.commons.web.view.EasyuiTreeNode;
import com.fcc.web.sys.model.Organization;
import com.fcc.web.sys.model.SysUser;
import com.fcc.web.sys.service.SysUserService;
import com.fcc.web.workflow.common.AmountApplyEnum;

/**
 * 分配风控人员
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009</p>
 * 
 * @author 傅泉明
 * @version v1.0
 */
@Component
public class AmountApplyAssignMemberService implements JavaDelegate {

	@Autowired
	private TreeableService organService;
	@Autowired
	private SysUserService sysUserService;
	
	// 风控人员组织机构
	private Expression assignMemberDept;

	@SuppressWarnings("unchecked")
	public void execute(DelegateExecution execution) throws Exception {
		String assignMemberDeptStr = (String) assignMemberDept.getValue(execution);
		try {
			List<EasyuiTreeNode> organList = organService.getTree(Organization.class, assignMemberDeptStr, true, false);
			List<String> organIdList = new ArrayList<String>(organList.size());
			for (EasyuiTreeNode organ : organList) {
				organIdList.add(organ.getId());
			}
		    
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("deptList", organIdList);
			ListPage listPage = sysUserService.queryPage(1, 0, param);
			List<SysUser> sysUserList = listPage.getDataList();
			int size = sysUserList.size();
			List<Integer> indexList = randomList(size);
			for (int i = 0; i < size; i++) {
				execution.setVariable(AmountApplyEnum.randomMember.toString() + i, sysUserList.get(indexList.get(i)).getUserId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<Integer> randomList(int maxSize) {
		Set<Integer> indexSet = new HashSet<Integer>();
		List<Integer> indexList = new ArrayList<Integer>();
		for (int i = 0; i < maxSize; i++) {
			int randomIndex = RandomUtils.nextInt(0, maxSize);
			if (indexSet.contains(randomIndex)) {
				i --;
			} else {
				indexSet.add(randomIndex);
				indexList.add(randomIndex);
			}
		}
		return indexList;
	}
	
	public static void main(String[] args) {
		List<Integer> index = new AmountApplyAssignMemberService().randomList(5);
		System.out.println(java.util.Arrays.toString(index.toArray()));
	}

}
