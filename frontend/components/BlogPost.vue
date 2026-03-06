<script setup lang="ts">

import type {IPost} from "~/lib/types/Blog";
import {toReadableLocaleDate} from "~/lib/utils";
import VueMarkdown from 'vue-markdown-render'
import TracingBeam from "~/components/inspira/TracingBeam.vue";

defineProps<{
  blog: IPost
}>()

</script>

<template>
  <div class="flex flex-1 flex-col items-center gap-8 p-8">
    <p class="text-3xl lg:text-6xl font-thin underline">{{blog.title}}</p>
    <Card>
      <CardContent class="p-4">
        <div class="flex flex-col gap-2">
          <div class="flex items-center gap-2">
            <NuxtImg :src="blog.author.profilePicture" class="w-8 h-8 rounded-full drop-shadow-lg"/>
            <p>Written by {{ blog.author.name }}</p>
          </div>
          <div>
            <p>Written {{toReadableLocaleDate(blog.createdAt)}}</p>
          </div>
          <Button @click="navigateTo('/authors/' + blog.author.id)" class="w-fit" size="sm" variant="secondary">View more from this author</Button>
        </div>
      </CardContent>
    </Card>

    <Card class="prose prose-xl text-sm lg:text-lg dark:prose-invert">
      <CardContent class="p-10">
        <TracingBeam class="hidden lg:flex">
            <VueMarkdown :source="blog.content"></VueMarkdown>
        </TracingBeam>

        <VueMarkdown class="lg:hidden" :source="blog.content"></VueMarkdown>
      </CardContent>
    </Card>

    <Button @click="navigateTo('/posts')" variant="secondary" class="drop-shadow-md m-4">Read more posts</Button>
  </div>
</template>

<style scoped>

</style>