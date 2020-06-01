<template>
    <div id="ip-manager">
        <el-tabs tab-position="left"
                 v-model="activeName">
            <el-tab-pane name="PC" label="PC端ip过滤配置">
                <el-divider content-position="left">过滤规则</el-divider>
                <div class="role">
                    <el-radio-group v-model="pcIpFilterRole">
                        <el-radio label="all">全部允许</el-radio>
                        <el-radio label="white">只允许白名单</el-radio>
                        <el-radio label="black">只禁止黑名单</el-radio>
                    </el-radio-group>
                    <el-button v-if="haveUpdateRole"
                               style="margin-left: 100px;" size="mini" type="primary"
                               @click="saveRole('PC')">保存
                    </el-button>
                </div>
                <el-divider content-position="left">ip名单</el-divider>
                <wei-table ref="pcTable" :showHeaderSearch="true"
                           :tableDataRequest="pcTableDataRequest"
                           :tableColumns="tableColumns"
                           :tableHeaderButtons="tableHeaderButtons"
                           :tableOperates="tableOperates"></wei-table>
            </el-tab-pane>
            <el-tab-pane name="WEB" label="WEB端ip过滤配置">
                <el-divider content-position="left">过滤规则</el-divider>
                <div class="role">
                    <el-radio-group v-model="webIpFilterRole">
                        <el-radio label="all">全部允许</el-radio>
                        <el-radio label="white">只允许白名单</el-radio>
                        <el-radio label="black">只禁止黑名单</el-radio>
                    </el-radio-group>
                    <el-button v-if="haveUpdateRole"
                               style="margin-left: 100px;" size="mini" type="primary"
                               @click="saveRole('WEB')">保存
                    </el-button>
                </div>
                <el-divider content-position="left">ip名单</el-divider>
                <wei-table ref="webTable" :showHeaderSearch="true"
                           :tableDataRequest="webTableDataRequest"
                           :tableColumns="tableColumns"
                           :tableHeaderButtons="tableHeaderButtons"
                           :tableOperates="tableOperates"></wei-table>
            </el-tab-pane>
        </el-tabs>
        <template>
            <el-dialog title="新增ip过滤名单"
                       :visible.sync="dialogAddIpFilter">
                <add-ip-filter :terminal="activeName"
                               @closeDialog="dialogAddIpFilter = false"
                               @renderTable="$refs[`${activeName.toLowerCase()}Table`].renderTable()"></add-ip-filter>
            </el-dialog>
        </template>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Complex.vue'),
            'add-ip-filter': () => import('./AddIpFilter')
        },
        data() {
            let that = this;
            let {
                ipManager_add, ipManager_delete, ipManager_updateRole
            } = this.$globalFun.getSessionStorage('buttonMap');
            return {
                //是否有保存按钮权限
                haveUpdateRole: ipManager_updateRole,
                //当前选中的选项卡
                activeName: 'PC',
                pcTableDataRequest: {
                    url: that.$global.URL.dataDictionary.ipManager.getIpFilterList,
                    data: {
                        terminal: 'PC'
                    }
                },
                webTableDataRequest: {
                    url: that.$global.URL.dataDictionary.ipManager.getIpFilterList,
                    data: {
                        terminal: 'WEB'
                    }
                },
                tableColumns: [
                    {label: '名称', prop: 'name'},
                    {label: '值', prop: 'value'},
                    {label: '备注', prop: 'remark'},
                    {
                        label: '创建时间', prop: 'createTime', type: 'icon', element(row) {
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
                        name: '新增', icon: 'el-icon-plus', type: 'success', show: ipManager_add,
                        handleClick() {
                            that.dialogAddIpFilter = true;
                        }
                    }
                ],
                //表格对应每一行按钮
                tableOperates: {
                    //按钮数组
                    buttons:
                        [
                            {
                                name: '删除', type: 'danger', show: ipManager_delete,
                                handleClick(row) {
                                    that.deleteIp(row);
                                }
                            },
                        ]
                },
                //pc端过滤规则
                pcIpFilterRole: '',
                //web端过滤规则
                webIpFilterRole: '',
                //新增ip弹窗
                dialogAddIpFilter: false
            }
        },
        watch: {
            activeName(name) {
                this.getIpFilterRole();
                this.$refs[`${name.toLowerCase()}Table`].renderTable();
            }
        },
        mounted() {
            this.getIpFilterRole();
        },
        methods: {
            /**
             * 获取ip过滤规则
             */
            getIpFilterRole() {
                let that = this;
                this.$axios({
                    url: that.$global.URL.dataDictionary.ipManager.getIpFilterRole,
                    data: {
                        type: that.activeName
                    },
                    success(data) {
                        switch (that.activeName) {
                            case 'PC': {
                                that.pcIpFilterRole = data;
                            }
                                break;
                            case 'WEB': {
                                that.webIpFilterRole = data;
                            }
                                break;
                            default: {

                            }
                        }
                    }
                })
            },
            /**
             * 保存规则
             * @param type
             */
            saveRole(type) {
                let role = '';
                switch (type) {
                    case 'PC': {
                        role = this.pcIpFilterRole;
                    }
                        break;
                    case 'WEB': {
                        role = this.webIpFilterRole;
                    }
                        break;
                    default: {
                        return;
                    }
                }
                let that = this;
                this.$globalFun.messageBox({
                    message: `确定变更规则`,
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.dataDictionary.ipManager.updateIpFilterRole,
                            method: 'post',
                            data: {
                                type, role
                            },
                            success() {
                                that.$globalFun.successMsg('更新成功');
                            }
                        })
                    }
                });
            },
            /**
             * 删除ip
             * @param id
             * @param value
             */
            deleteIp({id, value}) {
                let that = this;
                this.$globalFun.messageBox({
                    message: `确定删除 ${value} ,该操作无法撤销`,
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.dataDictionary.ipManager.deleteIpFilterList,
                            method: 'post',
                            data: {
                                id
                            },
                            success() {
                                that.$globalFun.successMsg('成功');
                                that.$refs[`${that.activeName.toLowerCase()}Table`].renderTable();
                            }
                        })
                    }
                })
            }
        }
    }
</script>

<style lang="scss">
    #ip-manager {
        .el-tabs {
            #wei-table {
                .content > div {
                    max-height: 60vh !important;
                }
            }
        }
    }
</style>