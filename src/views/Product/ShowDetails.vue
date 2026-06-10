<template>
<div class="main-div">
    <div class="container p-3" v-if="product">
        <div class="row align-items-center">
            <!-- display image, bootstrap carousel-->
            
            <div class="col-md-6 col-12">
                <div id="autoplayCarousel" class="carousel slide carousel-fade" data-bs-ride="carousel" data-bs-interval="5000">
                    <div class="carousel-inner">
                        <!-- dynamic images -->
                        <div class="carousel-item active">
                            <img :src="resolvedMainImage" class="d-block w-100" alt="Vehicle image" @click="openLightbox(resolvedMainImage)" style="cursor: pointer;">
                        </div>
                        <div v-for="(image, index) in filteredCarouselImages" :key="index" class="carousel-item">
                            <img :src="image" class="d-block w-100" alt="vehicle image" @click="openLightbox(image)" style="cursor: pointer;">
                        </div>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#autoplayCarousel" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#autoplayCarousel" data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </div>
            </div>
            <!-- display product details -->
            <div class="col-md-6 col-12">
                <h4 class="border-bottom border-dark py-2">{{ product.name }}</h4>
                <h6 class="category font-style-italic">{{ category.categoryName }}</h6>
                <h6 class="fw-bold">${{ product.price }} per day</h6>
                <p>{{ product.description }}</p>
                <div class="mb-3">
                  <BookingStatusBadge :status="product.bookingStatus" />
                </div>
                <form @submit="addToCart">
                    <div class="d-flex flex-row justify-content-between">
                        <div class="input-group input-group-parent p-0">
                            <div class="input-group col-md-6 col-lg-5 col-xl-5 col-sm-6 p-0 mb-2">
                                <span class="input-group-text">Pickup date</span>
                                <input type="date" :min="minDate" id="pickup-date-input" class="form-control" v-model="pickupDate" required/>
                            </div>
                            <div class="input-group col-md-6 col-lg-5 col-xl-6 col-sm-6 p-0 mb-2">
                                <span class="input-group-text">Dropoff date</span>
                                <input type="date" :min="minDate" id="dropoff-date-input" class="form-control" v-model="dropoffDate" required/>
                            </div>
                            <div class="input-group col-md-6 col-lg-5 col-xl-5 col-sm-6 p-0 mb-2">
                                <span class="input-group-text">Dropoff time</span>
                                <input type="time" id="dropoff-time-input" class="form-control" v-model="dropoffTime" placeholder="HH:MM" required/>
                            </div>
                            <div class="col-12 p-0">
                                <button type="submit" :disabled="inCart"
                                    class="btn btn-primary add-to-cart-button">{{cartString}}
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
                <div class="features pt-3 mb-3">
                    <h5><strong>Features</strong></h5>
                    <ul class="features-list">
                        <li v-for="(feature, index) in product.features" :key="index">{{ feature }}</li>
                    </ul>
                </div>
                <button v-show="!inWishlist" id="wishlist-button" class="btn mr-3" @click="addToWishlist" :disabled="addedToWishlist">
                    {{wishlistString}}
                </button>
                <button class="btn btn-danger mr-3" style="border-radius: 0%;" v-show="inWishlist" @click="removeWishlist(wishlistId)" :disabled="deletedFromWishlist">
                    {{wishlistString}}
                </button>
                <!-- edit button -->
                <router-link :to="{name: 'EditProduct', params: {id: product.id}}"
                    v-show="role == 'ADMIN'" >
                    <button class="btn edit-prod mr-2">Edit</button>
                </router-link>

                            <!-- Modal to confirm booking -->
                <div class="modal fade" id="bookingModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content text-center">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="staticBackdropLabel">Confirm Booking</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body d-flex justify-content-center pb-2">
                                <p class="m-0 py-3">Your car has been added to cart.<br>View your cart to confirm your booking</p>
                            </div>
                            <div class="modal-footer pt-0">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                <router-link :to="{name: 'CartView'}"><button class="btn btn-info text-light" data-bs-dismiss="modal">View Cart</button></router-link>
                            </div>
                        </div>
                    </div> 
                </div>
                <!-- Modal to direct user to signin page -->
                <div class="modal fade " id="registerModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content text-center">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="staticBackdropLabel">Login or Register</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body d-flex justify-content-center">
                                <p class="m-0 py-3">{{ modalText}}</p>
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-primary" @click="openAuthModalLocal('signup')">Create account</button>
                                <button class="btn btn-primary" @click="openAuthModalLocal('login')">Login</button>
                            </div>
                        </div>
                    </div> 
                </div>

                <!-- Image Lightbox Modal -->
                <div class="modal fade" id="imageLightbox" tabindex="-1" aria-labelledby="imageLightboxLabel" aria-hidden="true" @click="closeLightbox">
                    <div class="modal-dialog modal-dialog-centered modal-xl">
                        <div class="modal-content lightbox-content">
                            <div class="modal-body p-0 position-relative">
                                <button type="button" class="btn-close lightbox-close" @click="closeLightbox" aria-label="Close"></button>
                                <div class="lightbox-image-container" @click.stop>
                                    <img :src="lightboxImage" class="lightbox-image" :style="{ transform: `scale(${zoomLevel})` }" alt="Vehicle image">
                                </div>
                                <div class="zoom-controls">
                                    <button class="btn btn-light zoom-btn" @click.stop="zoomIn" :disabled="zoomLevel >= 3">
                                        <i class="bi bi-zoom-in"></i> +
                                    </button>
                                    <button class="btn btn-light zoom-btn" @click.stop="zoomOut" :disabled="zoomLevel <= 1">
                                        <i class="bi bi-zoom-out"></i> -
                                    </button>
                                    <button class="btn btn-light zoom-btn" @click.stop="resetZoom">
                                        Reset
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div> 
                </div>
            </div>
            <section class="booking-requirements mb-4" aria-labelledby="booking-req-heading">
                  <h5 id="booking-req-heading" class="requirements-heading">Before you book</h5>
                  <p class="requirements-lead text-secondary small mb-3">
                    Please have the following ready. Documents must be current and match the details on your booking.
                  </p>
                  <ul class="requirements-list row">
                    <li class="col-12 col-md-6 col-lg-4">
                      <span class="req-title">Driver's licence</span>
                      <span class="req-detail">Must be valid and held for at least two years.</span>
                    </li>
                    <li class="col-12 col-md-6 col-lg-4">
                      <span class="req-title">Minimum age</span>
                      <span class="req-detail">Primary drivers must be 25 or older.</span>
                    </li>
                    <li class="col-12 col-md-6 col-lg-4">
                      <span class="req-title">Photo ID</span>
                      <span class="req-detail">National ID or passport on hand at pick-up.</span>
                    </li>
                    <li class="col-12 col-md-6 col-lg-4">
                      <span class="req-title">Proof of residence</span>
                      <span class="req-detail">A recent utility bill or official letter showing your current address.</span>
                    </li>
                    <li class="col-12 col-md-6 col-lg-4">
                      <span class="req-title">Travel plans</span>
                      <span class="req-detail">Flight itinerary required when you are arriving from another city or country.</span>
                    </li>
                    <li class="col-12 col-md-6 col-lg-4">
                      <span class="req-title">Deposit &amp; rental charges</span>
                      <span class="req-detail">Security deposit and rental fees must be settled in full before we confirm the vehicle.</span>
                    </li>
                  </ul>
                </section>
        </div>
    </div>
