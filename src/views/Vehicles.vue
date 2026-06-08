<template>
  <div class="container py-4">
    <div v-if="notFound" class="row">
      <div class="col-12 text-center">
        <h2 class="pt-3 pb-3">No vehicles found for "{{ text }}"</h2>
      </div>
    </div>
    <div v-else-if="search && text" class="row">
      <div class="col-12 text-center">
        <h2 class="pt-3 pb-3">Search results for "{{ text }}"</h2>
        <p class="text-muted">{{ vehicles.length }} vehicle(s) match your search and filters.</p>
      </div>
    </div>
    <div v-else class="row">
      <div class="col-12 text-center">
        <h2 class="pt-3 pb-3">All Vehicles</h2>
      </div>
    </div>

    <p v-if="tripSummary" class="text-muted small mb-3 text-center">{{ tripSummary }}</p>

    <div class="row g-3 mb-3 align-items-end">
      <div class="col-12 col-lg-4">
        <form class="h-100" @submit.prevent="onSearchSubmit">
          <label class="form-label fw-semibold" for="vehicles-search">Search by name</label>
          <div class="input-group">
            <input
              id="vehicles-search"
              v-model="searchText"
              type="search"
              class="form-control"
              placeholder="Search vehicles"
              aria-label="Search vehicles"
              list="vehicleListOptions"
            />
            <datalist id="vehicleListOptions">
              <option value="NISSAN VERSA NOTE (2014)"></option>
              <option value="Ford Everest 2023"></option>
              <option value="LAND ROVER DISCOVERY 4"></option>
              <option value="TOYOTA CAMRY (2010)"></option>
              <option value="MERCEDES BENZ GLE 2022"></option>
              <option value="FIT NEW SHAPE HYBRID (2014)"></option>
            </datalist>
            <button
              v-if="text || searchText"
              type="button"
              class="input-group-text bi bi-trash border-end-0"
              style="background-color: var(--bs-body-bg, #fff); cursor: pointer"
              aria-label="Clear search"
              @click="clearSearch"
            ></button>
            <div class="input-group-prepend">
              <button type="submit" class="input-group-text font-weight-bold h-100" id="search-button-navbar">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  fill="currentColor"
                  class="bi bi-search"
                  viewBox="0 0 16 16"
                >
                  <path
                    d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"
                  />
                </svg>
              </button>
            </div>
          </div>
        </form>
      </div>
      <div class="col-12 col-md-6 col-lg-3">
        <label class="form-label fw-semibold" for="filter-category">Category</label>
        <select id="filter-category" v-model="filterCategoryId" class="form-select">
          <option value="">All categories</option>
          <option v-for="c in categories" :key="c.id" :value="String(c.id)">{{ c.categoryName }}</option>
        </select>
      </div>
      <div class="col-12 col-md-6 col-lg-2">
        <label class="form-label fw-semibold" for="filter-price">Max price / day ($)</label>
        <input
          id="filter-price"
          v-model="filterMaxPrice"
          type="number"
          min="0"
          step="1"
          class="form-control"
          placeholder="Any"
        />
      </div>
      <div class="col-12 col-md-6 col-lg-3">
        <label class="form-label fw-semibold" for="filter-status">Availability</label>
        <select id="filter-status" v-model="filterBookingStatus" class="form-select">
          <option value="">Any status</option>
          <option value="Available">Available</option>
          <option value="Reserved">Reserved</option>
          <option value="Booking Confirmed">Booking Confirmed</option>
          <option value="Checked Out">Checked Out</option>
          <option value="Unavailable">Unavailable</option>
        </select>
      </div>
    </div>

    <div v-if="!notFound && vehicles.length === 0 && hasActiveFilters" class="alert alert-info" role="status">
      No vehicles match your current filters. Try widening category, price, or status.
    </div>

    <div class="row justify-content-evenly">
      <div
        v-for="product of vehicles"
        :key="product.id"
        class="col-md-6 col-xl-4 col-12 pt-3 d-flex"
      >
        <ProductBox
          :role="role"
          :product="product"
          :users="users"
          :category-name="productCategory(product, categories)"
        />
      </div>
    </div>
  </div>
</template>

<script>
import ProductBox from "@/components/ProductBox.vue";
import axios from "axios";

