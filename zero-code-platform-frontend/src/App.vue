<script setup lang="ts">
import BasicLayout from './layouts/BasicLayout.vue';
import { useLoginUserStore } from './stores/LoginUser.ts'
import { ConfigProvider } from 'ant-design-vue';
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';

dayjs.locale('zh-cn');

const loginUserStore = useLoginUserStore();
loginUserStore.fetchLoginUser();
</script>

<template>
  <ConfigProvider :locale="zhCN">
    <BasicLayout />
  </ConfigProvider>
</template>

<style scoped>
/* 移除默认样式，使用 Ant Design Vue 的样式 */
</style>
