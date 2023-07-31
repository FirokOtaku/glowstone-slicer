
<!--suppress ExceptionCaughtLocallyJS, EqualityComparisonWithCoercionJS, JSUnresolvedReference, HtmlUnknownTag -->
<style scoped>
.full-height { height: 100% }
.absolute-child
{
  position: absolute;
  left: 0;
  top: 0;
}
</style>

<template>
<div style="" class="no-scroll full-height">

  <div class="grid padding full-height">
    <div class="s12 m12 l4">
      <article class="full-height">
        <div class="row">
          <h6 class="min">
            接入设备列表
          </h6>
          <div class="max"></div>
          <div class="min">
            <button class="circle small" @click="openEditorDeviceInfo" v-if="!isShowingEditorDeviceInfo" :disabled="isRefreshingDeviceInfo">
              <i>app_registration</i>
            </button>

            <button class="circle small" @click="tryReadEditorDeviceInfo" v-else>
              <i>check</i>
            </button>

            <button class="circle small" @click="refreshDeviceInfo" :disabled="isShowingEditorDeviceInfo || isRefreshingDeviceInfo">
              <i>refresh</i>
            </button>
          </div>
        </div>

        <div>
          <div v-if="isShowingEditorDeviceInfo">
            <div class="space"></div>
            <div class="field textarea label border extra">
              <textarea class="small-height" v-model="userInputEditorDeviceInfo"></textarea>
              <label>编辑设备数据列表</label>
            </div>
          </div>

          <div v-else-if="listDeviceInfo == null">
            <div class="large-space"></div>
            <div class="center-align secondary-text">
              等待获取设备信息
            </div>
          </div>
          <div v-else-if="listDeviceInfo.length > 0" >
            <div class="space"></div>
            <div class="scroll">
              <div style="max-height: calc(100vh - 180px)">
                <div v-for="(deviceInfo, indexDeviceInfo) in listDeviceInfo"
                     :key="deviceInfo.id"
                     :class="currentStreamDevice?.deviceId === deviceInfo.id ? 'light-blue2': ''"
                     class="row">
                  <div class="min" style="max-width: 75%">
                    <div class="bold primary-text">
                      {{ deviceInfo.deviceName }}
                    </div>
                    <div class="teal-text small-text" style="text-overflow: ellipsis; overflow-x: hidden">
                      {{ deviceInfo.id }}
                    </div>
                    <div class="lime-text">
                      {{ deviceInfo.deviceSerial }}
                    </div>
                  </div>
                  <div class="max"></div>
                  <div class="min no-padding no-margin no-space">
                    <button class="circle small green3 no-padding no-margin no-space"
                            v-if="currentStreamDevice?.deviceId === deviceInfo.id">
                      <i class="small_arrow">play_arrow</i>
                    </button>
                    <button class="circle small tertiary no-padding no-margin no-space"
                            v-else-if="deviceInfo.status > 0"
                            @click="clickStartStream(deviceInfo)">
                      <i class="small">videocam</i>
                    </button>
                    <button class="circle small pink9 no-padding no-margin no-space" v-else>
                      <i class="small">wifi_off</i>
                    </button>
                  </div>
                </div>
              </div>
            </div>

          </div>
          <div v-else>
            <div class="large-space"></div>
            <div class="center-align error-text">
              接入设备信息为空 <br>
              尚未接入设备?
            </div>
          </div>
        </div>
      </article>
    </div>
    <div class="s12 m12 l8">
      <article class="full-height" style="overflow-y: hidden">
        <div class="row left-align">
          <h6 class="min">切割面板</h6>
          <div class="min">
            <button class="circle primary small">
              <i class="small">gallery_thumbnail</i>
              <div class="tooltip bottom">
                <div class="small-width medium-height padding scroll">
                  <div class="right-align">
                    <button class="circle green5" @click="clickSendButton">
                      <i>send</i>
                    </button>
                    <button class="circle pink8" @click="listSnapshot.splice(0, listSnapshot.length)">
                      <i>delete</i>
                    </button>
                  </div>
                  <div class="space"></div>
                  <div v-for="(snapshot, index) in listSnapshot" :key="'snapshot-' + index">
                    <img :src="snapshot.snapshot" alt="#" class="responsive grey">
                    <div class="tiny-space"></div>
                  </div>
                </div>
              </div>
            </button>
            <button class="circle border small" @click="resetRange">
              <i class="small">restart_alt</i>
              <div class="tooltip bottom">
                重置裁剪区域
              </div>
            </button>
            <button class="circle border small">
              <i class="small">border_vertical</i>
              <div class="tooltip bottom">
                <div>
                  <div class="center-align">
                    缩放 X
                  </div>
                  <div class="row">
                    <label class="slider large min">
                      <input type="range" v-model="percentX" min="1" max="1000" step="1">
                      <span></span>
                    </label>
                    <div class="field border small min">
                      <input type="number" style="width: 70px" v-model.number="percentX" min="0" max="1000" step="1"/>
                    </div>
                  </div>
                  <div class="center-align">
                    起始 X
                  </div>
                  <div class="row">
                    <label class="slider large min">
                      <input type="range" v-model="percentStartX" min="0" max="1000" step="1">
                      <span></span>
                    </label>
                    <div class="field border small min">
                      <input type="number" style="width: 70px" v-model.number="percentStartX" min="0" max="1000" step="1"/>
                    </div>
                  </div>
                </div>
              </div>
            </button>
            <button class="circle border small">
              <i class="small">border_horizontal</i>
              <div class="tooltip bottom">
                <div>
                  <div class="center-align">
                    缩放 Y
                  </div>
                  <div class="row">
                    <label class="slider large min">
                      <input type="range" v-model="percentY" min="1" max="1000" step="1">
                      <span></span>
                    </label>
                    <div class="field border small min">
                      <input type="number" style="width: 70px" v-model.number="percentY" min="0" max="1000" step="1"/>
                    </div>
                  </div>
                  <div class="center-align">
                    起始 Y
                  </div>
                  <div class="row">
                    <label class="slider large min">
                      <input type="range" v-model="percentStartY" min="0" max="1000" step="1">
                      <span></span>
                    </label>
                    <div class="field border small min">
                      <input type="number" style="width: 70px" v-model.number="percentStartY" min="0" max="1000" step="1"/>
                    </div>
                  </div>
                </div>
              </div>
            </button>


            <button class="circle deep-orange4 small" v-if="currentStreamDevice != null" @click="clickStopStream">
              <i class="small">crop_square</i>
              <div class="tooltip bottom">
                关闭视频流
              </div>
            </button>
            <button class="circle indigo small" v-if="currentStreamDevice != null" @click="createSnapshot">
              <i class="small">screenshot_keyboard</i>
              <div class="tooltip bottom">
                裁剪!
              </div>
            </button>
          </div>
        </div>

        <div style="width: 600px">
          <div class="space"></div>
          <div class="absolute no-round">

            <video-player :style="{ height: PlayerHeight + 'px'}"
                          class="border absolute-child"
                          :options="currentStreamOptions"
                          ref="videoPlayerDom"
                          v-if="currentStreamDevice != null" />
