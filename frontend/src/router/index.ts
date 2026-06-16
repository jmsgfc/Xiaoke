import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../pages/HomePage.vue';
import AlbumPage from '../pages/AlbumPage.vue';
import PhotoDetailPage from '../pages/PhotoDetailPage.vue';
import MemoriesPage from '../pages/MemoriesPage.vue';
import MemoryDetailPage from '../pages/MemoryDetailPage.vue';
import AboutPage from '../pages/AboutPage.vue';
import AdminLoginPage from '../pages/admin/AdminLoginPage.vue';
import AdminDashboardPage from '../pages/admin/AdminDashboardPage.vue';
import AdminPhotoPage from '../pages/admin/AdminPhotoPage.vue';
import AdminMemoryPage from '../pages/admin/AdminMemoryPage.vue';
import AdminProfilePage from '../pages/admin/AdminProfilePage.vue';
import AdminSettingsPage from '../pages/admin/AdminSettingsPage.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/profile' },
    { path: '/profile', component: HomePage },
    { path: '/album', component: AlbumPage },
    { path: '/album/:id', component: PhotoDetailPage },
    { path: '/memories', component: MemoriesPage },
    { path: '/memories/:id', component: MemoryDetailPage },
    { path: '/about', component: AboutPage },
    { path: '/admin/login', component: AdminLoginPage },
    { path: '/admin', component: AdminDashboardPage, meta: { requiresAuth: true } },
    { path: '/admin/photos', component: AdminPhotoPage, meta: { requiresAuth: true } },
    { path: '/admin/memories', component: AdminMemoryPage, meta: { requiresAuth: true } },
    { path: '/admin/profile', component: AdminProfilePage, meta: { requiresAuth: true } },
    { path: '/admin/settings', component: AdminSettingsPage, meta: { requiresAuth: true } }
  ],
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    }

    const toRecord = to.matched[to.matched.length - 1];
    const fromRecord = from.matched[from.matched.length - 1];
    const sameView = toRecord?.components?.default === fromRecord?.components?.default;

    if (sameView && to.params.id && from.params.id && to.params.id !== from.params.id) {
      return false;
    }

    return { top: 0 };
  }
});

router.beforeEach((to) => {
  if (to.meta.requiresAuth && !localStorage.getItem('xiaoke_admin_token')) {
    return `/admin/login?redirect=${encodeURIComponent(to.fullPath)}`;
  }
  return true;
});

export default router;
