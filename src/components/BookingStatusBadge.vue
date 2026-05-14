<template>
  <span
    v-if="status"
    class="booking-status-badge badge rounded-pill"
    :class="badgeClass"
  >{{ status }}</span>
</template>

<script>
const STATUS_CLASS = {
  "Checked Out": "text-bg-primary",
  Reserved: "text-bg-info",
  "Booking Confirmed": "text-bg-success",
  Available: "text-bg-warning",
  Unavailable: "text-bg-dark",
};

export default {
  name: "BookingStatusBadge",
  props: {
    status: {
      type: String,
      default: "",
    },
  },
  computed: {
    badgeClass() {
      return STATUS_CLASS[this.status] || "text-bg-secondary";
    },
  },
};
</script>

<style scoped>
.booking-status-badge {
  font-size: 0.75rem;
  font-weight: 600;
  letter-spacing: 0.02em;
}
</style>
