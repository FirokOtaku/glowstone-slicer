import { createApp } from 'vue'
import App from './App.vue'
import 'beercss'
import 'material-dynamic-colors'
import './style.css'

ui('mode', 'light')
ui('theme', '#ffbc7d')

createApp(App).mount('#app')
