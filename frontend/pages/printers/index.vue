<script setup lang="ts">
import PrinterCard from "~/components/printers/PrinterCard.vue";
import type {IPrinterProfile} from "~/lib/types/PrinterProfile";
import Loading from "~/components/Loading.vue";

definePageMeta({
  layout: "landing-page-layout"
})

const { data, status } = useFetch<{printerData: IPrinterProfile[]}>("/api/printer/profiles")


</script>

<template>
  <div v-motion-fade v-if="status === 'pending'">
    <Loading/>
  </div>

  <div class="flex justify-center place-items-center" v-else-if="!(data && data.printerData)">
    <p class="text-2xl font-thin">There was an error fetching the page</p>
  </div>

  <div v-else class="flex flex-1 flex-col gap-8 place-items-center justify-center">
    <p class="text-3xl font-thin">Our printers</p>
    <div class="flex gap-8">
      <PrinterCard v-for="profile in data.printerData" :name="profile.printer.name" :src="profile.medias[0].location" description="" :docs_link="profile.learnMoreURL"/>
    </div>
  </div>
</template>

<style scoped>

</style>