package com.weiziplus.pc.core.system.mapper;

import com.weiziplus.common.models.SysError;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/29 15/51
 */
@Mapper
public interface SysErrorMapper {

    /**
     * 获取列表数据
     *
     * @param search
     * @param createTimeSort
     * @return
     */
    List<SysError> getList(@Param("search") String search, @Param("createTimeSort") String createTimeSort);

}
