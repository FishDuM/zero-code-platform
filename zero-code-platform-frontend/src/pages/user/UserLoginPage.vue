<template>
  <div id="userLoginPage">
    <h2 class="title">零代码生成平台用户登录</h2>
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

      <div class="tips">
        没有账号?
        <RouterLink to="/user/register">去注册</RouterLink>
      </div>

      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%;">登陆</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { reactive } from 'vue';

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
});
const handleSubmit = (values: any) => {
  console.log('Success:', values);
};

</script>

<style scoped>
#userLoginPage {
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
