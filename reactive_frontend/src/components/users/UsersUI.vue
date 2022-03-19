<template>
  <div class="element-outer">
    <header>
      <div class="title">
        <h1>
          Users
        </h1>
      </div>
    </header>
    <main>
      <AddUser/>
      <br>
      <ListUsers/>
    </main>
  </div>
</template>

<script>
import AddUser from "@/components/users/AddUser";
import ListUsers from "@/components/users/ListUsers";

export default {
  name: "UsersUI",
  components: {ListUsers, AddUser}
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