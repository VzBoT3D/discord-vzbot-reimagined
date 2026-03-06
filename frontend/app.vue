<template>
  <div v-if="!backendReachable">
    <LoadingPage/>
  </div>
  <div v-else>
    <NuxtLoadingIndicator />
    <NuxtLayout>
      <NuxtPage/>
    </NuxtLayout>
  </div>
</template>

<script setup lang="ts">

import {useIntervalFn} from "@vueuse/core";
import LoadingPage from "~/components/LoadingPage.vue";

const { data, refresh } = await useFetch<boolean>('/api/status')

const backendReachable = computed(() => {
  return data.value
})

useIntervalFn(() => {
  refresh()
}, 5000)

useHead({
  title: "VzBoT | Official Webpage"
})
</script>