package com.weiziplus.pc.core.system.service;

import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.models.SysFunction;
import com.weiziplus.common.util.ToolUtils;
import com.weiziplus.common.util.redis.RedisUtils;
import com.weiziplus.pc.core.system.mapper.SysFunctionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author wanglongwei
 * @date 2020/05/28 16/37
 */
@Slf4j
@Service
public class SysRoleFunctionService extends BaseService {

    @Autowired
    SysFunctionMapper sysFunctionMapper;

    /**
     * 唯一redis前缀
     */
    public final static String REDIS_KEY = createOnlyRedisKeyPrefix();

    /**
     * 根据角色id列表获取拥有的菜单
     *
     * @param roleIdList
     * @return
     */
    private List<SysFunction> getMenuListByRoleIdList(List<Integer> roleIdList) {
        if (null == roleIdList) {
            return null;
        }
        String redisKey = createRedisKey(REDIS_KEY + "getMenuListByRoleIdList:", roleIdList);
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfList(object, SysFunction.class);
        }
        List<SysFunction> menuListByRoleIdList = sysFunctionMapper.getFunctionListByRoleIdList(roleIdList, SysFunction.Type.MENU.getValue());
        RedisUtils.set(redisKey, menuListByRoleIdList);
        return menuListByRoleIdList;
    }

    /**
     * 根据角色id列表获取菜单树形列表
     *
     * @param roleIdList
     * @return
     */
    public List<SysFunction> getMenuTreeByRoleIdList(List<Integer> roleIdList) {
        if (null == roleIdList) {
            return null;
        }
        String redisKey = createRedisKey(REDIS_KEY + "getMenuTreeByRoleIdList:", roleIdList);
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfList(object, SysFunction.class);
        }
        List<SysFunction> resultList = new ArrayList<>();
        List<SysFunction> menuListByRoleIdList = getMenuListByRoleIdList(roleIdList);
        if (null == menuListByRoleIdList || 0 >= menuListByRoleIdList.size()) {
            return resultList;
        }
        //最小的父级id
        Integer minParentId = menuListByRoleIdList.get(0).getParentId();
        for (SysFunction sysFunction : menuListByRoleIdList) {
            Integer parentId = sysFunction.getParentId();
            minParentId = minParentId <= parentId ? minParentId : parentId;
        }
        for (SysFunction sysFunction : menuListByRoleIdList) {
            if (!minParentId.equals(sysFunction.getParentId())) {
                continue;
            }
            List<SysFunction> childrenFunctionByParentId = getChildrenFunctionByParentId(menuListByRoleIdList, sysFunction.getId());
            sysFunction.setChildren(childrenFunctionByParentId);
            resultList.add(sysFunction);
        }
        RedisUtils.set(redisKey, resultList);
        return resultList;
    }

    /**
     * 根据上级id获取子级功能列表
     *
     * @param sysFunctionList
     * @param parentId
     * @return
     */
    private List<SysFunction> getChildrenFunctionByParentId(List<SysFunction> sysFunctionList, Integer parentId) {
        List<SysFunction> resultList = new ArrayList<>();
        for (SysFunction sysFunction : sysFunctionList) {
            if (!parentId.equals(sysFunction.getParentId())) {
                continue;
            }
            List<SysFunction> childrenFunctionByParentId = getChildrenFunctionByParentId(sysFunctionList, sysFunction.getId());
            sysFunction.setChildren(childrenFunctionByParentId);
            resultList.add(sysFunction);
        }
        return resultList;
    }

    /**
     * 根据角色id列表获取拥有的按钮列表
     *
     * @param roleIdList
     * @return
     */
    private List<SysFunction> getButtonListByRoleIdList(List<Integer> roleIdList) {
        if (null == roleIdList) {
            return null;
        }
        String redisKey = createRedisKey(REDIS_KEY + "getButtonListByRoleIdList:", roleIdList);
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfList(object, SysFunction.class);
        }
        List<SysFunction> buttonListByRoleIdList = sysFunctionMapper.getFunctionListByRoleIdList(roleIdList, SysFunction.Type.BUTTON.getValue());
        RedisUtils.set(redisKey, buttonListByRoleIdList);
        return buttonListByRoleIdList;
    }

    /**
     * 根据角色id列表获取拥有的按钮name列表
     *
     * @param roleIdList
     * @return
     */
    public Set<String> getButtonNameListByRoleIdList(List<Integer> roleIdList) {
        if (null == roleIdList) {
            return null;
        }
        String redisKey = createRedisKey(REDIS_KEY + "getButtonNameListByRoleIdList:", roleIdList);
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> resultSet = new HashSet<>();
        List<SysFunction> buttonListByRoleIdList = getButtonListByRoleIdList(roleIdList);
        if (null == buttonListByRoleIdList || 0 >= buttonListByRoleIdList.size()) {
            return resultSet;
        }
        for (SysFunction sysFunction : buttonListByRoleIdList) {
            resultSet.add(sysFunction.getName());
        }
        RedisUtils.set(redisKey, resultSet);
        return resultSet;
    }

    /**
     * 获取所有的功能列表
     *
     * @return
     */
    private List<SysFunction> getAllFunctionList() {
        String redisKey = createRedisKey(REDIS_KEY + "getAllFunctionList:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfList(object, SysFunction.class);
        }
        List<SysFunction> sysFunctionList = baseFindAllByClass(SysFunction.class);
        RedisUtils.set(redisKey, sysFunctionList);
        return sysFunctionList;
    }

    /**
     * 获取所有功能包含的api
     *
     * @return
     */
    public Set<String> getAllFunContainApi() {
        String redisKey = createRedisKey(REDIS_KEY + "getAllFunContainApi:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> resultSet = new HashSet<>();
        List<SysFunction> allFunctionList = getAllFunctionList();
        if (null == allFunctionList || 0 >= allFunctionList.size()) {
            return resultSet;
        }
        for (SysFunction sysFunction : allFunctionList) {
            String containApi = sysFunction.getContainApi();
            if (ToolUtils.isBlank(containApi)) {
                continue;
            }
            String[] split = containApi.replaceAll("[^(a-zA-Z/，,)]*", "")
                    .replace("，", ",").split(",");
            resultSet.addAll(Arrays.asList(split));
        }
        return resultSet;
    }

    /**
     * 根据角色id列表获取拥有的功能列表
     *
     * @param roleIdList
     * @return
     */
    private List<SysFunction> getFunctionListByRoleIdList(List<Integer> roleIdList) {
        if (null == roleIdList) {
            return null;
        }
        String redisKey = createRedisKey(REDIS_KEY + "getFunctionListByRoleIdList:", roleIdList);
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfList(object, SysFunction.class);
        }
        List<SysFunction> functionListByRoleIdList = sysFunctionMapper.getFunctionListByRoleIdList(roleIdList, null);
        RedisUtils.set(redisKey, functionListByRoleIdList);
        return functionListByRoleIdList;
    }

    /**
     * 根据角色id列表获取拥有的api
     *
     * @param roleIdList
     * @return
     */
    public Set<String> getFunContainApiListByRoleIdList(List<Integer> roleIdList) {
        if (null == roleIdList) {
            return null;
        }
        String redisKey = createRedisKey(REDIS_KEY + "getFunContainApiListByRoleIdList:", roleIdList);
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> resultSet = new HashSet<>();
        List<SysFunction> functionListByRoleIdList = getFunctionListByRoleIdList(roleIdList);
        for (SysFunction sysFunction : functionListByRoleIdList) {
            String containApi = sysFunction.getContainApi();
            if (ToolUtils.isBlank(containApi)) {
                continue;
            }
            String[] split = containApi.replaceAll("[^(a-zA-Z/，,)]*", "")
                    .replace("，", ",").split(",");
            resultSet.addAll(Arrays.asList(split));
        }
        RedisUtils.set(redisKey, resultSet);
        return resultSet;
    }

}
