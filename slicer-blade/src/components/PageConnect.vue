
<style scoped>

</style>

<template>
<main class="responsive no-full-height">

  <div class="space"></div>

  <div>
    <div class="field label border">
      <input type="text" v-model="linkAppKey">
      <label>萤石云平台 APPKEY</label>
    </div>

    <div class="field label border">
      <input type="password" v-model="linkSecret">
      <label>萤石云平台 SECRET</label>
    </div>

    <div class="right-align">
      <button class="no-round" @click="tryConnect">连接</button>
    </div>
  </div>

</main>
</template>

<script setup>

import { ref, computed, onMounted, onUnmounted, defineProps, defineEmits, } from 'vue'
import { get, post, } from './network'

const linkAppKey = ref('')
const linkSecret = ref('')

const emits = defineEmits([
  'connect', // 如果给定的用户名密码可以正常连接萤石云 就抛出这个事件
])

async function tryConnect()
{
  const appKey = linkAppKey.value
  const secret = linkSecret.value
  // 开始调用萤石云 API 尝试获取 accessToken
  try
  {
    const form = new FormData()
    form.append('appKey', appKey)
    form.append('appSecret', secret)
    const result = await post({
      url: 'https://open.ys7.com/api/lapp/token/get',
      header: { 'Host': 'open.ys7.com', },
      data: form,
    })
    const { accessToken, expireTime, } = result.data
    if('string' !== typeof accessToken && 'number' !== typeof expireTime)
      throw '无法获取鉴权 token, 未知的返回数据类型'
    emits('connect', { accessToken, expireTime, })
  }
  catch (any)
  {
    console.error('连接到萤石云出错', any)
  }
}
</script>
