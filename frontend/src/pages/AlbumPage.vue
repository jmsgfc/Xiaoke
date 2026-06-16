<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { ArrowDown, ChevronDown, Heart } from 'lucide-vue-next';
import EmptyState from '../components/EmptyState.vue';
import { fetchPhotoGroups } from '../api/site';
import type { Photo, PhotoGroup } from '../types/models';

const groups = ref<PhotoGroup[]>([]);
const sortMode = ref('latest');
const sortLabel = computed(() => (sortMode.value === 'oldest' ? '最早上传' : '最新上传'));

onMounted(async () => {
  groups.value = await fetchPhotoGroups();
});

const monthTitle = (month: string) => {
  const match = month.match(/^(\d{4})[-年/](\d{1,2})/);
  if (!match) {
    return month;
  }
  return `${match[1]}年${Number(match[2])}月`;
};

const photoDate = (photo: Photo) => photo.uploadDate?.slice(0, 10) || '';

const sortedGroups = computed(() => {
  return groups.value.map((group) => ({
    ...group,
    photos: [...group.photos].sort((a, b) => {
      const left = new Date(a.uploadDate).getTime();
      const right = new Date(b.uploadDate).getTime();
      return sortMode.value === 'oldest' ? left - right : right - left;
    })
  }));
});
</script>

<template>
  <section class="album-toolbar" aria-label="相册排序">
    <label class="album-sort">
      <span>{{ sortLabel }}</span>
      <select v-model="sortMode" aria-label="照片排序">
        <option value="latest">最新上传</option>
        <option value="oldest">最早上传</option>
      </select>
      <ChevronDown :size="17" />
    </label>
  </section>

  <EmptyState v-if="groups.length === 0" title="还没有照片" description="等第一张小可照片被上传，这里就会亮起来。" />

  <section v-for="group in sortedGroups" :key="group.month" class="album-month">
    <div class="album-month-heading">
      <h2><span aria-hidden="true"></span>{{ monthTitle(group.month) }}</h2>
      <p>上传日期 <ArrowDown :size="15" /></p>
    </div>

    <div class="album-wall">
      <RouterLink v-for="photo in group.photos" :key="photo.id" :to="`/album/${photo.id}`" class="album-photo-card">
        <img :src="photo.thumbnailUrl || photo.imageUrl || '/images/xiaoke.png'" :alt="photo.title" />
        <div class="album-photo-body">
          <div class="album-photo-title">
            <h3>{{ photo.title }}</h3>
            <Heart :size="22" />
          </div>
          <p class="album-photo-date"><span>上传日期</span>{{ photoDate(photo) }}</p>
          <p class="album-photo-note">{{ photo.diaryNote }}</p>
        </div>
      </RouterLink>
    </div>
  </section>
</template>
