<template>
  <div class="element-outer">
    <div class="form-header">
      <h3 id="edit-new-ticket-label">Current exchange rates</h3>
    </div>
    <form action="#" name="internal-form" @submit.prevent="">
      <label v-if="(errorMsg === null) && ((exchangeRateStr === null))">
        Querying exchange rates...
      </label>
      <label v-if="(errorMsg === null) && (exchangeRateStr !== null) && (exchangeRateStr !== '')"
             class="ok-message">
        {{ exchangeRateStr }}
      </label>
      <br>
      <label v-if="(errorMsg!=null)&&(exchangeRateStr === null)" class="error-message">
        Error: {{ errorMsg }}
      </label>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ExchangeRates",
  data: function () {
    return {
      exchangeRateStr: null,
      errorMsg: null,
    }
  },
  methods: {
    resetAttrs() {
      this.exchangeRateStr = null;
      this.errorMsg = null;
    },
    getRates() {
      axios
          .get('/api/v1/currency/rates', {})
          .then(response => {
                this.resetAttrs();
                this.exchangeRateStr = response.data;
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
    this.getRates();
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
  margin: 10px;
}

</style>