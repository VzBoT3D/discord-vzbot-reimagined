<script setup lang="ts">

import type {IPost} from "~/lib/types/Blog";
import BlogPostPreview from "~/components/BlogPostPreview.vue";
import Loading from "~/components/Loading.vue";

definePageMeta({
  layout: "landing-page-layout"
})

const {data, status} = useFetch<{posts: IPost[]}>("/api/blog/all")

</script>

<template>
  <div v-motion-fade v-if="status === 'pending'">
    <Loading/>
  </div>

  <div class="text-center" v-else-if="!data?.posts">
    <p class="text-3xl">Oh no, an error occurred :(</p>
  </div>

  <div v-else class="flex flex-col gap-4 items-center p-8 lg:p-20">
    <p class="text-4xl font-thin text-center">VzBoT's blog posts</p>
    <div class="flex flex-wrap gap-4 justify-center content-center">
      <BlogPostPreview v-for="post in data.posts" :post="post"/>
    </div>
  </div>
</template>