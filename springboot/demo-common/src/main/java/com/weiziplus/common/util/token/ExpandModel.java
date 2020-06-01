package com.weiziplus.common.util.token;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * JWT中存放的自定义内容
 *
 * @author wanglongwei
 * @date 2020/05/27 15/45
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpandModel {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 系统用户角色列表
     */
    private List<Integer> roleIdList;

}
