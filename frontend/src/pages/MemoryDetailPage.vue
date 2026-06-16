<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {
  ArrowLeft,
  ArrowRight,
  BookOpenText,
  CalendarDays,
  Heart,
  MapPin,
  MoonStar,
  Paperclip,
  ScrollText,
  Sparkles,
  SunMedium
} from 'lucide-vue-next';
import { fetchMemories, toggleMemoryFavorite } from '../api/site';
import type { MemoryEntry } from '../types/models';

interface CompanionImage {
  imageUrl: string;
  title: string;
}

interface MemoryVisualMeta {
  scene: string;
  moment: string;
  location: string;
  atmosphere: string;
  mood: string;
  note: string;
  keywords: string[];
  shotTime: string;
  companionImages: CompanionImage[];
}

const route = useRoute();
const router = useRouter();

const memories = ref<MemoryEntry[]>([]);
const loading = ref(true);
const favoriteSubmitting = ref(false);
const likedMemoryIds = ref<number[]>([]);

const storageKey = 'xiaoke_favorite_memories';

const visualMetaMap: Record<number, MemoryVisualMeta> = {
  12: {
    scene: '晚饭',
    moment: '晚饭',
    location: '房间',
    atmosphere: '暖光',
    mood: '安静 / 满足',
    note: '这不是特别丰盛的一餐，但因为是在安静的晚上，反而变成了一段很容易被记住的小日子。',
    keywords: ['晚饭', '热牛奶', '夜晚', '普通日子'],
    shotTime: '19:42',
    companionImages: [
      { imageUrl: '/uploads/photos/069_小可_晚饭吐司牛奶.png', title: '晚饭和热牛奶' },
      { imageUrl: '/uploads/photos/065_小可_画桌桌面平铺照.png', title: '饭后小记' },
      { imageUrl: '/uploads/photos/052_小可_床边角落夜景.png', title: '床边暖灯' },
      { imageUrl: '/uploads/photos/049_小可_房间全景广角照.png', title: '晚上的房间' },
      { imageUrl: '/uploads/photos/063_小可_画室窗边留白空镜.png', title: '窗边夜色' }
    ]
  },
  17: {
    scene: '夜晚',
    moment: '画画',
    location: '画桌旁',
    atmosphere: '台灯',
    mood: '陪伴 / 温柔',
    note: '那晚没有很多对白，更多的是待在彼此身边。安静本身，就已经很像一场温柔的回应。',
    keywords: ['画画', '窗边', '夜晚', '陪伴'],
    shotTime: '22:16',
    companionImages: [
      { imageUrl: '/uploads/photos/048_小可_夜晚窗边画画背影.png', title: '窗边画画' },
      { imageUrl: '/uploads/photos/051_小可_画桌角落夜景.png', title: '画桌角落' },
      { imageUrl: '/uploads/photos/061_小可_画画时低头作画.png', title: '低头作画' },
      { imageUrl: '/uploads/photos/064_小可_画室自然动作坐姿照.png', title: '画室里' },
      { imageUrl: '/uploads/photos/059_小可_窗边室内成稿照.png', title: '窗边的光' }
    ]
  },
  16: {
    scene: '夜晚',
    moment: '小夜灯',
    location: '床边',
    atmosphere: '暖黄灯',
    mood: '安稳 / 柔软',
    note: '很多被记住的瞬间都不是大事件，它们只是静静待在那里，替我们把安稳一点点攒起来。',
    keywords: ['床边', '角落', '夜灯', '安稳'],
    shotTime: '23:08',
    companionImages: [
      { imageUrl: '/uploads/photos/052_小可_床边角落夜景.png', title: '床边角落' },
      { imageUrl: '/uploads/photos/049_小可_房间全景广角照.png', title: '房间全景' },
      { imageUrl: '/uploads/photos/050_小可_客厅窗边夜景.png', title: '夜里的窗边' },
      { imageUrl: '/uploads/photos/053_小可_平时穿的鞋子.png', title: '生活细节' },
      { imageUrl: '/uploads/photos/038_小可_床上靠枕看镜头.png', title: '靠在床上' }
    ]
  },
  11: {
    scene: '房间',
    moment: '夜里',
    location: '公寓',
    atmosphere: '灯光',
    mood: '安心 / 同居感',
    note: '一个房间真正变得特别，不是因为布置得多漂亮，而是因为它一点点收纳了两个人共同生活的痕迹。',
    keywords: ['房间', '灯光', '生活', '共同日常'],
    shotTime: '21:17',
    companionImages: [
      { imageUrl: '/uploads/photos/049_小可_房间全景广角照.png', title: '灯亮着的房间' },
      { imageUrl: '/uploads/photos/052_小可_床边角落夜景.png', title: '床边灯光' },
      { imageUrl: '/uploads/photos/051_小可_画桌角落夜景.png', title: '画桌小角落' },
      { imageUrl: '/uploads/photos/050_小可_客厅窗边夜景.png', title: '夜晚窗边' },
      { imageUrl: '/uploads/photos/069_小可_晚饭吐司牛奶.png', title: '房间里的晚饭' }
    ]
  },
  14: {
    scene: '散步',
    moment: '傍晚',
    location: '街边',
    atmosphere: '晚风',
    mood: '心动 / 漂亮',
    note: '很喜欢这种没有安排、却会让人突然想停下来认真看一眼的时刻，好像喜欢就是这样慢慢变得具体的。',
    keywords: ['散步', '街灯', '橱窗', '喜欢'],
    shotTime: '18:35',
    companionImages: [
      { imageUrl: '/uploads/photos/057_小可_街边橱窗前整理头发.png', title: '橱窗前停下' },
      { imageUrl: '/uploads/photos/058_小可_街边橱窗前整理头发新版.png', title: '同一阵晚风' },
      { imageUrl: '/uploads/photos/056_小可_商场里边走边看店铺.png', title: '一起逛街' },
      { imageUrl: '/uploads/photos/055_小可_路边站立侧身照.png', title: '路边站一会儿' },
      { imageUrl: '/uploads/photos/054_小可_商场扶梯边侧脸照.png', title: '傍晚侧脸' }
    ]
  },
  13: {
    scene: '照顾',
    moment: '休息',
    location: '床边',
    atmosphere: '安静',
    mood: '依赖 / 被照顾',
    note: '比起说很多话，那时更想做的是把陪伴放在手边，把热水和安稳都递过去一点。',
    keywords: ['照顾', '依赖', '休息', '安静'],
    shotTime: '20:11',
    companionImages: [
      { imageUrl: '/uploads/photos/066_小可_生理期卧床休息.png', title: '安静躺着' },
      { imageUrl: '/uploads/photos/038_小可_床上靠枕看镜头.png', title: '靠在床上' },
      { imageUrl: '/uploads/photos/052_小可_床边角落夜景.png', title: '床边小夜灯' },
      { imageUrl: '/uploads/photos/069_小可_晚饭吐司牛奶.png', title: '给你准备的热牛奶' },
      { imageUrl: '/uploads/photos/070_小可_正面表情照.png', title: '安静看向镜头' }
    ]
  },
  15: {
    scene: '初遇',
    moment: '午后',
    location: '走廊',
    atmosphere: '日光',
    mood: '心动 / 安静',
    note: '有些瞬间并不热烈，却会在后来被反复想起。像这一眼，很轻，却很久都没有忘。',
    keywords: ['初遇', '走廊', '书页', '心动'],
    shotTime: '16:08',
    companionImages: [
      { imageUrl: '/uploads/photos/031_小可_走廊抱书路过照.png', title: '抱着书走过来' },
      { imageUrl: '/uploads/photos/014_小可_图书馆低头看书.png', title: '图书馆一页' },
      { imageUrl: '/uploads/photos/060_小可_窗边室内正面小稿.png', title: '安静坐着' },
      { imageUrl: '/uploads/photos/061_小可_画画时低头作画.png', title: '认真低头' },
      { imageUrl: '/uploads/photos/044_小可_暖光下正脸特写.png', title: '抬眼的时候' }
    ]
  }
};

