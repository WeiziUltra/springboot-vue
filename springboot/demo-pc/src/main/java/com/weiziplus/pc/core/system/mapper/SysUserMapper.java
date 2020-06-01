package com.weiziplus.pc.core.system.mapper;

import com.weiziplus.pc.core.system.vo.SysUserVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/28 09/33
 */
@Mapper
public interface SysUserMapper {

    /**
     * 获取列表数据
     *
     * @param username
     * @param roleId
     * @param status
     * @param lastActiveTime
     * @param createTime
     * @param lastActiveTimeSort
     * @param createTimeSort
     * @return
     */
    List<SysUserVo> getListVo(@Param("username") String username, @Param("roleId") Integer roleId
            , @Param("status") Integer status, @Param("lastActiveTime") String lastActiveTime
            , @Param("createTime") String createTime, @Param("lastActiveTimeSort") String lastActiveTimeSort
            , @Param("createTimeSort") String createTimeSort);

    /**
     * 根据用户id删除用户角色
     *
     * @param userId
     * @return
     */
    @Delete("" +
            "DELETE FROM `sys_user_role` " +
            "WHERE `user_id` = #{userId} ")
    int deleteUserRoleByUserId(@Param("userId") Integer userId);

}
