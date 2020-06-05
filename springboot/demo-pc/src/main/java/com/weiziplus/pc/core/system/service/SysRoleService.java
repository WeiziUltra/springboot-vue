package com.weiziplus.pc.core.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.base.BaseWhere;
import com.weiziplus.common.models.SysFunction;
import com.weiziplus.common.models.SysRole;
import com.weiziplus.common.models.SysRoleFunction;
import com.weiziplus.common.util.DateUtils;
import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.common.util.ToolUtils;
import com.weiziplus.common.util.redis.RedisUtils;
import com.weiziplus.pc.common.config.MyGlobalConfig;
import com.weiziplus.pc.core.system.mapper.SysRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/28 09/59
 */
@Slf4j
@Service
public class SysRoleService extends BaseService {

    @Autowired
    SysRoleMapper mapper;

    /**
     * 唯一redis前缀
     */
    private final String REDIS_KEY = createOnlyRedisKeyPrefix();

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    public ResultUtils<PageUtils<List<SysRole>>> getPageList(Integer pageNum, Integer pageSize, String search) {
        String redisKey = createRedisKey(REDIS_KEY + "getPageList:", pageNum, pageSize, search);
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            List<SysRole> sysFunctionList = ToolUtils.objectOfList(object, SysRole.class);
            PageUtils<List<SysRole>> pageUtil = PageUtils.pageInfo(sysFunctionList);
            return ResultUtils.success(pageUtil);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<SysRole> sysRoleList = mapper.getList(search);
        RedisUtils.set(redisKey, sysRoleList);
        PageUtils<List<SysRole>> pageUtil = PageUtils.pageInfo(sysRoleList);
        return ResultUtils.success(pageUtil);
    }

    /**
     * 获取列表数据
     *
     * @return
     */
    public ResultUtils<List<SysRole>> getList() {
        String redisKey = createRedisKey(REDIS_KEY + "getList:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            List<SysRole> sysRoleList = ToolUtils.objectOfList(object, SysRole.class);
            return ResultUtils.success(sysRoleList);
        }
        List<SysRole> sysRoleList = baseFindAllByClassOrderByColumnAsc(
                SysRole.class, SysRole.COLUMN_SORT);
        for (SysRole sysRole : sysRoleList) {
            if (MyGlobalConfig.SYS_ROLE_SUPER_ADMIN_ID.equals(sysRole.getId())) {
                sysRoleList.remove(sysRole);
                break;
            }
        }
        RedisUtils.set(redisKey, sysRoleList);
        return ResultUtils.success(sysRoleList);
    }

    /**
     * 获取角色拥有的功能列表
     *
     * @param id
     * @return
     */
    public ResultUtils<List<Integer>> getFunctionList(Integer id) {
        if (null == id || 0 >= id) {
            return ResultUtils.error("id不能为空");
        }
        List<SysRoleFunction> sysRoleFunctionList = baseFindListByClassAndColumnAndValue(
                SysRoleFunction.class, SysRoleFunction.COLUMN_ROLE_ID, id);
        List<Integer> resultList = new ArrayList<>(ToolUtils.initialCapacity(sysRoleFunctionList.size()));
        for (SysRoleFunction sysRoleFunction : sysRoleFunctionList) {
            resultList.add(sysRoleFunction.getFunctionId());
        }
        return ResultUtils.success(resultList);
    }

