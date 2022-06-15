import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/theme-chalk/index.css'
import App from './App.vue'
import axios from './axios'
import VueAxios from 'vue-axios'
import router from './router/index'
import VueCookies from 'vue-cookies'
import locale from 'element-plus/lib/locale/lang/zh-cn'
import 'dayjs/locale/zh-cn'

const app = createApp(App)
app.use(VueAxios, axios)
app.use(ElementPlus, { locale })
app.use(router)
app.use(VueCookies)
app.mount('#app')
