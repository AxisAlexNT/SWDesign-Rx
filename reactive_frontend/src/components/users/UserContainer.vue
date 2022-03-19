<template>
  <div class="container-block">
    <h3 class="container-header">User #{{ userId }}</h3>
    <p v-if="(errorMsg === null) && (user === null)"
       class="fetching-placeholder">
      Fetching info...
    </p>
    <p v-if="errorMsg !== null" class="error-message">Error: {{ errorMsg }}</p>
    <div v-if="(errorMsg === null) && (user !== null)" class="element-info">
      <p>User ID: {{ user.id }}</p>
      <p>User name: {{ user.name }}</p>
      <p>Preferred currency {{ user.preferredCurrency }}</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "UserContainer",
  props: {
    userId: Number
  },
  data: function () {
    return {
      user: null,
      errorMsg: null
    }
  },
  methods: {
    resetAttrs() {
      this.user = null;
      this.errorMsg = null;
    },
    getInfo() {
      let query_payload = {userId: this.userId};
      axios
          .get('/api/v1/users/user', {params: query_payload})
          .then(response => {
                this.resetAttrs();
                if (response.data['errorMessage'] !== null) {
                  this.errorMsg = response.data['errorMessage'];
                } else {
                  this.user = response.data['user'];
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