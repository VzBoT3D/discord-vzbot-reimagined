<script setup lang="ts">

import {DonutChart} from "~/components/ui/chart-donut";

type DonutData = {
  name: string
  total: number
  predicted: number
}

defineProps<{data: DonutData[]}>()


</script>

<template>
  <div class="flex flex-col bg-secondary rounded-lg drop-shadow-lg h-64 w-96">
    <div class="p-4">
      <p class="text-center text-2xl font-thin">
        VzBoT distribution
      </p>
    </div>
    <DonutChart
      class="p-4"
      index="name"
      category="total"
      :data="data"
    />
  </div>
</template>

<style scoped>

</style>