const fallbackMeta: MemoryVisualMeta = {
  scene: '日常',
  moment: '回忆',
  location: '小可日常',
  atmosphere: '柔光',
  mood: '安静 / 被认真记住',
  note: '有些日子其实没有发生特别的事，但只要被认真留住，就会慢慢发亮。',
  keywords: ['日常', '回忆', '安静', '小片段'],
  shotTime: '20:18',
  companionImages: [
    { imageUrl: '/uploads/photos/070_小可_正面表情照.png', title: '今晚的小可' },
    { imageUrl: '/uploads/photos/060_小可_窗边室内正面小稿.png', title: '窗边' },
    { imageUrl: '/uploads/photos/061_小可_画画时低头作画.png', title: '认真画画' },
    { imageUrl: '/uploads/photos/069_小可_晚饭吐司牛奶.png', title: '晚饭' },
    { imageUrl: '/uploads/photos/052_小可_床边角落夜景.png', title: '床边灯光' }
  ]
};

const currentMemory = computed(() =>
  memories.value.find((item) => String(item.id) === String(route.params.id)) ?? null
);

const currentMeta = computed(() => {
  if (!currentMemory.value) return fallbackMeta;
  return visualMetaMap[currentMemory.value.id] ?? fallbackMeta;
});

const paragraphs = computed(() =>
  currentMemory.value?.content
    .split('\n')
    .map((item) => item.trim())
    .filter(Boolean) ?? []
);