export default {
  name: "VehiclesView",
  props: ["products", "baseURL", "users", "categories"],
  components: { ProductBox },
  data() {
    return {
      searchText: "",
      text: null,
      nameSearchActive: false,
      apiResults: [],
      search: false,
      notFound: false,
      role: null,
      filterCategoryId: "",
      filterMaxPrice: "",
      filterBookingStatus: "",
    };
  },
  computed: {
    baseList() {
      if (this.nameSearchActive) {
        return this.apiResults || [];
      }
      return this.products || [];
    },
    vehicles() {
      let list = [...this.baseList];
      if (this.filterCategoryId) {
        list = list.filter((p) => String(p.categoryId) === String(this.filterCategoryId));
      }
      const maxP = this.filterMaxPrice;
      if (maxP !== "" && maxP != null && !Number.isNaN(Number(maxP))) {
        const n = Number(maxP);
        list = list.filter((p) => Number(p.price) <= n);
      }
      if (this.filterBookingStatus) {
        list = list.filter((p) => p.bookingStatus === this.filterBookingStatus);
      }
      return list;
    },
    tripSummary() {
      const q = this.$route.query;
      const parts = [];
      if (q.pickupLocation) {
        parts.push(`Pick-up: ${q.pickupLocation}`);
      }
      if (q.pickupDate) {
        parts.push(`From ${q.pickupDate}`);
      }
      if (q.dropoffDate) {
        parts.push(`To ${q.dropoffDate}`);
      }
      return parts.length ? parts.join(" · ") : "";
    },
    hasActiveFilters() {
      return !!(this.filterCategoryId || this.filterMaxPrice !== "" || this.filterBookingStatus);
    },
  },
  watch: {
    $route: {
      async handler() {
        await this.applyFromRoute();
      },
      immediate: true,
    },
  },
  mounted() {
    this.role = localStorage.getItem("role");
  },
  methods: {
    async applyFromRoute() {
      const raw = this.$route.query.q;
      if (raw && String(raw).trim()) {
        const term = String(raw).trim();
        this.searchText = term;
        await this.runNameSearch(term);
      } else {
        this.nameSearchActive = false;
        this.apiResults = [];
        this.search = false;
        this.notFound = false;
        this.text = null;
      }
    },
    async runNameSearch(name) {
      try {
        const res = await axios.get(
          `${this.baseURL}/product/find/?name=${encodeURIComponent(name)}`
        );
        this.apiResults = res.data || [];
        this.nameSearchActive = true;
        this.search = true;
        this.notFound = this.apiResults.length === 0;
        this.text = name;
      } catch (err) {
        console.log("err", err);
        this.apiResults = [];
        this.nameSearchActive = true;
        this.search = true;
        this.notFound = true;
        this.text = name;
      }
    },
    async onSearchSubmit() {
      const term = (this.searchText || "").trim();
      if (!term) {
        this.nameSearchActive = false;
        this.apiResults = [];
        this.search = false;
        this.notFound = false;
        this.text = null;
        const q = { ...this.$route.query };
        delete q.q;
        await this.$router.replace({ name: "VehiclesView", query: q });
        return;
      }
      await this.runNameSearch(term);
      await this.$router.replace({
        name: "VehiclesView",
        query: { ...this.$route.query, q: term },
      });
    },
    async clearSearch() {
      this.nameSearchActive = false;
      this.apiResults = [];
      this.search = false;
      this.notFound = false;
      this.text = null;
      this.searchText = "";
      this.filterCategoryId = "";
      this.filterMaxPrice = "";
      this.filterBookingStatus = "";
      await this.$router.replace({ name: "VehiclesView", query: {} });
    },
    productCategory(product, categories) {
      let categoryName = "";
      if (categories) {
        for (let j = 0; j < categories.length; j++) {
          if (product.categoryId === categories[j].id) {
            categoryName = categories[j].categoryName;
          }
        }
      }
      return categoryName;
    },
  },
};
</script>

<style scoped>
.bg-body-tertiary {
  background-color: var(--royal-midnight-blue);
}
#search-button-navbar {
  background-color: #c18e32;
  border-radius: 0 0.5rem 0.5rem 0;
  border: 1px solid #c18e32;
  color: #fff;
  cursor: pointer;
}
div {
  transition: all 1s ease-in-out 0s;
}
</style>
