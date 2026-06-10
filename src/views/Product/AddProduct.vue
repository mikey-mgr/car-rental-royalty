<template>
    <div class="container" v-if="users">
        <div class="row justify-content-center pt-3">
            <form class="row px-3" @submit="addProduct">
                <h4 class="text-center">Add New Vehicle</h4>
                <div class="input-group p-0 pt-4 w-100">
                    <div class="form-group col-md-4 col-lg-3">
                        <label>Category</label>
                        <select class="form-control" v-model="categoryId" required>
                            <option v-for="category in categories" :key="category.id"
                                    :value="category.id">{{ category.categoryName }}</option>
                        </select>
                    </div>
                    <div class="form-group col-md-4 col-lg-3">
                        <label>Name</label>
                        <input type="text" v-model="name" class="form-control" required>
                    </div>
                    <div class="form-group col-md-4 col-lg-3 col-sm-6 col-xs-6">
                        <div class="input-group">
                            <label class="input-group">Price</label>
                            <span class="input-group-text">$</span>
                            <input type="number" v-model="price" class="form-control" required>
                        </div>
                    </div>
                    <div class="form-group col-md-4 col-lg-3 col-sm-6 col-xs-6">
                        <label>Booking Status</label>
                        <select class="form-control" v-model="bookingStatus" required>
                            <option>Available</option>
                            <option>Reserved</option>
                            <option>Booking Confirmed</option>
                            <option>Checked Out</option>
                            <option>Unavailable</option>
                        </select>
                    </div>
                    <div class="form-group col-md-8 col-lg-6">
                        <label>Image Url</label>
                        <input type="text" rows="3" v-model="imageURL" class="form-control" required>
                    </div>
                    <div class="form-group col-lg-6">
                        <label>Description</label>
                        <textarea type="text" v-model="description" class="form-control" required/>
                    </div>
                </div>
                <div class="form-group">
                    <!-- features edit -->
                    <label class="col-12 px-0" for="">Features</label>
                    <div class="input-group mb-2">
                        <div v-for="(feature, index) in this.features" :key="index" class="input-group col-md-6 col-lg-4 mb-3 p-0 border border-0">
                            <input v-model="features[index]" type="text" placeholder="Enter a feature" class="form-control" style="border-radius: 0;" required>
                            <a v-if="canRemoveFeature" class="btn btn-outline-danger" @click="removeFeature(index)" style="border-radius: 0;">Remove</a>
                        </div>
                    </div>
                    <div class="col-12 mb-4 px-0">
                        <a v-if="canAddFeature" class="btn btn-primary" @click="addFeature" style="border-radius: 2px;">Add Feature</a>
                    </div>
                    <!-- carousel images edit -->
                    <label class="col-12 px-0" for="">Vehicle images</label>
                    <div class="input-group mb-2">
                        <div v-for="(image, index) in this.carouselImg" :key="index" class="input-group col-md-6 col-lg-4 mb-3 p-0 border border-0">
                            <input v-model="carouselImg[index]" type="text" placeholder="Enter an image url" class="form-control" style="border-radius: 0;" required>
                            <a v-if="canRemoveImg" class="btn btn-outline-danger" @click="removeImg(index)" style="border-radius: 0;">Remove</a>
                        </div>
                    </div>
                    <div class="col-12 px-0">
                        <a v-if="canAddImg" class="btn btn-primary" @click="addImg" style="border-radius: 2px;">Add Image</a>
                    </div>
                </div>
                <div class="container">
                    <button
                      type="submit"
                      class="btn btn-primary btn-add-prod mt-3"
                      :disabled="isSubmitting"
                    >
                      <span
                        v-if="isSubmitting"
                        class="spinner-border spinner-border-sm me-2 button-spinner"
                        role="status"
                        aria-hidden="true"
                      ></span>
                      <span>{{ isSubmitting ? 'Adding Vehicle...' : 'Add Vehicle' }}</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script>
    import axios from 'axios'
    import swal from 'sweetalert'
    export default {
        props: ["baseURL", "categories", "users"],
        data() {
            return {
                id: "",
                categoryId: "",
                name: "",
                description: "",
                imageURL: "",
                price: "",
                bookingStatus: "",
                features: ["", "", ""],
                carouselImg: ["", "", "", ""],
                isSubmitting: false
            }
        },
        computed: {
            canAddFeature(){
                return this.features.length < 6;
            },
            canRemoveFeature(){
                return this.features.length > 3;
            },
            canAddImg(){
                return this.carouselImg.length < 12;
            },
            canRemoveImg(){
                return this.carouselImg.length > 4;
            },
        },
        methods: {
            addFeature(){
                if(this.canAddFeature){
                    this.features.push("");
                }
            },

            removeFeature(index){
                if(this.canRemoveFeature){
                    this.features.splice(index, 1)
                }
            },
            
            ifFeaturesEmpty(){
                if(!this.product.features){
                    this.features = ["", "", ""]
                } else this.features = this.product.features;
            },
            
            addImg(){
                if(this.canAddImg){
                    this.carouselImg.push("");
                }
            },

            removeImg(index){
                if(this.canRemoveImg){
                    this.carouselImg.splice(index, 1)
                }
            },
            
            ifImgsEmpty(){
                if(!this.product.carousel_imgs){
                    this.carouselImg = ["", "", "", ""]
                } else this.carouselImg = this.product.carousel_imgs;
            },

            addProduct(e) {
                e.preventDefault();
                if (this.isSubmitting) {
                    return;
                }

                this.isSubmitting = true;
                const newProduct = {
                    categoryId: this.categoryId,
                    description: this.description,
                    name: this.name,
                    imageURL: this.imageURL,
                    price: this.price,
                    bookingStatus: this.bookingStatus,
                    features: this.features,
                    carousel_imgs: this.carouselImg
                };

                axios.post(this.baseURL+"/product/add", newProduct)
                .then((res) => {
                    if(res.data.message == "product has been added"){
                        swal({
                            text: "Vehicle added successfully",
                            icon: "success"
                        }); 
                    } else swal({
                        text: "Something went wrong",
                        icon: "error"
                    })
                    this.$router.push({ name: 'AdminProduct' });
                }).catch((err)=> {
                    console.log("err", err);
                    swal({
                        text: "Failed to add vehicle. Please try again.",
                        icon: "error"
                    });
                }).finally(() => {
                    this.isSubmitting = false;
                })
            }
        }
    }
</script>
<style scoped>
.btn-add-prod{
  border-color: #c18e32;
  background-color: #c18e32;
  color: white;
}
.button-spinner {
  color: black;
}
.btn-add-prod:hover{
  color: black;
  background-color: white;
}
.form-group{
    padding-top: 4px;
}
label{
    font-weight: bold;
}
</style>
