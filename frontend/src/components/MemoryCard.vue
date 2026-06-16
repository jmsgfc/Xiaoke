<script setup lang="ts">
import { BookOpenText } from 'lucide-vue-next';
import type { MemoryEntry } from '../types/models';

defineProps<{ memory: MemoryEntry }>();
</script>

<template>
  <RouterLink :to="`/memories/${memory.id}`" class="memory-card">
    <img v-if="memory.coverImageUrl" :src="memory.coverImageUrl" :alt="memory.title" />
    <div class="memory-card-body">
      <div class="memory-kicker"><BookOpenText :size="15" /> {{ memory.memoryDate || '温柔记录' }}</div>
      <h3>{{ memory.title }}</h3>
      <p>{{ memory.summary }}</p>
      <div class="tag-row">
        <span v-for="tag in memory.moodTags" :key="tag">{{ tag }}</span>
      </div>
    </div>
  </RouterLink>
</template>

