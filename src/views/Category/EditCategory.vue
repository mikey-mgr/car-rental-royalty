<template>
    <div class="container" v-if="users">
        <div class="row justify-content-center">
            <div class="col-md-5 col-10">
                <h4 class="pt-3"> Edit Category</h4>
                <form v-if="category">
                    <div class="form-group">
                        <label>Category Name</label>
                        <input type="text" class="form-control"
                                v-model="category.categoryName"  required/>
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <input type="text" class="form-control"
                                v-model="category.description"  required/>
                    </div>
                    <div class="form-group">
                        <label>Image URL</label>
                        <div class="input-group">
                            <input type="text" class="form-control"
                                    v-model="category.imageUrl"  required/>
                            <button type="button" class="btn btn-outline-secondary" @click="triggerUpload">Upload</button>
                        </div>
                    </div>
                    <input type="file" ref="fileInput" accept="image/*" style="display:none" @change="handleUpload">
                    <button type="button" class="btn btn-primary mt-3" @click="editCategory" >Submit</button>
                </form>
            </div>
        </div>
    </div>
</template>
<script>
    import axios from 'axios'
    import swal from 'sweetalert'
    export default {
        props: ["baseURL", "categories","users"],
        data() {
            return {
                category: null,
                id: null
            }
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
                    const res = await axios.post(this.baseURL + '/admin/upload', formData);
                    if (res.data.success && res.data.message) {
                        this.category.imageUrl = res.data.message;
                    } else {
                        swal({ text: res.data.message || 'Upload failed', icon: 'error' });
                    }
                } catch (err) {
                    const msg = err.response?.data?.message || err.message || 'Upload failed';
                    swal({ text: msg, icon: 'error' });
                }
                e.target.value = '';
            },
            async editCategory() {
                delete this.category["products"]
                // console.log('category', this.category)
                await axios.post(`${this.baseURL}/category/update/${this.id}`, this.category)
                .then(() => {
                    this.$emit("fetchData");
                    this.$router.push({name: 'AdminCategory'})
                    swal({
                        text: "Category has been updated successfully",
                        icon: "success"
                    })
                }).catch(() => undefined);
            }
        },
        mounted() {
            this.id = this.$route.params.id;
            this.category = this.categories.find(category => category.id == this.id)
        }
    }
</script>

<style scoped>
.form-group{
    padding-top: 14px;
}
</style>
