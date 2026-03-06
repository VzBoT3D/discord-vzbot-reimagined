<script setup lang="ts">

import Globe from "~/components/inspira/Globe.vue";
import type {ISerial} from "~/lib/types/Serial";
import type {Marker} from "cobe";
import {DonutChart} from "~/components/ui/chart-donut";
import {AreaChart} from "~/components/ui/chart-area";
import SerialsWidget from "~/components/widgets/SerialsWidget.vue";
import PrinterDistributionWidget from "~/components/widgets/PrinterDistributionWidget.vue";
import Loading from "~/components/Loading.vue";

definePageMeta({
  layout: 'landing-page-layout'
})

const { data, status } = await useFetch<{serialData: ISerial[], printerData: { [key: string]: number} }>("/api/community")

const serials = data.value?.serialData!
const printerStats = data.value?.printerData!

const markers = serials.filter((s) => s.attributes?.latitude && s.attributes?.longitude).map((s) => ({
  location: [s.attributes?.latitude!, s.attributes?.longitude!],
  size: 0.05
})) as Marker[];

type LineData = {
  date: Date;
  "Registered VzBoTs": number;
};

function fillMissingDates(serials: ISerial[]): LineData[] {
  const lineData: LineData[] = serials
    .sort((a, b) => new Date(a.createdAt).valueOf() - new Date(b.createdAt).valueOf())
    .map((serial) => ({
      date: new Date(serial.createdAt),
      "Registered VzBoTs": serial.serialID,
    }));

  const startDate = lineData[0]?.date || new Date();
  const endDate = lineData[lineData.length - 1]?.date || new Date();

  const dateMap: Map<string, number> = new Map(
    lineData.map((data) => [data.date.toDateString(), data["Registered VzBoTs"]])
  );

  const filledLineData: LineData[] = [];

  let currentDate = new Date(startDate);
  while (currentDate <= endDate) {
    const dateStr = currentDate.toDateString();

    if (dateMap.has(dateStr)) {
      filledLineData.push({
        date: new Date(currentDate),
        "Registered VzBoTs": dateMap.get(dateStr)!,
      });
    } else {
      const lastKnownY = filledLineData[filledLineData.length - 1]?.["Registered VzBoTs"] || 0;
      filledLineData.push({
        date: new Date(currentDate),
        "Registered VzBoTs": lastKnownY,
      });
    }

    currentDate.setDate(currentDate.getDate() + 1);
  }

  return filledLineData;
}

const result = fillMissingDates(serials);


const donutData = Object.keys(printerStats).map((k) => ({
  name: k,
  total: printerStats[k],
  predicted: printerStats[k]
}))

</script>

<template>
  <div v-motion-fade v-if="status !== 'success'">
    <Loading/>
  </div>

  <div v-motion-fade v-else class="flex flex-col items-center gap-16 justify-center overflow-hidden rounded-lg md:shadow-xl">
    <div class="flex flex-col -space-y-10 h-screen items-center justify-center">
      <div class="pointer-events-none z-10 whitespace-pre-wrap bg-gradient-to-b from-black to-gray-300/80 bg-clip-text text-center text-8xl font-semibold leading-none text-transparent dark:from-white dark:to-slate-900/10">
        <p>VZ around the world</p>
      </div>
      <Globe :config="{ markers: markers, width: 300, height: 300}" class="z-0"/>
    </div>

    <div class="flex flex-col justify-center items-center gap-8 w-full pb-48">
      <p class="text-center font-thin text-5xl pb-20">VzBoT in numbers</p>

      <div class="flex flex-wrap justify-center gap-8">
        <SerialsWidget :data="result"/>
        <PrinterDistributionWidget :data="donutData"/>
      </div>
    </div>
  </div>
</template>