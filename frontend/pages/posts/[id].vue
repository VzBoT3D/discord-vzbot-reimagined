<script setup lang="ts">
import type {IPost} from "~/lib/types/Blog";
import BlogPost from "~/components/BlogPost.vue";
import Loading from "~/components/Loading.vue";

definePageMeta({
  layout: "landing-page-layout"
})

const route = useRoute()
const id = route.params.id

const {data, error, status} = useFetch<{blog: IPost}>("/api/blog/" + id)

</script>

<template>
  <div v-motion-fade v-if="status === 'pending'">
    <Loading/>
  </div>

  <div v-else-if="error || !data?.blog" class="flex flex-col items-center">
    <p class="text-4xl text-center">The given post does not exist</p>
    <Button class="w-fit">Browse all posts</Button>
  </div>

  <div v-else class="flex justify-center items-center">
    <BlogPost :blog="data.blog"/>
  </div>
</template>

<style scoped>

</style>