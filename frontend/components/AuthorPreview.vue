<script setup lang="ts">

import type {IAuthor} from "~/lib/types/Blog";

defineProps<{author: IAuthor}>()

</script>

<template>
  <Card class="w-fit">
    <CardHeader>
      <div class="flex gap-2 items-center">
        <NuxtImg :src="author.profilePicture" class="w-8 h-8 rounded-full drop-shadow-lg"/>
        <p class="text-2xl">{{author.name}}</p>
      </div>
      <p>{{author.description}}</p>
    </CardHeader>
  </Card>
</template>

<style scoped>

</style>