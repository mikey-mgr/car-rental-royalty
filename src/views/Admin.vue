<template>
    <div v-if="users" class="container text-center">
        <!-- Begin Page Content -->
        <div class="container-fluid pt-4">

            <!-- Page Heading -->
            <div class="d-sm-flex align-items-center justify-content-between mb-4">
                <h1 class="h3 mb-0 text-gray-800">Dashboard</h1>
            </div>

            <!-- Content Row -->
            <div class="row">

                <!-- Earnings (Monthly) Card Example -->
                <div class="col-xl-3 col-md-6 mb-4">
                    <div class="card border-left border-primary shadow h-100 py-2">
                        <div class="card-body">
                            <div class="row g-0 align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                        New Bookings</div>
                                    <div class="h5 mb-0 font-weight-bold text-gray-800">{{ cartItemsLength }}</div>
                                </div>
                                <div class="col-auto">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" 
                                        fill="blue" class="bi bi-calendar " viewBox="0 0 16 16">
                                        <path d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5M1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4z"/>
                                    </svg>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Earnings (Monthly) Card Example -->
                <div class="col-xl-3 col-md-6 mb-4">
                    <div class="card border-left border-success shadow h-100 py-2">
                        <div class="card-body">
                            <div class="row g-0 align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                        Earnings</div>
                                    <div v-if="totalCost" class="h5 mb-0 font-weight-bold text-gray-800">${{ totalCost.toFixed(2) }}</div>
                                    <div v-if="!totalCost" class="h5 mb-0 font-weight-bold text-gray-800">$0.00</div>
                                </div>
                                <div class="col-auto">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" 
                                        fill="green" class="bi bi-currency-dollar " viewBox="0 0 16 16">
                                        <path d="M4 10.781c.148 1.667 1.513 2.85 3.591 3.003V15h1.043v-1.216c2.27-.179 3.678-1.438 3.678-3.3 0-1.59-.947-2.51-2.956-3.028l-.722-.187V3.467c1.122.11 1.879.714 2.07 1.616h1.47c-.166-1.6-1.54-2.748-3.54-2.875V1H7.591v1.233c-1.939.23-3.27 1.472-3.27 3.156 0 1.454.966 2.483 2.661 2.917l.61.162v4.031c-1.149-.17-1.94-.8-2.131-1.718zm3.391-3.836c-1.043-.263-1.6-.825-1.6-1.616 0-.944.704-1.641 1.8-1.828v3.495l-.2-.05zm1.591 1.872c1.287.323 1.852.859 1.852 1.769 0 1.097-.826 1.828-2.2 1.939V8.73z"/>
                                    </svg>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Earnings (Monthly) Card Example -->
                <div class="col-xl-3 col-md-6 mb-4">
                    <div class="card border-left border-info shadow h-100 py-2">
                        <div class="card-body">
                            <div class="row g-0 align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs font-weight-bold text-info text-uppercase mb-1">Currently Booked
                                    </div>
                                    <div class="row g-0 align-items-center">
                                        <div class="col-auto">
                                            <div class="h5 mb-0 mr-3 font-weight-bold text-gray-800">{{ availableCount }}</div>
                                        </div>
                                        <div class="col">
                                            <div class="progress progress-sm mr-2">
                                                <div class="progress-bar bg-info" role="progressbar"
                                                    :style="{width: progressBarWidth + '%' }" :aria-valuenow="progressBarWidth" aria-valuemin="0"
                                                    aria-valuemax="100">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-auto">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" 
                                        fill="cornflowerblue" class="bi bi-list-columns-reverse " viewBox="0 0 16 16">
                                        <path fill-rule="evenodd" d="M0 .5A.5.5 0 0 1 .5 0h2a.5.5 0 0 1 0 1h-2A.5.5 0 0 1 0 .5m4 0a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1h-10A.5.5 0 0 1 4 .5m-4 2A.5.5 0 0 1 .5 2h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5m4 0a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5m-4 2A.5.5 0 0 1 .5 4h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5m4 0a.5.5 0 0 1 .5-.5h11a.5.5 0 0 1 0 1h-11a.5.5 0 0 1-.5-.5m-4 2A.5.5 0 0 1 .5 6h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5m4 0a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 0 1h-8a.5.5 0 0 1-.5-.5m-4 2A.5.5 0 0 1 .5 8h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5m4 0a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 0 1h-8a.5.5 0 0 1-.5-.5m-4 2a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5m4 0a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1h-10a.5.5 0 0 1-.5-.5m-4 2a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5m4 0a.5.5 0 0 1 .5-.5h6a.5.5 0 0 1 0 1h-6a.5.5 0 0 1-.5-.5m-4 2a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5m4 0a.5.5 0 0 1 .5-.5h11a.5.5 0 0 1 0 1h-11a.5.5 0 0 1-.5-.5"/>
                                    </svg>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Pending Requests Card Example -->
                <div class="col-xl-3 col-md-6 mb-4">
                    <div class="card border-left border-warning shadow h-100 py-2">
                        <div class="card-body">
                            <div class="row g-0 align-items-center">
                                <div class="col mr-2">
                                    <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                        Pending Bookings</div>
                                    <div class="h5 mb-0 font-weight-bold text-gray-800">{{ wishlistsLength }}</div>
                                </div>
                                <div class="col-auto">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48"
                                        fill="yellow" class="bi bi-chat-dots" viewBox="0 0 16 16">
                                        <path d="M5 8a1 1 0 1 1-2 0 1 1 0 0 1 2 0m4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0m3 1a1 1 0 1 0 0-2 1 1 0 0 0 0 2"/>
                                        <path d="m2.165 15.803.02-.004c1.83-.363 2.948-.842 3.468-1.105A9 9 0 0 0 8 15c4.418 0 8-3.134 8-7s-3.582-7-8-7-8 3.134-8 7c0 1.76.743 3.37 1.97 4.6a10.4 10.4 0 0 1-.524 2.318l-.003.011a11 11 0 0 1-.244.637c-.079.186.074.394.273.362a22 22 0 0 0 .693-.125m.8-3.108a1 1 0 0 0-.287-.801C1.618 10.83 1 9.468 1 8c0-3.192 3.004-6 7-6s7 2.808 7 6-3.004 6-7 6a8 8 0 0 1-2.088-.272 1 1 0 0 0-.711.074c-.387.196-1.24.57-2.634.893a11 11 0 0 0 .398-2"/>
                                    </svg>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- vehicles table and pie chart row -->

            <div class="row">
                <!-- bvehicles card -->
                <div class="col-xl-8 col-lg-7">
                    <div class="card shadow mb-4">
                        <!-- Card Header - Dropdown -->
                        <div
                            class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                            <h6 class="m-0 font-weight-bold text-primary">Vehicles Overview ({{ allVehicles }})</h6>
                        </div>
                        <!-- Card Body for Vehicles overview-->
                        <div class="card-body text-start p-2">

                            <!-- filtering for vehicles table-->
                            <div class="row d-flex m-0 p-0 mb-2 text-center">
                                <div class="col-md-6 col-xs-12 form-row form-group my-1 px-1">
                                    <label for="filter-input" class="m-0 py-2 pl-3">Filter</label>
                                    <div class="col">
                                        <div class="input-group">
                                            <input type="search" id="filter-input" class="form-control p-1" v-model="filter" placeholder="Type to Search"/>
                                            <b-button :disabled="!filter" @click="filter = ''">Clear</b-button>
                                        </div>
                                    </div>
                                </div>



                                <div class="col p-0">
                                    <div role="group" tabindex="-1" class="mt-1 bv-no-focus-ring">
                                        <label for="" class="mr-2">Filter on:</label>
                                        <div class="custom-control custom-control-inline custom-checkbox">
                                            <input type="checkbox" v-model="vehicleFilterOn" class="custom-control-input" value="vehicleName" id="vName" name="vName">
                                            <label class="custom-control-label" for="vName">Name</label>
                                        </div> 
                                        <div class="custom-control custom-control-inline custom-checkbox">
                                            <input type="checkbox" v-model="vehicleFilterOn" class="custom-control-input" value="bookingStatus" id="vStatus" name="vStatus">
                                            <label class="custom-control-label" for="vStatus">Status</label>
                                        </div> 
                                        <div class="custom-control custom-control-inline custom-checkbox">
                                            <input type="checkbox" v-model="vehicleFilterOn" class="custom-control-input" value="category" id="vCategory" name="vCategory">
                                            <label class="custom-control-label" for="vCategory">Category</label>
                                        </div>
                                    </div>
                                </div>

                                <!-- Pagination for vehicles table-->
                                <div class="col-md-6 col-xs-12 form-row form-group my-1 px-1">
                                    <label for="per-page-select" class="m-0 py-2 pl-3">Per page</label>
                                    <div class="col">
                                        <select class="form-select" id="per-page-select" v-model="perPage">
                                            <option v-for="index in pageOptions.length" :key="index">{{ pageOptions[index-1] }}</option>
                                        </select>
                                    </div>
                                </div>
                                <b-pagination
                                    v-model="currentPage"
                                    :total-rows="totalRows"
                                    :per-page="perPage"
                                    class="col-md-6 col-xs-12 my-1 px-1 d-flex"
                                ></b-pagination>
                            </div>

                            <!-- Bootstrap Table for vehicles-->
                            <b-table responsive class="table" head-variant="dark"
                                :items="vehicleItems"
                                :fields="carTblFields"
                                :per-page="perPage"
                                striped hover
                                :current-page="currentPage"
                                sort-icon-left
                                :filter="filter"
                                @filtered="onFiltered"
                                :filter-included-fields="vehicleFilterOn">
                                    <template #cell(vehicleName)="row">
                                        <router-link class="text-decoration-none text-secondary" :to="{name:'ProductDetails', params: {id: row.item.id, name: row.value}}">{{ row.value }}</router-link>
                                    </template>
                                    <template #cell(category)="row">
                                        <router-link class="text-decoration-none text-secondary" :to="{name:'ListProducts', params: {id: row.item.categoryId}}">
                                            {{ row.value }}
                                        </router-link>
                                    </template>
                                    <template #cell(price)="row">
                                        <p class="text-center fw-bold">${{ row.value.toFixed(2) }}</p>
                                    </template>
                            </b-table>
                            <!-- <div class="p-2">
                                <tr class="">{{ allVehicles }} Vehicles</tr>
                            </div> -->
                        </div>
                    </div>
                </div>
                
                <!-- Pie Chart -->
                <div class="col-xl-4 col-lg-5">
                    <div class="card shadow mb-4">
                        <!-- Card Header - Dropdown -->
                        <div
                            class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                            <h6 class="m-0 font-weight-bold text-primary"><router-link class="text-decoration-none" :to="{name: 'AdminCategory'}">Categories</router-link></h6>
                        </div>
                        <!-- Card Body -->
                        <div class="card-body">
                            <div class="chart-pie py-4">
                                <Doughnut :data="data" :options="options" />
                            </div>
                                <h5><router-link class="text-decoration-none mt-4" :to="{name: 'AdminProduct'}">View all Vehicles</router-link></h5>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Bookings List Row -->
            <div class="row">
                <!-- Content Column -->
                <div class="col-12 mb-4">
                    <!-- Project Card Example -->
                    <div class="card shadow mb-4 text-start">
                        <div class="card-header py-3">
                            <h6 class="m-0 font-weight-bold text-primary">Bookings</h6>
                        </div>
                        <div class="card-body p-2">
                            <!-- Pagination for bookings table-->
                            <div class="row d-flex m-0 p-0 mb-2">
                                <div class="col-md-6 col-xs-12 form-row form-group my-1">
                                    <label for="perPgSelect" class="m-0 p-2">Per page</label>
                                    <div class="col">
                                        <select class="form-select" id="perPgSelect" v-model="perPg">
                                            <option v-for="index in pgOptions.length" :key="index">{{ pgOptions[index-1] }}</option>
                                        </select>
                                    </div>
                                </div>
                                <b-pagination
                                    v-model="currentPg"
                                    :total-rows="ttlRows"
                                    :per-page="perPg"
                                    class="col-md-6 col-xs-12 my-1 px-3 d-flex"
                                ></b-pagination>
                            </div>
                            <!-- Bootstrap Table for bookings-->
                            <b-table responsive class="table" head-variant="dark"
                                :items="bookingItems"
                                :fields="bookingsTblFields"
                                :per-page="perPg"
                                striped hover
                                :current-page="currentPg"
                                v-model:sort-by="sortBy"
                                v-model:sort-asc="sortAsc"
                                sort-icon-left
                                @filtered="onFiltr">
                                    <template #cell(days)="row">
                                        <p class="text-start">{{ row.value }}</p>
                                    </template>
                                    <template #cell(price)="row">
                                        <p class="text-center fw-bold">${{ row.value.toFixed(2) }}</p>
                                    </template>
                            </b-table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>


