<template>
<!--  <img alt="Vue logo" src="./assets/logo.png">-->
  <ExchangeUI/>
  <br>
  <UsersUI/>
  <br>
  <ProductsUI/>
</template>

<script>
import ExchangeUI from "@/components/rates/ExchangeUI";
import UsersUI from "@/components/users/UsersUI";
import ProductsUI from "@/components/products/ProductsUI";

export default {
  name: 'App',
  components: {
    ProductsUI,
    UsersUI,
    ExchangeUI
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
