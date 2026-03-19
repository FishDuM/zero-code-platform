<template>
  <a-layout-header class="header">
    <div class="logo">
      <img src="/favicon.ico" alt="Logo" class="logo-image" />
      <span class="logo-title">零代码平台</span>
    </div>
    <a-menu
      mode="horizontal"
      :selected-keys="[currentRoute]"
      :items="menuItems"
      class="menu"
      @click="handleMenuClick"
    />
    <div class="user">
      <a-button type="primary">登录</a-button>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const currentRoute = computed(() => route.path)

const menuItems = ref([{ key: '/', label: '首页' }])

const handleMenuClick = ({ key }: { key: string }) => {
  router.push(key)
}
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
  height: 64px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-image {
  width: 32px;
  height: 32px;
}

.logo-title {
  font-size: 18px;
  font-weight: 600;
  color: #1890ff;
}

.menu {
  flex: 1;
  margin-left: 48px;
}

.user {
  display: flex;
  align-items: center;
}

@media (max-width: 768px) {
  .header {
    padding: 0 16px;
  }

  .logo-title {
    font-size: 16px;
  }

  .menu {
    margin-left: 24px;
  }
}
</style>
