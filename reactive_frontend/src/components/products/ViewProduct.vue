<template>
  <div class="element-outer">
    <div class="form-header">
      <h3 id="create-new-product-label">List all products</h3>
    </div>
    <form action="#" name="internal-form" @submit.prevent="">
      <div class="form-group">
        <label class="formbuilder-text-label" for="client-name-field">
          View as user with id:<span class="formbuilder-required">*</span>
        </label>
        <input id="client-name-field" v-model="userId" aria-required="true" class="form-control" maxlength="255"
               name="client-name-field"
               required="required" type="number">
      </div>
      <button type="submit" @click="onQuery()">
        Query all products
      </button>
    </form>
    <p v-if="errorMsg !== null" class="error-message">Error: {{ errorMsg }}</p>
    <div v-if="userId !== null && errorMsg === null">
      <ProductContainer v-for="productId in productIds" :key="productId" v-bind:product-id="productId"
                        v-bind:user-id="this.userId"/>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import ProductContainer from "@/components/products/ProductContainer";

export default {
  name: "ViewProduct",
  components: {ProductContainer},
  methods: {
    onQuery() {
      this.productIds = null;
      axios
          .get('/api/v1/products/productIds', {params: {userId: this.userId}})
          .then(response => {
                this.errorMsg = null;
                this.productIds = response.data;
              }
          )
          .catch(err => {
                this.productIds = null;
                this.errorMsg = err.response.data;
              }
          );
    }
  },
  data: function () {
    return {
      userId: null,
      productIds: null,
      errorMsg: null
    }
  }
}
</script>

<style scoped>
.error-message {
  color: red;
}

.element-outer {
  text-align: justify;
  border-radius: 10px;
  border-width: 1px;
  border-color: black;
  border-style: solid;
  padding: 10px;
  width: 80%;
  margin: 20px;
}
</style>