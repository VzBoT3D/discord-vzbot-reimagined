<script setup lang="ts">
import {AreaChart} from "~/components/ui/chart-area";
import { CurveType } from '@unovis/ts'

type LineData = {
  date: Date;
  "Registered VzBoTs": number;
};

defineProps<{data: LineData[]}>()

</script>

<template>
  <div class="flex flex-col bg-secondary rounded-lg h-64 w-96 overflow-hidden drop-shadow-lg">
    <div class="p-4">
      <p class="text-center text-2xl font-thin">
        {{ data[data.length - 1]["Registered VzBoTs"] }} Registered VzBoTs
      </p>
    </div>
    <AreaChart :data="data"
               class="h-64 w-full overflow-hidden"
               index="date"
               :margin="{left: 0, right: 0}"
               :categories="['Registered VzBoTs']"
               :show-x-axis="false"
               :show-y-axis="false"
               :show-grid-line="false"
               :show-legend="false"
               :show-tooltip="false"
               :curve-type="CurveType.MonotoneY"
    />
  </div>
</template>