<!--            <video src="#" :width="PlayerWidth" :height="PlayerHeight" ref="videoPlayerDom" v-else></video>-->
            <canvas class="absolute-child" :width="PlayerWidth" :height="PlayerHeight" style="opacity: 0.5%" ref="videoCanvasDom"></canvas>
            <svg class="absolute-child" style="opacity: 20%;" :width="PlayerWidth" :height="PlayerHeight">
              <rect x="0" y="0" :width="PlayerWidth" :height="PlayerHeight" color="black"></rect>
              <rect :x="startX" :y="startY" :width="rangeWidth" :height="rangeHeight" fill="lime"></rect>
            </svg>
          </div>
        </div>

      </article>

    </div>
  </div>

</div>
</template>

<script setup>
import {ref, defineProps, onMounted, onUnmounted, computed, onBeforeUnmount, } from 'vue'
import { get, post, createAuthedForm, } from './network'
import VideoPlayer from './VideoPlayer.vue'
import 'video.js/dist/video-js.css'

const PlayerWidth = 800, PlayerHeight = 600


const isShowingEditorDeviceInfo = ref(false)
const userInputEditorDeviceInfo = ref('')
function openEditorDeviceInfo()
{
  if(isShowingEditorDeviceInfo.value || isRefreshingDeviceInfo.value) return
  isShowingEditorDeviceInfo.value = true
}
function tryReadEditorDeviceInfo()
{
  if(!isShowingEditorDeviceInfo.value) return

  const userInputJsonText = userInputEditorDeviceInfo.value.trim()
  const listDeviceInfoTemp = []
  try
  {
    if(userInputJsonText === '') throw '输入数据为空'
    const jsonList = JSON.parse(userInputJsonText)
    if(jsonList[Symbol.iterator] == null) throw '不是列表'

    for(let jsonObj of jsonList)
    {
      const { id, deviceSerial, deviceName, deviceType, status, defence, deviceVersion, addTime, updateTime, parentCategory, riskLevel, netAddress, } = jsonObj
      if(id == null || deviceSerial == null || deviceName == null || status == null)
      {
        console.info('输入数据实例之一缺少字段, 跳过此实例', jsonObj)
        continue
      }
      listDeviceInfoTemp.push(jsonObj)
    }

    listDeviceInfo.value = listDeviceInfoTemp
    userInputEditorDeviceInfo.value = JSON.stringify(listDeviceInfoTemp) // 同步用户编辑数据
  }
  catch (any)
  {
    console.log('无法读取设备信息列表', any)
    listDeviceInfo.value = null
    userInputEditorDeviceInfo.value = ''
  }
  finally
  {
    isShowingEditorDeviceInfo.value = false
  }
}

