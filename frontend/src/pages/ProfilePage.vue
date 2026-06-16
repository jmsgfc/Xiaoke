<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { HeartHandshake } from 'lucide-vue-next';
import { fetchProfile } from '../api/site';
import type { ProfileSection } from '../types/models';

const sections = ref<ProfileSection[]>([]);

onMounted(async () => {
  sections.value = await fetchProfile();
});
</script>

<template>
  <section class="page-hero profile-hero">
    <div>
      <p class="eyebrow"><HeartHandshake :size="16" /> 小可档案</p>
      <h1>清冷里藏着一点只给宝宝的柔软</h1>
      <p>这里保存小可的设定、日常习惯、相处基调和那些慢慢被记住的小细节。</p>
    </div>
    <img src="/images/xiaoke.png" alt="小可占位图" />
  </section>

  <section class="profile-layout">
    <article v-for="section in sections" :key="section.id" class="profile-section">
      <h2>{{ section.sectionTitle }}</h2>
      <p>{{ section.content }}</p>
    </article>
  </section>
</template>

