<template>
  <div class="element-outer">
    <header>
      <div class="title">
        <h1>
          Products
        </h1>
      </div>
    </header>
    <main>
      <AddProductUI/>
      <br>
      <ViewProductUI/>
    </main>
  </div>
</template>

<script>
import AddProductUI from "@/components/products/AddProduct";
import ViewProductUI from "@/components/products/ViewProduct";

export default {
  name: "ProductsUI",
  components: {ViewProductUI, AddProductUI}
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