const isRefreshingDeviceInfo = ref(false)
const listDeviceInfo = ref(null) // 接入设备列表
async function refreshDeviceInfo() {
  if(isRefreshingDeviceInfo.value || isShowingEditorDeviceInfo.value) return
  isRefreshingDeviceInfo.value = true

  try
  {
    const listDeviceTemp = []

    console.log('开始加载设备列表')

    let loadingPage = 0
    while(true)
    {
      const form = createAuthedForm()
      form.append('pageStart', '' + loadingPage)
      form.append('pageSize', '' + 50)
      const retListDevice = await post({
        url: 'https://open.ys7.com/api/lapp/device/list',
        data: form,
      })
      if(retListDevice.code != 200)
      {
        throw retListDevice.msg
      }
      console.log('ret', retListDevice)
      const listDeviceTempPart = retListDevice.data
      listDeviceTemp.push(...listDeviceTempPart)
      const pageInfo = retListDevice.page
      const maxPage = (pageInfo.total % pageInfo.size > 0 ? 1 : 0) + parseInt('' + (pageInfo.total / pageInfo.size))
      if(pageInfo.page + 1 >= maxPage) break
      loadingPage = pageInfo.page + 1
      console.log('第', pageInfo.page, '页加载完成')
    }
    console.log('设备列表加载完成')
    listDeviceInfo.value = listDeviceTemp
    userInputEditorDeviceInfo.value = JSON.stringify(listDeviceTemp) // 同步用户编辑数据
  }
  catch (any)
  {
    console.error('发生错误', any)
  }
  finally
  {
    isRefreshingDeviceInfo.value = false
  }
}


