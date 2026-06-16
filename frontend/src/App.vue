<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import AppHeader from './components/AppHeader.vue';
import AppFooter from './components/AppFooter.vue';

const route = useRoute();
const isAdmin = computed(() => route.path.startsWith('/admin'));
const showPublicFooter = computed(() => !isAdmin.value && route.path !== '/profile' && !route.path.startsWith('/album/'));
</script>

<template>
  <AppHeader v-if="!isAdmin" />
  <main :class="isAdmin ? 'admin-shell' : 'public-shell'">
    <RouterView />
  </main>
  <AppFooter v-if="showPublicFooter" />
</template>
