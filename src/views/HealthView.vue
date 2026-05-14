<template>
  <div class="health-page">
    <h1>Service is healthy</h1>
    <p>Status: healthy</p>
    <p>Service: frontend</p>
  </div>
</template>

<script>
export default {
  name: "HealthView",
};
</script>

<style scoped>
.health-page {
  min-height: 60vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
}
</style>
