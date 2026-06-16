<script setup lang="ts">
import { Heart, Music, Palette, PawPrint, Shirt, Sparkles, Star, Sun, UserRound } from 'lucide-vue-next';

const basicInfo = [
  ['昵称', '小可'],
  ['生日', '2003-12-30'],
  ['星座', '摩羯座'],
  ['职业', '艺术系学生 / 插画师'],
  ['现居', '校外温馨小公寓'],
  ['MBTI', 'ISFP']
];

const traits = ['温柔细腻', '善解人意', '认真负责', '有点小敏感', '可爱有趣', '内心柔软'];

const habits = ['喜欢睡前听歌', '习惯早上喝温水', '出门前会整理好东西', '偶尔有点小拖延', '喜欢把房间收拾整洁', '会偷偷画宝宝的侧脸'];

const favorites = [
  { icon: Music, label: '音乐', value: '安静 / 治愈系' },
  { icon: Shirt, label: '食物', value: '甜食 / 水果' },
  { icon: Palette, label: '颜色', value: '粉色 / 白色' },
  { icon: PawPrint, label: '动物', value: '小猫' },
  { icon: Star, label: '活动', value: '看电影 / 散步' }
];

const profileImage = {
  src: '/uploads/photos/070_小可_正面表情照.png',
  alt: '小可档案主图'
};
</script>

<template>
  <section class="xiaoke-profile-page">
    <header class="xiaoke-profile-title">
      <h1>小可档案</h1>
      <span aria-hidden="true"></span>
    </header>

    <section class="xiaoke-profile-hero">
      <div class="profile-polaroid">
        <span class="profile-tape" aria-hidden="true"></span>
        <img :src="profileImage.src" :alt="profileImage.alt" />
        <Heart class="profile-heart-doodle" :size="56" />
      </div>

      <div class="profile-name-card">
        <h2>小可</h2>
        <div>
          <span>初遇</span>
          <Heart :size="14" fill="currentColor" />
          <strong>2024.10.17</strong>
        </div>
      </div>

      <blockquote class="profile-note-paper">
        <span>“</span>
        <p>和你在一起的每个平凡瞬间，都很珍贵。</p>
        <Heart :size="34" />
      </blockquote>
    </section>

    <section class="profile-dossier-grid">
      <article class="profile-dossier-card">
        <h2><UserRound :size="24" /> 基本资料</h2>
        <dl>
          <template v-for="item in basicInfo" :key="item[0]">
            <dt>{{ item[0] }}</dt>
            <dd>{{ item[1] }}</dd>
          </template>
        </dl>
      </article>

      <article class="profile-dossier-card">
        <h2><Heart :size="24" /> 性格印象</h2>
        <div class="trait-pills">
          <span v-for="trait in traits" :key="trait">{{ trait }}</span>
        </div>
        <p>在我眼里，你是独一无二的。</p>
      </article>

      <article class="profile-dossier-card">
        <h2><Sun :size="24" /> 日常习惯</h2>
        <ul>
          <li v-for="habit in habits" :key="habit">{{ habit }}</li>
        </ul>
      </article>

      <article class="profile-dossier-card">
        <h2><Sparkles :size="24" /> 喜欢的东西</h2>
        <ul class="favorite-list">
          <li v-for="favorite in favorites" :key="favorite.label">
            <component :is="favorite.icon" :size="19" />
            <span>{{ favorite.label }}</span>
            <strong>{{ favorite.value }}</strong>
          </li>
        </ul>
      </article>
    </section>
  </section>
</template>