</div>
</template>

<script>
import axios from 'axios';
import swal from 'sweetalert';
import bootstrap from 'bootstrap/dist/js/bootstrap.bundle'
import { resolveImageUrl } from '@/utils/resolveImageUrl';
import BookingStatusBadge from '@/components/BookingStatusBadge.vue';

export default {
    name: "ProductDetails",
    components: { BookingStatusBadge },
    props: ["baseURL", "products", "categories", "userRole"],
    data(){
        return{
            product: {},
            category: {},
            id: "",
            quantity: 1,
            bookedFor: null,
            pickupDate: null,
            dropoffDate: null,
            dropoffTime: null,
            wishlistString: "Add To Wishlist",
            cartString: "Book Now",
            addedToWishlist: false,
            deletedFromWishlist: null,
            inWishlist: false,
            inCart: false,
            cartItems: null,
            wishlist: null,
            wishlistId: null,
            modalText: null,
            role: null,
            lightboxImage: null,
            zoomLevel: 1
        }
    },
    methods: {
        //fetch all items in cart n set cart button to disabled if product is present
        async getCart(){
            await axios.get(`${this.baseURL}/cart/`, { withCredentials: true })
            .then((res) => {
                const result = res.data;
                this.cartItems = result.cartItems;
            }).catch(() => undefined);
            if(this.cartItems!=null){
                for(let i = 0;i < this.cartItems.length;this.cartItems[i++]){
                    if(this.id == this.cartItems[i].product.id){
                        this.inCart = true;
                        this.cartString = "Car booked"
                    }
                }
            }
            //sets button to disabled if vehicle is already reserved
            if(this.product.bookingStatus !='Available'){
                this.inCart = true;
            }
        },
        
        //fetch all items in wishlist n set wishlist button to disabled if product is present
        async getWishlist() {
            await axios.get(`${this.baseURL}/wishlist/`, { withCredentials: true })
            .then((result) => {
                this.wishlist = result.data;
            }).catch(() => undefined);
            if(this.wishlist!=null){
                for(let i = 0; i < this.wishlist.length; this.wishlist[i++]){
                    if(this.id == this.wishlist[i].productId){
                        this.wishlistString = 'Remove from wishlist';
                        this.inWishlist = true;
                        this.wishlistId = this.wishlist[i].id
                    }
                }
            }
        },

        //async call activated when add to cart button is clicked
        async addToCart(e){
            e.preventDefault();
            if(!this.userRole){
                //user isn't logged in
                let modal2 = document.getElementById('registerModal');
                let bsShowModal = new bootstrap.Modal(modal2, {toggle: false});
                bsShowModal.show();
                this.modalText = "Please login or create a new account to add booking";
                return;
            }
            //logged in, so add item to cart
            await axios.post(`${this.baseURL}/cart/add`, 
            {
                productId: this.id,
                quantity: this.quantity,
                bookedFor: this.dropoffDate,
                bookedFrom: this.pickupDate,
                dropoffTime: this.dropoffTime
            }, { withCredentials: true }
            ).then((res) => {
                //check if user is logged in
                if(res.data == "<!DOCTYPE html>\n<html lang=\"en\">\n  <head>\n    <meta charset=\"utf-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n    <meta name=\"description\" content=\"\">\n    <meta name=\"author\" content=\"\">\n    <title>Please sign in</title>\n    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n    <link href=\"https://getbootstrap.com/docs/4.0/examples/signin/signin.css\" rel=\"stylesheet\" integrity=\"sha384-oOE/3m0LUMPub4kaC09mrdEhIc+e3exm4xOGxAmuFXhBNF4hcg/6MiAXAf5p0P56\" crossorigin=\"anonymous\"/>\n  </head>\n  <body>\n     <div class=\"container\">\n      <form class=\"form-signin\" method=\"post\" action=\"/login\">\n        <h2 class=\"form-signin-heading\">Please sign in</h2>\n        <p>\n          <label for=\"username\" class=\"sr-only\">Username</label>\n          <input type=\"text\" id=\"username\" name=\"email\" class=\"form-control\" placeholder=\"Username\" required autofocus>\n        </p>\n        <p>\n          <label for=\"password\" class=\"sr-only\">Password</label>\n          <input type=\"password\" id=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required>\n        </p>\n        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>\n      </form>\n</div>\n</body></html>"){
                    this.$emit('openAuthModal', 'login');
                    swal({
                        text: "Please login or signup",
                        icon: "info"
                    });
                    return;
                }
                if(res.status == 201){
                    let modal1 = document.getElementById('bookingModal');
                    let bsShowModal = new bootstrap.Modal(modal1, {toggle: false});
                    bsShowModal.show();
                    this.cartString = "Added to bookings",
                    this.inCart = true,
                    this.$emit("usersInfo")
                }
            }).catch(() => undefined);
        },

        //async call activated when add to wishlist button is clicked
        async addToWishlist(){
            if(!this.userRole){
                //user isn't logged in
                let modal2 = document.getElementById('registerModal');
                let bsShowModal = new bootstrap.Modal(modal2, {toggle: false});
                bsShowModal.show();
                this.modalText = "Please login or create a new account to add to wishlist";
                return;
            }
            //logged in, so add item to wishlist
            await axios.post(`${this.baseURL}/wishlist/add`,
            {   productId: this.product.id,    }, { withCredentials: true }
            ).then((res) => {
                //check if user is logged in
                if(res.data == "<!DOCTYPE html>\n<html lang=\"en\">\n  <head>\n    <meta charset=\"utf-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n    <meta name=\"description\" content=\"\">\n    <meta name=\"author\" content=\"\">\n    <title>Please sign in</title>\n    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n    <link href=\"https://getbootstrap.com/docs/4.0/examples/signin/signin.css\" rel=\"stylesheet\" integrity=\"sha384-oOE/3m0LUMPub4kaC09mrdEhIc+e3exm4xOGxAmuFXhBNF4hcg/6MiAXAf5p0P56\" crossorigin=\"anonymous\"/>\n  </head>\n  <body>\n     <div class=\"container\">\n      <form class=\"form-signin\" method=\"post\" action=\"/login\">\n        <h2 class=\"form-signin-heading\">Please sign in</h2>\n        <p>\n          <label for=\"username\" class=\"sr-only\">Username</label>\n          <input type=\"text\" id=\"username\" name=\"email\" class=\"form-control\" placeholder=\"Username\" required autofocus>\n        </p>\n        <p>\n          <label for=\"password\" class=\"sr-only\">Password</label>\n          <input type=\"password\" id=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required>\n        </p>\n        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>\n      </form>\n</div>\n</body></html>"){
                    this.$emit('openAuthModal', 'login');
                    swal({
                        text: "Please login or signup",
                        icon: "info"
                    });
                    return;
                }
                //if backend sucessfully adds to wishlist, change button text to confirm
                if(res.status === 201){
                    this.wishlistString = "Added to wishlist";
                    this.addedToWishlist = true;
                    swal({
                    text: "Added to wishlist",
                    icon: "success"
                });
                }
            }).catch(() => undefined);
        },

        //remove item from wishlist
        async removeWishlist(id){
            await axios.delete(`${this.baseURL}/wishlist/delete/${id}`, { withCredentials: true })
            .then((res) => {
                if(res.status == 200){
                    this.wishlistString = 'Removed from wishlist'
                    this.deletedFromWishlist = true;
                    swal({
                        text: "Car has been removed from wishlist",
                        icon: "success"
                    })
                } else {
                    swal({
                        text: "Item not deleted",
                        icon: "error"
                    })
                }
            }).catch(() => undefined);
        },

        //open lightbox with selected image
        openLightbox(imageUrl) {
            this.lightboxImage = imageUrl;
            this.zoomLevel = 1;
            let modal = document.getElementById('imageLightbox');
            let bsShowModal = new bootstrap.Modal(modal, {toggle: false});
            bsShowModal.show();
        },

        // Open auth modal from within this component without removing backdrop incorrectly
        openAuthModalLocal(tab) {
            const modalEl = document.getElementById('registerModal');
            const bsModal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl, {toggle:false});
            // Hide the current register modal first, then emit event to show global auth modal
            bsModal.hide();
            // Wait for hidden event to ensure backdrop handling has settled
            const onHidden = () => {
                this.$emit('openAuthModal', tab);
                modalEl.removeEventListener('hidden.bs.modal', onHidden);
            };
            modalEl.addEventListener('hidden.bs.modal', onHidden);
        },

        //close lightbox
        closeLightbox() {
            let modal = document.getElementById('imageLightbox');
            let bsModal = bootstrap.Modal.getInstance(modal);
            if (bsModal) {
                bsModal.hide();
            }
            this.zoomLevel = 1;
            // Remove any lingering backdrops
            this.removeBackdrops();
        },

        // Remove any lingering modal backdrops
        removeBackdrops() {
            // Only remove backdrops if there are no other visible modals
            const openModals = document.querySelectorAll('.modal.show');
            if (openModals.length === 0) {
                const backdrops = document.querySelectorAll('.modal-backdrop');
                backdrops.forEach(backdrop => backdrop.remove());
                document.body.classList.remove('modal-open');
                document.body.style.overflow = '';
                document.body.style.paddingRight = '';
            }
        },

        //zoom in
        zoomIn() {
            if (this.zoomLevel < 3) {
                this.zoomLevel += 0.25;
            }
        },

        //zoom out
        zoomOut() {
            if (this.zoomLevel > 1) {
                this.zoomLevel -= 0.25;
            }
        },

        //reset zoom
        resetZoom() {
            this.zoomLevel = 1;
        },
    },

    computed: {
        //set the mindate for the date element (tomorrow)
        minDate() {
            const today = new Date();
            const tomorrow = new Date(today);
            tomorrow.setDate(tomorrow.getDate() + 1);
            const yyyy = tomorrow.getFullYear();
            let mm = tomorrow.getMonth() + 1;
            let dd = tomorrow.getDate();
            if (mm < 10) mm = "0" + mm;
            if (dd < 10) dd = "0" + dd;
            return `${yyyy}-${mm}-${dd}`;
        },

        //set the maxdate for the date element
        maxDate() {
            // Get the current date
            let today = new Date();
            // Add 30 days to the date
            let after30Days = new Date(today.setDate(today.getDate() + 30));
            // Format the date as yyyy-mm-dd
            let yyyy = after30Days.getFullYear();
            let mm = after30Days.getMonth() + 1; // January is 0
            let dd = after30Days.getDate();
            if (mm < 10) {
                mm = "0" + mm;
            }
            if (dd < 10) {
                dd = "0" + dd;
            }
            return yyyy + "-" + mm + "-" + dd;
        },

        // Filter out duplicate images from carousel
        filteredCarouselImages() {
            if (!this.product.carousel_imgs) return [];
            return this.product.carousel_imgs
                .filter(img => img !== this.product.imageURL)
                .map((img) => resolveImageUrl(img));
        },

        resolvedMainImage() {
            return resolveImageUrl(this.product?.imageURL);
        },
    },

    mounted() {
        this.id = this.$route.params.id;
        this.product = this.products.find(product => product.id == this.id);
        this.category = this.categories.find((category) => category.id == this.product.categoryId);
        // Token-based auth removed; server session used instead
        // Role should be provided from parent state or fetched via API
        this.getWishlist();
        this.getCart();
        
        // Initialize carousel after component is mounted
        this.$nextTick(() => {
            const carouselElement = document.getElementById('autoplayCarousel');
            if (carouselElement && bootstrap.Carousel) {
                new bootstrap.Carousel(carouselElement, {
                    interval: 5000,
                    ride: 'carousel'
                });
            }

            // Add event listeners to clean up backdrops when modals are hidden
            const modals = ['bookingModal', 'registerModal', 'imageLightbox'];
            modals.forEach(modalId => {
                const modalElement = document.getElementById(modalId);
                if (modalElement) {
                    modalElement.addEventListener('hidden.bs.modal', () => {
                        this.removeBackdrops();
                    });
                }
            });
        });
    },

    beforeUnmount() {
        // Clean up any lingering backdrops when component is destroyed
        this.removeBackdrops();
    },
}
</script>

