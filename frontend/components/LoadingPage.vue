<script setup lang="ts">
  import {Icon} from "@iconify/vue";

  const isDark = computed(() => useColorMode().value == "dark");

  const openGithub = () => {
    window.open("https://github.com/VzBoT3D", "_blank")
  }

  const openDiscord = () => {
    window.open("https://discord.gg/vzbot-829828765512106054", "_blank")
  }
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

    <div class="min-h-screen bg-transparent w-full flex flex-col gap-8 z-20 justify-center items-center">
      <p class="text-3xl font-thin">The VzBoT Webpage is currently in maintenance. Please visit us later again</p>
      <div class="flex gap-2 drop-shadow-sm">
        <Button @click="openDiscord">
          <Icon icon="ic:baseline-discord" class="h-[1.2rem] w-[1.2rem]"/>
          Join our Discord
        </Button>
        <Button @click="openGithub">
          <Icon icon="iconoir:github" class="h-[1.2rem] w-[1.2rem]"/>
          Visit our Github
        </Button>
      </div>
      <VZLogo/>
    </div>
  </div>
</template>

<style scoped>

</style>