package com.weiziplus.pc.core.system.mapper;

import com.weiziplus.pc.core.system.vo.SysUserLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/29 15/46
 */
@Mapper
public interface SysUserLogMapper {

    /**
     * 获取列表数据
     *
     * @param username
     * @param realName
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
    List<SysUserLogVo> getListVo(@Param("username") String username, @Param("realName") String realName
            , @Param("type") Integer type, @Param("description") String description, @Param("ipAddress") String ipAddress
            , @Param("borderName") String borderName, @Param("osName") String osName, @Param("startTime") String startTime
            , @Param("endTime") String endTime
            , @Param("createTimeSort") String createTimeSort);

}
