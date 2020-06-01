package com.weiziplus.pc.core.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.config.GlobalConfig;
import com.weiziplus.common.models.SysUser;
import com.weiziplus.common.models.SysUserLog;
import com.weiziplus.common.models.SysUserRole;
import com.weiziplus.common.util.*;
import com.weiziplus.common.util.token.AdminTokenUtils;
import com.weiziplus.pc.common.config.MyGlobalConfig;
import com.weiziplus.pc.core.system.dto.SysUserDto;
import com.weiziplus.pc.core.system.mapper.SysUserMapper;
import com.weiziplus.pc.core.system.vo.SysUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/28 09/27
 */
@Slf4j
@Service
public class SysUserService extends BaseService {

    @Autowired
    SysUserMapper mapper;

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @param username
     * @param roleId
     * @param status
     * @param lastActiveTime
     * @param createTime
     * @param lastActiveTimeSort
     * @param createTimeSort
     * @return
     */
    public ResultUtils<PageUtils<List<SysUserVo>>> getPageList(Integer pageNum, Integer pageSize
            , String username, Integer roleId, Integer status, String lastActiveTime, String createTime
            , String lastActiveTimeSort, String createTimeSort) {
        if (!OrderByType.contains(lastActiveTimeSort)
                || !OrderByType.contains(createTimeSort)) {
            return ResultUtils.error("排序类型错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<List<SysUserVo>> pageUtil = PageUtils.pageInfo(mapper.getListVo(
                username, roleId, status, lastActiveTime, createTime,
                lastActiveTimeSort, createTimeSort));
        return ResultUtils.success(pageUtil);
    }

    /**
     * 新增用户
     *
     * @param sysUserDto
     * @param roleIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultUtils add(SysUserDto sysUserDto, Integer[] roleIds) {
        if (ToolUtils.isBlank(sysUserDto.getUsername())) {
            return ResultUtils.error("用户名不能为空");
        }
        if (ToolUtils.isBlank(sysUserDto.getPassword())) {
            return ResultUtils.error("密码不能为空");
        }
        if (ToolUtils.isBlank(sysUserDto.getRealName())) {
            return ResultUtils.error("真实姓名不能为空");
        }
        if (ValidateUtils.notPhone(sysUserDto.getPhone())) {
            return ResultUtils.error("手机号码格式错误");
        }
        if (!SysUser.Status.contains(sysUserDto.getStatus())) {
            return ResultUtils.error("状态错误");
        }
        SysUser sysUser = baseFindOneDataByClassAndColumnAndValue(
                SysUser.class, SysUser.COLUMN_USERNAME, sysUserDto.getUsername());
        if (null != sysUser) {
            return ResultUtils.error("用户名已存在");
        }
        SysUser newSysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDto, newSysUser);
        newSysUser.setPassword(Md5Utils.encode(sysUserDto.getPassword()))
                .setCreateTime(DateUtils.getNowDateTime());
        //如果没有指定角色
        if (null == roleIds || 0 >= roleIds.length) {
            baseInsert(newSysUser);
            return ResultUtils.success();
        }
        List<SysUserRole> sysUserRoleList = new ArrayList<>(ToolUtils.initialCapacity(roleIds.length));
        //指定了角色
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try {
            baseInsert(newSysUser, true);
            for (Integer roleId : roleIds) {
                if (MyGlobalConfig.SYS_ROLE_SUPER_ADMIN_ID.equals(roleId)) {
                    TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
                    return ResultUtils.error("超级管理员只能有一个");
                }
                SysUserRole sysUserRole = new SysUserRole()
                        .setUserId(newSysUser.getId())
                        .setRoleId(roleId);
                sysUserRoleList.add(sysUserRole);
            }
            baseInsertList(sysUserRoleList);
        } catch (Exception e) {
            log.warn("新增系统用户出错，详情:" + e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return ResultUtils.error("系统错误，请重试。" + e);
        }
        return ResultUtils.success();
    }

    /**
     * 修改手机号
     *
     * @param id
     * @param phone
     * @return
     */
    public ResultUtils updatePhone(Integer id, String phone) {
        if (null == id || 0 >= id) {
            return ResultUtils.error("id错误");
        }
        if (ValidateUtils.notPhone(phone)) {
            return ResultUtils.error("手机号码格式错误");
        }
        SysUser sysUser = new SysUser()
                .setId(id)
                .setPhone(phone);
        baseUpdate(sysUser);
        return ResultUtils.success();
    }

    /**
     * 修改用户状态
     *
     * @param id
     * @param status
     * @return
     */
    public ResultUtils updateStatus(Integer id, Integer status) {
        if (null == id || 0 >= id) {
            return ResultUtils.error("id错误");
        }
        if (MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(id)) {
            return ResultUtils.error("禁止操作超级管理员");
        }
        if (!SysUser.Status.contains(status)) {
            return ResultUtils.error("状态错误");
        }
        SysUser sysUser = new SysUser()
                .setId(id)
                .setStatus(status);
        if (SysUser.Status.NORMAL.getValue().equals(status)) {
            baseUpdate(sysUser);
            return ResultUtils.success();
        }
        //如果是禁用状态,强制用户下线
        AdminTokenUtils.deleteToken(id);
        baseUpdate(sysUser);
        return ResultUtils.success();
    }

