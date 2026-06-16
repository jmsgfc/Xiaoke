<script setup lang="ts">
import AdminNav from '../../components/AdminNav.vue';
</script>

<template>
  <div class="admin-layout">
    <AdminNav />
    <section class="admin-content">
      <h1>站点设置</h1>
      <div class="admin-panel">
        <h2>本地运行配置</h2>
        <p>前端默认地址：<code>http://localhost:5173</code></p>
        <p>后端默认地址：<code>http://localhost:8080</code></p>
        <p>图片上传目录：<code>xiaoke-daily/uploads</code></p>
        <p>占位图：<code>frontend/public/images/xiaoke.png</code></p>
      </div>
      <div class="admin-panel">
        <h2>管理员账户</h2>
        <p>
          当前第一版只保留一个管理员账号，登录凭证由后端环境变量提供。部署前请先配置
          <code>ADMIN_INITIAL_USERNAME</code> 和 <code>ADMIN_INITIAL_PASSWORD</code>。
        </p>
      </div>
    </section>
  </div>
</template>
