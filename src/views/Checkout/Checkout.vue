<template>
    <div class="text-center div_class">
        <h3>You will be redirected to payment page</h3>
        <div class="alert alert-primary" role="alert">While making payment use card number
            4242 4242 4242 4242 and enter a random date and cvv (3 digits)
        </div>
        <button class="btn btn-primary checkout-button" @click="goToCheckout">Make Payment</button>
    </div>
    
</template>

<script>
import axios from 'axios';

export default {
    name: 'CheckoutPage',
    props: ["baseURL"],
    data(){
        return{
            stripeAPIToken: '',
            stripe: '',
            checkoutBodyArray: []
        }
    },
    methods: {
        getAllItems(){
            axios.get(`${this.baseURL}/cart/`, { withCredentials: true })
            .then((response) => {
                if(response.status == 200){
                    let products = response.data;
                    for(let i=0; i <products.cartItems.length; i++){
                        this.checkoutBodyArray.push({
                            price: products.cartItems[i].product.price,
                            productName: products.cartItems[i].product.name,
                            quantity: products.cartItems[i].quantity,
                            productId: products.cartItems[i].product.id,
                        });
                    }
                }
            }).catch(() => undefined);
        },
        goToCheckout(){
            axios.post(`${this.baseURL}/create-checkout-session`, this.checkoutBodyArray)
            .then((response) => {
                localStorage.setItem('sessionId', response.data.sessionId);
                // this.stripe.redirectToCheckout({sessionId: response.data.sessionId})
            }).catch(() => undefined);

        }
    },
    mounted(){
        this.stripe = window.Stripe(this.stripeAPIToken);
        // run code: npm i stripe
        this.getAllItems();
    }
}
</script>

<style scoped>
.alert{
    width: 50%;
    margin-left: 25%;
}
.div_class{
    margin-top: 5%;
}
.checkout-button{
    background-color: #5d3dec;
    border-color: #5d3dec;
    margin: 10px;
}
</style>
