<template>
  <div v-motion-fade v-if="status === 'idle'">
    <Loading/>
  </div>

  <div class="flex justify-center place-items-center" v-else-if="!(data && data.printerData)">
    <p class="text-2xl font-thin">There was an error fetching the page</p>
  </div>

  <div v-else class="flex flex-1 flex-col justify-center overflow-hidden gap-16">
    <div class="h-[calc(100vh)]">
      <HeroSection/>
    </div>

    <div class="flex flex-col w-full justify-center p-8 lg:p-0 pb-16 z-10 gap-12">
      <div class="flex justify-center text-3xl lg:text-5xl font-normal text-neutral-600 dark:text-neutral-400">
        Meet our
        <FlipWords
          :words="Array.prototype.concat(['Printers'], data.printerData.map((profile) => profile.printer.name))"
          :duration="3000"
          class="text-3xl lg:text-5xl !text-primary"
        />
      </div>
      <div v-motion-fade-visible class="flex gap-8 justify-center flex-wrap">
        <PrinterCard v-for="profile in data.printerData" :name="profile.printer.name" :src="profile.medias[0].location" description="" :docs_link="profile.learnMoreURL"/>
      </div>
    </div>

    <div class="flex flex-col w-full justify-center gap-4">
      <RadiantText
        class="inline-flex z-20 items-center justify-center px-4 transition ease-out hover:text-neutral-600 hover:duration-300 hover:dark:text-neutral-400"
        :duration="5"
      >
        <span class="text-3xl font-bold">Our goals at VzBoT✨</span>
      </RadiantText>

      <div class="flex justify-center">
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8 justify-center w-fit py-20">
          <MeteorCard title="KISS" description="Keep it simple stupid is the main motto of VzBoT. We wan´t to keep our machines and parts simple, and reduce overengineering."/>
          <MeteorCard title="Make it fast" description="We try to push the limits with VzBoT. Chasing speeds but keeping a good print quality is the goal."/>
          <MeteorCard title="Open source" description="All of our findings are published to our GitHub or YouTube channel to keep development ongoing."/>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import HeroSection from "~/components/landing_page/HeroSection.vue";
import FlipWords from "~/components/inspira/FlipWords.vue";
import PrinterCard from "~/components/printers/PrinterCard.vue";
import RadiantText from "~/components/inspira/RadiantText.vue";
import VZLogo from "~/components/VZLogo.vue";
import type {IPrinterProfile} from "~/lib/types/PrinterProfile";
import Loading from "~/components/Loading.vue";

onMounted(() => {
  window.scrollTo(0,0);
})

const { data, status } = useFetch<{printerData: IPrinterProfile[]}>("/api/printer/profiles")

definePageMeta({
  layout: "landing-page-layout"
})
</script>