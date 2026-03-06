<script lang="ts" setup>
import Meteors from "~/components/inspira/Meteors.vue";
import {Icon} from "@iconify/vue";

interface MeteorCardProps {
  title: string
  description: string
}

defineProps<MeteorCardProps>()

</script>
<template>
  <ClientOnly>
    <div class="flex flex-col h-full items-center justify-center z-20">
      <div class="relative w-full h-full max-w-xs">
        <div class="absolute inset-0 size-full scale-[0.80] rounded-full bg-gradient-to-r from-blue-500 to-teal-500 blur-2xl dark:blur-3xl"/>
        <div class="relative flex h-full flex-col items-start justify-start overflow-hidden rounded-2xl border border-gray-100 dark:border-gray-800 bg-gray-100 dark:bg-gray-900 px-4 py-8 shadow-xl">
          <div class="flex items-center justify-center">
            <Icon icon="material-symbols-light:info-outline" class="w-8 h-8 text-gray-500"/>
          </div>
          <p class="text-xl font-bold text-black dark:text-white z-50 px-1">
            {{title}}
          </p>

          <p class="relative z-50 mb-4 px-1 text-base font-normal text-slate-500">
            {{description}}
          </p>

          <Meteors />
        </div>
      </div>
    </div>
  </ClientOnly>
</template>