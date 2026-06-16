<script setup lang="ts">
import { onMounted, ref } from 'vue';
import AdminNav from '../../components/AdminNav.vue';
import { updateProfileSection } from '../../api/admin';
import { fetchProfile } from '../../api/site';
import type { ProfileSection } from '../../types/models';

const sections = ref<ProfileSection[]>([]);
const message = ref('');

async function load() {
  sections.value = await fetchProfile();
}

async function save(section: ProfileSection) {
  await updateProfileSection(section.id, section);
  message.value = '档案已经更新。';
}

onMounted(load);
</script>

<template>
  <div class="admin-layout">
    <AdminNav />
    <section class="admin-content">
      <h1>档案管理</h1>
      <p v-if="message" class="form-success">{{ message }}</p>
      <article v-for="section in sections" :key="section.id" class="admin-form section-editor">
        <label>模块标题<input v-model="section.sectionTitle" /></label>
        <label>内容<textarea v-model="section.content" rows="5" /></label>
        <button class="primary-button" @click="save(section)">保存这个模块</button>
      </article>
    </section>
  </div>
</template>

