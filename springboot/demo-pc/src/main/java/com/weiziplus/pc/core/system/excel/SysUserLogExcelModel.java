package com.weiziplus.pc.core.system.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wanglongwei
 * @date 2020/07/23 14/18
 */
@Data
@Accessors(chain = true)
@ColumnWidth(20)
public class SysUserLogExcelModel {

    @ExcelProperty(value = "请求路径", index = 0)
    private String url;

    @ExcelProperty(value = "请求参数", index = 1)
    private String param;

    @ColumnWidth(7)
    @ExcelProperty(value = "请求类型", index = 2, converter = SysUserLogExcelModelTypeConverter.class)
    private Integer type;

    @ExcelProperty(value = "操作描述", index = 3)
    private String description;

    @ExcelProperty(value = "浏览器", index = 4)
    private String borderName;

    @ExcelProperty(value = "操作系统", index = 5)
    private String osName;

    @ExcelProperty(value = "创建时间", index = 6)
    private String createTime;

}