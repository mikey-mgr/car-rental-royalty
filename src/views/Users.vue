<template>
    <div class="container">
        <h3 class="pt-4 pb-4 text-center ff-bold">Users List</h3>
        <ol class="list-group list-group-numbered">
            <p class="mt-2 pt-2">Total Cost: <span class="fw-bold">$ {{ totalCost }}</span></p>
            <li v-for="user of users" :key="user.id"
                class="list-group-item d-flex justify-content-between align-items-start">
                <div class="ms-2 me-auto">
                    <div class="fw-bold ff-bold">{{ user.firstName }} {{ user.lastName }}</div>
                    {{ user.email }}<br>
                    <div v-for="cart of cartItems" :key="cart.id">
                        <div v-if="cart.userId == user.id"><hr class="border border-black border-2">
                            {{cart.product.name}}, <span class="fw-bold text-end">${{ cart.product.price }}/day</span><br>
                            Pickup: {{ cart.bookedFrom || '-' }}<br>
                            Dropoff: {{ cart.bookedFor }} {{ cart.dropoffTime ? 'at ' + cart.dropoffTime : '' }}<br>
                            Days: {{ cart.quantity }} &middot; Total: <span class="fw-bold">${{ (cart.product.price * cart.quantity).toFixed(2) }}</span>
                        </div>
                    </div>
                </div>
            </li>
            
        </ol>
    </div>
</template>

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

.ff-bold{
  font-family: var(--font-akrobat-bold);
}
.ff-semibold{
  font-family: var(--font-akrobat-semibold);
}
.ff-regular{
  font-family: var(--font-akrobat-regular);
}
.ff-light{
  font-family: var(--font-akrobat-light);
}

</style>