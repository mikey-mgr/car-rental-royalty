<template>
    <div class="container mt-4">
        <div class="row">
            <div class="col-12 text-center">
                <h4 v-if="userProducts.length" class="pt-3">Your Wishlist</h4>
            </div>
        </div>
        <!-- display products -->
        <div class="row justify-content-center">
            <div v-for="product of userProducts" :key="product.id"
            class="col-md-6 col-xl-4 col-12 pt-3 d-flex">
                <ProductBox :product="product"/>
            </div>
        </div>
        <div class="col-12 mt-5 text-center">
            <h4 v-if="!userProducts.length">WishList is Empty.</h4>
        </div>
    </div>
</template>

<!-- {
    "id": 3,
    "productId": 23,
    "userId": 1,
    "createdDate": "2024-02-19T18:41:28.943+00:00"
  }, -->
<script>
import axios from 'axios';
import ProductBox from '@/components/ProductBox.vue';
import swal from 'sweetalert';

export default {
    name: 'WishList',
    props: ["baseURL", "products"],
    component: {ProductBox},
    data() {
        return {
            wishlists: null,
            userProducts: [],
        };
    },
    methods: {
        //get all items in wishlist
        fetchWishlist() {
            axios.get(`${this.baseURL}/wishlist/`, { withCredentials: true })
                .then((result) => {
                this.wishlists = result.data;
                if(this.wishlists!=null){
                for(let i = 0;i < this.products.length;this.products[i++]){
                    for(let j = 0;j < this.wishlists.length;this.wishlists[j++]){
                        if(this.products[i].id == this.wishlists[j].productId){
                            this.userProducts.push(this.products[i]);
                        }
                    }
                }
            }
            }).catch(() => undefined);
        },
        //delete a wishlist item
        deleteItem(id){
            axios.delete(`${this.baseURL}/wishlist/delete/${id}`, { withCredentials: true })
            .then((res) => {
                if(res.status == 200){
                    this.$router.go(0);
                } else {
                    swal({
                        text: "Item not deleted",
                        icon: "error"
                    })
                }
            }).catch(() => undefined);
        }
    },
    mounted() {
        // Token-based auth removed; requests use browser cookies (withCredentials)
        this.fetchWishlist();
    },
    components: { ProductBox }
}
</script>


<style scoped>

</style>