    /**
     * 新增角色
     *
     * @param sysRole
     * @return
     */
    public ResultUtils add(SysRole sysRole) {
        if (ToolUtils.isBlank(sysRole.getName())) {
            return ResultUtils.error("角色名称不能为空");
        }
        if (!SysRole.Status.contains(sysRole.getStatus())) {
            return ResultUtils.error("状态错误");
        }
        SysRole sysRole1 = baseFindOneDataByClassAndColumnAndValue(
                SysRole.class, SysRole.COLUMN_NAME, sysRole.getName());
        if (null != sysRole1) {
            return ResultUtils.error("角色名称已存在");
        }
        sysRole.setCreateTime(DateUtils.getNowDateTime());
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY);
        baseInsert(sysRole);
        RedisUtils.deleteLikeKey(REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 编辑角色
     *
     * @param sysRole
     * @return
     */
    public ResultUtils update(SysRole sysRole) {
        if (null == sysRole.getId() || 0 >= sysRole.getId()) {
            return ResultUtils.error("id不能为空");
        }
        if (MyGlobalConfig.SYS_ROLE_SUPER_ADMIN_ID.equals(sysRole.getId())) {
            return ResultUtils.error("禁止操作超级管理员");
        }
        if (!SysRole.Status.contains(sysRole.getStatus())) {
            return ResultUtils.error("状态错误");
        }
        sysRole.setName(null)
                .setCreateTime(null);
        RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY);
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY);
        baseUpdate(sysRole);
        RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY);
        RedisUtils.deleteLikeKey(REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    public ResultUtils delete(Integer id) {
        if (null == id || 0 >= id) {
            return ResultUtils.error("id错误");
        }
        if (MyGlobalConfig.SYS_ROLE_SUPER_ADMIN_ID.equals(id)) {
            return ResultUtils.error("禁止操作超级管理员");
        }
        RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY);
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY);
        baseDeleteByClassAndId(SysRole.class, id);
        RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY);
        RedisUtils.deleteLikeKey(REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 修改角色功能
     *
     * @param roleId
     * @param functionIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultUtils updateRoleFunction(Integer roleId, Integer[] functionIds) {
        if (null == roleId || 0 >= roleId) {
            return ResultUtils.error("角色id不能为空");
        }
        //为空，表示移除该角色的权限
        if (null == functionIds || 0 >= functionIds.length) {
            RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY);
            RedisUtils.setExpireDeleteLikeKey(REDIS_KEY);
            mapper.deleteRoleFunctionByRoleId(roleId);
            RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY);
            RedisUtils.deleteLikeKey(REDIS_KEY);
            return ResultUtils.success();
        }
        //不为空，重新赋值权限
        List<SysFunction> sysFunctionList = baseFindByClassAndIds(SysFunction.class, functionIds);
        for (SysFunction sysFunction : sysFunctionList) {
            if (SysFunction.SuperFlag.VIP.getValue().equals(sysFunction.getSuperFlag())) {
                return ResultUtils.error("功能ids超出权限");
            }
        }
        List<SysRoleFunction> sysRoleFunctionList = new ArrayList<>(ToolUtils.initialCapacity(functionIds.length));
        for (Integer functionId : functionIds) {
            SysRoleFunction sysRoleFunction = new SysRoleFunction()
                    .setRoleId(roleId)
                    .setFunctionId(functionId);
            sysRoleFunctionList.add(sysRoleFunction);
        }
        RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY);
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY);
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try {
            mapper.deleteRoleFunctionByRoleId(roleId);
            baseInsertList(sysRoleFunctionList);
        } catch (Exception e) {
            log.warn("更新角色权限出错，详情:" + e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return ResultUtils.error("系统错误，请重试。" + e);
        }
        RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY);
        RedisUtils.deleteLikeKey(REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 修改角色状态
     *
     * @param id
     * @param status
     * @return
     */
    public ResultUtils updateStatus(Integer id, Integer status) {
        if (null == id || 0 >= id) {
            return ResultUtils.error("角色id不能为空");
        }
        if (MyGlobalConfig.SYS_ROLE_SUPER_ADMIN_ID.equals(id)) {
            return ResultUtils.error("禁止操作超级管理员");
        }
        SysRole sysRole = new SysRole()
                .setId(id)
                .setStatus(status);
        RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY);
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY);
        baseUpdate(sysRole);
        RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY);
        RedisUtils.deleteLikeKey(REDIS_KEY);
        return ResultUtils.success();
    }

}