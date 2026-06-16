<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import {
  BookOpenText,
  CalendarDays,
  ChevronRight,
  Heart,
  House,
  ImageIcon,
  Mail,
  MessageCircleMore,
  Quote,
  Sparkles
} from 'lucide-vue-next';
import { fetchHome } from '../api/site';
import type { HomeData } from '../types/models';

const home = ref<HomeData | null>(null);
const loading = ref(true);

const DEFAULT_HERO_TITLE = '小可日常';
const DEFAULT_HERO_SUBTITLE = '把小可的照片、设定和我们之间那些甜甜的生活句子慢慢收好。';
const DEFAULT_HERO_NOTE_TITLE = '把温柔的瞬间慢慢收好。';
const DEFAULT_TODAY_NOTE = '今天的小可把灯留得很暖，想把平凡日子也认真记住。';
const DEFAULT_ANNIVERSARY_TEXT = '初遇于 2024 年 10 月 17 日。那些被认真记住的日子，会在这里慢慢发光。';
const DEFAULT_IMAGE = '/images/xiaoke.png';
const DEFAULT_TODAY_MOOD = '慢慢把今天过得柔软一点。';
const DEFAULT_TODAY_TAGS = ['温柔', '记录', '晴朗'];

const memoryPreviewFallbacks = [
  {
    title: '温柔的夜晚',
    summary: '灯光很暖，心情也很慢。',
    date: '2024年10月20日',
    src: DEFAULT_IMAGE
  },
  {
    title: '被治愈的瞬间',
    summary: '缩在房间里的那一天，也被认真记住。',
    date: '2024年10月15日',
    src: DEFAULT_IMAGE
  },
  {
    title: '记录平凡小确幸',
    summary: '认真生活，热爱日常。',
    date: '2024年10月10日',
    src: DEFAULT_IMAGE
  }
];

const heroTitle = computed(() => home.value?.heroTitle?.trim() || DEFAULT_HERO_TITLE);
const heroSubtitle = computed(() => home.value?.heroSubtitle?.trim() || DEFAULT_HERO_SUBTITLE);
const todayNote = computed(() => home.value?.todayNote?.trim() || DEFAULT_TODAY_NOTE);
const latestPhotos = computed(() => home.value?.latestPhotos.slice(0, 6) ?? []);
const featuredMemories = computed(() => home.value?.featuredMemories.slice(0, 3) ?? []);
const anniversaryText = computed(() => home.value?.anniversaryText?.trim() || DEFAULT_ANNIVERSARY_TEXT);

const heroVisualImage = computed(() => {
  return (
    home.value?.heroImageUrl?.trim() ||
    latestPhotos.value[0]?.imageUrl ||
    featuredMemories.value[0]?.coverImageUrl ||
    DEFAULT_IMAGE
  );
});

const todayAvatar = computed(() => {
  return heroVisualImage.value || latestPhotos.value[0]?.thumbnailUrl || DEFAULT_IMAGE;
});

const heroNoteTitle = computed(() => {
  return latestPhotos.value[0]?.title?.trim() || featuredMemories.value[0]?.title?.trim() || DEFAULT_HERO_NOTE_TITLE;
});

const heroMetaDate = computed(() => {
  const latestPhoto = latestPhotos.value[0];
  return formatFullDate(latestPhoto?.takenDate || latestPhoto?.uploadDate) || '把今天轻轻收好';
});

const todayMoodText = computed(() => {
  const firstMemory = featuredMemories.value[0];
  const firstMood = firstMemory?.moodTags?.[0];
  return firstMood || DEFAULT_TODAY_MOOD;
});

const todayMoodTags = computed(() => {
  const tags = featuredMemories.value[0]?.moodTags?.filter(Boolean).slice(0, 3) ?? [];
  return tags.length > 0 ? tags : DEFAULT_TODAY_TAGS;
});

