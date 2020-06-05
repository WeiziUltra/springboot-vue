<template>
    <div id="index">
        <el-tabs tab-position="left">
            <el-tab-pane label="日志文件">
                <log-file></log-file>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'log-file': () => import('./LogFile.vue')
        }
    }
</script>