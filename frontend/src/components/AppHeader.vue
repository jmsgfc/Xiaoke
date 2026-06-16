<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { Camera, Heart, House, NotebookTabs } from 'lucide-vue-next';

const route = useRoute();
const isHome = computed(() => route.path === '/' || route.path === '/profile');

const links = [
  { to: '/profile', label: '首页', icon: House },
  { to: '/album', label: '日常相册', icon: Camera },
  { to: '/memories', label: '回忆手账', icon: NotebookTabs },
  { to: '/about', label: '小可档案', icon: Heart }
];
</script>

<template>
  <header :class="['site-header', { 'home-header': isHome }]">
    <RouterLink to="/profile" class="brand home-brand">
      <span class="brand-mark">小</span>
      <span class="brand-copy">
        <strong>小可日常</strong>
        <small>记录温柔，收藏热爱</small>
      </span>
    </RouterLink>
    <nav class="nav-links" aria-label="主导航">
      <RouterLink v-for="link in links" :key="`${link.to}-${link.label}`" :to="link.to">
        <component :is="link.icon" :size="17" />
        <span>{{ link.label }}</span>
      </RouterLink>
    </nav>
  </header>
</template>
