package com.weiziplus.pc.core.system.mapper;

import com.weiziplus.common.models.SysFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/28 11/38
 */
@Mapper
public interface SysFunctionMapper {

    /**
     * 根据roleId列表获取拥有的功能列表
     *
     * @param roleIdList
     * @param type       不传查询全部
     * @return
     */
    List<SysFunction> getFunctionListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList, @Param("type") Integer type);

    /**
     * 获取列表
     *
     * @return
     */
    List<SysFunction> getList();

    /**
     * 获取最小的上级id
     *
     * @return
     */
    @Select("" +
            "SELECT MIN(parent_id) " +
            "FROM `sys_function` ")
    Integer getMinParentId();

    /**
     * 根据上级id获取功能列表
     *
     * @param parentId
     * @return
     */
    @Select("" +
            "SELECT * " +
            "FROM `sys_function` " +
            "WHERE `parent_id` = #{parentId} " +
            "ORDER BY `sort` ASC,`create_time` DESC ")
    List<SysFunction> getFunctionListByParentId(@Param("parentId") Integer parentId);

    /**
     * 获取所有功能列表
     *
     * @return
     */
    @Select("" +
            "SELECT * " +
            "FROM `sys_function` " +
            "ORDER BY `sort` ASC,`create_time` DESC ")
    List<SysFunction> getAllFunctionList();

    /**
     * 获取所有功能菜单列表
     *
     * @return
     */
    @Select("" +
            "SELECT * " +
            "FROM `sys_function` " +
            "WHERE `type` = 1 " +
            "ORDER BY `sort` ASC,`create_time` DESC ")
    List<SysFunction> getAllMenuList();

}