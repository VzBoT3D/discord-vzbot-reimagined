<script setup lang="ts">

import type {IAuthor} from "~/lib/types/Blog";
import BlogPostPreview from "~/components/BlogPostPreview.vue";

defineProps<{author: IAuthor, showPosts: boolean}>()

</script>

<template>
  <div class="flex flex-col gap-6 items-center">
    <Card class="w-fit">
      <CardHeader>
        <div class="flex gap-2 items-center">
          <NuxtImg :src="author.profilePicture" class="w-8 h-8 rounded-full drop-shadow-lg"/>
          <p class="text-2xl">{{author.name}}</p>
        </div>
        <p>{{author.description}}</p>
      </CardHeader>
    </Card>

    <template v-if="showPosts">
      <p class="text-3xl">{{author.name}}'s blog posts</p>
      <BlogPostPreview v-for="post in author.posts" :key="post.id" :post="post"/>
    </template>
  </div>
</template>

<style scoped>

</style>