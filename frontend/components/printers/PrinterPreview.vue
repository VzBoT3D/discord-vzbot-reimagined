<script setup lang="ts">

interface PrinterProps {
  name: string
}

defineProps<PrinterProps>()

</script>

<template>
  <div class="flex-1 grid grid-cols2 grid-flow-col">
    <div class="w-1/2 p-8">
      <Swiper
        :modules="[SwiperAutoplay, SwiperNavigation, SwiperPagination]"
        :slides-per-view="1"
        :loop="true"
        :autoplay="{
          delay: 8000,
          disableOnInteraction: true,
        }"
        :pagination="{ clickable: true }"
        navigation
        class="bg-accent rounded-md"
      >
        <SwiperSlide>
          <NuxtImg src="/vz235_preview.jpg"></NuxtImg>
        </SwiperSlide>
        <SwiperSlide>
          <NuxtImg src="/vz330_preview.jpg"></NuxtImg>
        </SwiperSlide>
      </Swiper>
    </div>
    <div>
      <p>w</p>
    </div>
  </div>
</template>

<style scoped>

</style>