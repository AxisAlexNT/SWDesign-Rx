<template>
  <div class="element-outer">
    <div class="form-header">
      <h3 id="create-new-user-label">List all users</h3>
    </div>
    <form action="#" name="internal-form" @submit.prevent="">
      <button type="submit" @click="onQuery()">
        Query all users
      </button>
    </form>
    <p v-if="errorMsg !== null" class="error-message">Error: {{ errorMsg }}</p>
    <div v-if="userIds !== null && errorMsg === null">
      <UserContainer v-for="userId in userIds" :key="userId" v-bind:user-id="userId"/>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import UserContainer from "@/components/users/UserContainer";

export default {
  name: "ListUsers",
  components: {UserContainer},
  methods: {
    onQuery() {
      this.userIds = null;
      axios
          .get('/api/v1/users/userIds', {})
          .then(response => {
                this.errorMsg = null;
                this.userIds = response.data;
              }
          )
          .catch(err => {
                this.userIds = null;
                this.errorMsg = err.response.data;
              }
          );
    }
  },
  data: function () {
    return {
      userId: null,
      userIds: null,
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