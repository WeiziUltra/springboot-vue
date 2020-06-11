package com.weiziplus.common.core.datadictionary;

import com.github.pagehelper.PageHelper;
import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.base.BaseWhere;
import com.weiziplus.common.models.DataDictionary;
import com.weiziplus.common.models.DataDictionaryValue;
import com.weiziplus.common.util.DateUtils;
import com.weiziplus.common.util.PageUtils;
import com.weiziplus.common.util.ResultUtils;
import com.weiziplus.common.util.ToolUtils;
import com.weiziplus.common.util.redis.RedisUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * @author wanglongwei
 * @date 2020/05/29 09/09
 */
@Slf4j
@Service("CommonDataDictionaryIpManagerService")
public class DataDictionaryIpManagerService extends BaseService {

    @Autowired
    DataDictionaryIpManagerMapper mapper;

    /**
     * redis的key
     */
    private static final String REDIS_KEY = createOnlyRedisKeyPrefix();

    /**
     * ip过滤规则
     */
    @Getter
    public enum IpFilterRole {

        /**
         * ip过滤规则
         */
        ALL("全部允许", "all"),
        WHITE("只允许白名单", "white"),
        BLACK("只禁止黑名单", "black");

        private String name;
        private String value;

        IpFilterRole(String name, String value) {
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
            for (IpFilterRole ipFilterRole : IpFilterRole.values()) {
                if (ipFilterRole.getValue().equals(value)) {
                    return true;
                }
            }
            return false;
        }

    }

    /**
     * 获取pc端ip过滤规则
     *
     * @return
     */
    public String getPcIpFilterRole() {
        String redisKey = createRedisKey(REDIS_KEY + "getPcIpFilterRole:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return String.valueOf(object);
        }
        DataDictionaryValue dataDictionaryValue = baseFindOneDataByClassAndColumnAndValue(
                DataDictionaryValue.class, DataDictionaryValue.COLUMN_DICTIONARY_CODE, DataDictionary.Code.PC_IP_FILTER_ROLE.getValue());
        if (null == dataDictionaryValue) {
            return IpFilterRole.ALL.getValue();
        }
        String value = dataDictionaryValue.getValue();
        RedisUtils.set(redisKey, value);
        return value;
    }

    /**
     * 获取web端ip过滤规则
     *
     * @return
     */
    public String getWebIpFilterRole() {
        String redisKey = createRedisKey(REDIS_KEY + "getWebIpFilterRole:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return String.valueOf(object);
        }
        DataDictionaryValue dataDictionaryValue = baseFindOneDataByClassAndColumnAndValue(
                DataDictionaryValue.class, DataDictionaryValue.COLUMN_DICTIONARY_CODE, DataDictionary.Code.WEB_IP_FILTER_ROLE.getValue());
        if (null == dataDictionaryValue) {
            return IpFilterRole.ALL.getValue();
        }
        String value = dataDictionaryValue.getValue();
        RedisUtils.set(redisKey, value);
        return value;
    }

