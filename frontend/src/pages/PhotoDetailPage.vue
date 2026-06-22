<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {
  ArrowLeft,
  ArrowRight,
  CalendarDays,
  CloudSun,
  Download,
  Grid2X2,
  Heart,
  ImageIcon,
  NotebookPen,
  Share2,
  Smartphone,
  Sparkles,
  Tag
} from 'lucide-vue-next';
import ImageLightbox from '../components/ImageLightbox.vue';
import { fetchPhoto, fetchPhotos } from '../api/site';
import type { Photo } from '../types/models';

interface PhotoMoodMeta {
  tags: string[];
  moodText: string;
  moodState: string;
  moodLevel: number;
  weatherLabel: string;
  weatherTemp: string;
  location: string;
  albumName: string;
  journal: string;
  note: string;
}

const route = useRoute();
const router = useRouter();

const photo = ref<Photo | null>(null);
const photos = ref<Photo[]>([]);
const loading = ref(true);
const collected = ref(false);
const imageSize = ref<{ width: number; height: number } | null>(null);
const previewOpen = ref(false);

const favoriteStorageKey = 'xiaoke_collected_photos';

const metaMap: Record<string, PhotoMoodMeta> = {
  '画室里的样子': {
    tags: ['画室', '日常', '画画', '安静时刻', '暖光'],
    moodText: '安静 / 专注 / 有点害羞',
    moodState: '正在画画',
    moodLevel: 4,
    weatherLabel: '晴天',
    weatherTemp: '24°C',
    location: '画室',
    albumName: '小可的日常',
    journal:
      '那天她坐在画室靠窗的位置，桌上散着画纸和颜料。灯光很安静，连翻页的声音都变得很轻。她抬头看过来的那一瞬间，像是刚好被温柔记住。',
    note: '专注时光总是过得很快，不知不觉就画了一下午。'
  },
  '今日小可': {
    tags: ['今夜', '表情照', '靠近', '温柔', '被记住'],
    moodText: '心情晴朗，万物可爱',
    moodState: '看着镜头',
    moodLevel: 4,
    weatherLabel: '夜晚',
    weatherTemp: '刚刚好',
    location: '房间',
    albumName: '小可的日常',
    journal:
      '她很轻地抬起眼，安安静静地看过来。没有刻意摆拍，也没有很多解释，只是把那一点靠近和信任留在了镜头里。',
    note: '今天也要好好生活呀。'
  },
  '专注时刻': {
    tags: ['写字', '学习', '认真', '安静', '桌边'],
    moodText: '平静 / 投入 / 认真',
    moodState: '低头写字',
    moodLevel: 4,
    weatherLabel: '晴天',
    weatherTemp: '22°C',
    location: '书桌边',
    albumName: '小可的日常',
    journal:
      '她低头写字的时候，周围的声音会自然退开一点。那种认真不是刻意装出来的，而是刚好和她的气质很配。',
    note: '认真起来的时候，连时间都会慢一点。'
  },
  '简单的早餐': {
    tags: ['早餐', '热牛奶', '清晨', '温热', '生活'],
    moodText: '温热 / 满足 / 很日常',
    moodState: '准备吃早餐',
    moodLevel: 4,
    weatherLabel: '晴天',
    weatherTemp: '20°C',
    location: '餐桌边',
    albumName: '小可的日常',
    journal:
      '吐司和热牛奶都很普通，可正因为普通，才更像被认真过的生活。安静吃早餐的那几分钟，也会让人觉得很值得。',
    note: '简单一点，也照样很满足。'
  },
  '出门走走': {
    tags: ['散步', '街边', '傍晚', '风', '随手拍'],
    moodText: '轻松 / 放松 / 被风吹过',
    moodState: '出门走走',
    moodLevel: 4,
    weatherLabel: '傍晚',
    weatherTemp: '21°C',
    location: '街边',
    albumName: '小可的日常',
    journal:
      '她在路边停了一下，顺手整理头发，风刚好吹过来。明明只是很小的停顿，却让喜欢这件事忽然有了很具体的样子。',
    note: '只是随便走走，也能把心情吹得很轻。'
  },
  '比耶一下': {
    tags: ['自拍', '可爱', '轻松', '晚一点', '表情'],
    moodText: '开心 / 轻松 / 偷偷得意',
    moodState: '举起相机',
    moodLevel: 5,
    weatherLabel: '室内',
    weatherTemp: '暖暖的',
    location: '房间',
    albumName: '小可的日常',
    journal:
      '她说只是随手拍一下，可那点藏不住的小得意还是露出来了。被看见之后，反而更像一张想认真收藏的照片。',
    note: '偶尔也会有一点点想被夸。'
  }
};

