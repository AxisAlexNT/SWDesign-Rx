<template>
  <!--    Create user form:-->
  <div class="element-outer">
    <div class="form-header">
      <h3 id="create-new-user-label">Register new user</h3>
    </div>
    <form name="internal-form" @submit.prevent="onCreateFormSubmit">
      <div class="formbuilder-text form-group field-user_name_field">
        <label class="formbuilder-text-label" for="user-name-field">
          User name:<span class="formbuilder-required">*</span>
        </label>
        <input id="user-name-field" v-model="userName" aria-required="true" class="form-control" maxlength="255"
               name="user-name-field"
               required="required" type="text">
      </div>
      <div class="formbuilder-text form-group field-user_login_field">
        <label class="formbuilder-text-label" for="user-login-field">
          User login:<span class="formbuilder-required">*</span>
        </label>
        <input id="user-login-field" v-model="userLogin" aria-required="true" class="form-control" maxlength="255"
               name="user-login-field"
               required="required" type="text">
      </div>

      <div>
        <label for="currency-select">Choose a preferred currency:<span class="formbuilder-required">*</span></label>
        <select id="currency-select" v-model="currency" name="currency" required="required">
          <option value="RUB">RUB</option>
          <option value="EUR">EUR</option>
          <option value="USD">USD</option>
        </select>
      </div>

      <div>
        <label v-if="(errorMsg === null) && (userId !== null)" id="create_user_result_label"
               class="ok-message">
          User created with ID={{ userId }}
        </label>
        <label v-if="(errorMsg!=null)&&(userId===null)" id="create_user_error_label" class="error-message">
          Error while creating user: {{ errorMsg }}
        </label>
      </div>
      <br>
      <div class="button-field">
        <button type="submit" value="Submit">Create user</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AddUser",
  data: function () {
    return {
      userName: null,
      userLogin: null,
      currency: null,
      userId: null,
      errorMsg: null
    }
  },
  methods: {
    resetAttrs() {
      this.userId = null;
      this.userName = null;
      this.userLogin = null;
      this.currency = null;
      this.errorMsg = null;
    },
    onCreateFormSubmit() {
      let query_payload = {name: this.userName, login: this.userLogin, preferredCurrency: this.currency};
      axios
          .get('/api/v1/users/addUser', {params: query_payload})
          .then(response => {
                this.resetAttrs();
                if (response.data.error === true) {
                  this.errorMsg = response.data["errorMessage"];
                } else {
                  this.userId = response.data["registeredUser"]["id"];
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

#user-name-field {
  margin-left: 10px;
}

</style>