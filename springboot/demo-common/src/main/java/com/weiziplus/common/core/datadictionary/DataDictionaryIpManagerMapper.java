package com.weiziplus.common.core.datadictionary;

import com.weiziplus.common.models.DataDictionaryValue;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/05/29 09/43
 */
@Mapper
@Repository("CommonDataDictionaryIpManagerMapper")
public interface DataDictionaryIpManagerMapper {

    /**
     * 根据ip地址获取一个pc端的ip信息
     *
     * @param ipAddress
     * @return
     */
    @Select("" +
            "SELECT * " +
            "FROM `data_dictionary_value` " +
            "WHERE dictionary_code IN ('pcIpFilterWhiteList','pcIpFilterBlackList','pcIpFilterAbnormalList') AND `value` = #{ipAddress} " +
            "LIMIT 1")
    DataDictionaryValue getOnePcIpInfoByAddress(@Param("ipAddress") String ipAddress);

    /**
     * 根据ip地址获取一个web端的ip信息
     *
     * @param ipAddress
     * @return
     */
    @Select("" +
            "SELECT * " +
            "FROM `data_dictionary_value` " +
            "WHERE dictionary_code IN ('webIpFilterWhiteList','webIpFilterBlackList','webIpFilterAbnormalList') AND `value` = #{ipAddress} " +
            "LIMIT 1")
    DataDictionaryValue getOneWebIpInfoByAddress(@Param("ipAddress") String ipAddress);

    /**
     * 根据code删除字典值
     *
     * @param code
     * @return
     */
    @Delete("" +
            "DELETE FROM `data_dictionary_value` " +
            "WHERE `dictionary_code` = #{code} ")
    int deleteDataDictionaryValueByCode(@Param("code") String code);

    /**
     * 根据终端获取ip列表
     *
     * @param terminal
     * @param search
     * @return
     */
    List<DataDictionaryValue> getIpListByTerminal(@Param("terminal") String terminal,
                                                  @Param("search") String search);

}
