<template>
    <div class="container p-4">
        <div class="row">
            <div class="col-12 text-center">
                <h3 class="pt-3">
                    Your Car Rental Bookings
                </h3>
                <!-- loop over cart items -->
                <div class="mt-2 pt-3"></div>
                <hr>
                    <div v-for="cartItem in usrCartItems" 
                        :key="cartItem.id"
                        class="row mt-2 d-flex justify-content-evenly">
                        <div class="col-md-3 embed-responsive-16by9 justify-content-around">
                            <!-- dynamic image -->
                            <img :src="productImage(cartItem.product)" alt="vehicle image" class="w-100 card-image-top embed-responsive-item">

                            <!-- static image -->
                            <!-- <img src="../assets/AppImages/cars/demio.jpg" alt="Vehicle image" 
                            class="w-100 card-image-top embed-responsive-item"
                            style="object-fit: cover;"> -->
                        </div>
                        <!-- display product name and qty -->
                        <div class="col-md-5 px-3 justify-content-evenly p-3 pb-0">
                            <div class="card-block justify-content-evenly">
                                <h6 class="card-title">
                                    <router-link class="text-decoration-none text-bg-warning rounded rounded-pill px-2" :to="{name:'ProductDetails', params: {id: cartItem.product.id, name: cartItem.product.name}}"
                                    >{{ cartItem.product.name }}
                                    </router-link>
                                </h6>
                                <p class="mb-0 font-weight-bold" id="item-price">$ {{ cartItem.product.price }} per day</p>
                                <p class="mb-0" style="float: left;">Number of days: {{ cartItem.quantity }}</p>
                                <p class="mb-0" style="float: right;">
                                    Total : 
                                    <span class="font-weight-bold">
                                        $ {{ cartItem.quantity * cartItem.product.price }}
                                    </span>
                                </p>
                                <br>
                                
                            </div><a href="#" @click="deleteItem(cartItem.id)" class="align-self-end text-decoration-none">
                                    Remove booking
                                </a>
                        </div>
                        <div class="col-2"></div>
                        <div class="col-12"><hr></div>
                    </div>
                <!-- display total price -->
                <div class="total-cost pt-2 text-end">
                    <h5 class ="text-white" v-if="usrTotalCost">Total : ${{ usrTotalCost }}</h5>
                    <button @click="bookRental" v-if="usrTotalCost" class="btn btn-primary confirm">Confirm Booking</button>
                </div>
                <div class="col-12 mt-5">
                    <h6 v-if="!usrCartItems">Cart is Empty.<br>Visit our 
                        <router-link :to="{name: 'VehiclesView'}">Vehicles</router-link> page to book some vehicles.
                    </h6>
                </div>
            </div>
        </div>
        
    </div>
</template>

<script>
import axios from 'axios';
import swal from 'sweetalert';
import { resolveImageUrl } from '@/utils/resolveImageUrl';

export default {
    name: 'CartView',
    props: ["baseURL", "usrCartItems", "usrTotalCost"],

    data() {
        return {
            
        }
    },

    methods:{
        productImage(product) {
            return resolveImageUrl(product?.imageURL);
        },
        //book a vehicle
        bookRental(){
            this.$router.push({name: 'CheckoutPage'});
        },

        //delete a cart item
        deleteItem(id){
            axios.delete(`${this.baseURL}/cart/delete/${id}`, { withCredentials: true })
            .then((res) => {
                if(res.status == 200){
                    this.$router.go(0);
                } else {
                    swal({
                        text: "Item not deleted",
                        icon: "error"
                    })
                }
            }).catch((err) => console.log('err', err));
        }
    },
    mounted() {
        // Token-based auth removed; server session used instead
        this.$emit("usersInfo")
    },
}
</script>

<style scoped>
h4, h5{
    color: #484848;
    font-size: 700;
}

</style>