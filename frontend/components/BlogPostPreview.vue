<script setup lang="ts">

import type {IPost} from "~/lib/types/Blog";
import {calculateReadingTime, formatToMinutes, toReadableLocaleDate} from "~/lib/utils";
import VueMarkdown from "vue-markdown-render";

defineProps<{
  post: IPost
}>()

</script>

<template>
  <Card>
    <CardHeader>
      <p class="text-3xl">{{post.title}}</p>
      <p>{{formatToMinutes(calculateReadingTime(post.content))}} read</p>
      <p>{{toReadableLocaleDate(post.createdAt)}}</p>
    </CardHeader>
    <CardContent>
      <div class="flex flex-col gap-2">
        <div class="overflow-hidden h-24 w-fit lg:w-96 bg-clip-text text-transparent bg-gradient-to-b from-foreground">
          <VueMarkdown :source="post.content"/>
        </div>
        <Button @click="navigateTo('/posts/' + post.id)" variant="outline">Read full</Button>
      </div>

    </CardContent>
  </Card>
</template>

<style scoped>

</style>