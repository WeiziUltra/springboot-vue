/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : muteki

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 01/06/2020 14:43:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary`;
CREATE TABLE `data_dictionary`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典标识',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '字典创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_dictionary
-- ----------------------------
INSERT INTO `data_dictionary` VALUES (1, 'pcIpFilterRole', 'pc端ip过滤规则', 'ip过滤规则，全部允许(all)，只允许白名单(white)，只限制黑名单(black)', '2020-02-24 20:00:35');
INSERT INTO `data_dictionary` VALUES (2, 'pcIpFilterWhiteList', 'pc端ip过滤白名单', 'ip白名单', '2020-02-24 20:46:10');
INSERT INTO `data_dictionary` VALUES (3, 'pcIpFilterBlackList', 'pc端ip过滤黑名单', 'ip黑名单', '2020-02-24 21:15:06');
INSERT INTO `data_dictionary` VALUES (4, 'pcIpFilterAbnormalList', 'pc端ip过滤异常名单', 'ip异常名单', '2020-02-24 21:15:29');
INSERT INTO `data_dictionary` VALUES (5, 'webIpFilterRole', 'web端ip过滤规则', 'ip过滤规则，全部允许(all)，只允许白名单(white)，只限制黑名单(black)', '2020-06-01 08:34:33');
INSERT INTO `data_dictionary` VALUES (6, 'webIpFilterWhiteList', 'web端ip过滤白名单', 'ip白名单', '2020-02-24 20:46:10');
INSERT INTO `data_dictionary` VALUES (7, 'webIpFilterBlackList', 'web端ip过滤黑名单', 'ip黑名单', '2020-02-24 21:15:06');
INSERT INTO `data_dictionary` VALUES (8, 'webIpFilterAbnormalList', 'web端ip过滤异常名单', 'ip异常名单', '2020-02-24 21:15:29');

-- ----------------------------
-- Table structure for data_dictionary_value
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary_value`;
CREATE TABLE `data_dictionary_value`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `dictionary_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典编号',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '值',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `num` int(3) NOT NULL DEFAULT 0 COMMENT '可以作为排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dictionary_code`(`dictionary_code`) USING BTREE,
  CONSTRAINT `data_dictionary_value_ibfk_1` FOREIGN KEY (`dictionary_code`) REFERENCES `data_dictionary` (`code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_error
-- ----------------------------
DROP TABLE IF EXISTS `sys_error`;
CREATE TABLE `sys_error`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类名',
  `method_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '方法名',
  `line_number` int(5) NOT NULL DEFAULT 0 COMMENT '第几行',
  `content` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '详情',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统异常表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_function`;
CREATE TABLE `sys_function`  (
  `id` int(7) NOT NULL AUTO_INCREMENT COMMENT '系统功能表主键，自增',
  `parent_id` int(7) NOT NULL DEFAULT 0 COMMENT '上级id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '功能唯一标识',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '功能路径',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '功能标题',
  `contain_api` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '当前功能对应的api列表，多个用,隔开',
  `type` tinyint(2) NOT NULL DEFAULT 0 COMMENT '功能类型;1:菜单,2:按钮',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'el-icon-info' COMMENT '功能图标',
  `sort` tinyint(2) NOT NULL DEFAULT 77 COMMENT '功能排序，数字越小越靠前',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '功能描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '功能创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE COMMENT '功能名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_function
-- ----------------------------
INSERT INTO `sys_function` VALUES (1, 0, 'system', 'system', '系统管理', '', 1, 'el-icon-s-tools', 88, '后台的管理', '2019-05-09 16:55:47');
INSERT INTO `sys_function` VALUES (2, 1, 'sysFunction', 'sysFunction', '功能管理', '/sysFunction/getPageList', 1, 'el-icon-s-tools', 9, '管理后台的菜单、按钮等。对应api可以将权限精确到接口', '2019-05-09 16:56:10');
INSERT INTO `sys_function` VALUES (3, 1, 'sysRole', 'sysRole', '角色管理', '/pc/sysRole/getAllRoleTreePageList,/pc/sysRole/getRoleList,/pc/sysFunction/getAllFunctionTree', 1, 'el-icon-user-solid\r\n', 2, '管理后台系统的各项角色，以及角色所拥有的功能等', '2019-05-09 16:56:26');
INSERT INTO `sys_function` VALUES (4, 1, 'sysUser', 'sysUser', '用户管理', '', 1, 'el-icon-user', 1, '管理后台的用户', '2019-05-09 16:56:52');
INSERT INTO `sys_function` VALUES (5, 1, 'sysUserLog', 'sysUserLog', '系统日志管理', '/pc/sysUserLog/getPageList,/pc/sysRole/getRoleList', 1, 'el-icon-s-order', 4, '记录系统用户的操作', '2019-05-13 15:30:57');
INSERT INTO `sys_function` VALUES (6, 3, 'sysRole_add', 'add', '新增', '/sysRole/add', 2, 'el-icon-info', 0, '', '2019-05-10 10:23:08');
INSERT INTO `sys_function` VALUES (7, 3, 'sysRole_update', 'update', '修改', '/sysRole/update', 2, 'el-icon-info', 0, '', '2019-05-10 10:23:21');
INSERT INTO `sys_function` VALUES (8, 3, 'sysRole_delete', 'delete', '删除', '/sysRole/delete', 2, 'el-icon-info', 0, '', '2019-05-10 10:23:53');
INSERT INTO `sys_function` VALUES (9, 3, 'sysRole_save', 'save', '修改功能', '/pc/sysRole/addRoleFun', 2, 'el-icon-info', 0, '', '2019-05-10 10:24:07');
INSERT INTO `sys_function` VALUES (10, 3, 'sysRole_status', 'status', '修改角色状态(启用/禁用)', '/sysRole/updateStatus', 2, '', 4, '', '2019-05-10 15:58:15');
INSERT INTO `sys_function` VALUES (11, 4, 'sysUser_add', 'add', '新增', '/sysUser/add', 2, 'el-icon-info', 0, '', '2019-05-10 10:24:43');
INSERT INTO `sys_function` VALUES (12, 4, 'sysUser_delete', 'delete', '删除', '/sysUser/delete', 2, 'el-icon-info', 0, '', '2019-05-10 10:25:08');
INSERT INTO `sys_function` VALUES (13, 4, 'sysUser_role', 'role', '修改角色', '/sysUser/updateRole', 2, 'el-icon-info', 0, '', '2019-05-10 10:25:43');
INSERT INTO `sys_function` VALUES (14, 4, 'sysUser_resetPwd', 'resetPwd', '重置密码', '/sysUser/resetPwd', 2, 'el-icon-info', 0, '', '2019-05-10 10:25:52');
INSERT INTO `sys_function` VALUES (15, 0, 'tools', 'tools', '常用工具', '', 1, 'el-icon-s-promotion', 99, '进一步封装element-ui的常用组件，约定大于配置思想', '2019-08-24 16:41:28');
INSERT INTO `sys_function` VALUES (16, 15, 'toolsUpload', 'upload', '图片上传', '', 1, 'el-icon-picture', 0, '进一步封装了图片上传，方便调用', '2019-08-24 16:41:49');
INSERT INTO `sys_function` VALUES (17, 15, 'richText', 'richText', '富文本', '', 1, 'el-icon-s-release', 1, '进一步封装了富文本，方便调用', '2019-09-04 16:21:09');
INSERT INTO `sys_function` VALUES (18, 1, 'ipManager', 'ipManager', 'ip管理', '', 1, '', 3, '对ip进行管理', '2020-02-24 20:04:42');
INSERT INTO `sys_function` VALUES (19, 18, 'ipManager_get', 'ipManager_get', '查看', '/ipFilter/getIpFilterList,/ipFilter/getIpFilterRole', 2, '', 0, '', '2020-02-25 15:50:24');
INSERT INTO `sys_function` VALUES (20, 1, 'userLog', 'userLog', '用户日志', '/userLog/getPageList', 1, 'el-icon-s-order', 5, '用户日志', '2020-02-28 20:44:20');
INSERT INTO `sys_function` VALUES (21, 1, 'sysError', 'sysError', '系统异常', '/sysError/getPageList', 1, 'el-icon-s-release', 7, '系统异常', '2020-02-28 20:44:20');
INSERT INTO `sys_function` VALUES (22, 4, 'sysUser_updatePhone', 'sysUser_updatePhone', '修改手机号', '/sysUser/updatePhone', 2, 'el-icon-info', 77, '', '2020-06-01 11:41:41');
INSERT INTO `sys_function` VALUES (23, 4, 'sysUser_get', 'sysUser_get', '查看', '/sysUser/getPageList,/sysRole/getList', 2, 'el-icon-info', 77, '', '2020-06-01 11:43:09');
INSERT INTO `sys_function` VALUES (24, 4, 'sysUser_updateStatus', 'sysUser_updateStatus', '改变状态', '/sysUser/updateStatus', 2, 'el-icon-info', 77, '', '2020-06-01 11:49:47');
INSERT INTO `sys_function` VALUES (25, 3, 'sysRole_get', 'sysRole_get', '查看', '/sysRole/getPageList,/sysFunction/getTree,/sysRole/getFunctionList', 2, 'el-icon-info', 77, '', '2020-06-01 14:10:22');
INSERT INTO `sys_function` VALUES (26, 18, 'ipManager_add', 'ipManager_add', '新增', '/ipFilter/addIpFilterList', 2, 'el-icon-info', 77, '', '2020-06-01 14:18:41');
INSERT INTO `sys_function` VALUES (27, 18, 'ipManager_delete', 'ipManager_delete', '删除', '/ipFilter/deleteIpFilterList', 2, 'el-icon-info', 77, '', '2020-06-01 14:19:11');
INSERT INTO `sys_function` VALUES (28, 18, 'ipManager_updateRole', 'ipManager_updateRole', '更新ip规则', '/ipFilter/updateIpFilterRole', 2, 'el-icon-info', 77, '', '2020-06-01 14:20:00');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '状态,1:正常,2:禁用',
  `sort` tinyint(2) NOT NULL DEFAULT 77 COMMENT '排序，数字越小越靠前',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '角色创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 1, 0, '超级管理员，拥有至高无上的权力', '2020-05-30 16:28:33');
INSERT INTO `sys_role` VALUES (101, '管理员', 1, 0, '管理员', '2020-05-30 16:28:35');
INSERT INTO `sys_role` VALUES (102, '测试', 1, 77, '测试', '2020-06-01 14:38:23');

-- ----------------------------
-- Table structure for sys_role_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_function`;
CREATE TABLE `sys_role_function`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色表id',
  `function_id` int(11) NOT NULL COMMENT '功能表id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `function_id`(`function_id`) USING BTREE,
  CONSTRAINT `sys_role_function_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_role_function_ibfk_2` FOREIGN KEY (`function_id`) REFERENCES `sys_function` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 280 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_function
-- ----------------------------
INSERT INTO `sys_role_function` VALUES (238, 102, 4);
INSERT INTO `sys_role_function` VALUES (239, 102, 14);
INSERT INTO `sys_role_function` VALUES (240, 102, 13);
INSERT INTO `sys_role_function` VALUES (241, 102, 12);
INSERT INTO `sys_role_function` VALUES (242, 102, 11);
INSERT INTO `sys_role_function` VALUES (243, 102, 24);
INSERT INTO `sys_role_function` VALUES (244, 102, 23);
INSERT INTO `sys_role_function` VALUES (245, 102, 22);
INSERT INTO `sys_role_function` VALUES (246, 102, 3);
INSERT INTO `sys_role_function` VALUES (247, 102, 9);
INSERT INTO `sys_role_function` VALUES (248, 102, 8);
INSERT INTO `sys_role_function` VALUES (249, 102, 7);
INSERT INTO `sys_role_function` VALUES (250, 102, 6);
INSERT INTO `sys_role_function` VALUES (251, 102, 10);
INSERT INTO `sys_role_function` VALUES (252, 102, 25);
INSERT INTO `sys_role_function` VALUES (253, 102, 19);
INSERT INTO `sys_role_function` VALUES (254, 102, 1);
INSERT INTO `sys_role_function` VALUES (255, 102, 18);
INSERT INTO `sys_role_function` VALUES (256, 101, 1);
INSERT INTO `sys_role_function` VALUES (257, 101, 4);
INSERT INTO `sys_role_function` VALUES (258, 101, 14);
INSERT INTO `sys_role_function` VALUES (259, 101, 13);
INSERT INTO `sys_role_function` VALUES (260, 101, 12);
INSERT INTO `sys_role_function` VALUES (261, 101, 11);
INSERT INTO `sys_role_function` VALUES (262, 101, 24);
INSERT INTO `sys_role_function` VALUES (263, 101, 23);
INSERT INTO `sys_role_function` VALUES (264, 101, 22);
INSERT INTO `sys_role_function` VALUES (265, 101, 3);
INSERT INTO `sys_role_function` VALUES (266, 101, 9);
INSERT INTO `sys_role_function` VALUES (267, 101, 8);
INSERT INTO `sys_role_function` VALUES (268, 101, 7);
INSERT INTO `sys_role_function` VALUES (269, 101, 6);
INSERT INTO `sys_role_function` VALUES (270, 101, 10);
INSERT INTO `sys_role_function` VALUES (271, 101, 25);
INSERT INTO `sys_role_function` VALUES (272, 101, 18);
INSERT INTO `sys_role_function` VALUES (273, 101, 19);
INSERT INTO `sys_role_function` VALUES (274, 101, 28);
INSERT INTO `sys_role_function` VALUES (275, 101, 27);
INSERT INTO `sys_role_function` VALUES (276, 101, 26);
INSERT INTO `sys_role_function` VALUES (277, 101, 5);
INSERT INTO `sys_role_function` VALUES (278, 101, 20);
INSERT INTO `sys_role_function` VALUES (279, 101, 21);
INSERT INTO `sys_role_function` VALUES (280, 101, 2);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `real_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '状态,1:正常,2:禁用',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '/logo.png' COMMENT '用户头像',
  `last_ip_address` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户最后活跃ip地址',
  `last_active_time` datetime(0) NULL DEFAULT NULL COMMENT '用户最后活跃时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'superadmin', 'E07C3D1032C1C13CD618602EFA1E4903', 'superadmin', '18888888888', 1, '/user/icon/20200530/BCA8E6EB93B6477DA4DFF93F5C2032C0.jpg', '127.0.0.1', '2020-05-31 18:22:58', '2020-06-01 14:42:12');
INSERT INTO `sys_user` VALUES (101, 'weiziplus', 'E07C3D1032C1C13CD618602EFA1E4903', 'weiziplus', '18888888888', 1, '/logo.png', '', NULL, '2020-06-01 11:40:01');

-- ----------------------------
-- Table structure for sys_user_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_log`;
CREATE TABLE `sys_user_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL DEFAULT 0 COMMENT '用户表id',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求的路径',
  `param` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '当前请求的参数',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '请求的类型,1:查询,2:新增,3:修改,4:删除',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作描述',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `border_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浏览器名字',
  `os_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作系统名字',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `sys_user_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户表主键',
  `role_id` int(11) NOT NULL COMMENT '角色表主键',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (12, 101, 101);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户表主键，自增',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '状态,1:正常,2:停用',
  `last_ip_address` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户最后活跃ip地址',
  `last_active_time` datetime(0) NULL DEFAULT NULL COMMENT '用户最后活跃时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'web用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'weiziplus', 'B7966B0719AF24AD7CBE954DDFB32DC0', 1, '127.0.0.1', '2020-06-01 09:42:12', '2020-06-01 09:34:31');

-- ----------------------------
-- Table structure for t_user_log
-- ----------------------------
DROP TABLE IF EXISTS `t_user_log`;
CREATE TABLE `t_user_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户表主键',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求的路径',
  `param` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '当前请求的参数',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '请求的类型,1:查询,2:新增,3:修改,4:删除',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作描述',
  `ip_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip地址',
  `border_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '浏览器名字',
  `os_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作系统名字',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'web用户日志' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
