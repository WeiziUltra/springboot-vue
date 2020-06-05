/**
 * 全局变量
 * @type {{}}
 */
const GLOBAL = {
    //请求前缀
    base_url: process.env.VUE_APP_URL,
    //超级管理员id
    super_admin_id: 1,
    //超级管理员roleId
    super_admin_role_id: 1,
    //请求头token名称
    token: 'Authorization',
    //后台状态码
    axios_result_code: {
        //成功
        success: 200,
        //token异常
        errorToken: 401,
        //error
        error: 402,
        //没有权限,拒绝访问
        errorRole: 403,
        //后台系统异常
        errorException: 500
    }
};

/**
 * 请求url
 * @type {{}}
 */
const URL = {
    /*登录 */
    login: '/login',
    /*登录验证码*/
    loginValidateCode: '/getValidateCode',
    /*退出登录*/
    logout: '/logout',
    /*************系统管理*****************/
    system: {
        /*********************用户管理*****************/
        sysUser: {
            /*获取分页数据*/
            getPageList: '/sysUser/getPageList',
            /*修改头像*/
            updateIcon: '/sysUser/updateIcon',
            /*修改密码*/
            updatePwd: '/sysUser/updatePwd',
            /*修改状态*/
            updateStatus: '/sysUser/updateStatus',
            /*修改手机号码*/
            updatePhone: '/sysUser/updatePhone',
            /*修改角色*/
            updateRole: '/sysUser/updateRole',
            /*重置密码*/
            resetPwd: '/sysUser/resetPwd',
            /*新增*/
            add: '/sysUser/add',
            /*删除*/
            delete: '/sysUser/delete',
        },
        /*********************角色管理*****************/
        sysRole: {
            /*获取分页数据*/
            getPageList: '/sysRole/getPageList',
            /*获取列表*/
            getList: '/sysRole/getList',
            /*获取角色拥有的功能列表*/
            getFunctionList: '/sysRole/getFunctionList',
            /*修改角色功能*/
            updateRoleFunction: '/sysRole/updateRoleFunction',
            /*修改状态*/
            updateStatus: '/sysRole/updateStatus',
            /*新增*/
            add: '/sysRole/add',
            /*修改*/
            update: '/sysRole/update',
            /*删除*/
            delete: '/sysRole/delete',
        },
        /*********************功能管理*****************/
        sysFunction: {
            /*获取分页数据*/
            getPageList: '/sysFunction/getPageList',
            /*获取树形结构*/
            getTree: '/sysFunction/getTree',
            /*新增*/
            add: '/sysFunction/add',
            /*修改*/
            update: '/sysFunction/update',
            /*删除*/
            delete: '/sysFunction/delete',
        },
        /*********************系统用户日志*****************/
        sysUserLog: {
            /*获取分页数据*/
            getPageList: '/sysUserLog/getPageList',
            /*导出excel*/
            exportExcel: '/sysUserLog/exportExcel'
        },
        /*********************系统异常*****************/
        sysError: {
            /*获取分页数据*/
            getPageList: '/sysError/getPageList'
        },
        /*********************用户日志*****************/
        userLog: {
            /*获取分页数据*/
            getPageList: '/userLog/getPageList'
        },
        /*********************系统文件*****************/
        sysFile: {
            /*获取日志文件*/
            getLogFile: '/sysFile/getLogFile',
            /*下载日志文件*/
            downLogFile: '/sysFile/downLogFile',
        },
    },
    /*************字典管理*****************/
    dataDictionary: {
        /*********************ip管理*****************/
        ipManager: {
            /*查看ip过滤名单*/
            getIpFilterList: '/ipFilter/getIpFilterList',
            /*查看ip过滤规则*/
            getIpFilterRole: '/ipFilter/getIpFilterRole',
            /*更新ip过滤规则*/
            updateIpFilterRole: '/ipFilter/updateIpFilterRole',
            /*新增ip过滤名单*/
            addIpFilterList: '/ipFilter/addIpFilterList',
            /*删除ip过滤名单*/
            deleteIpFilterList: '/ipFilter/deleteIpFilterList',
        },
    },
    /********常用工具*****************/
    tools: {
        /*文件上传*/
        upload: '/tools/upload',
        /*文件上传*/
        downTemp: '/tools/downTemp'
    },
};

export default {
    GLOBAL, URL
}