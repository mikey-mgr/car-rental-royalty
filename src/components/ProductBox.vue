<template>
    <div class="card h-100 w-100 card-hover" style="width: 18rem; min-height: 18rem;">
        <div class="embed-responsive embed-responsive-16by9 image-container">
            <!-- dynamic image -->
            <img
                class="card-img-top embed-responsive-item card-image"
                :src="resolvedImageUrl"
                alt="Vehicle Image"
                title="Click on the Vehicle name to view details"
            />
            <!-- static image -->
            <!-- <img
                class="card-img-top embed-responsive-item"
                src="../assets/AppImages/cars/benz.jpg"
                alt="Vehicle image"
                title="Click on the Vehicle name to view details"
            /> -->
        </div>
        <div class="card-body">
                <router-link :to="{name:'ProductDetails', params: {id: product.id, name: product.name}}" title="Click to view more details">
                    <h5 class="card-title">{{ product.name }}</h5>
                </router-link>
            <div class="mb-2">
              <BookingStatusBadge :status="product.bookingStatus" />
            </div>
            <!-- <strong class="card-text">${{ product.price }}.00</strong> -->
            <p class="card-text">
                {{ descriptionPreview }}
            </p>
            <div class="card-actions">
              <router-link :to="{name: 'EditProduct', params: {id: product.id}}"
                  v-show="$route.name == 'AdminProduct' || role == 'ADMIN'" >
                  <button class="btn btn-primary edit-prod mr-2">Edit</button>
              </router-link>
              <router-link :to="{name:'ListProducts', params: {id: product.categoryId}}" title="Click to view the category">
                  <h5 class="float-right">{{ categoryName }}</h5>
              </router-link>
            </div>
        </div>
    </div>
</template>
<script>
import { resolveImageUrl } from "@/utils/resolveImageUrl";
import BookingStatusBadge from "@/components/BookingStatusBadge.vue";

export default {
    name: "ProductBox",
    components: { BookingStatusBadge },
    props: ["product", "role", "categoryName"],
    data() {
        return {

        }
    },
    computed: {
        resolvedImageUrl() {
            return resolveImageUrl(this.product?.imageURL);
        },
        descriptionPreview() {
            const d = this.product?.description;
            if (!d) {
                return "";
            }
            return d.length > 65 ? `${d.substring(0, 65)}...` : d;
        },
    },
    methods:{

    }
}
</script>
<style scoped>
    .card-hover {
        transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        overflow: hidden;
    }
    
    .card-hover:hover {
        transform: translateY(-12px);
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
    }
    
    .image-container {
        overflow: hidden;
        position: relative;
    }
    
    .card-image {
        transition: transform 0.6s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    }
    
    .card-hover:hover .card-image {
        transform: scale(1.15);
    }
    
    .card-img-top {
        object-fit: cover;
    }
    .card-body {
        display: flex;
        flex-direction: column;
    }
    .card-actions {
        margin-top: auto;
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 8px;
    }
    a {
        text-decoration: none;
    }
    .edit-prod{
      background-color: #c18e32;
      color: white;
      border-color: #c18e32;
    }
    .float-right{
        font-size: medium;
        margin: 0;
    }
</style>