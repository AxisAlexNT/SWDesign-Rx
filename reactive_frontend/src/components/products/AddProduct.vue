<template>
  <!--    Create product form:-->
  <div class="element-outer">
    <div class="form-header">
      <h3 id="create-new-product-label">Create new product</h3>
    </div>
    <form name="internal-form" @submit.prevent="onCreateFormSubmit">
      <div class="formbuilder-text form-group field-product_name_field">
        <label class="formbuilder-text-label" for="product-name-field">
          Product name:<span class="formbuilder-required">*</span>
        </label>
        <input id="product-name-field" v-model="productName" aria-required="true" class="form-control" maxlength="255"
               name="product-name-field"
               required="required" type="text">
      </div>
      <div class="formbuilder-text form-group field-product_login_field">
        <label class="formbuilder-text-label" for="product-description-field">
          Product description:<span class="formbuilder-required">*</span>
        </label>
        <input id="product-description-field" v-model="productDescription" aria-required="true" class="form-control"
               maxlength="255"
               name="product-login-field"
               required="required" type="text">
      </div>

      <div class="formbuilder-text form-group">
        <label class="formbuilder-text-label" for="product-price-field">
          Product price (in RUB):<span class="formbuilder-required">*</span>
        </label>
        <input id="product-price-field" v-model="price" aria-required="true" class="form-control" maxlength="255"
               name="product-login-field"
               required="required" type="text">
      </div>

      <div>
        <label v-if="(errorMsg === null) && (productId !== null)" id="create_product_result_label"
               class="ok-message">
          Product created with ID={{ productId }}
        </label>
        <label v-if="(errorMsg!=null)&&(productId===null)" id="create_product_error_label" class="error-message">
          Error while creating product: {{ errorMsg }}
        </label>
      </div>
      <br>
      <div class="button-field">
        <button type="submit" value="Submit">Create product</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AddProduct",
  data: function () {
    return {
      productName: null,
      productDescription: null,
      currency: null,
      productId: null,
      errorMsg: null
    }
  },
  methods: {
    resetAttrs() {
      this.productId = null;
      this.productName = null;
      this.productDescription = null;
      this.price = null;
      this.errorMsg = null;
    },
    onCreateFormSubmit() {
      let query_payload = {
        name: this.productName,
        description: this.productDescription,
        priceRub: (Math.round(this.price * 100)) // Not the best practice, I acknowledge :)
      };
      axios
          .get('/api/v1/products/addProduct', {params: query_payload})
          .then(response => {
                this.resetAttrs();
                if (response.data.error === true) {
                  this.errorMsg = response.data["errorMessage"];
                } else {
                  this.productId = response.data["product"]["id"];
                }
              }
          )
          .catch(err => {
                this.resetAttrs();
                this.errorMsg = err.response.data;
              }
          );
    }
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

.element-outer {
  text-align: justify;
  border-radius: 10px;
  border-width: 1px;
  border-color: gray;
  border-style: solid;
  padding: 10px;
  width: 80%;
  margin: 20px;
}

.form-control {
  margin: 2px;
}

#product-name-field {
  margin-left: 10px;
}

</style>