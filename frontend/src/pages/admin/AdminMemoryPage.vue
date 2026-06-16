<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import { useRoute } from 'vue-router';
import { ImagePlus, X } from 'lucide-vue-next';
import AdminNav from '../../components/AdminNav.vue';
import { createMemory, removeMemory } from '../../api/admin';
import { fetchMemories, fetchPhotos } from '../../api/site';
import type { MemoryEntry, Photo } from '../../types/models';

const route = useRoute();

const memories = ref<MemoryEntry[]>([]);
const photos = ref<Photo[]>([]);
const message = ref('');
const pickerOpen = ref(false);
const pickerQuery = ref('');
const pickerSortMode = ref<'latest' | 'oldest'>('latest');
const draftKey = 'xiaoke_memory_draft';

const form = reactive({
  title: '雨后画室与红豆小汤圆',
  summary: '台灯光线下的温度，被悄悄收进了手账。',
  content:
    '雨后的画室很安静，桌上放着还冒着热气的甜汤。小可嘴上说只是困了，手指却把杯子捧得很紧。',
  memoryDate: '2024-10-20',
  moodTags: '画室,甜甜的,深夜',
  coverImageUrl: '/images/xiaoke.png',
  featured: true
});

function applyDraft() {
  const raw = localStorage.getItem(draftKey);
  if (!raw) return;

  try {
    const draft = JSON.parse(raw) as Partial<typeof form> & { moodTags?: string[] | string };
    form.title = draft.title || form.title;
    form.summary = draft.summary || form.summary;
    form.content = draft.content || form.content;
    form.memoryDate = draft.memoryDate || form.memoryDate;
    form.coverImageUrl = draft.coverImageUrl || form.coverImageUrl;
    form.featured = Boolean(draft.featured);
    form.moodTags = Array.isArray(draft.moodTags)
      ? draft.moodTags.join(',')
      : draft.moodTags || form.moodTags;
    message.value = '已经帮你把照片里的内容预填到回忆表单里了。';
  } catch {
    message.value = '';
  }
}

async function load() {
  memories.value = (await fetchMemories()).records;
  photos.value = (await fetchPhotos()).records;
}

const selectedCoverPhoto = computed(
  () =>
    photos.value.find((photo) => photo.imageUrl === form.coverImageUrl || photo.thumbnailUrl === form.coverImageUrl) ??
    null
);

const filteredPhotos = computed(() => {
  const keyword = pickerQuery.value.trim().toLowerCase();
  if (!keyword) return photos.value;
  return photos.value.filter((photo) => {
    const haystack = [photo.title, photo.diaryNote, photo.takenDate, photo.uploadDate]
      .filter(Boolean)
      .join(' ')
      .toLowerCase();
    return haystack.includes(keyword);
  });
});

const sortedFilteredPhotos = computed(() => {
  const direction = pickerSortMode.value === 'latest' ? -1 : 1;
  return [...filteredPhotos.value].sort((a, b) => {
    const left = new Date(a.takenDate || a.uploadDate || 0).getTime();
    const right = new Date(b.takenDate || b.uploadDate || 0).getTime();
    return (left - right) * direction;
  });
});

function formatPhotoDate(date?: string) {
  if (!date) return '未记录日期';
  return date.slice(0, 10);
}

function openPicker() {
  pickerQuery.value = '';
  pickerSortMode.value = 'latest';
  pickerOpen.value = true;
}

function closePicker() {
  pickerOpen.value = false;
}

function selectCoverPhoto(photo: Photo) {
  form.coverImageUrl = photo.imageUrl;
  closePicker();
}

async function submit() {
  await createMemory({
    ...form,
    moodTags: form.moodTags
      .split(',')
      .map((tag) => tag.trim())
      .filter(Boolean),
    isFeatured: form.featured,
    visibility: 'PUBLIC'
  });
  localStorage.removeItem(draftKey);
  message.value = '回忆已经写进手账。';
  await load();
}

async function deleteMemory(id: number) {
  if (!confirm('确定删除这段回忆吗？')) return;
  await removeMemory(id);
  await load();
}