const isSyncStreamInfo = ref(false)
const currentStreamDevice = ref(null)
// const currentStreamDom = ref(null)
async function clickStartStream(deviceInfo)
{
  try
  {
    if(isSyncStreamInfo.value) return
    isSyncStreamInfo.value = true

    await clickStopStream(false)

    const { deviceSerial, } = deviceInfo

    const form = createAuthedForm()
    form.append('deviceSerial', deviceSerial)
    form.append('protocol', '2')
    form.append('expireTime', '300')
    form.append('type', '1')
    form.append('quality', '1')
    form.append('supportH265', '1')
    const retStartQuery = await post({
      url: 'https://open.ys7.com/api/lapp/v2/live/address/get',
      data: form,
    })
    if(retStartQuery.code != 200)
      throw retStartQuery.msg
    const { id, url } = retStartQuery.data

    currentStreamDevice.value = {
      deviceId: deviceInfo.id,
      deviceSerial: deviceInfo.deviceSerial,
      deviceName: deviceInfo.deviceName,
      streamId: id,
      streamUrl: url,
    }
    console.log('加载播放流信息成功')
  }
  catch (any)
  {
    console.error('加载播放流信息失败', any)
    currentStreamDevice.value = null
  }
  finally
  {
    isSyncStreamInfo.value = false
  }
}
async function clickStopStream(changeSyncStatus = true)
{
  try
  {
    if(currentStreamDevice.value == null) return
    if(changeSyncStatus) isSyncStreamInfo.value = true

    const {
      deviceSerial,
      streamId,
    } = currentStreamDevice.value

    const form = createAuthedForm()
    form.append('deviceSerial', deviceSerial)
    form.append('urlId', streamId)
    const retStopQuery = await post({
      url: 'https://open.ys7.com/api/lapp/v2/live/address/disable',
      data: form,
    })
    if(retStopQuery.code != 200)
      throw retStopQuery.msg

    console.log('停止播放流成功')
    currentStreamDevice.value = null
  }
  catch (any)
  {
    console.error('停止播放流失败', any)
  }
  finally
  {
    if(changeSyncStatus) isSyncStreamInfo.value = false
  }
}
const videoPlayerDom = ref()
const currentStreamOptions = computed(() => {
  const device = currentStreamDevice.value
  if(device == null) return null
  const { streamUrl, } = device
  console.log('streamUrl', streamUrl)
  return {
    autoplay: true,
    controls: true,
    height: PlayerHeight,
    width: PlayerWidth,
    sources: [
      {
        src: streamUrl,
        type: 'application/vnd.apple.mpegurl',
      },
    ]
  }
})

const listSnapshot = ref([])
const isSendingImage = ref(false)
async function clickSendButton()
{
  if(isSendingImage.value) return
  isSendingImage.value = true
  try
  {
    let result = await post({
      url: 'http://localhost:29120/save-image',
      header: { 'Content-Type': 'application/json' },
      data: listSnapshot.value,
    })
    if(!result.success) throw result.msg
  }
  catch (any)
  {
    console.error('保存图片发生错误', any)
  }
  finally
  {
    isSendingImage.value = false
  }
}

const videoCanvasDom = ref()

function createSnapshot()
{
  const domPlayer = videoPlayerDom.value.videoPlayer
  const domCanvas = videoCanvasDom.value
  const ctx = domCanvas.getContext('2d')
  ctx.drawImage(domPlayer, 0, 0, domCanvas.width, domCanvas.height)
  const snapshotImage = domCanvas.toDataURL('image/png')
  ctx.clearRect(0, 0, PlayerWidth, PlayerHeight)

  listSnapshot.value.push({
    snapshot: snapshotImage,
    startX: startX.value,
    startY: startY.value,
    width: rangeWidth.value,
    height: rangeHeight.value,
  })
}
const percentX = ref(1000)
const percentY = ref(1000)
const percentStartX = ref(500)
const percentStartY = ref(500)
const rangeWidth = computed(() => PlayerWidth * percentX.value / 1000)
const rangeHeight = computed(() => PlayerHeight * percentY.value / 1000)
const startX = computed(() => (PlayerWidth - rangeWidth.value) * percentStartX.value / 1000)
const startY = computed(() => (PlayerHeight - rangeHeight.value) * percentStartY.value / 1000)
function resetRange()
{
  percentX.value = 1000
  percentY.value = 1000
  percentStartX.value = 500
  percentStartY.value = 500
}

onBeforeUnmount(async () => {
  await clickStopStream()
})


</script>
