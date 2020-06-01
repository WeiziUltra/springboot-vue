package com.weiziplus.pc.core.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @date 2020/06/01 09/55
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("用户日志")
public class UserLogVo implements Serializable {
    /**
     */
    private Long id;

    /**
     * 用户表id
     */
    @ApiModelProperty("用户表id")
    private Integer userId;

    /**
     * 请求的路径
     */
    @ApiModelProperty("请求的路径")
    private String url;

    /**
     * 当前请求的参数
     */
    @ApiModelProperty("当前请求的参数")
    private String param;

    /**
     * 请求的类型,1:查询,2:新增,3:修改,4:删除
     */
    @ApiModelProperty("请求的类型,1:查询,2:新增,3:修改,4:删除")
    private Integer type;

    /**
     * 操作描述
     */
    @ApiModelProperty("操作描述")
    private String description;

    /**
     * ip地址
     */
    @ApiModelProperty("ip地址")
    private String ipAddress;

    /**
     * 浏览器名字
     */
    @ApiModelProperty("浏览器名字")
    private String borderName;

    /**
     * 操作系统名字
     */
    @ApiModelProperty("操作系统名字")
    private String osName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("用户名")
    private String username;

}