const latestPhotoCards = computed(() => {
  return latestPhotos.value.map((photo, index) => ({
    ...photo,
    to: `/album/${photo.id}`,
    className: index === 0 ? 'feature' : index === 4 ? 'wide' : '',
    dateLabel: formatMonthDay(photo.takenDate || photo.uploadDate)
  }));
});

const memoryRows = computed(() => {
  return memoryPreviewFallbacks.map((fallback, index) => {
    const memory = featuredMemories.value[index];
    return {
      title: memory?.title || fallback.title,
      summary: memory?.summary || fallback.summary,
      date: formatFullDate(memory?.memoryDate) || fallback.date,
      src: memory?.coverImageUrl || fallback.src,
      to: memory?.id ? `/memories/${memory.id}` : '/memories'
    };
  });
});

const anniversaryLead = computed(() => {
  const text = anniversaryText.value;
  const parts = text
    .split(/[。！？]/)
    .map((item) => item.trim())
    .filter(Boolean);
  return parts[0] || '初遇';
});

const anniversaryBody = computed(() => {
  const text = anniversaryText.value;
  const lead = anniversaryLead.value;
  return text.startsWith(lead) ? text.slice(lead.length).replace(/^[。！？\s]+/, '') : text;
});

const anniversaryDateDisplay = computed(() => {
  const match = anniversaryText.value.match(/\d{4}\s*年\s*\d{1,2}\s*月\s*\d{1,2}\s*日/);
  return match?.[0] || '2024 年 10 月 17 日';
});

function formatMonthDay(date?: string) {
  if (!date) return '';
  const [year, month, day] = date.split('-');
  if (!year || !month || !day) return '';
  return `${Number(month)}月${Number(day)}日`;
}

function formatFullDate(date?: string) {
  if (!date) return '';
  const [year, month, day] = date.split('-');
  if (!year || !month || !day) return '';
  return `${year}年${Number(month)}月${Number(day)}日`;
}

