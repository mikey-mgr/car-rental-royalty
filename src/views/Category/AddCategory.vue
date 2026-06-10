<template>
    <div v-if="users" class="container">
      <div class="row justify-content-center">
        <div class="col-md-5 col-10">
          <h3 class="pt-3">Add Category</h3>
          <form @submit="addCategory">
            <div class="form-group">
              <label>Name</label>
              <input type="text" class="form-control" v-model="categoryName" required/>
            </div>
            <div class="form-group">
              <label>Description</label>
              <textarea type="text" class="form-control" v-model="description" required/>
            </div>
            <div class="form-group">
              <label>Image</label>
              <input type="text" class="form-control" v-model="imageUrl" required/>
            </div>
            <button type="submit" class="btn btn-primary btn-add-cat mt-4">
              Add Category
            </button>
          </form>
        </div>
      </div>
    </div>
  </template>
  <script>
  const axios = require("axios");
  const sweetalert = require("sweetalert");
  export default {
    props: ["users", "baseURL"],
    data() {
      return {
        categoryName: "",
        description: "",
        imageUrl: "",
      };
    },
    methods: {
      addCategory(e) {
        e.preventDefault();
        // console.log(this.categoryName, this.description);
        const newCategory = {
          categoryName: this.categoryName,
          description: this.description,
          imageUrl: this.imageUrl,
        };
  
        axios({
          method: "post",
          url: `${this.baseURL}/category/create`,
          data: JSON.stringify(newCategory),
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true
        })
          .then(() => {
            sweetalert({
              text: "Category added successfully",
              icon: "success",
            });
            this.$router.push({ name: 'AdminCategory' });
          })
          .catch(() => undefined);
      },
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
.form-group{
  padding-top: 14px;
}
</style>
  
