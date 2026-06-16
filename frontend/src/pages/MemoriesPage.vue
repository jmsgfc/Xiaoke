<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { Grid2X2, Heart, List, Plus, SlidersHorizontal } from 'lucide-vue-next';
import { fetchMemories } from '../api/site';
import type { MemoryEntry } from '../types/models';

const memories = ref<MemoryEntry[]>([]);
const sortMode = ref('latest');
const viewMode = ref<'grid' | 'list'>('grid');

onMounted(async () => {
  memories.value = (await fetchMemories()).records;
});

const sortedMemories = computed(() => {
  return [...memories.value].sort((a, b) => {
    const left = new Date(a.memoryDate || '1970-01-01').getTime();
    const right = new Date(b.memoryDate || '1970-01-01').getTime();
    return sortMode.value === 'oldest' ? left - right : right - left;
  });
});

const formatDate = (date?: string) => {
  if (!date) {
    return '温柔记录';
  }
  return date.slice(0, 10).replace(/-/g, '.');
};

const firstTag = (memory: MemoryEntry) => memory.moodTags?.[0] || '甜甜的';

const tapeClass = (index: number) => `tape-${(index % 4) + 1}`;
</script>

<template>
  <section class="memory-book-hero">
    <div>
      <h1>回忆手账 <Heart :size="30" /></h1>
      <p>记录我们一起走过的每个小瞬间</p>
    </div>
    <RouterLink to="/admin/memories" class="memory-add-button">
      <Plus :size="24" />
      <span>新增回忆</span>
    </RouterLink>
  </section>

  <section class="memory-book-toolbar">
    <div>
      <strong>最近记录</strong>
      <span aria-hidden="true"></span>
    </div>
    <div class="memory-controls">
      <label class="memory-sort">
        <span>{{ sortMode === 'oldest' ? '按最早' : '按最新' }}</span>
        <select v-model="sortMode" aria-label="回忆排序">
          <option value="latest">按最新</option>
          <option value="oldest">按最早</option>
        </select>
        <SlidersHorizontal :size="15" />
      </label>
      <button :class="{ active: viewMode === 'grid' }" type="button" aria-label="网格视图" @click="viewMode = 'grid'">
        <Grid2X2 :size="18" />
      </button>
      <button :class="{ active: viewMode === 'list' }" type="button" aria-label="列表视图" @click="viewMode = 'list'">
        <List :size="18" />
      </button>
    </div>
  </section>

  <section :class="['memory-book-grid', { 'is-list': viewMode === 'list' }]">
    <RouterLink
      v-for="(memory, index) in sortedMemories"
      :key="memory.id"
      :to="`/memories/${memory.id}`"
      class="memory-note-card"
    >
      <span :class="['memory-tape', tapeClass(index)]" aria-hidden="true"></span>
      <img :src="memory.coverImageUrl || '/images/xiaoke.png'" :alt="memory.title" />
      <div class="memory-note-copy">
        <h2>{{ memory.title }}</h2>
        <time>{{ formatDate(memory.memoryDate) }}</time>
        <p>{{ memory.summary }}</p>
        <span class="memory-note-tag">{{ firstTag(memory) }} <Heart :size="14" fill="currentColor" /></span>
      </div>
    </RouterLink>

    <RouterLink to="/admin/memories" class="memory-create-card">
      <span><Plus :size="38" /></span>
      <strong>新增回忆</strong>
      <p>记录下此刻的美好瞬间</p>
    </RouterLink>
  </section>
</template>
