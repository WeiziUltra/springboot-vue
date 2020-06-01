<template>
    <div id="index">
        <wei-form :formData="form" :formOptions="formOptions"
                  @closeDialog="$emit('closeDialog')" @submit="submit">
        </wei-form>
    </div>
</template>

<script>
    export default {
        name: "EditForm",
        components: {
            'wei-form': () => import('@/components/form/Index.vue')
        },
        props: {
            //类型
            terminal: {
                type: String,
                default: 'PC'
            }
        },
        data() {
            return {
                formOptions: [
                    {
                        type: 'radio', label: '类型', prop: 'type', required: true,
                        options: [
                            {label: '白名单', value: 'White'},
                            {label: '黑名单', value: 'Black'},
                            {label: '异常名单', value: 'Abnormal'},
                        ]
                    },
                    {type: 'input', label: 'ip地址', prop: 'ipAddress', required: true},
                    {type: 'textarea', label: '备注', prop: 'remark'},
                ],
                form: {
                    type: 'White'
                }
            }
        },
        methods: {
            submit(form) {
                let that = this;
                let {terminal} = this;
                let {type} = form;
                type = `${terminal.toLowerCase()}IpFilter${type}List`;
                form['terminal'] = terminal.toUpperCase();
                form['type'] = type;
                that.$axios({
                    url: that.$global.URL.dataDictionary.ipManager.addIpFilterList,
                    method: 'post',
                    data: form,
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('closeDialog');
                        that.$emit('renderTable');
                    }
                });
            }
        }
    }
</script>