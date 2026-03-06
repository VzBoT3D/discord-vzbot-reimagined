<script setup lang="ts">
  const isDark = computed(() => useColorMode().value == "dark");
</script>

<template>
  <div class="z-0">
    <div class="z-[-3] absolute flex-1 w-full h-full ">
      <ParticlesBg
        class="fixed opacity-70 h-full w-full"
        :quantity="250"
        :ease="100"
        :color="isDark ? '#FFF' : '#000'"
        :staticity="10"
        refresh
      />
    </div>

    <div class="min-h-screen bg-transparent w-full flex flex-col z-20 justify-between">
      <AppHeader class="z-30"/>
      <slot/>
      <AppFooter class="z-30"/>
    </div>
  </div>
</template>

<style scoped>

</style>