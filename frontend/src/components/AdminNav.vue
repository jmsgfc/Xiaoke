<script setup lang="ts">
import { Camera, Home, LogOut, NotebookTabs, Settings, UserRound } from 'lucide-vue-next';
import { useRouter } from 'vue-router';

const router = useRouter();

const items = [
  { to: '/admin', label: '概览', icon: Home },
  { to: '/admin/photos', label: '照片', icon: Camera },
  { to: '/admin/memories', label: '回忆', icon: NotebookTabs },
  { to: '/admin/profile', label: '档案', icon: UserRound },
  { to: '/admin/settings', label: '设置', icon: Settings }
];

function logout() {
  localStorage.removeItem('xiaoke_admin_token');
  router.push('/admin/login');
}
</script>

<template>
  <aside class="admin-nav">
    <RouterLink to="/" class="brand admin-brand">
      <span class="brand-mark">小</span>
      <span>小可日常</span>
    </RouterLink>
    <nav>
      <RouterLink v-for="item in items" :key="item.to" :to="item.to">
        <component :is="item.icon" :size="18" />
        <span>{{ item.label }}</span>
      </RouterLink>
    </nav>
    <button class="ghost-button" type="button" @click="logout">
      <LogOut :size="18" />
      退出
    </button>
  </aside>
</template>

