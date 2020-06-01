package com.weiziplus.common.models;

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
 * 数据字典表
 * data_dictionary
 *
 * @author 16028
 * @date 2020-05-29
 */
@BaseTable("data_dictionary")
@Alias("DataDictionary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("数据字典表")
public class DataDictionary implements Serializable {
    /**
     * 自增
     */
    @BaseId("id")
    @ApiModelProperty("自增")
    private Integer id;

    /**
     * 字典标识
     */
    @BaseColumn("code")
    @ApiModelProperty("字典标识")
    private String code;

    /**
     * 字典的code值
     */
    @Getter
    @Alias("DataDictionaryCode")
    public enum Code {

        /**
         * 字典的code值
         */
        PC_IP_FILTER_ROLE("pc端ip过滤规则", "pcIpFilterRole"),
        PC_IP_FILTER_WHITE_LIST("pc端ip过滤白名单", "pcIpFilterWhiteList"),
        PC_IP_FILTER_BLACK_LIST("pc端ip过滤黑名单", "pcIpFilterBlackList"),
        PC_IP_FILTER_ABNORMAL_LIST("pc端ip过滤异常名单", "pcIpFilterAbnormalList"),
        WEB_IP_FILTER_ROLE("web端ip过滤规则", "webIpFilterRole"),
        WEB_IP_FILTER_WHITE_LIST("web端ip过滤白名单", "webIpFilterWhiteList"),
        WEB_IP_FILTER_BLACK_LIST("web端ip过滤黑名单", "webIpFilterBlackList"),
        WEB_IP_FILTER_ABNORMAL_LIST("web端ip过滤异常名单", "webIpFilterAbnormalList");

        private String name;
        private String value;

        Code(String name, String value) {
            this.name = name;
            this.value = value;
        }

        /**
         * 是否存在
         *
         * @param value
         * @return
         */
        public static boolean contains(String value) {
            for (Code code : Code.values()) {
                if (code.getValue().equals(value)) {
                    return true;
                }
            }
            return false;
        }

    }

    /**
     * 名称
     */
    @BaseColumn("name")
    @ApiModelProperty("名称")
    private String name;

    /**
     * 字典备注
     */
    @BaseColumn("remark")
    @ApiModelProperty("字典备注")
    private String remark;

    /**
     * 字典创建时间
     */
    @BaseColumn("create_time")
    @ApiModelProperty("字典创建时间")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_CODE = "code";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_REMARK = "remark";

    public static final String COLUMN_CREATE_TIME = "create_time";
}