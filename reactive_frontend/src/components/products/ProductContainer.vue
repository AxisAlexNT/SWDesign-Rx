<template>
  <div class="container-block">
    <h3 class="container-header">Product #{{ productId }} viewed by user #{{ userId }}</h3>
    <p v-if="(errorMsg === null) && (product === null)"
       class="fetching-placeholder">
      Fetching info...
    </p>
    <p v-if="errorMsg !== null" class="error-message">Error: {{ errorMsg }}</p>
    <div v-if="(errorMsg === null) && (product !== null)" class="element-info">
      <p>Product ID: {{ product.id }}</p>
      <p>Product name: {{ product.name }}</p>
      <p>Product description {{ product.description }}</p>
      <p>Product price: {{ product.price }} {{ product.currency }}</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ProductContainer",
  props: {
    productId: Number,
    userId: Number
  },
  data: function () {
    return {
      product: null,
      errorMsg: null
    }
  },
  methods: {
    resetAttrs() {
      this.product = null;
      this.errorMsg = null;
    },
    getInfo() {
      let query_payload = {productId: this.productId, userId: this.userId};
      axios
          .get('/api/v1/products/product', {params: query_payload})
          .then(response => {
                this.resetAttrs();
                if (response.data['errorMessage'] !== null) {
                  this.errorMsg = response.data['errorMessage'];
                } else {
                  this.product = response.data;
                }
              }
          )
          .catch(err => {
                this.resetAttrs();
                this.errorMsg = err.response.data;
              }
          );
    }
  },
  beforeMount() {
    this.getInfo();
  }
}
</script>

<style scoped>
.ok-message {
  color: limegreen;
}

.error-message {
  color: red;
}

.container-block {
  text-align: justify;
  border-radius: 10px;
  border-width: 1px;
  border-color: gray;
  border-style: solid;
  padding: 10px;
  width: 80%;
  margin: 20px;
}
</style>