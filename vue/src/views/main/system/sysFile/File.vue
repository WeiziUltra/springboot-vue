<template>
    <div id="index">
        <simple-table :maxHeight="500"
                      :tableData="tableData"
                      :tableColumns="tableColumns"
                      :tableOperates="tableOperates"></simple-table>
        <el-dialog title="替换文件" width="70%"
                   :visible.sync="dialogVisible">
            <wei-upload :action="$global.URL.system.sysFile.uploadFile"
                        :data="params"
                        :fileList.sync="fileList"
                        :tip="'因为替换后文件名不变，请注意缓存问题'"
                        @success="dialogVisible = false"></wei-upload>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "File",
        components: {
            'simple-table': () => import('@/components/table/Simple.vue'),
            'wei-upload': () => import('@/components/upload/Index.vue')
        },
        props: {
            isShow: {
                type: Boolean,
                default: false
            }
        },
        data() {
            let that = this;
            return {
                tableData: [],
                tableColumns: [
                    {label: '文件名', prop: 'name'},
                    {
                        label: '类型', type: 'tag',
                        element({url}) {
                            let result = {
                                'mkdir': {
                                    content: '目录'
                                },
                                'file': {
                                    content: '文件',
                                    type: 'success'
                                }
                            }
                            let key = (null == url || '' === url) ? 'mkdir' : 'file';
                            return result[key];
                        }
                    },
                    {
                        label: '附件', type: 'avatar', element({url}) {
                            return {
                                src: url
                            }
                        }
                    }
                ],
                tableOperates: {
                    buttons: [
                        {
                            name: '替换文件', type: 'success',
                            showFormatter(row) {
                                if (null == row) {
                                    return true;
                                }
                                let {url} = row;
                                return (null != url && '' !== url);
                            },
                            handleClick(row) {
                                that.$globalFun.errorMsg(`因为替换后文件名不变，请注意<strong>缓存</storage>问题`, 10000, true);
                                let {url, name} = row;
                                that.fileList = [{
                                    name,
                                    url
                                }];
                                that.params = {
                                    url
                                };
                                that.dialogVisible = true;
                            }
                        }
                    ]
                },
                dialogVisible: false,
                //目前文件
                fileList: [],
                //参数
                params: {}
            }
        },
        watch: {
            isShow() {
                this.getTableData();
            }
        },
        mounted() {
            this.getTableData();
        },
        methods: {
            getTableData() {
                if (!this.isShow) {
                    return;
                }
                let that = this;
                this.$axios({
                    url: that.$global.URL.system.sysFile.getFile,
                    success(data) {
                        for (let i = 0; i < data.length; i++) {
                            data[i]['id'] = that.$globalFun.createUUID();
                            data[i]['children'] = that.handleTableChildren(data[i]['children']);
                        }
                        that.tableData = data;
                    }
                });
            },
            handleTableChildren(data) {
                for (let i = 0; i < data.length; i++) {
                    data[i]['id'] = this.$globalFun.createUUID();
                    data[i]['children'] = this.handleTableChildren(data[i]['children']);
                }
                return data;
            }
        }
    }
</script>