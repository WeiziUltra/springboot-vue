<template>
    <div id="index" style="height: 80vh;overflow-y: scroll;">
        <el-timeline>
            <template v-for="item in fileList">
                <el-timeline-item placement="top"
                                  :timestamp="item.name">
                    <el-table border style="width: 100%" size="mini"
                              :data="item.children || []">
                        <el-table-column prop="name" label="文件名"></el-table-column>
                        <el-table-column prop="length" label="大小(字节)"></el-table-column>
                        <el-table-column fixed="right" label="操作" width="100">
                            <template slot-scope="scope">
                                <el-button type="primary" size="mini"
                                           @click="handleClick(scope.row,item)">下载
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-timeline-item>
            </template>
        </el-timeline>
    </div>
</template>

<script>
    export default {
        name: "LogFile",
        props: {
            //类型
            type: {
                type: String
            },
            //是否展示
            isShow: {
                type: Boolean,
                default: true
            }
        },
        data() {
            return {
                fileList: []
            }
        },
        watch: {
            type() {
                this.getFile();
            },
            isShow() {
                this.getFile();
            }
        },
        mounted() {
            this.getFile();
        },
        methods: {
            getFile() {
                if (!this.isShow) {
                    return;
                }
                let that = this;
                this.$axios({
                    url: that.$global.URL.system.sysFile.getLogFile,
                    data: {
                        type: that.type
                    },
                    success(data) {
                        if (null == data) {
                            return;
                        }
                        data.sort((a, b) => {
                            return a.name < b.name ? 1 : -1;
                        });
                        that.fileList = data;
                    }
                })
            },
            /**
             * 下载
             * @param row
             * @param item
             */
            handleClick(row, item) {
                let that = this;
                let {name} = row;
                this.$globalFun.messageBox({
                    message: `确定下载 ${name}`,
                    confirm() {
                        that.$axiosDown({
                            url: that.$global.URL.system.sysFile.downLogFile,
                            data: {
                                dir: item.name,
                                name: name,
                                type: that.type
                            },
                            filename: `${that.type}---${name}`
                        });
                    }
                });
            }
        }
    }
</script>