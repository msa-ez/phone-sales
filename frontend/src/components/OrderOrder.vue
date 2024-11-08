<template>
    <v-card style="width:450px; height:100%;" outlined>
        <template slot="progress">
            <v-progress-linear
                    color="primary-darker-1"
                    height="10"
                    indeterminate
            ></v-progress-linear>
        </template>

        <v-card-title v-if="value._links">
            주문 # {{decode(value._links.self.href.split("/")[value._links.self.href.split("/").length - 1])}}
        </v-card-title >
        <v-card-title v-else>
            주문
        </v-card-title > 

        <div v-if="orderDialog" style="padding:0px 100px 0px 100px;">
            <div>
                <div class="text-center">
                    <div style="font-size: 48px; font-weight: 900; text-align: center; margin: 20px 0;">장바구니 총액: ₩{{ formattedTotalPrice }}원</div>
                    <div style="font-size: 16px; margin-bottom: 20px;">모든 주문에 무료 배송 서비스가 제공됩니다.</div>
                </div>
                <v-divider class="mt-8 mb-8"></v-divider>
                <v-row class="ma-0 pa-0 mb-4" style="padding:0px 0px 0px 100px !important;">
                    <v-col cols="3" class="pa-0">
                        <img :src="value.image ? value.image.imgFile : '/image/no-image.png'" alt="상품 이미지" style="max-width: 200px; height: auto;">
                        <div v-if="!value.image">등록된 이미지가 없습니다.</div>
                    </v-col>
                    <v-col class="pa-0">
                        <v-row>
                            <v-col>
                                <div style="font-size:24px; font-weight: 900;">{{ value.spec }}</div>
                            </v-col>
                            <v-col>
                                <v-text-field class="delete-input-detail"
                                    v-model="quantity"
                                    label="수량"
                                    type="number"
                                    min="1"
                                    outlined
                                    style="width: 100px; margin: 0 auto;"
                                ></v-text-field>
                            </v-col>
                            <v-col>
                                <div style="font-size:24px; font-weight: 900;">₩{{ formattedPrice }} 원</div>
                            </v-col>
                        </v-row>
                        <v-divider class="mb-4 mt-4"></v-divider>
                        <v-row class="ma-0 pa-0 mb-2">
                            <v-icon class="mr-1">mdi-truck-delivery</v-icon>
                            <div style="font-size:14px;">배송지를 입력해주세요</div>
                        </v-row>
                        <String style="width:200px;" label="주소" v-model="value.address" :editMode="editMode" :inputUI="''"/>
                        <v-checkbox
                            v-model="value.saveLocation"
                            label="이후 방문을 위해 내 위치 저장"
                            class="mt-2"
                        ></v-checkbox>
                    </v-col>
                </v-row>
                <v-divider style="margin:50px 0px 50px 0px;"></v-divider>
                <div style="padding-left:40%;">
                    <v-row class="ma-0 pa-0"
                        style="font-size:14px;"
                    >
                        <div>소개</div>
                        <v-spacer></v-spacer>
                        <div>₩{{ formattedPrice }} 원</div>
                    </v-row>
                    <v-row class="ma-0 pa-0"
                        style="font-size:14px;"
                    >
                        <div>배송</div>
                        <v-spacer></v-spacer>
                        <div>무료</div>
                    </v-row>
                    <v-divider style="margin:20px 0px 20px 0px;"></v-divider>
                    <v-row class="ma-0 pa-0"
                        style="font-size:24px; font-weight: 900;"
                    >
                        <div>총계</div>
                        <v-spacer></v-spacer>
                        <div style="display: flex; flex-direction: column; align-items: flex-end;">
                            <div>₩{{ formattedTotalPrice }} 원</div>
                            <div style="font-size:10px; color:gray">
                                ₩{{ (parseInt(formattedTotalPrice.replace(/,/g, ''), 10) * 0.9).toLocaleString('ko-KR') }} 원의 VAT 포함
                            </div>
                        </div>
                    </v-row>
                    <v-row class="ma-0 pa-0 mt-4">
                        <v-spacer></v-spacer>
                        <v-btn style="width:200px; display: block;"
                            color="primary"
                            @click="save"
                        >
                            결제
                        </v-btn>
                    </v-row>
                </div>
            </div>
        </div>

        <v-card-text v-else style="background-color: white;">
            <String label="상품 ID" v-model="value.productId" :editMode="false" :inputUI="''" disabled class="mb-2" />
            <String label="이름" v-model="value.spec" :editMode="false" :inputUI="''" disabled class="mb-2" />
            <String label="가격" v-model="formattedPrice" :editMode="false" :inputUI="''" disabled class="mb-2" />
            <String label="총 가격" v-model="formattedTotalPrice" :editMode="false" :inputUI="''" disabled class="mb-2" />
            <String label="수량" v-model="value.qty" :editMode="false" :inputUI="''" class="mb-2" />
            <String label="옵션" v-model="value.options" :editMode="editMode" :inputUI="''"/>
            <String label="고객 ID" v-model="value.customerId" :editMode="editMode" :inputUI="''"/>
            <String label="주소" v-model="value.address" :editMode="editMode" :inputUI="''"/>
            <String label="보험 옵션" v-model="value.insuranceOption" :editMode="editMode" :inputUI="''"/>
        </v-card-text>

        <v-card-actions style="background-color: white;">
            <v-spacer></v-spacer>
            <v-btn
                color="primary"
                text
                @click="edit"
                v-if="!editMode"
            >
                수정
            </v-btn>
            <div v-else>
                <v-btn v-if="!orderDialog"
                    color="primary"
                    @click="save"
                >
                    결제
                </v-btn>
                <v-btn
                    color="primary"
                    text
                    @click="remove"
                    v-if="!editMode"
                >
                    삭제
                </v-btn>
                <v-btn
                    color="primary"
                    text
                    @click="editMode = false"
                    v-if="editMode && !isNew"
                >
                    취소
                </v-btn>
            </div>
        </v-card-actions>
        <v-card-actions>
            <v-spacer></v-spacer>
        </v-card-actions>

        <v-snackbar
            v-model="snackbar.status"
            :top="true"
            :timeout="snackbar.timeout"
            color="error"
        >
            {{ snackbar.text }}
            <v-btn dark text @click="snackbar.status = false">
                Close
            </v-btn>
        </v-snackbar>
    </v-card>

