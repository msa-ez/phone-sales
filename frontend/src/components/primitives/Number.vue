<template>
    <div>
        <div v-if="editMode" style="">
            <v-text-field type="number" :label="label" v-model="value" @change="change" outlined class="delete-input-detail"/>
        </div>
        <div v-else>
            {{label}} :  {{value}}
        </div>
    </div>
</template>

<script>
    export default {
        name: 'Number',
        props: {
            value:{
                type: Number,
                default: 0
            },
            editMode: Boolean,
            label: String
        },
        methods:{
            change(){
                this.$emit("input", Number(this.value));
            }
        }
    }
</script>
