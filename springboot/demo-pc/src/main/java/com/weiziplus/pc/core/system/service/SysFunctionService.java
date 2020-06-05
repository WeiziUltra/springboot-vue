package com.weiziplus.pc.core.system.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.models.SysFunction;
import com.weiziplus.common.util.DateUtils;
import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.common.util.ToolUtils;
import com.weiziplus.common.util.redis.RedisUtils;
import com.weiziplus.common.util.token.AdminTokenUtils;
import com.weiziplus.pc.common.config.MyGlobalConfig;
import com.weiziplus.pc.core.system.mapper.SysFunctionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author wanglongwei
 * @date 2020/05/28 11/38
 */
@Slf4j
@Service
public class SysFunctionService extends BaseService {

    @Autowired
    SysFunctionMapper mapper;

    /**
     * 唯一redis前缀
     */
    private final String REDIS_KEY = createOnlyRedisKeyPrefix();

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ResultUtils<PageUtils<List<SysFunction>>> getPageList(Integer pageNum, Integer pageSize) {
        String redisKey = createRedisKey(REDIS_KEY + "getPageList:", pageNum, pageSize);
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            List<SysFunction> sysFunctionList = ToolUtils.objectOfList(object, SysFunction.class);
            PageUtils<List<SysFunction>> pageUtil = PageUtils.pageInfo(sysFunctionList);
            return ResultUtils.success(pageUtil);
        }
        Integer minParentId = mapper.getMinParentId();
        PageHelper.startPage(pageNum, pageSize);
        List<SysFunction> functionListByParentId = mapper.getFunctionListByParentId(minParentId);
        List<SysFunction> sysFunctionList = baseFindAllByClass(SysFunction.class);
        for (SysFunction sysFunction : functionListByParentId) {
            List<SysFunction> childrenListByParentId = getChildrenListByParentId(sysFunctionList, sysFunction.getId());
            sysFunction.setChildren(childrenListByParentId);
        }
        RedisUtils.set(redisKey, functionListByParentId);
        PageUtils<List<SysFunction>> pageUtil = PageUtils.pageInfo(functionListByParentId);
        return ResultUtils.success(pageUtil);
    }

    /**
     * 根据上级id获取子级列表
     *
     * @param sysFunctionList
     * @param parentId
     * @return
     */
    private List<SysFunction> getChildrenListByParentId(List<SysFunction> sysFunctionList, Integer parentId) {
        List<SysFunction> resultList = new ArrayList<>();
        for (SysFunction sysFunction : sysFunctionList) {
            if (!parentId.equals(sysFunction.getParentId())) {
                continue;
            }
            List<SysFunction> childrenListByParentId = getChildrenListByParentId(sysFunctionList, sysFunction.getId());
            sysFunction.setChildren(childrenListByParentId);
            resultList.add(sysFunction);
        }
        return resultList;
    }

    /**
     * 获取树形结构
     *
     * @return
     */
    public ResultUtils<List<SysFunction>> getTree() {
        String redisKey = createRedisKey(REDIS_KEY + "getTree:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            List<SysFunction> sysFunctionList = ToolUtils.objectOfList(object, SysFunction.class);
            return ResultUtils.success(sysFunctionList);
        }
        Integer minParentId = mapper.getMinParentId();
        List<SysFunction> allFunctionList = mapper.getAllFunctionListNotVip();
        List<SysFunction> resultList = new ArrayList<>();
        for (SysFunction sysFunction : allFunctionList) {
            if (!minParentId.equals(sysFunction.getParentId())) {
                continue;
            }
            List<SysFunction> childrenListByParentId = getChildrenListByParentId(allFunctionList, sysFunction.getId());
            sysFunction.setChildren(childrenListByParentId);
            resultList.add(sysFunction);
        }
        RedisUtils.set(redisKey, resultList);
        return ResultUtils.success(resultList);
    }

    /**
     * 获取所有菜单树形结构
     *
     * @return
     */
    public List<SysFunction> getAllMenuTree() {
        Integer minParentId = mapper.getMinParentId();
        List<SysFunction> allFunctionList = mapper.getAllMenuList();
        List<SysFunction> resultList = new ArrayList<>();
        for (SysFunction sysFunction : allFunctionList) {
            if (!minParentId.equals(sysFunction.getParentId())) {
                continue;
            }
            List<SysFunction> childrenListByParentId = getChildrenListByParentId(allFunctionList, sysFunction.getId());
            sysFunction.setChildren(childrenListByParentId);
            resultList.add(sysFunction);
        }
        return resultList;
    }

    /**
     * 新增功能
     *
     * @param sysFunctionDto
     * @return
     */
    public ResultUtils add(SysFunction sysFunctionDto) {
        //只有超级管理员才可以操作功能
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer userIdByHttpServletRequest = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(userIdByHttpServletRequest)) {
            return ResultUtils.errorRole("您没有权限");
        }
        if (ToolUtils.isBlank(sysFunctionDto.getName())) {
            return ResultUtils.error("功能名不能为空");
        }
        if (ToolUtils.isBlank(sysFunctionDto.getPath())) {
            return ResultUtils.error("路径不能为空");
        }
        if (ToolUtils.isBlank(sysFunctionDto.getTitle())) {
            return ResultUtils.error("标题不能为空");
        }
        if (!SysFunction.Type.contains(sysFunctionDto.getType())) {
            return ResultUtils.error("类型错误");
        }
        if (!SysFunction.SuperFlag.contains(sysFunctionDto.getSuperFlag())) {
            return ResultUtils.error("专属类型错误");
        }
        if (!ToolUtils.isBlank(sysFunctionDto.getContainApi())) {
            String replace = sysFunctionDto.getContainApi()
                    .replaceAll("[^(a-zA-Z/，,)]*", "")
                    .replace("，", ",");
            sysFunctionDto.setContainApi(replace);
        }
        SysFunction sysFunction = baseFindOneDataByClassAndColumnAndValue(
                SysFunction.class, SysFunction.COLUMN_NAME, sysFunctionDto.getName());
        if (null != sysFunction) {
            return ResultUtils.error("功能名已存在");
        }
        sysFunctionDto.setCreateTime(DateUtils.getNowDateTime());
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY);
        baseInsert(sysFunctionDto);
        RedisUtils.deleteLikeKey(REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 修改功能
     *
     * @param sysFunction
     * @return
     */
    public ResultUtils update(SysFunction sysFunction) {
        //只有超级管理员才可以操作功能
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer userIdByHttpServletRequest = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(userIdByHttpServletRequest)) {
            return ResultUtils.errorRole("您没有权限");
        }
        if (null == sysFunction.getId() || 0 >= sysFunction.getId()) {
            return ResultUtils.error("id不能为空");
        }
        if (ToolUtils.isBlank(sysFunction.getPath())) {
            return ResultUtils.error("路径不能为空");
        }
        if (ToolUtils.isBlank(sysFunction.getTitle())) {
            return ResultUtils.error("标题不能为空");
        }
        if (!SysFunction.Type.contains(sysFunction.getType())) {
            return ResultUtils.error("类型错误");
        }
        if (!SysFunction.SuperFlag.contains(sysFunction.getSuperFlag())) {
            return ResultUtils.error("专属类型错误");
        }
        if (!ToolUtils.isBlank(sysFunction.getContainApi())) {
            String replace = sysFunction.getContainApi()
                    .replaceAll("[^(a-zA-Z/，,)]*", "")
                    .replace("，", ",");
            sysFunction.setContainApi(replace);
        }
        //如果修改为按钮
        if (SysFunction.Type.BUTTON.getValue().equals(sysFunction.getType())) {
            SysFunction sysFunction1 = baseFindOneDataByClassAndColumnAndValue(
                    SysFunction.class, SysFunction.COLUMN_PARENT_ID, sysFunction.getId());
            if (null != sysFunction1) {
                return ResultUtils.error("存在下级，不能修改为按钮");
            }
        }
        sysFunction.setTitle(null)
                .setName(null)
                .setParentId(null)
                .setCreateTime(null);
        //如果修改了功能对应接口
        if (!ToolUtils.isBlank(sysFunction.getContainApi())) {
            RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY);
        }
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY);
        baseUpdate(sysFunction);
        if (!ToolUtils.isBlank(sysFunction.getContainApi())) {
            RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY);
        }
        RedisUtils.deleteLikeKey(REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 删除功能
     *
     * @param id
     * @return
     */
    public ResultUtils delete(Integer id) {
        //只有超级管理员才可以操作功能
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer userIdByHttpServletRequest = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(userIdByHttpServletRequest)) {
            return ResultUtils.errorRole("您没有权限");
        }
        if (null == id || 0 >= id) {
            return ResultUtils.error("id错误");
        }
        SysFunction sysFunction = baseFindOneDataByClassAndColumnAndValue(
                SysFunction.class, SysFunction.COLUMN_PARENT_ID, id);
        if (null != sysFunction) {
            return ResultUtils.error("存在下级，不能删除");
        }
        RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY);
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY);
        baseDeleteByClassAndId(SysFunction.class, id);
        RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY);
        RedisUtils.deleteLikeKey(REDIS_KEY);
        return ResultUtils.success();
    }

}