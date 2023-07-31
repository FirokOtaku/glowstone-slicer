
<!--suppress HtmlUnknownTag -->
<style scoped>
#header
{
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;

  z-index: 100000;
}
#body
{
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: calc(100vh - 64px);

  z-index: 5000;
}
</style>

<template>
  <header class="responsive primary" id="header">
    <nav>
      <img src="/slicer.png" alt="icon" class="tiny circle">
      <h5 class="max">萤石切刀</h5>

      <button class="circle transparent" @click="switchPage('page-connect')">
        <i>cable</i>
        <div class="tooltip">连接性</div>
      </button>

      <button class="circle transparent" @click="switchPage('page-slice')">
        <i>carpenter</i>
        <div class="tooltip">狠狠地切割!</div>
      </button>

      <div></div>
    </nav>
  </header>

  <div id="body" class="scroll fill">

    <page-connect v-if="currentPage === 'page-connect'" @connect="onConnected" />
    <page-slice v-if="currentPage === 'page-slice'" :access-token="accessToken"/>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import PageConnect from './components/PageConnect.vue'
import PageSlice from './components/PageSlice.vue'
import { setToken } from './components/network'

const currentPage = ref('page-connect') // page-connect, page-slice
function switchPage(page)
{
  if(page === currentPage.value) return
  currentPage.value = page
}

const accessToken = ref(null)
const expireTime = ref(-1)
function onConnected(event)
{
  accessToken.value = event.accessToken
  expireTime.value = event.expireTime
  setToken(event.accessToken)
  currentPage.value = 'page-slice'
}

</script>
