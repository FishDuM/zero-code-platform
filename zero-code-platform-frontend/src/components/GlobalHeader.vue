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
      <div v-if="userLoginStore.loginUser.id">
        <a-dropdown>
          <a-space>
            <a-avatar :src="userLoginStore.loginUser.userAvatar" />
            {{userLoginStore.loginUser.userName}}
          </a-space>
          <template #overlay>
            <a-menu>
              <a-menu-item @click="doLogout">
                <LogoutOutlined />
                退出登陆
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
      <div v-else>
        <a-button type="primary" href="/user/login">登录</a-button>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useLoginUserStore } from '@/stores/LoginUser.ts'
import { LogoutOutlined } from '@ant-design/icons-vue'
import { userLogout } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import { h } from 'vue'
import { HomeOutlined, BarChartOutlined} from '@ant-design/icons-vue';
// 获取登陆用户状态
const userLoginStore = useLoginUserStore()

const router = useRouter()
const route = useRoute()

const currentRoute = computed(() => route.path)

// 菜单配置项
const originItems = [
  {
    key: '/',
    icon: h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/admin/userManage',
    icon: h(BarChartOutlined),
    label: '用户管理',
    title: '用户管理',
  },
]

// 过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    const menuKey = menu?.key as string
    if (menuKey?.startsWith('/admin')) {
      const loginUser = userLoginStore.loginUser
      if (!loginUser || loginUser.userRole !== 'admin') {
        return false
      }
    }
    return true
  })
}

// 展示在菜单的路由数组
const menuItems = computed<MenuProps['items']>(() => filterMenus(originItems))

const handleMenuClick = ({ key }: { key: string }) => {
  router.push(key)
}

const doLogout = async () => {
  const res = await userLogout()
  if (res.data.code !== 0) {
    message.error("退出失败:"+res.data.message)
    return
  }
  userLoginStore.setLoginUser({userName: '未登录',})
  message.success("退出成功")
  await router.push({
    path: '/user/login'
  })
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
