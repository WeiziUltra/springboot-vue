package com.weiziplus.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.common.base.BaseColumn;
import com.weiziplus.common.base.BaseId;
import com.weiziplus.common.base.BaseTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

/**
 * 系统功能菜单表
 * sys_function
 *
 * @author 16028
 * @date 2020-05-28
 */
@BaseTable("sys_function")
@Alias("SysFunction")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("系统功能菜单表")
public class SysFunction implements Serializable {
    /**
     */
    @BaseId("id")
    private Integer id;

    /**
     * 上级id
     */
    @BaseColumn("parent_id")
    @ApiModelProperty("上级id")
    private Integer parentId;

    /**
     * 功能名称，唯一标识
     */
    @BaseColumn("name")
    @ApiModelProperty("功能名称，唯一标识")
    private String name;

    /**
     * 功能路径
     */
    @BaseColumn("path")
    @ApiModelProperty("功能路径")
    private String path;

    /**
     * 功能标题
     */
    @BaseColumn("title")
    @ApiModelProperty("功能标题")
    private String title;

    /**
     * 当前功能对应的api列表，多个用,隔开
     */
    @BaseColumn("contain_api")
    @ApiModelProperty("当前功能对应的api列表，多个用,隔开")
    private String containApi;

    /**
     * 功能类型,1:菜单,2:按钮
     */
    @BaseColumn("type")
    @ApiModelProperty("功能类型,1:菜单,2:按钮")
    private Integer type;

    @Getter
    @Alias("SysFunctionType")
    public enum Type {

        /**
         * 功能类型
         */
        MENU("菜单", 1),
        BUTTON("按钮", 2);

        private String name;
        private Integer value;

        Type(String name, Integer value) {
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
            for (Type type : Type.values()) {
                if (type.getValue().equals(value)) {
                    return true;
                }
            }
            return false;
        }

    }

    /**
     * 功能图标
     */
    @BaseColumn("icon")
    @ApiModelProperty("功能图标")
    private String icon;

    /**
     * 功能排序，数字越小越靠前
     */
    @BaseColumn("sort")
    @ApiModelProperty("功能排序，数字越小越靠前")
    private Integer sort;

    /**
     * 备注
     */
    @BaseColumn("remark")
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建时间
     */
    @BaseColumn("create_time")
    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("子级功能列表")
    private List<SysFunction> children;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_PARENT_ID = "parent_id";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_PATH = "path";

    public static final String COLUMN_TITLE = "title";

    public static final String COLUMN_CONTAIN_API = "contain_api";

    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_ICON = "icon";

    public static final String COLUMN_SORT = "sort";

    public static final String COLUMN_REMARK = "remark";

    public static final String COLUMN_CREATE_TIME = "create_time";
}