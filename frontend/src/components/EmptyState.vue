<script setup lang="ts">
defineProps<{ title: string; description: string }>();
</script>

<template>
  <div class="empty-state">
    <h3>{{ title }}</h3>
    <p>{{ description }}</p>
  </div>
</template>

