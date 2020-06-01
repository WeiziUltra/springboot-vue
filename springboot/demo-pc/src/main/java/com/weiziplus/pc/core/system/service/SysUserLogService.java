package com.weiziplus.pc.core.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.pc.core.system.mapper.SysUserLogMapper;
import com.weiziplus.pc.core.system.vo.SysUserLogVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/29 15/41
 */
@Slf4j
@Service
public class SysUserLogService extends BaseService {

    @Autowired
    SysUserLogMapper mapper;

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
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
    public ResultUtils<PageUtils<List<SysUserLogVo>>> getPageList(Integer pageNum, Integer pageSize
            , String username, String realName, Integer type, String description, String ipAddress
            , String borderName, String osName, String startTime, String endTime
            , String createTimeSort) {
        if (!OrderByType.contains(createTimeSort)) {
            return ResultUtils.error("排序字段错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<List<SysUserLogVo>> pageUtil = PageUtils.pageInfo(mapper.getListVo(
                username, realName, type, description, ipAddress, borderName, osName, startTime, endTime,
                createTimeSort));
        return ResultUtils.success(pageUtil);
    }

}
