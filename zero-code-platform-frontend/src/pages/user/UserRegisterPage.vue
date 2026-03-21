<template>
  <div id="userRegisterPage">
    <h2 class="title">零代码生成平台用户注册</h2>
    <div class="desc">不用写代码也能开发应用</div>
    <a-form
      :model="formState"
      name="basic"
      autocomplete="off"
      @finish="handleSubmit"
    >
      <a-form-item
        name="userAccount"
        :rules="[{ required: true, message: '请输入账号' }, { min: 4, message: '账号长度不能小于4'}]"
      >
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
      </a-form-item>

      <a-form-item
        name="userPassword"
        :rules="[{ required: true, message: '请输入密码' }, { min: 8, message: '密码长度不能小于8'}]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>

      <a-form-item
        name="checkPassword"
        :rules="[{ required: true, message: '请确认密码' }, { min: 8, message: '密码长度不能小于8'}]"
      >
        <a-input-password v-model:value="formState.checkPassword" placeholder="请确认密码" />
      </a-form-item>

      <div class="tips">
        已有账号?
        <RouterLink to="/user/login">去登录</RouterLink>
      </div>

      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%;">注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { reactive } from 'vue';
import { register } from '@/api/userController.ts'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
});

const router = useRouter()

/**
 * 提交表单
 * @param values
 */
const handleSubmit = async (values: any) => {
  if (values.userPassword !== values.checkPassword) {
    message.error("两次输入的密码不一致")
    return
  }
  const res = await register(values)
  if (res.data.code === 0 || res.data.data) {
    message.success("注册成功")
    await router.push({
      path: '/user/login',
      replace: true
    })
  }else {
    message.error("注册失败:"+res.data.message)
  }
};

</script>

<style scoped>
#userRegisterPage {
  max-width: 480px;
  margin: 0 auto;
}

.title {
  margin-bottom: 16px;
  text-align: center;
}

.desc {
  margin-bottom: 16px;
  text-align: center;
  color: #bbb;
}

.tips {
  margin-bottom: 16px;
  text-align: right;
  font-size: 13px;
  color: #888;
}
</style>
