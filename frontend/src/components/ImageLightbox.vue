<script setup lang="ts">
import { computed, onBeforeUnmount, watch } from 'vue';

const props = defineProps<{
  open: boolean;
  imageUrl: string;
  title?: string;
  subtitle?: string;
}>();

const emit = defineEmits<{
  close: [];
}>();

const isVisible = computed(() => props.open && !!props.imageUrl);

function closeLightbox() {
  emit('close');
}

function handleKeydown(event: KeyboardEvent) {
  if (event.key === 'Escape' && isVisible.value) {
    closeLightbox();
  }
}

watch(
  () => props.open,
  (open) => {
    document.body.style.overflow = open ? 'hidden' : '';
  }
);

window.addEventListener('keydown', handleKeydown);

onBeforeUnmount(() => {
  document.body.style.overflow = '';
  window.removeEventListener('keydown', handleKeydown);
});
</script>

<template>
  <Teleport to="body">
    <div v-if="isVisible" class="image-lightbox" @click.self="closeLightbox">
      <div class="image-lightbox-shell">
        <img :src="imageUrl" :alt="title || '大图预览'" class="image-lightbox-image" />
      </div>
    </div>
  </Teleport>
</template>
