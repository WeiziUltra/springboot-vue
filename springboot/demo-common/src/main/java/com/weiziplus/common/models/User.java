package com.weiziplus.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.common.base.BaseColumn;
import com.weiziplus.common.base.BaseId;
import com.weiziplus.common.base.BaseTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

/**
 * web用户表
 * t_user
 *
 * @author 16028
 * @date 2020-06-01
 */
@BaseTable("t_user")
@Alias("TUser")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("web用户表")
public class User implements Serializable {
    /**
     * 用户表主键，自增
     */
    @BaseId("id")
    @ApiModelProperty("用户表主键，自增")
    private Long id;

    /**
     * 用户名
     */
    @BaseColumn("username")
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    @BaseColumn("password")
    @ApiModelProperty("密码")
    private String password;

    /**
     * 状态,1:正常,2:停用
     */
    @BaseColumn("status")
    @ApiModelProperty("状态,1:正常,2:停用")
    private Integer status;

    @Getter
    @Alias("TUserStatus")
    public enum Status {
        /**
         * 状态
         */
        NORMAL("正常", 1),
        DISABLE("停用", 2);

        private String name;
        private Integer value;

        Status(String name, Integer value) {
            this.name = name;
            this.value = value;
        }

        /**
         * 是否存在
         *
         * @param value
         * @return
         */
        public static boolean contains(Integer value) {
            for (Status status : Status.values()) {
                if (status.getValue().equals(value)) {
                    return true;
                }
            }
            return false;
        }

    }

    /**
     * 用户最后活跃ip地址
     */
    @BaseColumn("last_ip_address")
    @ApiModelProperty("用户最后活跃ip地址")
    private String lastIpAddress;

    /**
     * 用户最后活跃时间
     */
    @BaseColumn("last_active_time")
    @ApiModelProperty("用户最后活跃时间")
    private String lastActiveTime;

    /**
     * 用户创建时间
     */
    @BaseColumn("create_time")
    @ApiModelProperty("用户创建时间")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_USERNAME = "username";

    public static final String COLUMN_PASSWORD = "password";

    public static final String COLUMN_STATUS = "status";

    public static final String COLUMN_LAST_IP_ADDRESS = "last_ip_address";

    public static final String COLUMN_LAST_ACTIVE_TIME = "last_active_time";

    public static final String COLUMN_CREATE_TIME = "create_time";
}