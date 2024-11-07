<template>
    <div class="photo-container">
        <v-card-title v-if="editMode">
            {{label}}
        </v-card-title>

        <v-img
            :key="photoRender"
            class="photo-image"
            :style="editMode ? 'cursor:pointer;':''"
            :src="value.imgFile ? value.imgFile:'https://cdn.vuetifyjs.com/images/cards/cooking.png'"
            @click="selectFile()"
            contain
        ></v-img>

        <!-- <v-card-text v-if="value">
            <div v-if="editMode">
                <v-text-field label="" v-model="value.imgName"/>
                <slot name="actions"></slot>
            </div>
        </v-card-text> -->
    </div>
</template>

<script>
export default {
    name:"Photo",
    props: {
        editMode: Boolean,
        value : Object,
        label : String, 
    },
    data: () => ({
        photoRender:0
    }),
    created(){
        if(!this.value) {
            this.value = {
                'imgName': '',
                'imgFile': '',
            };
        }
    },
    watch: {
        value(newVal) {
            this.$emit('input', newVal);
        },
    },
    methods: {
        selectFile(){
            if(this.editMode == false) {
                return false;
            }

            var me = this
            var input = document.createElement("input");
            input.type = "file";
            input.accept = "image/*";
            input.id = "uploadInput";
            
            input.click();
            input.onchange = function (event) {
                var file = event.target.files[0]
                var reader = new FileReader();

                reader.onload = function () {
                    var result = reader.result;
                    me.value.imgFile = result;
                    me.photoRender++;
                };
                reader.readAsDataURL( file );
            };
        },
    }
}
</script>

<style scoped>
.photo-container {
    width: 200px; /* 고정된 너비 */
    height: 250px; /* 고정된 높이 */
    display: flex;
    justify-content: center; /* 수평 중앙 정렬 */
    align-items: center; /* 수직 중앙 정렬 */
}

.photo-image {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain; /* 이미지가 컨테이너 내에 맞게 조정되도록 설정 */
}
</style>