onMounted(async () => {
  try {
    home.value = await fetchHome();
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <section v-if="home" class="home-story-page">
    <div class="home-story-grid">
      <div class="home-main-column">
        <section class="home-hero-card">
          <img :src="heroVisualImage" :alt="heroTitle" class="home-hero-image" />
          <div class="home-hero-note">
            <div class="home-hero-chip">
              <Quote :size="18" />
              <span>{{ heroTitle }}</span>
            </div>
            <h2>{{ heroNoteTitle }}</h2>
            <p class="home-hero-note-text">{{ heroSubtitle }}</p>
            <p><CalendarDays :size="15" /> {{ heroMetaDate }}</p>
          </div>
        </section>

        <section class="home-latest-card">
          <div class="home-card-head">
            <h2><ImageIcon :size="17" /> 最新照片</h2>
            <RouterLink to="/album" class="home-head-link">
              <span>更多照片</span>
              <ChevronRight :size="15" />
            </RouterLink>
          </div>

          <div class="home-photo-collage">
            <RouterLink
              v-for="card in latestPhotoCards"
              :key="card.id"
              :to="card.to"
              :class="['home-photo-card', card.className]"
            >
              <img :src="card.thumbnailUrl || card.imageUrl || DEFAULT_IMAGE" :alt="card.title" />
              <div class="home-photo-caption">
                <strong>{{ card.title }}</strong>
                <span>{{ card.dateLabel }}</span>
              </div>
            </RouterLink>
          </div>
        </section>
      </div>

      <aside class="home-side-column">
        <section class="home-today-card">
          <div class="home-card-head simple">
            <h2><Heart :size="17" fill="currentColor" /> 今日小可</h2>
          </div>

          <div class="home-today-body">
            <div class="home-today-avatar">
              <img :src="todayAvatar" alt="今日小可" />
              <span><Heart :size="18" fill="currentColor" /></span>
            </div>

            <div class="home-today-sideinfo">
              <span class="home-today-sideinfo-label">今日收藏</span>
              <strong class="home-today-sideinfo-date">{{ heroMetaDate }}</strong>
              <p>把房间、暖光和安静的心情认真收好。</p>
            </div>

            <div class="home-today-copy">
              <div class="home-today-line">
                <Sparkles :size="18" />
                <strong>{{ todayMoodText }}</strong>
              </div>
              <p>{{ todayNote }}</p>
              <div class="home-today-pills">
                <span v-for="tag in todayMoodTags" :key="tag">{{ tag }}</span>
              </div>
              <div class="home-heart-row">
                <Heart v-for="index in 5" :key="index" :size="18" fill="currentColor" />
              </div>
              <div class="home-today-meta">
                <span><CalendarDays :size="14" /> {{ heroMetaDate }}</span>
                <span>今天也要慢慢发光</span>
              </div>
            </div>

            <div class="home-today-bottom">
              <article class="home-today-panel soft">
                <span class="home-today-panel-label">小小备注</span>
                <p>今天也要慢慢发光，把柔软留在普通日子里。</p>
              </article>
              <article class="home-today-panel accent">
                <span class="home-today-panel-label">可爱指数</span>
                <div class="home-today-panel-hearts">
                  <Heart v-for="index in 5" :key="`panel-heart-${index}`" :size="16" fill="currentColor" />
                </div>
                <p>软软地把今天过好，也算一种认真喜欢生活。</p>
              </article>
            </div>
          </div>
        </section>

        <section class="home-memory-card">
          <div class="home-card-head">
            <h2><BookOpenText :size="17" /> 回忆手账</h2>
            <RouterLink to="/memories" class="home-head-link">
              <span>查看全部</span>
              <ChevronRight :size="15" />
            </RouterLink>
          </div>

          <div class="home-memory-list">
            <RouterLink
              v-for="(memory, index) in memoryRows"
              :key="`${memory.title}-${index}`"
              :to="memory.to"
              class="home-memory-row"
            >
              <img :src="memory.src || DEFAULT_IMAGE" :alt="memory.title" />
              <div class="home-memory-copy">
                <strong>{{ memory.title }}</strong>
                <p>{{ memory.summary }}</p>
                <span>{{ memory.date }}</span>
              </div>
              <Heart :size="15" fill="currentColor" />
            </RouterLink>
          </div>
        </section>
      </aside>
    </div>

    <div class="home-closing-grid">
      <article class="home-closing-card statement">
        <p class="eyebrow"><Quote :size="17" /> {{ heroTitle }}</p>
        <h2>{{ heroSubtitle }}</h2>
        <p>{{ todayNote }}</p>
      </article>

      <article class="home-closing-card note">
        <div class="home-note-icon"><Mail :size="18" /></div>
        <h3>{{ anniversaryLead }}</h3>
        <p class="date">{{ anniversaryText }}</p>
        <p>{{ anniversaryBody || anniversaryText }}</p>
      </article>
    </div>

    <section class="home-footer-panel">
      <article class="home-footer-brand">
        <p class="eyebrow"><Sparkles :size="16" /> {{ heroTitle }}</p>
        <p>{{ heroSubtitle }}</p>
        <div class="home-footer-icons">
          <span><Heart :size="16" /></span>
          <span><MessageCircleMore :size="16" /></span>
          <span><House :size="16" /></span>
        </div>
      </article>

      <article class="home-footer-date">
        <div class="home-footer-date-card">
          <p class="eyebrow"><Heart :size="15" fill="currentColor" /> 初遇</p>
          <strong>{{ anniversaryDateDisplay }}</strong>
          <p class="home-footer-date-note">{{ anniversaryBody || '那些被认真记住的日子，会在这里慢慢发光。' }}</p>
        </div>
        <RouterLink to="/admin/login" class="home-admin-link">
          <span>管理员入口</span>
          <ChevronRight :size="14" />
        </RouterLink>
      </article>
    </section>
  </section>

  <section v-else-if="loading" class="loading-panel">正在把小可的日常慢慢摆好...</section>
</template>