const fallbackMeta: PhotoMoodMeta = {
  tags: ['日常', '照片', '安静时刻', '小可', '被记住'],
  moodText: '平静 / 温柔 / 很适合收藏',
  moodState: '留在这一天',
  moodLevel: 4,
  weatherLabel: '室内',
  weatherTemp: '刚刚好',
  location: '小可的日常',
  albumName: '小可的日常',
  journal:
    '这张照片没有很刻意的情节，却把那天的光线、呼吸和情绪一起留下来了。越是这样的日常，越值得被慢慢收好。',
  note: '把温柔的瞬间认真存起来。'
};

const currentMeta = computed(() => (photo.value ? metaMap[photo.value.title] ?? fallbackMeta : fallbackMeta));
const currentIndex = computed(() => photos.value.findIndex((item) => item.id === photo.value?.id));
const previousPhoto = computed(() => (currentIndex.value > 0 ? photos.value[currentIndex.value - 1] : null));
const nextPhoto = computed(() =>
  currentIndex.value >= 0 && currentIndex.value < photos.value.length - 1 ? photos.value[currentIndex.value + 1] : null
);

const relatedPhotos = computed(() => {
  if (!photo.value) return [];
  const sameDate = photos.value.filter(
    (item) => item.id !== photo.value?.id && item.takenDate && item.takenDate === photo.value?.takenDate
  );
  if (sameDate.length >= 5) return sameDate.slice(0, 5);
  const fillers = photos.value.filter((item) => item.id !== photo.value?.id && !sameDate.some((same) => same.id === item.id));
  return [...sameDate, ...fillers].slice(0, 5);
});

const infoRows = computed(() => {
  if (!photo.value) return [];
  return [
    ['拍摄时间', formatDateTime(photo.value.takenDate || photo.value.uploadDate)],
    ['拍摄地点', currentMeta.value.location],
    ['天气', currentMeta.value.weatherLabel],
    ['相册', currentMeta.value.albumName],
    ['设备', 'iPhone 15 Pro'],
    ['尺寸', imageSize.value ? `${imageSize.value.width} × ${imageSize.value.height}` : '读取中']
  ];
});

function formatDate(date?: string) {
  if (!date) return '';
  return date.slice(0, 10);
}

function formatDateLabel(date?: string) {
  if (!date) return '';
  return date.slice(0, 10).replace(/-/g, '.');
}

function formatDateTime(date?: string) {
  if (!date) return '';
  const [day, timeRaw] = date.split('T');
  const time = timeRaw?.slice(0, 5) ?? '20:31';
  return `${day} ${time}`;
}

function resolveImageSize(url: string) {
  imageSize.value = null;
  const image = new Image();
  image.onload = () => {
    imageSize.value = { width: image.naturalWidth, height: image.naturalHeight };
  };
  image.src = url;
}

function syncCollectedState() {
  if (!photo.value) return;
  const saved = JSON.parse(localStorage.getItem(favoriteStorageKey) ?? '[]') as number[];
  collected.value = saved.includes(photo.value.id);
}

async function loadPage() {
  loading.value = true;
  try {
    const [photoData, photoPage] = await Promise.all([fetchPhoto(String(route.params.id)), fetchPhotos()]);
    photo.value = photoData;
    photos.value = photoPage.records;
    if (photo.value?.imageUrl) {
      resolveImageSize(photo.value.imageUrl);
      syncCollectedState();
    }
  } finally {
    loading.value = false;
  }
}

function goToPhoto(target: Photo | null) {
  if (!target) return;
  router.push(`/album/${target.id}`);
}

