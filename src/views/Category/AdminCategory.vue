<template>
    <div class="container py-4" v-if="users">
      <div class="row">
        <div class="col-12 text-center">
          <h3 class="pt-4 pb-4">Admin Categories</h3>
          <div class="d-flex justify-content-end gap-2 mb-2">
            <router-link :to="{ name: 'AddCategory' }">
              <button class="btn btn-add-cat">Add Category</button>
            </router-link>
          </div>
        </div>
      </div>
      <div class="row justify-content-evenly">
        <div
          v-for="category of categories"
          :key="category.id"
          class="col-xl-4 col-md-6 col-12 pt-3 d-flex"
        >
          <CategoryBox :category="category"></CategoryBox>
        </div>
      </div>
    </div>
  </template>
  <script>
  import CategoryBox from "../../components/Category/CategoryBox.vue";
  export default {
    name: "AdminCategory",
    props:["categories", "users", "baseURL"],
    components: { CategoryBox },
    data() {
      return {
      };
    },
    methods: {
    },
    mounted(){
      this.$emit("adminInfo")
    },
  };
  </script>
<style scoped>
.btn-add-cat{
  border-color: #c18e32;
  background-color: #c18e32;
  color: white;
}
.btn-add-cat:hover{
  color: black;
  background-color: white;
}
.btn-seed-cat{
  border-color: #6c757d;
  background-color: #6c757d;
  color: white;
}
.btn-seed-cat:hover{
  color: black;
  background-color: white;
}
</style>