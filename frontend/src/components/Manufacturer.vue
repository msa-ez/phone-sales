<template>
    <div style="margin: 0 -15px 0 -15px;">
        <v-card-text>
            <div v-if="editMode" style="">
                <v-select outlined
                        v-model="newValue"
                        :label="'제조사'"
                        :items="manufacturerList"
                ></v-select>
            </div>
            <div v-else style="">
                제조업체 : {{ value }}
            </div>
        </v-card-text>
    </div>
</template>

<script>
    export default {
        name: 'Manufacturer',
        components:{},
        props: {
            value: [Object, String, Number, Boolean, Array],
            editMode: Boolean,
            isNew: Boolean,
            offline: Boolean,
            inList: Boolean,
            label: String,
        },
        data: () => ({
            manufacturerList : [ "Samsung",  "Apple",  "Hawei", ],
            newValue: '',
        }),
        async created() {
            if(!this.value) {
                this.newValue = this.manufacturerList[0];
            } else {
                this.newValue = this.value;
            }
        },
        watch: {
            value(val) {
                this.$emit('input', val);
            },
            newValue(val) {
                this.$emit('input', val);
            },
        },
        methods: {
        }
    }
</script>

