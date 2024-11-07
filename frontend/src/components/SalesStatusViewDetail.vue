<template>

    <v-card outlined>
        <v-card-title>
            SalesStatus # {{$route.params.id }}
        </v-card-title>

        <v-card-text>
            <div>
                <Number label="주문ID" v-model="item.orderId" :editMode="editMode" @change="change" />
            </div>
            <div>
                <String label="상품ID" v-model="item.productId" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Boolean label="고객 성별" v-model="item.customerGender" :editMode="editMode" @change="change" />
            </div>
            <div>
                <String label="고객 지역" v-model="item.customerRegion" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Boolean label="보험" v-model="item.insurance" :editMode="editMode" @change="change" />
            </div>
            <div>
                <String label="제조업체" v-model="item.manufacturer" :editMode="editMode" @change="change" />
            </div>
            <div>
                <String label="휴대폰 색상" v-model="item.phoneColor" :editMode="editMode" @change="change" />
            </div>
        </v-card-text>
    </v-card>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'SalesStatusViewDetail',
    props: {
      value: Object,
    },
    data: () => ({
        item : [],
    }),
    async created() {
      var params = this.$route.params;
      var temp = await axios.get(axios.fixUrl('/salesStatuses/' + params.id))

      this.item = temp.data;

    },
    methods: {
    }
  }
</script>