function toggleCollected() {
  if (!photo.value) return;
  const saved = new Set<number>(JSON.parse(localStorage.getItem(favoriteStorageKey) ?? '[]') as number[]);
  if (saved.has(photo.value.id)) {
    saved.delete(photo.value.id);
    collected.value = false;
  } else {
    saved.add(photo.value.id);
    collected.value = true;
  }
  localStorage.setItem(favoriteStorageKey, JSON.stringify([...saved]));
}

function downloadImage() {
  if (!photo.value) return;
  const link = document.createElement('a');
  link.href = photo.value.imageUrl;
  link.download = `${photo.value.title}.png`;
  link.click();
}

async function sharePhoto() {
  if (!photo.value) return;
  const shareData = {
    title: photo.value.title,
    text: photo.value.diaryNote,
    url: window.location.href
  };
  if (navigator.share) {
    await navigator.share(shareData);
    return;
  }
  await navigator.clipboard.writeText(window.location.href);
}

function openPreview() {
  if (!photo.value?.imageUrl) return;
  previewOpen.value = true;
}

function closePreview() {
  previewOpen.value = false;
}

function writeMemory() {
  if (!photo.value) return;
  const draft = {
    title: `${photo.value.title}的小记`,
    summary: photo.value.diaryNote,
    content: `${photo.value.diaryNote}\n\n${currentMeta.value.journal}`,
    memoryDate: formatDate(photo.value.takenDate || photo.value.uploadDate),
    moodTags: currentMeta.value.tags,
    coverImageUrl: photo.value.imageUrl || '/images/xiaoke.png',
    featured: false
  };
  localStorage.setItem('xiaoke_memory_draft', JSON.stringify(draft));
  router.push('/admin/memories?draft=photo');
}

watch(
  () => route.params.id,
  async () => {
    await loadPage();
  }
);

onMounted(async () => {
  await loadPage();
});
</script>

