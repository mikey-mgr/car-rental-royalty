<template>
    <div class="container" v-if="users">
        <div class="row justify-content-center">
            <form class="row px-3" v-if="product" @submit="editProduct">
                <h4 class="py-3">Edit Car</h4>
                <div class="form-group col-lg-6">
                    <label> Category</label>
                    <select class="form-control" v-model="product.categoryId" required>
                        <option v-for="category of categories"
                                :key="category.id"
                                :value="category.id"> 
                            {{category.categoryName}}
                        </option>

                    </select>
                </div>
                <div class="form-group col-lg-6">
                    <label>Name</label>
                    <input type="text" class="form-control" v-model="product.name" required/>
                </div>
                <div class="form-group">
                    <label>Description</label>
                    <textarea type="text" class="form-control" v-model="product.description" required/>
                </div>
                <div class="form-group col-md-4">
                    <label>Image URL</label>
                    <div class="input-group">
                        <input type="text" class="form-control" v-model="product.imageURL" required/>
                        <button type="button" class="btn btn-outline-secondary" @click="triggerUpload('imageURL')">Upload</button>
                    </div>
                </div>
                <div class="form-group col-md-4">
                    <label>Price</label>
                    <input type="number" class="form-control" v-model="product.price" required/>
                </div>
                <div class="form-group col-md-4">
                    <label>Booking Status</label>
                    <select class="form-control" v-model="product.bookingStatus" required>
                        <option>Available</option>
                        <option>Reserved</option>
                        <option>Booking Confirmed</option>
                        <option>Checked Out</option>
                        <option>Unavailable</option>
                    </select>
                </div>
                <div class="input-group">
                    <!-- features edit -->
                    <label class="col-12 px-0" for="">Features</label>
                    <div class="input-group mb-2">
                        <div v-for="(feature, index) in this.features" :key="index" class="input-group col-md-6 col-lg-4 mb-3 p-0 border border-0">
                            <input v-model="features[index]" type="text" maxlength="100" placeholder="Enter a feature" class="form-control" style="border-radius: 0;" required>
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
                            <button type="button" class="btn btn-outline-secondary" @click="triggerUpload(index)" style="border-radius: 0;">Upload</button>
                            <a v-if="canRemoveImg" class="btn btn-outline-danger" @click="removeImg(index)" style="border-radius: 0;">Remove</a>
                        </div>
                    </div>
                    <div class="col-12 mb-4 px-0">
                        <a v-if="canAddImg" class="btn btn-primary" @click="addImg" style="border-radius: 2px;">Add Image</a>
                    </div>
                </div>
                <input type="file" ref="fileInput" accept="image/*" style="display:none" @change="handleUpload">
                <div class="col-12 mt-3">
                    <button
                      type="submit"
                      class="btn btn-success mx-2"
                      :disabled="isSubmitting"
                    >
                      <span
                        v-if="isSubmitting"
                        class="spinner-border spinner-border-sm me-2 button-spinner"
                        role="status"
                        aria-hidden="true"
                      ></span>
                      <span>{{ isSubmitting ? 'Saving...' : 'Submit' }}</span>
                    </button>
                    <button
                      class="btn btn-danger"
                      @click="deleteProduct"
                      :disabled="isSubmitting"
                    >
                        Delete
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
    props: ["baseURL", "categories", "products", "users"],
    data() {
        return {
            product: null,
            id: null,
            features: ["", "", ""],
            carouselImg: ["", "", "", ""],
            isSubmitting: false,
            uploadTarget: null
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
        
        triggerUpload(target) {
            this.uploadTarget = target;
            this.$refs.fileInput.click();
        },
        async handleUpload(e) {
            const file = e.target.files[0];
            if (!file) return;
            const formData = new FormData();
            formData.append('file', file);
            try {
                const res = await axios.post(this.baseURL + '/admin/upload', formData, {
                    // Don't set Content-Type — Axios must add boundary
                });
                if (res.data.success && res.data.message) {
                    const url = res.data.message;
                    if (this.uploadTarget === 'imageURL') {
                        this.product.imageURL = url;
                    } else if (typeof this.uploadTarget === 'number') {
                        this.$set(this.carouselImg, this.uploadTarget, url);
                    }
                } else {
                    swal({ text: res.data.message || 'Upload failed', icon: 'error' });
                }
                } catch (err) {
                    const msg = err.response?.data?.message || err.message || 'Upload failed';
                    swal({ text: msg, icon: 'error' });
                }
            e.target.value = '';
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

        async editProduct(e) {
            e.preventDefault();
            if (this.isSubmitting) {
                return;
            }

            this.isSubmitting = true;
            this.product.features = this.features;
            this.product.carousel_imgs = this.carouselImg;
            await axios.post(`${this.baseURL}/product/update/${this.id}`, this.product)
                .then((res) => {
                    if(res.data.message=="Product has been updated"){
                        this.$emit("fetchData");
                        this.$router.push({name: 'ListProducts', params: {id: this.product.categoryId}});
                        swal({
                            text: "Car has been updated successfully",
                            icon: "success"
                        })
                    } else swal({
                        text: 'Something went wrong',
                        icon: 'warning'
                    });
                }).catch(() => {
                    swal({
                        text: 'Failed to update car. Please try again.',
                        icon: 'error'
                    });
                }).finally(() => {
                    this.isSubmitting = false;
                });
        },
        async deleteProduct(){
            if (this.isSubmitting) {
                return;
            }

            this.isSubmitting = true;
            await axios.delete(`${this.baseURL}/product/delete/${this.id}`)
            .then((res) =>{
                if(res.data.message == "Product deleted successfully"){
                    this.$emit("fetchData");
                    swal({
                        text: "Car has been deleted successfully",
                        icon: "success"
                    });
                    this.$router.push({ name: 'AdminProduct' });
                } else {
                    swal({
                        text: 'Something went wrong',
                        icon: 'warning'
                    })
                }
            }).catch(() => {
                swal({
                    text: 'Failed to delete car. Please try again.',
                    icon: 'error'
                });
            }).finally(() => {
                this.isSubmitting = false;
            })
        }
    },
    mounted() {
        this.id = this.$route.params.id;
        this.product = this.products.find(product => product.id == this.id);
        this.ifFeaturesEmpty();
        this.ifImgsEmpty();
    }
}
</script>

<style scoped>
.form-group{
    padding-top: 14px;
}
label{
    font-weight: bold;
}
.button-spinner {
    color: black;
}
</style>
