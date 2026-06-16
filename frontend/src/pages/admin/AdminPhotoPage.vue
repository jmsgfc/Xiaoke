<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import AdminNav from '../../components/AdminNav.vue';
import { createPhoto, removePhoto, uploadImage } from '../../api/admin';
import { fetchPhotos } from '../../api/site';
import type { Photo } from '../../types/models';

const photos = ref<Photo[]>([]);
const saving = ref(false);
const message = ref('');
const form = reactive({
  title: '窗边的小可',
  diaryNote: '她说只是刚好坐在那里，其实耳尖红了。',
  takenDate: '',
  featured: true,
  imageUrl: '/images/xiaoke.png',
  thumbnailUrl: '/images/xiaoke.png',
  storagePath: '',
  thumbnailStoragePath: ''
});

async function load() {
  photos.value = (await fetchPhotos()).records;
}

async function onFileChange(event: Event) {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;
  const result = await uploadImage(file);
  form.imageUrl = result.imageUrl;
  form.thumbnailUrl = result.thumbnailUrl;
  form.storagePath = result.storagePath;
  form.thumbnailStoragePath = result.thumbnailStoragePath;
}

async function submit() {
  saving.value = true;
  message.value = '';
  try {
    await createPhoto({ ...form, visibility: 'PUBLIC', isFeatured: form.featured });
    message.value = '照片已经收好了。';
    await load();
  } finally {
    saving.value = false;
  }
}

async function deletePhoto(id: number) {
  if (!confirm('确定删除这张照片吗？')) return;
  await removePhoto(id);
  await load();
}

onMounted(load);
</script>

<template>
  <div class="admin-layout">
    <AdminNav />
    <section class="admin-content">
      <h1>照片管理</h1>
      <form class="admin-form" @submit.prevent="submit">
        <label>照片文件<input type="file" accept="image/*" @change="onFileChange" /></label>
        <label>标题<input v-model="form.title" /></label>
        <label>日记句子<textarea v-model="form.diaryNote" rows="3" /></label>
        <label>拍摄日期<input v-model="form.takenDate" type="date" /></label>
        <label class="check-row"><input v-model="form.featured" type="checkbox" /> 首页精选</label>
        <button class="primary-button" :disabled="saving" type="submit">{{ saving ? '保存中...' : '保存照片' }}</button>
        <p v-if="message" class="form-success">{{ message }}</p>
      </form>

      <div class="admin-list">
        <article v-for="photo in photos" :key="photo.id">
          <img :src="photo.thumbnailUrl || photo.imageUrl" :alt="photo.title" />
          <div>
            <h3>{{ photo.title }}</h3>
            <p>{{ photo.diaryNote }}</p>
          </div>
          <button class="danger-button" @click="deletePhoto(photo.id)">删除</button>
        </article>
      </div>
    </section>
  </div>
</template>

