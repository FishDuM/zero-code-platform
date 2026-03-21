<script setup lang="ts">
import BasicLayout from './layouts/BasicLayout.vue';
import { useLoginUserStore } from './stores/LoginUser.ts'

const loginUserStore = useLoginUserStore();
loginUserStore.fetchLoginUser();
</script>

<template>
  <BasicLayout />
</template>

<style scoped>
/* 移除默认样式，使用 Ant Design Vue 的样式 */
</style>