onMounted(async () => {
  if (route.query.draft) {
    applyDraft();
  }
  await load();
});
</script>

<template>
  <div class="admin-layout">
    <AdminNav />
    <section class="admin-content">
      <h1>回忆管理</h1>
      <form class="admin-form" @submit.prevent="submit">
        <label>标题<input v-model="form.title" /></label>
        <label>摘要<input v-model="form.summary" /></label>
        <label>正文<textarea v-model="form.content" rows="5" /></label>
        <label>日期<input v-model="form.memoryDate" type="date" /></label>
        <label>标签<input v-model="form.moodTags" /></label>
        <label class="memory-cover-field">
          <span>封面图</span>
          <div class="memory-cover-input-row">
            <input v-model="form.coverImageUrl" />
            <button class="ghost-button" type="button" @click="openPicker">
              <ImagePlus :size="16" />
              选择已有照片
            </button>
          </div>
        </label>
        <div v-if="selectedCoverPhoto" class="memory-cover-preview">
          <img :src="selectedCoverPhoto.thumbnailUrl || selectedCoverPhoto.imageUrl" :alt="selectedCoverPhoto.title" />
          <div>
            <strong>{{ selectedCoverPhoto.title }}</strong>
            <span>{{ formatPhotoDate(selectedCoverPhoto.takenDate || selectedCoverPhoto.uploadDate) }}</span>
          </div>
        </div>
        <label class="check-row"><input v-model="form.featured" type="checkbox" /> 设为精选</label>
        <button class="primary-button" type="submit">保存回忆</button>
        <p v-if="message" class="form-success">{{ message }}</p>
      </form>

      <div class="admin-list text-list">
        <article v-for="memory in memories" :key="memory.id">
          <div>
            <h3>{{ memory.title }}</h3>
            <p>{{ memory.summary }}</p>
          </div>
          <button class="danger-button" @click="deleteMemory(memory.id)">删除</button>
        </article>
      </div>

      <div v-if="pickerOpen" class="admin-photo-picker-overlay" @click.self="closePicker">
        <div class="admin-photo-picker">
          <div class="admin-photo-picker-head">
            <div>
              <h2>选择已有照片</h2>
              <p>选中的照片会自动作为这条回忆的封面图。</p>
            </div>
            <button class="ghost-button icon-only" type="button" aria-label="关闭选图器" @click="closePicker">
              <X :size="18" />
            </button>
          </div>

          <div class="admin-photo-picker-toolbar">
            <div class="admin-photo-picker-toolbar-left">
              <input
                v-model="pickerQuery"
                type="search"
                placeholder="搜索标题、日记句子或日期"
                aria-label="搜索已有照片"
              />
            </div>
            <div class="admin-photo-picker-toolbar-right">
              <div class="admin-photo-picker-sort" role="tablist" aria-label="照片排序">
                <button
                  type="button"
                  :class="{ active: pickerSortMode === 'latest' }"
                  @click="pickerSortMode = 'latest'"
                >
                  按最新
                </button>
                <button
                  type="button"
                  :class="{ active: pickerSortMode === 'oldest' }"
                  @click="pickerSortMode = 'oldest'"
                >
                  按最早
                </button>
              </div>
              <span>共 {{ sortedFilteredPhotos.length }} 张</span>
            </div>
          </div>

          <div class="admin-photo-picker-grid">
            <button
              v-for="photo in sortedFilteredPhotos"
              :key="photo.id"
              type="button"
              class="admin-photo-picker-card"
              :class="{ active: selectedCoverPhoto?.id === photo.id }"
              @click="selectCoverPhoto(photo)"
            >
              <span v-if="selectedCoverPhoto?.id === photo.id" class="admin-photo-picker-badge">当前已选</span>
              <img :src="photo.thumbnailUrl || photo.imageUrl" :alt="photo.title" />
              <div>
                <strong>{{ photo.title }}</strong>
                <span>{{ formatPhotoDate(photo.takenDate || photo.uploadDate) }}</span>
              </div>
            </button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
