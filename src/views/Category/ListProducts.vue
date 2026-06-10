<template>
    <div class="container text-text-start">
        <div class="row justify-content-center align-items-center">
            <div class="col-12 text-center px-4">
                <h4 class="pt-4 pb-4">{{ category.categoryName }}</h4>
                <router-link v-show="role=='ADMIN'" :to="{name: 'AddProduct'}" style="float: right">
                    <button class="btn btn-add-prod" >
                        Add Vehicle
                    </button>
                </router-link>
            </div>
            
            <div class="row align-items-center justify-content-center">
                <!-- <div v-for="(product, productSize) in filteredProds" :key="productSize"> -->
                <div v-for="product of filteredProds" :key="product.id"
                    class="col-md-6 col-xl-4 col-12 pt-3 justify-content-around d-flex">
                    <ProductBox :product="product" :role="role"></ProductBox>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import ProductBox from '@/components/ProductBox.vue';
import axios from 'axios';

export default {
    name: 'ListProducts',
    props: ["products", "categories", "baseURL"], 
    components: { ProductBox },

    data() {
        return {
            category: "",
            filteredProds: "",
            id: "",
            role: null
        };
    },
    methods: {
        async getProducts(){
            //API call to get FilteredProducts
            axios({
                method: "get",
                url: `${this.baseURL}/category/show/${this.id}`,
                data: JSON.stringify(this.filteredProds),
                headers: {
                    "Content-Type": "application/json",
                },
                 withCredentials: true 
            }).then(result => {
                this.filteredProds = result.data
            }).catch(() => undefined);
        }
    },

    mounted() {
        this.id = this.$route.params.id;
        this.category = this.categories.find(category => category.id == this.id);
        // this.filteredProds = this.products.find(filteredProds => filteredProds.categoryId == this.id);
        this.getProducts();
        this.role = localStorage.getItem("role");
    },
}
</script>

<style scoped>

.btn-add-prod{
  border-color: #c18e32;
  background-color: #c18e32;
  color: white;
}
.btn-add-prod:hover{
  color: black;
  background-color: white;
}
</style>