    /**
     * 修改用户角色
     *
     * @param id
     * @param roleIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultUtils updateRole(Integer id, Integer[] roleIds) {
        if (null == id || 0 >= id) {
            return ResultUtils.error("id错误");
        }
        if (MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(id)) {
            return ResultUtils.error("禁止操作超级管理员");
        }
        if (null == roleIds || 0 >= roleIds.length) {
            mapper.deleteUserRoleByUserId(id);
            return ResultUtils.success();
        }
        List<SysUserRole> sysUserRoleList = new ArrayList<>(roleIds.length);
        for (Integer roleId : roleIds) {
            if (MyGlobalConfig.SYS_ROLE_SUPER_ADMIN_ID.equals(roleId)) {
                return ResultUtils.error("超级管理员只能有一个");
            }
            SysUserRole sysUserRole = new SysUserRole()
                    .setUserId(id)
                    .setRoleId(roleId);
            sysUserRoleList.add(sysUserRole);
        }
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try {
            mapper.deleteUserRoleByUserId(id);
            baseInsertList(sysUserRoleList);
        } catch (Exception e) {
            log.warn("修改用户角色出错，详情:" + e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return ResultUtils.error("系统错误，请重试。" + e);
        }
        return ResultUtils.success();
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    public ResultUtils delete(Integer id) {
        if (null == id || 0 >= id) {
            return ResultUtils.error("id错误");
        }
        if (MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(id)) {
            return ResultUtils.error("禁止操作超级管理员");
        }
        SysUserLog sysUserLog = baseFindOneDataByClassAndColumnAndValue(
                SysUserLog.class, SysUserLog.COLUMN_USER_ID, id);
        //如果用户没有操作过
        if (null == sysUserLog) {
            baseDeleteByClassAndId(SysUser.class, id);
            return ResultUtils.success();
        }
        //用户有过操作记录，停用用户，并强制用户下线
        SysUser sysUser = new SysUser()
                .setId(id)
                .setStatus(SysUser.Status.DISABLE.getValue());
        AdminTokenUtils.deleteToken(id);
        baseUpdate(sysUser);
        return ResultUtils.success();
    }

    /**
     * 修改头像
     *
     * @param file
     * @return
     */
    public ResultUtils updateIcon(MultipartFile file) {
        BufferedImage image = FileUtils.getImage(file);
        if (null == image) {
            return ResultUtils.error("请上传图片");
        }
        //长宽比
        float minScale = 0.7F;
        float maxScale = 1.2F;
        double scale = 1F;
        if (0 != image.getWidth()) {
            scale = (float) image.getHeight() / image.getWidth();
        }
        if (scale < minScale || scale > maxScale) {
            return ResultUtils.error("头像建议长宽比1:1");
        }
        String path = FileUtils.upFile(file, "user/icon");
        if (null == path) {
            return ResultUtils.error("文件上传失败，请重试");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer userId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        SysUser sysUser = new SysUser()
                .setId(userId)
                .setIcon(path);
        baseUpdate(sysUser);
        return ResultUtils.success(GlobalConfig.getMybatisFilePathPrefix() + path);
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param newPwd
     * @return
     */
    public ResultUtils updatePwd(String oldPwd, String newPwd) {
        if (ToolUtils.isBlank(oldPwd)) {
            return ResultUtils.error("旧密码不能为空");
        }
        if (ToolUtils.isBlank(newPwd)) {
            return ResultUtils.error("新密码不能为空");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer userId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        SysUser sysUser = baseFindByClassAndId(SysUser.class, userId);
        if (!sysUser.getPassword().equals(Md5Utils.encode(oldPwd))) {
            return ResultUtils.error("原密码错误");
        }
        sysUser.setPassword(Md5Utils.encode(newPwd));
        baseUpdate(sysUser);
        AdminTokenUtils.deleteToken(userId);
        return ResultUtils.success();
    }

    /**
     * 重置密码
     *
     * @param id
     * @param password
     * @return
     */
    public ResultUtils resetPwd(Integer id, String password) {
        if (null == id || 0 >= id) {
            return ResultUtils.error("id错误");
        }
        if (MyGlobalConfig.SYS_USER_SUPER_ADMIN_ID.equals(id)) {
            return ResultUtils.error("禁止操作超级管理员");
        }
        if (ToolUtils.isBlank(password)) {
            return ResultUtils.error("密码不能为空");
        }
        SysUser sysUser = new SysUser()
                .setId(id)
                .setPassword(Md5Utils.encode(password));
        baseUpdate(sysUser);
        AdminTokenUtils.deleteToken(id);
        return ResultUtils.success();
    }
}