<script setup lang="ts">
import { CalendarDays } from 'lucide-vue-next';
import type { Photo } from '../types/models';

defineProps<{ photo: Photo }>();
</script>

<template>
  <RouterLink :to="`/album/${photo.id}`" class="photo-card">
    <img :src="photo.thumbnailUrl || photo.imageUrl || '/images/xiaoke.png'" :alt="photo.title" />
    <div class="photo-card-body">
      <div class="photo-card-title">
        <h3>{{ photo.title }}</h3>
        <span v-if="photo.featured">精选</span>
      </div>
      <p>{{ photo.diaryNote }}</p>
      <small><CalendarDays :size="14" /> {{ photo.uploadDate.slice(0, 10) }}</small>
    </div>
  </RouterLink>
</template>

