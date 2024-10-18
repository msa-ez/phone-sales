<template>
    <v-container>
        <v-card>
            <v-card-title>
                스펙 상세 조회
            </v-card-title>
            <v-card-text>
                <v-text-field
                    v-model="searchParams.color"
                    label="색상"
                ></v-text-field>
                <v-select
                    v-model="searchParams.manufacturer"
                    :items="manufacturers"
                    label="제조사"
                ></v-select>
                <v-btn @click="getSpecDetail" color="primary">조회</v-btn>
                <v-list>
                    <v-list-item-group>
                        <template v-if="specDetails.length === 0">
                            <v-list-item>
                                <v-list-item-content>
                                    <v-list-item-title>조회된 스펙이 없습니다.</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </template>
                        <v-list-item v-for="(spec, index) in specDetails" :key="index">
                            <v-list-item-content>
                                <v-list-item-title>{{ spec.spec }}</v-list-item-title>
                                <v-list-item-subtitle>{{ spec.phoneColor }} - {{ spec.phoneType }}</v-list-item-subtitle>
                                <Photo v-if="spec.image" v-model="spec.image" :editMode="false" />
                                <v-btn @click="addToComparison(spec)" color="success">비교 추가</v-btn>
                            </v-list-item-content>
                        </v-list-item>
                    </v-list-item-group>
                </v-list>
            </v-card-text>
        </v-card>
        <v-card>
            <v-card-title>
                비교 목록
            </v-card-title>
            <v-card-text>
                <v-row>
                    <v-col
                        v-for="(item, index) in comparisonList"
                        :key="index"
                        cols="12"
                        md="4"
                    >
                        <v-card>
                            <v-card-title>{{ item.spec }}</v-card-title>
                            <v-card-subtitle>{{ item.phoneColor }} - {{ item.phoneType }}</v-card-subtitle>
                            <v-img
                                v-if="item.image"
                                :src="item.image.imgFile"
                                height="200px"
                                class="white--text align-end"
                                gradient="to bottom, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.3)"
                            >
                                <v-card-actions>
                                    <v-btn @click="removeFromComparison(index, item.comparationId)" color="red">비교 목록에서 제거</v-btn>
                                    <v-btn @click="openOrderDialog(item.productId)" color="blue">주문</v-btn>
                                </v-card-actions>
                            </v-img>
                        </v-card>
                    </v-col>
                </v-row>
            </v-card-text>
        </v-card>


        <!-- Order Dialog -->
        <v-dialog v-model="orderDialog" max-width="600px">
            <template >
                <OrderOrder :editMode=true :isNew=true v-model="selectedProduct" @close="orderDialog = false" />
            </template>
        </v-dialog>
    </v-container>
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
                selectedProduct: null // Store the selected product ID for the order
            };
        },
        async created() {
            this.manufacturers = await this.fetchManufacturers();
            await this.loadComparisonList(); // Load comparison list on page load
        },
        methods: {
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
        width:300px; 
        margin-left:4.5%; 
        margin-top:50px; 
        margin-bottom:50px;
    }
</style>
