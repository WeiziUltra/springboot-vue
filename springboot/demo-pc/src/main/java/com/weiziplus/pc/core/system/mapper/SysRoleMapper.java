package com.weiziplus.pc.core.system.mapper;

import com.weiziplus.common.models.SysRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/28 11/38
 */
@Mapper
public interface SysRoleMapper {

    /**
     * 获取列表数据
     *
     * @param search
     * @return
     */
    List<SysRole> getList(@Param("search") String search);

    /**
     * 根据角色id删除角色功能
     *
     * @param roleId
     * @return
     */
    @Delete("" +
            "DELETE FROM `sys_role_function` " +
            "WHERE `role_id` = #{roleId} ")
    int deleteRoleFunctionByRoleId(@Param("roleId") Integer roleId);

}
