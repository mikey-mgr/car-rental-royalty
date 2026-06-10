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
                        <input type="text" class="form-control"
                                v-model="category.imageUrl"  required/>
                    </div>
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
