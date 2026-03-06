<script setup lang="ts">

import type {IAuthor} from "~/lib/types/Blog";
import BlogPostPreview from "~/components/BlogPostPreview.vue";
import type {ITeam} from "~/lib/types/Team";
import AuthorPreview from "~/components/AuthorPreview.vue";
import Loading from "~/components/Loading.vue";

definePageMeta({
  layout: "landing-page-layout"
})

const {data, status} = useFetch<{teamData: ITeam}>("/api/team")

const openDiscord = () => {
  window.open("https://discord.gg/vzbot-829828765512106054", "_blank")
}

</script>

<template>
  <div v-motion-fade v-if="status === 'pending'">
    <Loading/>
  </div>

  <div class="text-center" v-else-if="!data?.teamData">
    <p class="text-3xl">Oh no, an error occurred :(</p>
  </div>

  <div v-else class="flex flex-col gap-4 items-center p-20">
    <p class="text-4xl font-thin text-center">VzBoT's team members</p>
    <div class="flex flex-col flex-wrap gap-12 justify-center content-center">
      <div class="flex flex-col gap-4 justify-start">
        <div class="flex flex-wrap gap-4 justify-center">
          <AuthorPreview v-for="author in data.teamData.team" :author="author"/>
        </div>
      </div>
      <div class="flex flex-col gap-4">
        <p class="text-3xl text-center">Moderators</p>
        <div class="flex flex-wrap gap-4 justify-center">
          <AuthorPreview v-for="author in data.teamData.moderators" :author="author"/>
        </div>
      </div>
      <div class="flex flex-col gap-4">
        <p class="text-3xl text-center">Contributors</p>
        <div class="flex flex-wrap gap-4 justify-center">
          <AuthorPreview v-for="author in data.teamData.contributor" :author="author"/>
        </div>
      </div>
    </div>

    <div class="flex flex-col justify-center gap-2 pt-20">
      <p class="text-center text-2xl">Want to join us?</p>
      <Button size="lg" @click="openDiscord">Join our discord</Button>
    </div>
  </div>
</template>