<style scoped>
.modal{
    --bs-modal-footer-border-color: none;
    --bs-modal-footer-border-width: none;
    --bs-modal-header-border-color: none;
    --bs-modal-header-border-width: none;
    z-index: 1055 !important;
}

[data-theme="dark"] .modal-content{
    background-color: var(--royal-midnight-blue) !important;
}
.modal-content{
    height: 230px;
    z-index: 1056 !important;
}

/* Close button visibility in dark mode */
[data-theme="dark"] .btn-close {
    filter: invert(1) grayscale(100%) brightness(200%);
}

/* Modal footer buttons - add bottom margin on mobile */
@media (max-width: 576px) {
    .modal-footer {
        padding-top: 0;
    }
}

/* Lightbox modal should be above other modals */
#imageLightbox {
    z-index: 1060 !important;
}


/* Lightbox close button - keep it white */
[data-theme="dark"] #imageLightbox .btn-close {
    filter: none;
}

/* Carousel improvements */
.carousel-fade .carousel-item {
    opacity: 0;
    transition: opacity 0.6s ease-in-out;
}
.carousel-fade .carousel-item.active {
    opacity: 1;
}
.carousel-inner {
    background-color: #000;
}
.d-block.w-100{
    height: clamp(260px, 45vh, 520px);
    width: 100%;
    object-fit: cover;
    display: block;
}

