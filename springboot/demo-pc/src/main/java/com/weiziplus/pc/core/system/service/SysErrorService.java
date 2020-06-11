package com.weiziplus.pc.core.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.models.SysError;
import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.pc.core.system.mapper.SysErrorMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wanglongwei
 * @date 2020/05/29 15/51
 */
@Slf4j
@Service
public class SysErrorService extends BaseService {

    @Autowired
    SysErrorMapper mapper;

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @param createTimeSort
     * @return
     */
    public ResultUtils<PageUtils<SysError>> getPageList(Integer pageNum, Integer pageSize, String search, String createTimeSort) {
        if (!OrderByType.contains(createTimeSort)) {
            return ResultUtils.error("排序字段错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<SysError> pageUtil = PageUtils.pageInfo(mapper.getList(
                search, createTimeSort));
        return ResultUtils.success(pageUtil);
    }

}
