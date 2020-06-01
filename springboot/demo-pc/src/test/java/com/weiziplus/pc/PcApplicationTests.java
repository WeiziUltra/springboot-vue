package com.weiziplus.pc;

import com.alibaba.fastjson.JSON;
import com.weiziplus.common.base.BaseService;
import com.weiziplus.common.models.SysFunction;
import com.weiziplus.common.models.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class PcApplicationTests extends BaseService {

    @Test
    public void contextLoads() {
        List<SysFunction> sysFunctions = baseFindByClassAndIds(SysFunction.class, Arrays.asList(1, 2, 5));
        System.out.println(JSON.toJSONString(sysFunctions));
    }

}