/* Features list alignment */
.features-list {
    list-style-type: disc;
    padding-left: 20px;
    margin: 0;
}
.features-list li {
    margin-bottom: 8px;
}

.booking-requirements {
    padding: 1.25rem 16px;
}

.requirements-heading {
    font-size: 1.1rem;
    font-weight: 700;
    letter-spacing: 0.04em;
    text-transform: uppercase;
    color: var(--accent-color, #d4af37);
    margin-bottom: 0.35rem;
}

.requirements-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

.requirements-list li {
    display: grid;
    gap: 0.15rem 0.75rem;
    padding: 0.65rem 0.9rem 0.65rem 0;
    border-bottom: 1px solid rgba(212, 175, 55, 0.12);
}

.req-title {
    font-weight: 600;
    color: var(--text-primary, #f8f9fa);
}

.req-detail {
    font-size: 0.92rem;
    color: var(--text-primary, #e9ecef);
    opacity: 0.88;
}

[data-theme="light"] .req-detail {
    color: #334155;
    opacity: 1;
}

[data-theme="light"] .req-title {
    color: #102040;
}

/* Lightbox styles */
#imageLightbox .modal-dialog {
    max-width: 95vw;
}
.lightbox-content {
    background-color: rgba(0, 0, 0, 0.95);
    border: none;
    height: auto;
}
.lightbox-image-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 70vh;
    max-height: 85vh;
    overflow: auto;
    padding: 20px;
}
.lightbox-image {
    max-width: 100%;
    max-height: 80vh;
    object-fit: contain;
    transition: transform 0.3s ease;
    transform-origin: center center;
}
.lightbox-close {
    position: absolute;
    top: 15px;
    right: 15px;
    z-index: 1060;
    background-color: rgba(255, 255, 255, 0.9);
    border-radius: 50%;
    width: 40px;
    height: 40px;
    opacity: 1;
}
.lightbox-close:hover {
    background-color: #fff;
}
.zoom-controls {
    position: absolute;
    bottom: 20px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    gap: 10px;
    z-index: 1060;
}
.zoom-btn {
    padding: 8px 16px;
    font-weight: 600;
    border-radius: 5px;
    background-color: rgba(255, 255, 255, 0.9);
}
.zoom-btn:hover:not(:disabled) {
    background-color: #fff;
}
.zoom-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

.category{
    font-weight: 400;
}
.main-div{
    padding-top: 10px;
}
#wishlist-button {
    border-radius: 0%;
    background-color: rgb(62, 0, 112);
    border-color: rgb(62, 0, 112);
    color: white;
} 
.edit-prod{
    border-radius: 0%;
    background-color: #f0c14b;
    color: black;
    border-color: #f0c14b;
}
#wishlist-button:hover{
    background-color: white;
    color: black;
    
}
.add-to-cart-button{
    background-color: #f0c14b;
    color: black;
    border-color: #f0c14b;
}
.add-to-cart-button:hover{
    
    background-color: white;
    border-color: #f0c14b;
    color: black;
}
@media (min-width: 0px) and (max-width: 576px) {
      .input-group-parent{
        width: 15rem;
      }
}
</style>

<!-- Global (non-scoped) modal backdrop z-index for body-inserted backdrops -->
<style>
.modal-backdrop {
    z-index: 1050 !important;
}

#imageLightbox .modal-backdrop {
    z-index: 1059 !important;
}
</style>
