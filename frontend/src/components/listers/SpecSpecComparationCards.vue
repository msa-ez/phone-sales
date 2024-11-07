<template>
    <div>
        <h1 style="padding-left:17px;">
            단말기 스펙 비교
        </h1>
        <v-row class="ma-0 pa-0">
            <v-col v-for="n in 3" :key="n" class="pa-0">
                <v-card-text>
                    <v-select
                        v-model="selectedSpecs[n - 1]"
                        :items="specDetails.map(spec => spec.spec)"
                        label="기종 선택"
                        outlined
                        class="delete-input-detail"
                    ></v-select>
                    <template v-if="!selectedSpecs[n - 1]">
                        <v-list-item class="pa-0">
                            <v-list-item-content>
                                <v-list-item-title>선택된 기종이 없습니다.</v-list-item-title>
                            </v-list-item-content>
                        </v-list-item>
                    </template>
                    <div v-for="(spec, index) in filteredSpecs(n - 1)" :key="index">
                        <v-card-title>{{ spec.spec }}</v-card-title>
                        <v-card-subtitle>{{ spec.phoneColor }} - {{ spec.phoneType }}</v-card-subtitle>
                        <v-card-subtitle>가격 : {{ spec.price }} 원</v-card-subtitle>
                        <div class="image-container mb-2">
                            <Photo v-if="spec.image" v-model="spec.image" :editMode="false" />
                            <!-- 각 이미지에 대해 개별적으로 선택된 색상을 적용 -->
                            <div class="overlay" :style="{ backgroundColor: selectedColors[n - 1] || 'transparent', opacity: 0.1 }"></div>
                        </div>
                        <v-row class="ma-0 pa-0 justify-center">
                            <v-btn v-for="(item, colorIndex) in colors" 
                                :key="colorIndex"
                                class="color-picker-btn"
                                :class="{'selected-color': selectedColors[n - 1] === item.color}"
                                small
                                fab
                                :color="item.color"
                                @click="setColor(n - 1, item.color)" 
                            ></v-btn>
                        </v-row>
                    </div>
                </v-card-text>
            </v-col>
        </v-row>
        <v-dialog v-model="orderDialog" max-width="600px">
            <template >
                <OrderOrder :editMode=true :isNew=true v-model="selectedProduct" @close="orderDialog = false" />
            </template>
        </v-dialog>
    </div>
</template>

<script>
    import OrderOrder from '../OrderOrder.vue'; // Import the OrderOrder component

    const axios = require('axios').default;

    export default {
        name: 'SpecSpecComparationManager',
        components: {
            OrderOrder // Register the OrderOrder component
        },
        data() {
            return {
                colors: [
                    { type: 'Brown', color: '#964B00' },
                    { type: 'deepRed', color: '#8B0000' },
                    { type: 'Black', color: '#000000' }
                ],
                selectedSpec: null,
                valid: false,
                specComparation: {
                    productId: '',
                    options: '',
                    customerId: ''
                },
                selectedManufacturer: null,
                manufacturers: [],
                searchParams: {
                    color: '',
                    manufacturer: ''
                },
                specDetails: [],
                comparisonList: [], // List to hold selected specs for comparison
                rules: {
                    required: value => !!value || '필수 항목입니다.'
                },
                orderDialog: false, // Control the visibility of the order dialog
                selectedProduct: null, // Store the selected product ID for the order
                selectedSpecs: [null, null, null],
                selectedColors: [null, null, null],
            };
        },
        async created() {
            this.manufacturers = await this.fetchManufacturers();
            await this.loadComparisonList(); // Load comparison list on page load
            await this.getSpecDetail();  // 스펙 상세 정보를 미리 로드
        },
        computed: {
            filteredSpecs() {
                return (index) => {
                    return this.specDetails.filter(spec => spec.spec === this.selectedSpecs[index]);
                };
            }
        },
        methods: {
            setColor(index, color) {
                // 각 선택된 스펙에 대해 선택된 색상을 설정
                this.$set(this.selectedColors, index, color);
            },
            async fetchManufacturers() {
                return ['Samsung', 'Apple', 'LG', 'Nokia']; // Example manufacturers
            },
            async loadComparisonList() {
                try {
                    const response = await axios.get('/specComparations');
                    const comparisons = response.data._embedded.specComparations; // Assuming this returns an array of comparisons

                    // For each comparison, fetch the detailed spec using productId
                    for (const comparison of comparisons) {
                        const specResponse = await axios.get(`/specs/${comparison.productId}`);
                        specResponse.data.comparationId = comparison._links.self.href.split('/').pop();
                        this.comparisonList.push(specResponse.data); // Assuming the response contains the spec details
                    }
                } catch (error) {
                    console.error('비교 목록 로드 실패:', error);
                }
            },
            async saveSpecComparation() {
                try {
                    const response = await axios.post('/specComparations', {
                        productId: this.specComparation.productId,
                        options: this.specComparation.options.split(','),
                        customerId: this.specComparation.customerId,
                        manufacturer: this.selectedManufacturer
                    });
                    console.log('스펙 비교 저장 성공:', response.data);

                    return response
                } catch (error) {
                    console.error('스펙 비교 저장 실패:', error);
                }
            },
            async getSpecDetail() {
                try {
                    const response = await axios.get(`/specs/search/FindByColorsAndManufacturer`, {
                        params: {
                            color: this.searchParams.color,
                            manufacturer: this.searchParams.manufacturer
                        }
                    });
                    this.specDetails = response.data._embedded.specs;
                    console.log('스펙 상세 조회 성공:', this.specDetails);
                } catch (error) {
                    console.error('스펙 상세 조회 실패:', error);
                }
            },
            async addToComparison(spec) {
                console.log('비교 목록에 추가됨:', spec);
                this.specComparation.productId = spec._links.self.href.split('/').pop();
                this.specComparation.manufacturer = spec.manufacturer;

                let response = await this.saveSpecComparation();
                spec.comparationId = response.data._links.self.href.split('/').pop();

                // Add the selected spec to the comparison list
                this.comparisonList.push(spec);
            },
            async removeFromComparison(index, comparationId) {
                // Remove the spec from the comparison list
                try {
                    await axios.delete(`/specComparations/${comparationId}`); // Delete from server
                    this.comparisonList.splice(index, 1); // Remove from local list
                    console.log('비교 목록에서 제거됨:', comparationId);
                } catch (error) {
                    console.error('비교 목록에서 제거 실패:', error);
                }
            },
            openOrderDialog(productId) {

                this.selectedProduct = {productId: productId}; // Set the selected product ID
                this.orderDialog = true; // Open the dialog
            }
        }
    };
</script>

<style>
    .video-card {
        margin-left:4.5%; 
        margin-top:50px; 
        margin-bottom:50px;
    }
    
    .image-container {
        position: relative;
        display: inline-block;
        height: auto; /* 이미지와 동일한 높이 */
        display: flex;
        justify-content: center; /* 수평 중앙 정렬 */
        align-items: center; /* 수직 중앙 정렬 */
    }

    .overlay {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        pointer-events: none;  /* 클릭 이벤트가 아래로 전달되도록 설정 */
    }
    .selected-color {
        box-shadow: 0 0 0 3px #1976D2 !important; /* 선택된 색상에 외부 테두리 추가 */
    }
</style>