<script>
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
  ArcElement,
} from 'chart.js'
import { Doughnut } from 'vue-chartjs';
// import swal from "sweetalert"

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement)



export default {
    name: "AdminView",
    props: ["users", "products", "categories", "cartItems", "totalCost",
            "wishlists", "colors", "labels1", "data1"],
    components: { Doughnut  },
    data(){
        return{
            //data for vehicles table
            carTblFields: [
                        {
                            key: "vehicleName",
                            label: "Vehicle Name",
                            sortable: true,
                        },
                        {
                            key: "bookingStatus",
                            label: "Booking Status",
                            sortable: true,
                        },
                        {
                            key: "price",
                            label: "Price",
                            sortable: true,
                        },
                        {
                            key: "category",
                            label: "Category",
                        },
            ],
            vehicleItems: this.vehicleTblConfig(this.categories, this.products),
            perPage: 5,
            pageOptions: [5,10,15,30,50,100,200],
            totalRows: 1,
            currentPage: 1,
            allVehicles: 0,
            filter: null,
            vehicleFilterOn: [],


            //data for bookings table
            bookingsTblFields: [
                        {
                            key: "vehicle",
                            label: "Vehicle",
                            sortable: true,
                        },                        
                        {
                            key: "user",
                            label: "User",
                            sortable: true,
                        },                        
                        {
                            key: "days",
                            label: "Days",
                            sortable: true,
                        },                        
                        {
                            key: "bookedFor",
                            label: "Booked for",
                            sortable: true,
                        },                        
                        {
                            key: "price",
                            label: "Price",
                            sortable: true,
                        },
            ],
            bookingItems: this.bookingTblConfig(this.cartItems, this.users),
            perPg: 5,
            pgOptions: [5,10,15,30,50,100,200],
            ttlRows: 1,
            currentPg: 1,
            allBookings: 0,
            sortBy: "bookedFor",
            sortAsc: true,

            //data for pie chart
            data: {
                labels: this.labels1,
                datasets: [{
                data: this.data1,
                backgroundColor: this.colors,
                hoverBorderColor: "rgba(234, 236, 244, 1)",
                }],
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                tooltips: {
                    backgroundColor: "rgb(255,255,255)",
                    bodyFontColor: "#858796",
                    borderColor: '#dddfeb',
                    borderWidth: 1,
                    xPadding: 15,
                    yPadding: 15,
                    displayColors: false,
                    caretPadding: 10,
                },
                legend: {
                display: false
                },
            },

            //general variables
            availableCount: 0,
        }
    },
    computed:{
        progressBarWidth(){
            return this.progressBarCalc(this.products);
        },
        wishlistLength(){
            if(!this.wishlists){
                return 0;
            } else return this.wishlists.length
        },

        cartItemsLength(){
            if(!this.cartItems){
                return 0;
            } else return this.cartItems.length
        },
    },
    methods: {
        //method to set the width of progress bar and booked vehicles count
        progressBarCalc(products){
            let totalLength = products.length;
            let availableCount = 0;
            if(products){
                for(let i=0; i<totalLength; i++){
                    if(products[i].bookingStatus == 'Booking Confirmed' || products[i].bookingStatus == 'Checked Out')
                        availableCount++;
                }
            }
            this.availableCount = availableCount;
            let percentage = availableCount/totalLength * 100;
            return percentage.toFixed(0);
        },
        //filter and pagination for vehicles table
        onFiltered(filteredItems) {
            // Trigger pagination to update the number of buttons/pages due to filtering
            this.totalRows = filteredItems.length
            this.currentPage = 1
        },
        //filter pagination for bookings table
        onFiltr(filteredItems) {
            // Trigger pagination to update the number of buttons/pages due to filtering
            this.ttlRows = filteredItems.length
            this.currentPg = 1
        },

        //configure items for bookings table in admin panel
        bookingTblConfig(cartItems, users){
            let items = [];
            if(cartItems)
                for(let i=0; i<cartItems.length; i++){
                    let usrItem = {
                            vehicle: "",
                            user: "",
                            days: "",
                            bookedFor: "",
                            price: ""
                    }
                    usrItem.vehicle = cartItems[i].product.name;
                    usrItem.user = this.getUserName(cartItems[i].userId, users);
                    usrItem.days = cartItems[i].quantity;
                    usrItem.bookedFor = cartItems[i].bookedFor;
                    usrItem.price = cartItems[i].product.price * cartItems[i].quantity;
                    items.push(usrItem);
                }
            return items;
        },

        getUserName(userId, users){
            let userName = "";
            if(userId, users)
                for(let j=0; j<users.length; j++){
                    if(userId == users[j].id){
                        userName = users[j].firstName+ " "+users[j].lastName;
                    }
                }
            return userName;
        },

        //configure items for table for vehicles in admin panel
        vehicleTblConfig(categories, products){
            let items = [];
            if(products)
                for(let i=0; i < products.length; i++){
                    let tblItem = {
                                id: "",
                                vehicleName: "",
                                bookingStatus: "",
                                price: "",
                                category: "",
                                categoryId: "",
                    };
                    tblItem.id = products[i].id
                    tblItem.vehicleName = products[i].name;
                    tblItem.bookingStatus = products[i].bookingStatus;
                    tblItem.price = products[i].price;
                    tblItem.category = this.productCategory(products[i], categories);
                    tblItem.categoryId = products[i].categoryId;
                    items.push(tblItem);
                }
            return items;
        },

        //get the category name for a product
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
        this.$emit("adminInfo");
        //setting the initial lengths for the vehicles table
        if(this.products){
            this.allVehicles = this.products.length;
        } else this.allVehicles = 0;
        // Set the initial number of items for b-table
        this.totalRows = this.vehicleItems.length;

        //setting the initial length for the bookings table
        if(this.cartItems){
            this.allBookings = this.cartItems.length;
        } else this.allBookings = 0;
        // Set the initial number of items for b-table
        this.ttlRows = this.bookingItems.length;
        
    },
}
</script>


<style>
.table td, .table th{
    border: 1px solid #dee2e6;
}
li.page-item{
    display: flex;
    flex: 1;
    flex-grow: 1 !important;
    flex-direction: column;
}
li.page-item.disabled{
    display: flex;
    flex: 1;
    flex-grow: 1 !important;
    flex-direction: column;
}

</style>