    /**
     * 获取pc端ip过滤白名单
     *
     * @return
     */
    public Set<String> getPcIpFilterWhiteList() {
        String redisKey = createRedisKey(REDIS_KEY + "getPcIpFilterWhiteList:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> resultSet = new HashSet<>();
        List<DataDictionaryValue> dataDictionaryValueList = baseFindListByClassAndColumnAndValue(
                DataDictionaryValue.class, DataDictionaryValue.COLUMN_DICTIONARY_CODE, DataDictionary.Code.PC_IP_FILTER_WHITE_LIST.getValue());
        if (null == dataDictionaryValueList || 0 >= dataDictionaryValueList.size()) {
            return resultSet;
        }
        for (DataDictionaryValue dataDictionaryValue : dataDictionaryValueList) {
            resultSet.add(dataDictionaryValue.getValue());
        }
        RedisUtils.set(redisKey, resultSet);
        return resultSet;
    }

    /**
     * 获取web端ip过滤白名单
     *
     * @return
     */
    public Set<String> getWebIpFilterWhiteList() {
        String redisKey = createRedisKey(REDIS_KEY + "getWebIpFilterWhiteList:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> resultSet = new HashSet<>();
        List<DataDictionaryValue> dataDictionaryValueList = baseFindListByClassAndColumnAndValue(
                DataDictionaryValue.class, DataDictionaryValue.COLUMN_DICTIONARY_CODE, DataDictionary.Code.WEB_IP_FILTER_WHITE_LIST.getValue());
        if (null == dataDictionaryValueList || 0 >= dataDictionaryValueList.size()) {
            return resultSet;
        }
        for (DataDictionaryValue dataDictionaryValue : dataDictionaryValueList) {
            resultSet.add(dataDictionaryValue.getValue());
        }
        RedisUtils.set(redisKey, resultSet);
        return resultSet;
    }

    /**
     * 获取pc端ip过滤黑名单
     *
     * @return
     */
    public Set<String> getPcIpFilterBlackList() {
        String redisKey = createRedisKey(REDIS_KEY + "getPcIpFilterBlackList:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> resultSet = new HashSet<>();
        List<DataDictionaryValue> dataDictionaryValueList = baseFindListByClassAndColumnAndValue(
                DataDictionaryValue.class, DataDictionaryValue.COLUMN_DICTIONARY_CODE, DataDictionary.Code.PC_IP_FILTER_BLACK_LIST.getValue());
        if (null == dataDictionaryValueList || 0 >= dataDictionaryValueList.size()) {
            return resultSet;
        }
        for (DataDictionaryValue dataDictionaryValue : dataDictionaryValueList) {
            resultSet.add(dataDictionaryValue.getValue());
        }
        RedisUtils.set(redisKey, resultSet);
        return resultSet;
    }

    /**
     * 获取pc端ip过滤黑名单
     *
     * @return
     */
    public Set<String> getWebIpFilterBlackList() {
        String redisKey = createRedisKey(REDIS_KEY + "getWebIpFilterBlackList:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> resultSet = new HashSet<>();
        List<DataDictionaryValue> dataDictionaryValueList = baseFindListByClassAndColumnAndValue(
                DataDictionaryValue.class, DataDictionaryValue.COLUMN_DICTIONARY_CODE, DataDictionary.Code.WEB_IP_FILTER_BLACK_LIST.getValue());
        if (null == dataDictionaryValueList || 0 >= dataDictionaryValueList.size()) {
            return resultSet;
        }
        for (DataDictionaryValue dataDictionaryValue : dataDictionaryValueList) {
            resultSet.add(dataDictionaryValue.getValue());
        }
        RedisUtils.set(redisKey, resultSet);
        return resultSet;
    }

    /**
     * 处理pc端的异常ip
     *
     * @param ipAddress
     */
    public void handlePcAbnormalIp(String ipAddress) {
        if (ToolUtils.isBlank(ipAddress)) {
            return;
        }
        //根据ip获取一条pc端的数据
        DataDictionaryValue dictionaryValue = baseFindOneDataByClassAndBaseWhereList(DataDictionaryValue.class, new ArrayList<BaseWhere>() {{
            add(new BaseWhere(DataDictionaryValue.COLUMN_VALUE, BaseWhere.Where.EQUAL.getValue(), ipAddress));
            add(new BaseWhere(DataDictionaryValue.COLUMN_DICTIONARY_CODE, BaseWhere.Where.IN.getValue(),
                    new ArrayList<String>(ToolUtils.initialCapacity(3)) {{
                        add(DataDictionary.Code.PC_IP_FILTER_WHITE_LIST.getValue());
                        add(DataDictionary.Code.PC_IP_FILTER_BLACK_LIST.getValue());
                        add(DataDictionary.Code.PC_IP_FILTER_ABNORMAL_LIST.getValue());
                    }})
            );
        }});
        if (null == dictionaryValue) {
            dictionaryValue = new DataDictionaryValue()
                    .setDictionaryCode(DataDictionary.Code.PC_IP_FILTER_ABNORMAL_LIST.getValue())
                    .setValue(ipAddress)
                    .setNum(1)
                    .setName(ipAddress)
                    .setRemark("单位时间内访问频率过快的ip")
                    .setCreateTime(DateUtils.getNowDateTime());
            baseInsert(dictionaryValue);
            return;
        }
        //如果存在记录并且不是异常ip,跳过
        if (!DataDictionary.Code.PC_IP_FILTER_ABNORMAL_LIST.getValue().equals(dictionaryValue.getDictionaryCode())) {
            return;
        }
        Integer num = dictionaryValue.getNum();
        num++;
        dictionaryValue.setNum(num);
        baseUpdate(dictionaryValue);
    }

    /**
     * 处理web端的异常ip
     *
     * @param ipAddress
     */
    public void handleWebAbnormalIp(String ipAddress) {
        if (ToolUtils.isBlank(ipAddress)) {
            return;
        }
        //根据ip获取一条web端的数据
        DataDictionaryValue dictionaryValue = baseFindOneDataByClassAndBaseWhereList(DataDictionaryValue.class, new ArrayList<BaseWhere>() {{
            add(new BaseWhere(DataDictionaryValue.COLUMN_VALUE, BaseWhere.Where.EQUAL.getValue(), ipAddress));
            add(new BaseWhere(DataDictionaryValue.COLUMN_DICTIONARY_CODE, BaseWhere.Where.IN.getValue(),
                    new ArrayList<String>(ToolUtils.initialCapacity(3)) {{
                        add(DataDictionary.Code.WEB_IP_FILTER_WHITE_LIST.getValue());
                        add(DataDictionary.Code.WEB_IP_FILTER_BLACK_LIST.getValue());
                        add(DataDictionary.Code.WEB_IP_FILTER_ABNORMAL_LIST.getValue());
                    }})
            );
        }});
        if (null == dictionaryValue) {
            dictionaryValue = new DataDictionaryValue()
                    .setDictionaryCode(DataDictionary.Code.WEB_IP_FILTER_ABNORMAL_LIST.getValue())
                    .setValue(ipAddress)
                    .setNum(1)
                    .setName(ipAddress)
                    .setRemark("单位时间内访问频率过快的ip")
                    .setCreateTime(DateUtils.getNowDateTime());
            baseInsert(dictionaryValue);
            return;
        }
        //如果存在记录并且不是异常ip,跳过
        if (!DataDictionary.Code.WEB_IP_FILTER_ABNORMAL_LIST.getValue().equals(dictionaryValue.getDictionaryCode())) {
            return;
        }
        Integer num = dictionaryValue.getNum();
        num++;
        dictionaryValue.setNum(num);
        baseUpdate(dictionaryValue);
    }

    /**
     * 查看ip规则
     *
     * @param type
     * @return
     */
    public ResultUtils<String> getIpFilterRole(String type) {
        if (ToolUtils.isBlank(type)) {
            return ResultUtils.error("类型不能为空");
        }
        String role = "";
        switch (type.toUpperCase()) {
            case "PC": {
                role = getPcIpFilterRole();
            }
            break;
            case "WEB": {
                role = getWebIpFilterRole();
            }
            break;
            default: {
                return ResultUtils.error("类型错误");
            }
        }
        return ResultUtils.success(role);
    }

    /**
     * 更新ip过滤规则
     *
     * @param role
     * @param type
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultUtils updateIpFilterRole(String role, String type) {
        if (ToolUtils.isBlank(role)) {
            return ResultUtils.error("规则不能为空");
        }
        if (ToolUtils.isBlank(type)) {
            return ResultUtils.error("类型不能为空");
        }
        if (!IpFilterRole.contains(role)) {
            return ResultUtils.error("规则错误");
        }
        //ip过滤规则
        String ipFilterRole = "";
        DataDictionaryValue value = new DataDictionaryValue()
                .setValue(role)
                .setName(role)
                .setCreateTime(DateUtils.getNowDateTime());
        switch (type.toUpperCase()) {
            case "PC": {
                ipFilterRole = DataDictionary.Code.PC_IP_FILTER_ROLE.getValue();
                //如果设置为只允许白名单
                if (IpFilterRole.WHITE.getValue().equals(role)) {
                    DataDictionaryValue value1 = baseFindOneDataByClassAndColumnAndValue(
                            DataDictionaryValue.class, DataDictionaryValue.COLUMN_DICTIONARY_CODE, DataDictionary.Code.PC_IP_FILTER_WHITE_LIST.getValue());
                    if (null == value1) {
                        return ResultUtils.error("请先添加一个ip白名单");
                    }
                }
            }
            break;
            case "WEB": {
                ipFilterRole = DataDictionary.Code.WEB_IP_FILTER_ROLE.getValue();
                //如果设置为只允许白名单
                if (IpFilterRole.WHITE.getValue().equals(role)) {
                    DataDictionaryValue value1 = baseFindOneDataByClassAndColumnAndValue(
                            DataDictionaryValue.class, DataDictionaryValue.COLUMN_DICTIONARY_CODE, DataDictionary.Code.WEB_IP_FILTER_WHITE_LIST.getValue());
                    if (null == value1) {
                        return ResultUtils.error("请先添加一个ip白名单");
                    }
                }
            }
            break;
            default: {
                return ResultUtils.error("类型错误");
            }
        }
        value.setDictionaryCode(ipFilterRole);
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try {
            RedisUtils.setExpireDeleteLikeKey(REDIS_KEY);
            //删除原有的规则
            mapper.deleteDataDictionaryValueByCode(ipFilterRole);
            baseInsert(value);
            RedisUtils.deleteLikeKey(REDIS_KEY);
            return ResultUtils.success();
        } catch (Exception e) {
            log.warn("更新ip过滤规则出错，详情:" + e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return ResultUtils.error("系统错误，请重试。" + e);
        }
    }

    /**
     * 查看ip过滤名单
     *
     * @param pageNum
     * @param pageSize
     * @param terminal
     * @param search
     * @return
     */
    public ResultUtils<PageUtils<DataDictionaryValue>> getIpFilterList(Integer pageNum, Integer pageSize, String terminal, String search) {
        if (ToolUtils.isBlank(terminal)) {
            return ResultUtils.error("终端不能为空");
        }
        terminal = terminal.toUpperCase();
        String[] terminalArr = {"PC", "WEB"};
        if (!Arrays.asList(terminalArr).contains(terminal)) {
            return ResultUtils.error("终端错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<DataDictionaryValue> ipListByTerminal = mapper.getIpListByTerminal(terminal, search);
        PageUtils<DataDictionaryValue> pageUtil = PageUtils.pageInfo(ipListByTerminal);
        return ResultUtils.success(pageUtil);
    }

    /**
     * 新增ip过滤名单
     *
     * @param terminal
     * @param type
     * @param ipAddress
     * @param remark
     * @return
     */
    public ResultUtils addIpFilterList(String terminal, String type, String ipAddress, String remark) {
        if (ToolUtils.isBlank(terminal)) {
            return ResultUtils.error("终端不能为空");
        }
        if (ToolUtils.isBlank(type)) {
            return ResultUtils.error("类型不能为空");
        }
        if (ToolUtils.isBlank(ipAddress)) {
            return ResultUtils.error("ip地址不能为空");
        }
        switch (terminal.toUpperCase()) {
            case "PC": {
                if (!DataDictionary.Code.PC_IP_FILTER_WHITE_LIST.getValue().equals(type)
                        && !DataDictionary.Code.PC_IP_FILTER_BLACK_LIST.getValue().equals(type)
                        && !DataDictionary.Code.PC_IP_FILTER_ABNORMAL_LIST.getValue().equals(type)) {
                    return ResultUtils.error("类型错误");
                }
                //根据ip地址获取pc名单中的一条数据
                DataDictionaryValue onePcIpInfoByAddress = baseFindOneDataByClassAndBaseWhereList(DataDictionaryValue.class, new ArrayList<BaseWhere>() {{
                    add(new BaseWhere(DataDictionaryValue.COLUMN_VALUE, BaseWhere.Where.EQUAL.getValue(), ipAddress));
                    add(new BaseWhere(DataDictionaryValue.COLUMN_DICTIONARY_CODE, BaseWhere.Where.IN.getValue(),
                            new ArrayList<String>(ToolUtils.initialCapacity(3)) {{
                                add(DataDictionary.Code.PC_IP_FILTER_WHITE_LIST.getValue());
                                add(DataDictionary.Code.PC_IP_FILTER_BLACK_LIST.getValue());
                                add(DataDictionary.Code.PC_IP_FILTER_ABNORMAL_LIST.getValue());
                            }})
                    );
                }});
                if (null != onePcIpInfoByAddress) {
                    return ResultUtils.error("该ip地址已存在,code:" + onePcIpInfoByAddress.getDictionaryCode());
                }
            }
            break;
            case "WEB": {
                if (!DataDictionary.Code.WEB_IP_FILTER_WHITE_LIST.getValue().equals(type)
                        && !DataDictionary.Code.WEB_IP_FILTER_BLACK_LIST.getValue().equals(type)
                        && !DataDictionary.Code.WEB_IP_FILTER_ABNORMAL_LIST.getValue().equals(type)) {
                    return ResultUtils.error("类型错误");
                }
                //根据ip地址获取web名单中的一条数据
                DataDictionaryValue oneWebIpInfoByAddress = baseFindOneDataByClassAndBaseWhereList(DataDictionaryValue.class, new ArrayList<BaseWhere>() {{
                    add(new BaseWhere(DataDictionaryValue.COLUMN_VALUE, BaseWhere.Where.EQUAL.getValue(), ipAddress));
                    add(new BaseWhere(DataDictionaryValue.COLUMN_DICTIONARY_CODE, BaseWhere.Where.IN.getValue(),
                            new ArrayList<String>(ToolUtils.initialCapacity(3)) {{
                                add(DataDictionary.Code.WEB_IP_FILTER_WHITE_LIST.getValue());
                                add(DataDictionary.Code.WEB_IP_FILTER_BLACK_LIST.getValue());
                                add(DataDictionary.Code.WEB_IP_FILTER_ABNORMAL_LIST.getValue());
                            }})
                    );
                }});
                if (null != oneWebIpInfoByAddress) {
                    return ResultUtils.error("该ip地址已存在,code:" + oneWebIpInfoByAddress.getDictionaryCode());
                }
            }
            break;
            default: {
                return ResultUtils.error("终端错误");
            }
        }
        DataDictionaryValue value = new DataDictionaryValue()
                .setDictionaryCode(type)
                .setName(ipAddress)
                .setValue(ipAddress)
                .setRemark(remark)
                .setCreateTime(DateUtils.getNowDateTime());
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY);
        baseInsert(value);
        RedisUtils.deleteLikeKey(REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 删除ip过滤名单
     *
     * @param id
     * @return
     */
    public ResultUtils deleteIpFilterList(Integer id) {
        if (null == id || 0 > id) {
            return ResultUtils.error("id错误");
        }
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY);
        baseDeleteByClassAndId(DataDictionaryValue.class, id);
        RedisUtils.deleteLikeKey(REDIS_KEY);
        return ResultUtils.success();
    }

}
