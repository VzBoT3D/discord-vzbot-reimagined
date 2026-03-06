// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2024-04-03',
  devtools: { enabled: false },
  ssr: false,
  runtimeConfig: {
    BACKEND_TOKEN: process.env.BACKEND_TOKEN,
    BACKEND_URL: process.env.BACKEND_URL || "http://localhost:8080",
  },
  modules: [
    '@nuxtjs/tailwindcss',
    'shadcn-nuxt',
    'radix-vue/nuxt',
    '@nuxtjs/color-mode',
    '@nuxt/image',
    '@vueuse/motion/nuxt',
    '@nuxt/fonts',
    'nuxt-swiper'
  ],
  image: {
    dir: "public",
    provider: 'ipx'
  },
  colorMode: {
    preference: 'dark', // default value of $colorMode.preference
    fallback: 'dark', // fallback value if not system preference found
    classSuffix: '',
  },
  shadcn: {
    prefix: '',
    componentDir: './components/ui'
  },
})