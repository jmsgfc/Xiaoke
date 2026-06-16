<script setup lang="ts">
import { onMounted, ref } from 'vue';
import AdminNav from '../../components/AdminNav.vue';
import { fetchHome, fetchMemories, fetchPhotos } from '../../api/site';
import type { HomeData } from '../../types/models';

const home = ref<HomeData | null>(null);
const photoTotal = ref(0);
const memoryTotal = ref(0);

onMounted(async () => {
  home.value = await fetchHome();
  photoTotal.value = (await fetchPhotos()).total;
  memoryTotal.value = (await fetchMemories()).total;
});
</script>

<template>
  <div class="admin-layout">
    <AdminNav />
    <section class="admin-content">
      <h1>后台概览</h1>
      <p>这里先放最常用的维护入口，后续可以继续加统计和备份状态。</p>
      <div class="stat-grid">
        <article><span>{{ photoTotal }}</span><p>照片记录</p></article>
        <article><span>{{ memoryTotal }}</span><p>回忆卡片</p></article>
        <article><span>1</span><p>管理员账号</p></article>
      </div>
      <div v-if="home" class="admin-panel">
        <h2>今日小可</h2>
        <p>{{ home.todayNote }}</p>
      </div>
    </section>
  </div>
</template>

