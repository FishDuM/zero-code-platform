<template>
  <a-layout class="layout">
    <GlobalHeader />
    <a-layout-content class="content">
      <router-view />
    </a-layout-content>
    <GlobalFooter />
  </a-layout>
</template>

<script setup lang="ts">
import GlobalHeader from '../components/GlobalHeader.vue';
import GlobalFooter from '../components/GlobalFooter.vue';
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.content {
  flex: 1;
  padding: 24px;
  margin: 0;
  background-color: #f0f2f5;
  margin-bottom: 64px; /* 为固定的 footer 留出空间 */
}

@media (max-width: 768px) {
  .content {
    padding: 16px;
  }
}
</style>