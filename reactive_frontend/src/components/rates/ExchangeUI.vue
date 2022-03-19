<template>
  <div class="element-outer">
    <header>
      <div class="title">
        <h1>
          Exchange UI
        </h1>
      </div>
    </header>
    <main>
      <ExchangeRates/>
    </main>
  </div>
</template>
<script>
import ExchangeRates from "@/components/rates/ExchangeRates";

export default {
  name: "ExchangeUI",
  components: {ExchangeRates}
}
</script>

<style scoped>
.title {
  text-align: center;
}

.element-outer {
  text-align: justify;
  border-radius: 10px;
  border-width: 1px;
  border-color: gray;
  border-style: solid;
  width: 80%;
}
</style>