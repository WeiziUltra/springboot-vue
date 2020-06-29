<template>
    <div id="index">
        <el-tabs tab-position="left"
                 v-model="activeName">
            <el-tab-pane name="PC" label="系统日志文件">
                <log-file :isShow="'PC' === activeName" :type="'PC'"></log-file>
            </el-tab-pane>
            <el-tab-pane name="WEB" label="web日志文件">
                <log-file :isShow="'WEB' === activeName" :type="'WEB'"></log-file>
            </el-tab-pane>
            <el-tab-pane name="FILE" label="常用文件">
                <wei-file :isShow="'FILE' === activeName"></wei-file>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'log-file': () => import('./LogFile.vue'),
            'wei-file': () => import('./File.vue')
        },
        data() {
            return {
                //当前选中项
                activeName: 'PC'
            }
        }
    }
</script>