const displayTags = computed(() => {
  if (!currentMemory.value) return [];
  return [...new Set([currentMeta.value.scene, ...currentMemory.value.moodTags, currentMeta.value.atmosphere])];
});

const currentIndex = computed(() =>
  memories.value.findIndex((item) => String(item.id) === String(route.params.id))
);

const previousMemory = computed(() =>
  currentIndex.value > 0 ? memories.value[currentIndex.value - 1] : null
);

const nextMemory = computed(() =>
  currentIndex.value >= 0 && currentIndex.value < memories.value.length - 1
    ? memories.value[currentIndex.value + 1]
    : null
);

const relatedMemories = computed(() => {
  if (!currentMemory.value) return [];
  const current = currentMemory.value;

  const sameTagMemories = memories.value.filter((item) => {
    if (item.id === current.id) return false;
    return item.moodTags.some((tag) => current.moodTags.includes(tag));
  });

  const fillers = memories.value.filter(
    (item) => item.id !== current.id && !sameTagMemories.some((same) => same.id === item.id)
  );

  return [current, ...sameTagMemories, ...fillers].slice(0, 5);
});

const detailRows = computed(() => {
  if (!currentMemory.value) return [];
  return [
    ['拍摄时间', `${currentMemory.value.memoryDate} ${currentMeta.value.shotTime}`],
    ['拍摄地点', currentMeta.value.location],
    ['相册', '回忆手账'],
    ['当时心情', currentMeta.value.mood],
    ['关键词', currentMeta.value.keywords.join('、')]
  ];
});

const isLiked = computed(() => {
  if (!currentMemory.value) return false;
  return likedMemoryIds.value.includes(currentMemory.value.id);
});

function hydrateFavoriteState() {
  try {
    const raw = localStorage.getItem(storageKey);
    likedMemoryIds.value = raw ? JSON.parse(raw) : [];
  } catch {
    likedMemoryIds.value = [];
  }
}

function persistFavoriteState() {
  localStorage.setItem(storageKey, JSON.stringify(likedMemoryIds.value));
}

async function handleFavorite() {
  if (!currentMemory.value || favoriteSubmitting.value) return;

  favoriteSubmitting.value = true;
  const nextFavoriteState = !isLiked.value;

  try {
    const updated = await toggleMemoryFavorite(currentMemory.value.id, nextFavoriteState);
    memories.value = memories.value.map((item) => (item.id === updated.id ? updated : item));
    likedMemoryIds.value = nextFavoriteState
      ? [...likedMemoryIds.value, updated.id]
      : likedMemoryIds.value.filter((id) => id !== updated.id);
    persistFavoriteState();
  } finally {
    favoriteSubmitting.value = false;
  }
}

function goToMemory(id?: number) {
  if (!id) return;
  router.push(`/memories/${id}`);
}

async function loadPage() {
  loading.value = true;
  try {
    const page = await fetchMemories();
    memories.value = page.records;
  } finally {
    loading.value = false;
  }
}

watch(
  () => route.params.id,
  async () => {
    await loadPage();
  }
);

onMounted(async () => {
  hydrateFavoriteState();
  await loadPage();
});
</script>