</template>

<script>
    const axios = require('axios').default;


    export default {
        name: 'OrderOrder',
        components:{
        },
        props: {
            value: [Object, String, Number, Boolean, Array],
            editMode: Boolean,
            isNew: Boolean,
            offline: Boolean,
            orderDialog: Boolean
        },
        data: () => ({
            snackbar: {
                status: false,
                timeout: 5000,
                text: '',
            },
            quantity: 1,
        }),
	    async created() {
        },
        computed: {
            formattedPrice: {
                get() {
                    return new Intl.NumberFormat('ko-KR').format(this.value.price || 0);
                },
                set(newValue) {
                    this.value.price = parseInt(newValue.replace(/,/g, ''), 10);
                }
            },
            formattedTotalPrice() {
                // 총 가격을 한국 표기식으로 포맷팅
                const totalPrice = (this.value.price || 0) * (this.value.qty || this.quantity);
                return new Intl.NumberFormat('ko-KR').format(totalPrice || 0);
            }
        },
        
        methods: {
            decode(value) {
                return decodeURIComponent(value);
            },
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
                        me.imageUrl = result;
                        me.value.photo = result;
                        
                    };
                    reader.readAsDataURL( file );
                };
            },
            edit() {
                this.editMode = true;
            },
            async save() {
            try {
                // 수량과 총 가격을 value에 반영
                this.value.qty = this.quantity;
                this.value.totalPrice = (this.value.price || 0) * this.quantity;

                var temp = null;

                if (!this.offline) {
                    if (this.isNew) {
                        temp = await axios.post(axios.fixUrl('/orders'), this.value);
                    } else {
                        temp = await axios.put(axios.fixUrl(this.value._links.self.href), this.value);
                    }
                }

                if (this.value != null) {
                    for (var k in temp.data) this.value[k] = temp.data[k];
                } else {
                    this.value = temp.data;
                }

                this.editMode = false;
                this.$emit('input', this.value);

                if (this.isNew) {
                    this.$emit('add', this.value);
                } else {
                    this.$emit('edit', this.value);
                }

                location.reload();

            } catch (e) {
                this.snackbar.status = true;
                if (e.response && e.response.data.message) {
                    this.snackbar.text = e.response.data.message;
                } else {
                    this.snackbar.text = e;
                }
            }
        },
            async remove(){
                try {
                    if (!this.offline) {
                        await axios.delete(axios.fixUrl(this.value._links.self.href))
                    }

                    this.editMode = false;
                    this.isDeleted = true;

                    this.$emit('input', this.value);
                    this.$emit('delete', this.value);

                } catch(e) {
                    this.snackbar.status = true
                    if(e.response && e.response.data.message) {
                        this.snackbar.text = e.response.data.message
                    } else {
                        this.snackbar.text = e
                    }
                }
            },
            change(){
                this.$emit('input', this.value);
            },
        },
    }
</script>

