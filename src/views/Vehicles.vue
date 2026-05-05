<template>
    <div class="container ">
        <div v-if="!search && !notFound" class="row">
        <div class="col-12 text-center">
          <h2 class="pt-3 pb-3">All Vehicles</h2>
        </div>
      </div>
      <div v-if="search && !notFound" class="row">
        <div class="col-12 text-center">
          <h2 class="pt-3 pb-3">Search results for '{{ text }}'</h2>
        </div>
      </div>
      <div v-if="notFound" class="row">
        <div class="col-12 text-center">
          <h2 class="pt-3 pb-3">No vehicles found for '{{ text }}'</h2>
        </div>
      </div>
      <div class="row justify-content-end      p-3">

        <!--      Search Bar-->
        <form class="form col-lg-4" @submit="searchVehicle">
            <div class="input-group">
            <input
              size="40"
              v-model="searchText"
              type="text"
              class="form-control"
              placeholder="Search Vehicles"
              aria-label="Username"
              aria-describedby="basic-addon1"
              list="vehicleListOptions" required
            />
            <datalist id="vehicleListOptions">
                <option value="NISSAN VERSA NOTE (2014)"></option>
                <option value="Ford Everest 2023"></option>
                <option value="LAND ROVER DISCOVERY 4"></option>
                <option value="TOYOTA CAMRY (2010)"></option>
                <option value="MERCEDES BENZ GLE 2022"></option>
                <option value="FIT NEW SHAPE HYBRID (2014)"></option>
            </datalist>
            <a v-if="text" @click="clearSearch" class="input-group-text bi bi-trash" style="background-color: white;"></a>
            <div class="input-group-prepend">
              <button type="submit" class="input-group-text font-weight-bold h-100" id="search-button-navbar">
                  <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  fill="currentColor"
                  class="bi bi-search"
                  viewBox="0 0 16 16">
                  <path
                      d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"
                  />
                  </svg>
              </button>
            </div>
          </div>
        </form>
      </div>
        <div class="row justify-content-evenly">
            <div v-for="product of vehicles" :key="product.id"
                 class="col-md-6 col-xl-4 col-12 pt-3 d-flex">
                <ProductBox :role="role" :product="product" :users="users" :categoryName="productCategory(product, categories)"/>
            </div>
        </div>
    </div>
</template>

<script>
import ProductBox from '@/components/ProductBox.vue';
import axios from 'axios';
export default {
    name: 'VehiclesView',
    props: ['products', "baseURL", "users", "categories"],
    components: {ProductBox},
    data(){
        return{
            searchText: null,
            text: null,
            vehicles: this.products,
            search: false,
            notFound: false,
            role: null
        }
    },
    methods:{
        async searchVehicle(e){
            e.preventDefault();
            await axios.get(`${this.baseURL}/product/find/?name=${this.searchText}`)
            .then((res) =>{
                this.vehicles = res.data;
                this.search = true;
                this.notFound = false;
                this.text = this.searchText;
               
            }).catch((err) => {
                console.log('err', err);
                this.vehicles = null;
                this.text = this.searchText;
                this.notFound = true;
            });
        },
        clearSearch(){
          this.search = false;
          this.notFound = false;
          this.text = null;
          this.searchText = null;
          this.vehicles = this.products;
        },
        productCategory(product, categories){
            let categoryName = "";
            if(categories)
                for(let j=0; j < categories.length; j++){
                    if(product.categoryId == categories[j].id){
                        categoryName = categories[j].categoryName;
                    }
                }
            return categoryName;
        },
    },
    mounted() {
      this.role = localStorage.getItem("role");
    },
}
</script>

<style scoped>
.bg-body-tertiary{
  background-color: rgb(16, 32, 64);
}
#search-button-navbar{
  background-color: #c18e32;
  border-radius: 0 0.5rem 0.5rem 0;
}
div{
  transition: all 1s ease-in-out 0s;
}
</style>