<template>
  <section v-if="currentMemory" class="memory-story-page">
    <div class="memory-story-layout">
      <section class="memory-story-visual">
        <article class="memory-photo-card">
          <span class="tape tape-left"></span>
          <img
            :src="currentMemory.coverImageUrl || '/images/xiaoke.png'"
            :alt="currentMemory.title"
            class="memory-photo-main"
          />
          <div class="memory-photo-meta">
            <span><CalendarDays :size="15" /> {{ currentMemory.memoryDate }} {{ currentMeta.shotTime }}</span>
            <span><MapPin :size="15" /> {{ currentMeta.location }}</span>
            <span><SunMedium :size="15" /> {{ currentMeta.atmosphere }}</span>
          </div>
          <div class="memory-photo-caption">
            <p>{{ currentMemory.summary }}</p>
            <button type="button" class="memory-favorite-icon" @click="handleFavorite">
              <Heart :size="20" :fill="isLiked ? 'currentColor' : 'none'" />
            </button>
          </div>
        </article>

        <article class="memory-companion-card">
          <h2>同一天的其他照片</h2>
          <div class="memory-companion-strip">
            <div
              v-for="image in currentMeta.companionImages"
              :key="image.imageUrl"
              class="memory-companion-thumb"
              :title="image.title"
            >
              <img :src="image.imageUrl" :alt="image.title" />
            </div>
            <button
              v-if="nextMemory"
              type="button"
              class="memory-strip-next"
              aria-label="下一篇"
              @click="goToMemory(nextMemory.id)"
            >
              <ArrowRight :size="18" />
            </button>
          </div>
        </article>
      </section>

      <section class="memory-story-paper">
        <div class="memory-paper-topline">
          <span><CalendarDays :size="15" /> {{ currentMemory.memoryDate }}</span>
          <span><MoonStar :size="15" /> {{ currentMeta.moment }}</span>
        </div>

        <div class="memory-paper-heading">
          <h1>{{ currentMemory.title }}</h1>
          <Paperclip :size="34" />
        </div>

        <div class="memory-paper-tags">
          <span v-for="tag in displayTags" :key="tag">{{ tag }}</span>
        </div>

        <div class="memory-action-row">
          <button type="button" class="memory-inline-action" :disabled="favoriteSubmitting" @click="handleFavorite">
            <Heart :size="16" :fill="isLiked ? 'currentColor' : 'none'" />
            <span>{{ isLiked ? '已收藏这篇' : '收藏这篇' }}</span>
            <strong>{{ currentMemory.favoriteCount ?? 0 }}</strong>
          </button>
        </div>

        <div class="memory-paper-body">
          <p v-for="paragraph in paragraphs" :key="paragraph">{{ paragraph }}</p>
        </div>

        <div class="memory-paper-bottom">
          <article class="memory-note-story-card">
            <div class="memory-note-head">
              <span>今日小记</span>
              <Heart :size="16" />
            </div>
            <p>{{ currentMeta.note }}</p>
          </article>

          <article class="memory-story-info-card">
            <h2><ScrollText :size="16" /> 照片信息</h2>
            <dl>
              <template v-for="row in detailRows" :key="row[0]">
                <dt>{{ row[0] }}</dt>
                <dd>{{ row[1] }}</dd>
              </template>
            </dl>
          </article>
        </div>
      </section>
    </div>

    <div class="memory-story-nav">
      <button type="button" class="memory-nav-button" :disabled="!previousMemory" @click="goToMemory(previousMemory?.id)">
        <div class="memory-nav-title">
          <ArrowLeft :size="18" />
          <span>上一篇</span>
        </div>
        <small>{{ previousMemory?.title || '已经是最前一篇' }}</small>
      </button>

      <RouterLink to="/memories" class="memory-nav-button center">
        <div class="memory-nav-title">
          <BookOpenText :size="18" />
          <span>回到手账</span>
        </div>
      </RouterLink>

      <button type="button" class="memory-nav-button" :disabled="!nextMemory" @click="goToMemory(nextMemory?.id)">
        <div class="memory-nav-title">
          <span>下一篇</span>
          <ArrowRight :size="18" />
        </div>
        <small>{{ nextMemory?.title || '已经是最后一篇' }}</small>
      </button>
    </div>

    <section class="memory-related-panel">
      <div class="memory-related-head">
        <h2><Sparkles :size="16" /> 同一段日子里的小片段</h2>
      </div>
      <div class="memory-related-grid">
        <RouterLink
          v-for="item in relatedMemories"
          :key="item.id"
          :to="`/memories/${item.id}`"
          class="memory-related-card"
          :class="{ active: item.id === currentMemory.id }"
        >
          <img :src="item.coverImageUrl || '/images/xiaoke.png'" :alt="item.title" />
          <div class="memory-related-copy">
            <strong>{{ item.title }}</strong>
            <span>{{ item.memoryDate }}</span>
          </div>
        </RouterLink>
        <button
          v-if="nextMemory"
          type="button"
          class="memory-related-next"
          aria-label="查看更多"
          @click="goToMemory(nextMemory.id)"
        >
          <ArrowRight :size="18" />
        </button>
      </div>
    </section>
  </section>

  <section v-else-if="loading" class="loading-panel">正在把这页手账慢慢翻开...</section>
</template>
