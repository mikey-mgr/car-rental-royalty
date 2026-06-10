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
              <div class="input-group">
                <input type="text" class="form-control" v-model="imageUrl" required/>
                <button type="button" class="btn btn-outline-secondary" @click="triggerUpload">Upload</button>
              </div>
            </div>
            <input type="file" ref="fileInput" accept="image/*" style="display:none" @change="handleUpload">
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
      triggerUpload() {
        this.$refs.fileInput.click();
      },
      async handleUpload(e) {
        const file = e.target.files[0];
        if (!file) return;
        const formData = new FormData();
        formData.append('file', file);
        try {
          const res = await axios({
            method: 'post',
            url: `${this.baseURL}/admin/upload`,
            data: formData,
            withCredentials: true
          });
          if (res.data.success && res.data.message) {
            this.imageUrl = res.data.message;
          } else {
            sweetalert({ text: res.data.message || 'Upload failed', icon: 'error' });
          }
        } catch (err) {
          const msg = err.response?.data?.message || err.message || 'Upload failed';
          sweetalert({ text: msg, icon: 'error' });
        }
        e.target.value = '';
      },
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
  