<template>
  <section v-if="photo" class="photo-story-page photo-story-page-refined">
    <div class="photo-story-layout refined-layout">
      <section class="photo-visual-column">
        <div class="photo-main-frame refined-main-frame">
          <button type="button" class="story-image-trigger" @click="openPreview">
            <img :src="photo.imageUrl || '/images/xiaoke.png'" :alt="photo.title" />
          </button>

          <div class="photo-weather-chip">
            <CloudSun :size="20" />
            <div>
              <strong>{{ currentMeta.weatherLabel }}</strong>
              <span>{{ currentMeta.weatherTemp }}</span>
            </div>
          </div>

          <div class="photo-quote-card">
            <p>“</p>
            <strong>{{ currentMeta.journal.slice(0, 22) }}{{ currentMeta.journal.length > 22 ? '…' : '' }}</strong>
            <span>— 画室日常 {{ formatDateLabel(photo.takenDate || photo.uploadDate) }}</span>
            <Heart :size="16" fill="currentColor" />
          </div>
        </div>

        <div class="photo-action-bar refined-action-bar">
          <button type="button" class="story-action" @click="toggleCollected">
            <Heart :size="18" :fill="collected ? 'currentColor' : 'none'" />
            <span>{{ collected ? '已收藏这张' : '收藏这张' }}</span>
          </button>
          <button type="button" class="story-action" @click="writeMemory">
            <NotebookPen :size="18" />
            <span>写下回忆</span>
          </button>
          <button type="button" class="story-action" @click="downloadImage">
            <Download :size="18" />
            <span>下载原图</span>
          </button>
          <button type="button" class="story-action" @click="sharePhoto">
            <Share2 :size="18" />
            <span>分享照片</span>
          </button>
        </div>

        <section class="same-day-panel refined-same-day-panel">
          <div class="story-section-title">
            <h2><Sparkles :size="16" /> 同一天的其他瞬间</h2>
          </div>
          <div class="same-day-strip refined-same-day-strip">
            <RouterLink
              v-for="related in relatedPhotos"
              :key="related.id"
              :to="`/album/${related.id}`"
              class="same-day-thumb refined-same-day-thumb"
              :aria-label="related.title"
            >
              <img :src="related.thumbnailUrl || related.imageUrl || '/images/xiaoke.png'" :alt="related.title" />
            </RouterLink>
            <button
              v-if="nextPhoto"
              type="button"
              class="same-day-next refined-same-day-next"
              aria-label="下一张照片"
              @click="goToPhoto(nextPhoto)"
            >
              <ArrowRight :size="18" />
            </button>
          </div>
        </section>
      </section>

      <section class="photo-story-copy refined-copy">
        <p class="story-date"><CalendarDays :size="16" /> {{ formatDate(photo.takenDate || photo.uploadDate) }}</p>
        <h1>{{ photo.title }}</h1>
        <p class="story-summary">{{ photo.diaryNote }}</p>

        <div class="story-tag-row refined-tag-row">
          <span v-for="tag in currentMeta.tags" :key="tag"><Tag :size="14" /> {{ tag }}</span>
        </div>

        <div class="story-card-grid refined-card-grid">
          <article class="story-info-card refined-info-card">
            <h2><ImageIcon :size="18" /> 照片信息</h2>
            <dl>
              <template v-for="row in infoRows" :key="row[0]">
                <dt>{{ row[0] }}</dt>
                <dd>{{ row[1] }}</dd>
              </template>
            </dl>
          </article>

          <article class="story-mood-card refined-mood-card">
            <h2><Heart :size="18" fill="currentColor" /> 今日心情</h2>
            <div class="mood-grid">
              <span>心情状态</span>
              <strong>{{ currentMeta.moodText }}</strong>
              <span>可爱指数</span>
              <div class="mood-hearts">
                <Heart
                  v-for="index in 5"
                  :key="index"
                  :size="17"
                  :fill="index <= currentMeta.moodLevel ? 'currentColor' : 'none'"
                />
              </div>
              <span>当时状态</span>
              <strong>{{ currentMeta.moodState }}</strong>
            </div>

            <div class="mood-note-bubble">
              <p>{{ currentMeta.note }}</p>
              <span>◡̈</span>
            </div>
          </article>
        </div>

        <article class="story-journal-card refined-journal-card">
          <h2><NotebookPen :size="18" /> 回忆手账</h2>
          <p>{{ currentMeta.journal }}</p>
        </article>

        <div class="story-nav-row refined-nav-row">
          <button type="button" class="story-nav-button" :disabled="!previousPhoto" @click="goToPhoto(previousPhoto)">
            <ArrowLeft :size="18" />
            <span>上一张</span>
          </button>
          <RouterLink to="/album" class="story-nav-button center">
            <Grid2X2 :size="18" />
            <span>返回相册</span>
          </RouterLink>
          <button type="button" class="story-nav-button" :disabled="!nextPhoto" @click="goToPhoto(nextPhoto)">
            <span>下一张</span>
            <ArrowRight :size="18" />
          </button>
        </div>
      </section>
    </div>

    <section class="photo-detail-footer-card">
      <article class="photo-footer-panel photo-footer-brand">
        <h3><Heart :size="18" fill="currentColor" /> 小可日常</h3>
        <p>把温柔的照片和句子认真收好，愿每一天都有一点点可爱。</p>
        <div class="photo-detail-socials">
          <span>♡</span>
          <span>☺</span>
          <span>♫</span>
          <span>☁</span>
        </div>
      </article>
      <article class="photo-footer-panel photo-footer-note">
        <h3>小小备注</h3>
        <p>初遇于 2024 年 10 月 17 日</p>
        <strong>今天也要好好生活呀 🌸</strong>
      </article>
      <article class="photo-footer-panel photo-footer-admin">
        <h3><Smartphone :size="18" /> 管理员入口</h3>
        <p class="photo-footer-admin-copy">把后台入口轻轻收在这里，方便整理照片和小回忆。</p>
        <RouterLink to="/admin/login" class="photo-footer-admin-link">
          <span>进入管理后台</span>
        </RouterLink>
        <p class="footer-cat">૮₍˶ᵔ ᵕ ᵔ˶₎ა</p>
      </article>
    </section>

    <ImageLightbox
      :open="previewOpen"
      :image-url="photo.imageUrl || '/images/xiaoke.png'"
      :title="photo.title"
      :subtitle="photo.diaryNote"
      @close="closePreview"
    />
  </section>

  <section v-else-if="loading" class="loading-panel">正在把这页照片轻轻摆好…</section>
</template>
