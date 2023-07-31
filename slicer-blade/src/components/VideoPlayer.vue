
<style scoped>

</style>

<template>
<div>
  <video ref="videoPlayer" class="video-js"></video>
</div>
</template>

<script setup>
import videojs from 'video.js'
import { ref, defineProps, onMounted, onBeforeUnmount, defineExpose } from 'vue'

const props = defineProps({
  options: { type: Object, default: () => { return {} } },
})
const videoPlayer = ref()
const player = ref(null)

onMounted(() => {
  player.value = videojs(videoPlayer.value, props.options, (event) => {
    player.value.log('onPlayerReady', event)
  })
})
onBeforeUnmount(() => {
  player.value?.dispose()
})
defineExpose({
  videoPlayer
})

</script>
