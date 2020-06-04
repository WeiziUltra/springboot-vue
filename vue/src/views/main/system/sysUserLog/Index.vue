<template>
    <div id="index">
        <template class="wei-table">
            <wei-table ref="table"
                       :tableDataRequest="tableDataRequest"
                       :tableColumns="tableColumns"
                       :tableHeaderButtons="tableHeaderButtons"
                       :tableOperates="tableOperates"
                       :tableSearch="tableSearch"></wei-table>
        </template>
        <template>
            <el-dialog title="操作详情"
                       :visible.sync="dialogDetail">
                <detail :formData="formData"></detail>
            </el-dialog>
            <el-dialog title="导出日志"
                       :visible.sync="dialogExportExcel">
                <el-form :inline="true">
                    <el-form-item label="开始时间">
                        <el-date-picker type="datetime" placeholder="开始时间"
                                        :value-format="'yyyy-MM-dd HH:mm:ss'"
                                        v-model="startTime">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="截止时间">
                        <el-date-picker type="datetime" placeholder="截止时间"
                                        :value-format="'yyyy-MM-dd HH:mm:ss'"
                                        v-model="endTime">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="exportExcel">导出</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>
        </template>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Complex.vue'),
            'detail': () => import('./Detail.vue')
        },
        data() {
            let that = this;
            let roleIdList = this.$globalFun.getSessionStorage('roleIdList');
            let {id} = this.$globalFun.getSessionStorage('userInfo');
            let {
                super_admin_id, super_admin_role_id
            } = this.$global.GLOBAL;
            //如果是超级管理员
            let superAdminFlag = roleIdList.includes(super_admin_role_id)
                || id === super_admin_id;
            return {
                tableDataRequest: {
                    url: that.$global.URL.system.sysUserLog.getPageList,
                    data: {}
                },
                tableColumns: [
                    {label: '用户名', prop: 'username', fixed: 'left'},
                    {label: '真实姓名', prop: 'realName', fixed: 'left'},
                    {label: '请求路径', prop: 'url'},
                    {
                        label: '类型', type: 'tag',
                        element({type}) {
                            let result = [
                                null,
                                {content: '查询'},
                                {content: '新增', type: 'success'},
                                {content: '修改', type: 'warning'},
                                {content: '删除', type: 'danger'},
                            ];
                            return result[type] || {
                                content: '未知类型',
                                type: 'danger'
                            }
                        }
                    },
                    {label: '参数', prop: 'param'},
                    {label: '操作', prop: 'description'},
                    {label: 'ip地址', prop: 'ipAddress'},
                    {label: '浏览器', prop: 'borderName'},
                    {label: '操作系统', prop: 'osName'},
                    {
                        label: '创建时间',
                        prop: 'createTime',
                        width: 120,
                        sortable: 'custom',
                        type: 'icon',
                        element(row) {
                            return {
                                leftIcon: 'el-icon-time',
                                content: row['createTime']
                            };
                        }
                    }
                ],
                //表格上面按钮
                tableHeaderButtons: [
                    {
                        name: '导出excel', icon: 'el-icon-notebook-2', type: 'success', show: superAdminFlag,
                        handleClick() {
                            that.dialogExportExcel = true;
                        }
                    }
                ],
                //表格对应每一行按钮
                tableOperates: {
                    //按钮数组
                    buttons:
                        [
                            {
                                name: '查看', type: 'primary', show: true,
                                handleClick(row) {
                                    that.formData = row;
                                    that.dialogDetail = true;
                                }
                            }
                        ]
                },
                //表格顶部搜索
                tableSearch: [
                    {type: 'input', prop: 'username', placeholder: '用户名'},
                    {type: 'input', prop: 'realName', placeholder: '真实姓名'},
                    {
                        type: 'select', prop: 'type', placeholder: '类型',
                        options: [
                            {label: '查询', value: 1},
                            {label: '新增', value: 2},
                            {label: '修改', value: 3},
                            {label: '删除', value: 4}
                        ]
                    },
                    {type: 'input', prop: 'description', placeholder: '操作'},
                    {type: 'input', prop: 'ipAddress', placeholder: 'ip地址'},
                    {type: 'input', prop: 'borderName', placeholder: '浏览器'},
                    {type: 'input', prop: 'osName', placeholder: '操作系统'},
                    {type: 'dateTimePicker', prop: 'startTime', placeholder: '起始时间'},
                    {type: 'dateTimePicker', prop: 'endTime', placeholder: '截止时间'}
                ],
                //表单数据
                formData: {},
                //详情弹窗
                dialogDetail: false,
                //导出系统日志
                dialogExportExcel: false,
                //导出开始时间
                startTime: '',
                //导出截止时间
                endTime: ''
            }
        },
        methods: {
            exportExcel() {
                let that = this;
                this.$axiosDown({
                    url: that.$global.URL.system.sysUserLog.exportExcel,
                    data: {
                        startTime: that.startTime,
                        endTime: that.endTime,
                    },
                    filename: '系统用户日志.xlsx',
                    success() {
                        that.dialogExportExcel = false;
                    }
                })
            }
        }
    }
</script>