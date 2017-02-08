package com.fcc.commons.workflow.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;

/**
 * <p>Description: 流程定义缓存</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class ProcessDefinitionCache {

    private static Map<String, ProcessDefinition> map = new HashMap<String, ProcessDefinition>();

    private static Map<String, List<ActivityImpl>> activities = new HashMap<String, List<ActivityImpl>>();

    private static Map<String, ActivityImpl> singleActivity = new HashMap<String, ActivityImpl>();

    private static RepositoryService repositoryService;

    public static ProcessDefinition get(String processDefinitionId) {
        ProcessDefinition processDefinition = map.get(processDefinitionId);
        if (processDefinition == null) {
            processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinitionId);
            if (processDefinition != null) {
                put(processDefinitionId, processDefinition);
            }
        }
        return processDefinition;
    }

    public static void put(String processDefinitionId, ProcessDefinition processDefinition) {
        map.put(processDefinitionId, processDefinition);
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) processDefinition;
        activities.put(processDefinitionId, pde.getActivities());
        for (ActivityImpl activityImpl : pde.getActivities()) {
            singleActivity.put(processDefinitionId + "_" + activityImpl.getId(), activityImpl);
        }
    }

    public static ActivityImpl getActivity(String processDefinitionId, String activityId) {
        ProcessDefinition processDefinition = get(processDefinitionId);
        if (processDefinition != null) {
            ActivityImpl activityImpl = singleActivity.get(processDefinitionId + "_" + activityId);
            if (activityImpl != null) {
                return activityImpl;
            }
        }
        return null;
    }

    public static String getActivityName(String processDefinitionId, String activityId) {
        ActivityImpl activity = getActivity(processDefinitionId, activityId);
        if (activity != null) {
            return activity.getProperty("name").toString();
//            return ObjectUtils.toString(activity.getProperty("name"));
        }
        return null;
    }

    public static void setRepositoryService(RepositoryService repositoryService) {
        ProcessDefinitionCache.repositoryService = repositoryService;
    }

}
