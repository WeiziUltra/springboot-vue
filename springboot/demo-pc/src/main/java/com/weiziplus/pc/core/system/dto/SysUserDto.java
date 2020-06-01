package com.weiziplus.pc.core.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @date 2020/05/29 14/26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("系统用户")
public class SysUserDto implements Serializable {
    /**
     */
    private Integer id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("状态,1:正常,2:禁用")
    private Integer status;
}
