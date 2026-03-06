<script setup lang="ts">
import { Icon } from '@iconify/vue'
import CardContainer from "~/components/inspira/CardContainer.vue";
import CardBody from "~/components/inspira/CardBody.vue";
import CardItem from "~/components/inspira/CardItem.vue";
import BorderBeam from "~/components/inspira/BorderBeam.vue";
import type {IPost} from "~/lib/types/Blog";
import {calculateReadingTime, formatToMinutes} from "../../lib/utils";

const {data} = await useFetch<{blog: IPost}>("/api/blog/latest")
const blog = data.value?.blog!

const initial = ref({
  y: 100,
  opacity: 0,
})

const enter = ref({
  y: 0,
  opacity: 1,
})
</script>

<template>
  <div class="flex flex-col justify-center items-center h-full gap-16">
    <div class="flex flex-col">
          <span v-motion
                :initial="initial"
                :enter="enter"
                :duration="1200"
                class="pointer-events-none font-sans font-bold italic whitespace-pre-wrap bg-gradient-to-b from-black to-red-300/80 bg-clip-text text-center text-8xl leading-none text-transparent dark:from-white/80 dark:to-slate-900/10">
        VzBoT
      </span>

      <h1
        v-motion
        :initial="initial"
        :enter="enter"
        :delay="800"
        :duration="1200"
        class="text-center text-3xl font-bold leading-tight tracking-tighter lg:leading-[1.1] md:text-6xl"
      >
        Print it, but
        <TextHighlight
          :delay="1700"
          :duration="200"
          class="rounded-xl bg-gradient-to-r from-pink-500 to-violet-500 px-4 py-1"
          text-end-color="hsl(var(--accent))"
        >
          FAST
        </TextHighlight>
      </h1>
    </div>

    <div v-motion :initial="initial" :enter="enter" :delay="1500" :duration="500" class="flex size-fit flex-col items-center justify-center rounded-3xl">
      <CardContainer>
        <CardBody
          class="group/card h-fit w-full gap-2 rounded-xl border border-black/[0.1] bg-gray-50 px-4 py-6 dark:border-white/[0.2] dark:bg-black dark:hover:shadow-2xl dark:hover:shadow-emerald-500/[0.1]"
        >
          <CardItem
            as="p"
            translate-z="25"
            class="mt-2 max-w-sm text-sm text-neutral-500 dark:text-neutral-300"
          >
            <div class="flex flex-col gap-4 justify-between">
              <div class="flex gap-2">
                <p>
                  Check out our latest blog post by
                </p>
                <p class="underline">
                  {{ blog.author.name }}
                </p>
              </div>

              <p class="text-xl text-foreground">{{blog.title}}</p>
              <p>{{formatToMinutes(calculateReadingTime(blog.content))}} read</p>
              <Button @click="navigateTo('/posts/' + blog.id)" class="w-fit">Click to read more...</Button>
            </div>
          </CardItem>

          <CardItem class="mt-4 flex w-full flex-col gap-4">
          </CardItem>
        </CardBody>
      </CardContainer>
      <BorderBeam
        :size="200"
        :duration="4"
        :delay="9"
        :border-width="4"
      />
    </div>

    <div v-motion :initial="initial" :enter="enter" :delay="1500" :duration="2000" class="relative top-28 flex flex-col gap-2 items-center">
      <Icon icon="formkit:arrowdown" class="w-12 h-12"/>
      <p class="font-thin">Scroll down to see more</p>
    </div>
  </div>
</template>

<style scoped>

</style>