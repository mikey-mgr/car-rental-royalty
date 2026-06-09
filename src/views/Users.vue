<template>
    <div class="container m-3">
        <h3 class="pt-4 pb-4 text-center">Users List</h3>
        <ol class="list-group list-group-numbered mx-1 p-1">
            <p class="mt-2 pt-2">Total Cost: <span class="fw-bold">$ {{ totalCost }}</span></p>
            <li v-for="user of users" :key="user.id"
                class="list-group-item d-flex justify-content-between align-items-start">
                <div class="ms-2 me-auto">
                    <div class="fw-bold">{{ user.firstName }} {{ user.lastName }}</div>
                    {{ user.email }}<br>
                    <div v-for="cart of cartItems" :key="cart.id">
                        <div v-if="cart.userId == user.id"><hr class="border border-black border-2">
                            {{cart.product.name}}, <span class="fw-bold text-end">${{ cart.product.price }}</span><br>
                            Number of days: {{ cart.quantity }}<br>
                            Booked for: {{ cart.bookedFor }}
                        </div>
                    </div>
                </div>
            </li>
            
        </ol>
    </div>
</template>
<!-- 
"id": 0,
"firstName": "string",
"lastName": "string",
"email": "string",
"role": 0 
{
  "cartItems": [
    {
      "id": 0,
      "userId": 0,
      "quantity": 0,
      "product": {
        "id": 0,
        "name": "string",
        "imageURL": "string",
        "price": 0,
        "description": "string",
        "bookingStatus": "string"
      },
      "bookedFor": "2024-02-18"
    }
  ],
  "totalCost": 0
}
-->
<script>

export default {
    name: 'UsersView',
    props: ["users", "products", "cartItems", "totalCost", "wishlists"],
    return:{
        data(){
            return{
                userList: {},
                wishlistCount: 0
            }
        }
    },
    methods: {
        
    },
    mounted(){
        this.$emit("adminInfo")
    },
}
</script>

<style>


</style>