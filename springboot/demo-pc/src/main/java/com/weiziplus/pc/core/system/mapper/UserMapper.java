package com.weiziplus.pc.core.system.mapper;

import com.weiziplus.common.models.SysUserLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/06/01 09/57
 */
@Mapper
public interface UserMapper {

    /**
     * 获取列表数据
     *
     * @param username
     * @param type
     * @param description
     * @param ipAddress
     * @param borderName
     * @param osName
     * @param startTime
     * @param endTime
     * @param createTimeSort
     * @return
     */
    List<SysUserLog> getListVo(@Param("username") String username
            , @Param("type") Integer type, @Param("description") String description, @Param("ipAddress") String ipAddress
            , @Param("borderName") String borderName, @Param("osName") String osName, @Param("startTime") String startTime
            , @Param("endTime") String endTime
            , @Param("createTimeSort") String createTimeSort);

}
