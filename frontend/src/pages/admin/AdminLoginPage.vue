<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { LockKeyhole } from 'lucide-vue-next';
import { login } from '../../api/admin';

const route = useRoute();
const router = useRouter();
const username = ref('');
const password = ref('');
const error = ref('');
const submitting = ref(false);

onMounted(() => {
  username.value = '';
  password.value = '';
});

async function submit() {
  error.value = '';
  submitting.value = true;
  try {
    const result = await login(username.value, password.value);
    localStorage.setItem('xiaoke_admin_token', result.token);
    const target = typeof route.query.redirect === 'string' ? route.query.redirect : '/admin';
    await router.push(target);
  } catch {
    error.value = '登录失败，请检查账号和密码。';
  } finally {
    submitting.value = false;
  }
}
</script>

<template>
  <section class="admin-login">
    <form class="login-card" autocomplete="off" @submit.prevent="submit">
      <p class="eyebrow"><LockKeyhole :size="16" /> 管理员登录</p>
      <h1>小可日常后台</h1>
      <label>
        用户名
        <input
          v-model="username"
          name="xiaoke_admin_login_username"
          autocomplete="off"
          autocapitalize="none"
          spellcheck="false"
          placeholder="请输入用户名"
        />
      </label>
      <label>
        密码
        <input
          v-model="password"
          name="xiaoke_admin_login_password"
          type="password"
          autocomplete="new-password"
          spellcheck="false"
          placeholder="请输入密码"
        />
      </label>
      <p v-if="error" class="form-error">{{ error }}</p>
      <button class="primary-button full" :disabled="submitting" type="submit">
        {{ submitting ? '登录中…' : '进入后台' }}
      </button>
      <RouterLink to="/" class="text-link">返回前台</RouterLink>
    </form>
  </section>
</template>
