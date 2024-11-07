

<template>
    <v-app id="inspire">
        <div>
            <v-app-bar color="primary" app clipped-left flat>
                <v-btn @click="openSideBar()" class="mr-2" icon text color="white"
                    style="cursor: pointer;"
                >
                    <v-icon>mdi-menu</v-icon>
                </v-btn>
                <!-- <v-btn v-if="urlPath!=null" @click="goHome()" icon text color="white"
                    style="cursor: pointer;"
                >
                    <v-icon>mdi-home</v-icon>
                </v-btn> -->
                <v-spacer></v-spacer>
            </v-app-bar>

            <v-navigation-drawer app clipped flat v-model="sideBar">
                <v-list>
                    <v-list-item
                        class="px-2"
                        to="/"
                        @click="goHome()"
                        color="primary"
                        style="font-weight:700; border-bottom: 1px solid #e0e0e0;"
                    >
                        Home
                    </v-list-item>

                    <v-list-item
                        class="px-2"
                        key="specs"
                        to="/specs/specs"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700; border-bottom: 1px solid #e0e0e0;"
                    >
                        단말기 스펙 등록
                    </v-list-item>

                    <v-list-item
                        class="px-2"
                        key="specComparations"
                        to="/specs/specComparations"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700; border-bottom: 1px solid #e0e0e0;"
                    >
                        단말기 스펙 비교
                    </v-list-item>

                    <v-list-item
                        class="px-2"
                        key="orders"
                        to="/orders/orders"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700; border-bottom: 1px solid #e0e0e0;"
                    >
                        주문
                    </v-list-item>

                    <v-list-item
                        class="px-2"
                        key="retargettings"
                        to="/marketings/retargettings"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700; border-bottom: 1px solid #e0e0e0;"
                    >
                        리타겟팅
                    </v-list-item>

                    <v-list-item
                        class="px-2"
                        key="insuranceSubscriptions"
                        to="/insurances/insuranceSubscriptions"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700; border-bottom: 1px solid #e0e0e0;"
                    >
                        보험가입
                    </v-list-item>

                    <v-list-item
                        class="px-2"
                        key="payments"
                        to="/payments/payments"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700; border-bottom: 1px solid #e0e0e0;"
                    >
                        결제
                    </v-list-item>

                    <v-list-item
                        class="px-2"
                        key="customers"
                        to="/customers/customers"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700; border-bottom: 1px solid #e0e0e0;"
                    >
                        고객
                    </v-list-item>

                    <v-list-item
                        class="px-2"
                        key="messages"
                        to="/customers/messages"
                        @click="changeUrl()"
                        color="primary"
                        style="font-weight:700; border-bottom: 1px solid #e0e0e0;"
                    >
                        메시지
                    </v-list-item>
                </v-list>
            </v-navigation-drawer>
        </div>

        <v-main>
            <v-container style="padding:0px;" v-if="urlPath" fluid>
                <router-view></router-view>
            </v-container>
            <v-container style="padding:0px;" v-else fluid>
                <div style="width:100%; position: relative;">
                    <v-img style="width:100%; height:200px;"
                        src="/image/main2.png"
                    ></v-img>
                    <div class="App-main-text-overlap"></div>
                    <div class="App-sub-text-overlap"></div>
                </div>
                <v-row class="pa-0 ma-0">
                    <v-col cols="4" class="pa-0 pa-0" v-for="(aggregate, index) in aggregate" :key="index">
                        <div 
                            class="flip-card pa-4"
                        >
                            <v-card
                                :key="aggregate.key"
                                :to="aggregate.route"
                                @click="changeUrl()"
                                class="mx-auto main-card pa-4"
                                style="text-align: center; border-radius: 10px;"
                                outlined
                            >
                                <div class="d-flex justify-center" style="width:120px; height:120px; border-radius: 10px; margin: 0 auto; background-color:white;">
                                    <v-img style="width:100%; height:100%; object-fit:contain; border-radius: 10px;" :src="aggregate.ImageUrl"></v-img>
                                </div>
                                <div style="text-align: center;">
                                    <h2 class="main-card-title">{{ aggregate.title }}</h2>
                                </div>
                            </v-card>
                        </div>
                    </v-col>
                </v-row>
            </v-container>
        </v-main>
    </v-app>
</template>

<script>

export default {
    name: "App",
    data: () => ({
        useComponent: "",
        drawer: true,
        components: [],
        sideBar: true,
        urlPath: null,
        flipped: [],
        ImageUrl: '',
        aggregate: [
            { 
                title: '단말기 스펙 등록', 
                description: '', 
                key: 'specs', 
                route: '/specs/specs',
                ImageUrl: '/image/Specification2.png',
            },
            { 
                title: '단말기 스펙 비교', 
                description: '', 
                key: 'specComparations', 
                route: '/specs/specComparations',
                ImageUrl: '/image/Specification1.png',
            },
            { 
                title: '주문', 
                description: '', 
                key: 'orders', 
                route: '/orders/orders',
                ImageUrl: '/image/order.png',
            },
            { 
                title: '리타게팅', 
                description: '', 
                key: 'retargettings', 
                route: '/marketings/retargettings',
                ImageUrl: '/image/retargettings.png',
            },
            { 
                title: '보험가입', 
                description: '', 
                key: 'insuranceSubscriptions', 
                route: '/insurances/insuranceSubscriptions',
                ImageUrl: '/image/insurance.png',
            },
            { 
                title: '결제', 
                description: '', 
                key: 'payments', 
                route: '/payments/payments',
                ImageUrl: '/image/payment.png',
            },
            { 
                title: '고객', 
                description: '', 
                key: 'customers', 
                route: '/customers/customers',
                ImageUrl: '/image/customer.png',
            },
            { 
                title: '메시지', 
                description: '', 
                key: 'messages', 
                route: '/customers/messages',
                ImageUrl: '/image/message.png',
            },
        ],
    }),
    
    async created() {
      var path = document.location.href.split("#/")
      this.urlPath = path[1];

    },
    watch: {
        cards(newCards) {
            this.flipped = new Array(newCards.length).fill(false);
        },
    },

    mounted() {
        var me = this;
        me.components = this.$ManagerLists;
    },

    methods: {
        openSideBar(){
            this.sideBar = !this.sideBar
        },
        changeUrl() {
            var path = document.location.href.split("#/")
            this.urlPath = path[1];
            this.flipped.fill(false);
        },
        goHome() {
            this.urlPath = null;
        },
        flipCard(index) {
            this.$set(this.flipped, index, true);
        },
        unflipCard(index) {
            this.$set(this.flipped, index, false);
        },
    }
};
</script>
<style>
</style>
