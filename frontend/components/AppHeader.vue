<script setup lang="ts">
import {Icon} from "@iconify/vue";
import type {IPrinterProfile} from "~/lib/types/PrinterProfile";
import {
  Drawer,
  DrawerClose,
  DrawerContent,
  DrawerDescription,
  DrawerFooter,
  DrawerHeader,
  DrawerTitle,
  DrawerTrigger,
} from '@/components/ui/drawer'
const openGithub = () => {
  window.open("https://github.com/VzBoT3D", "_blank")
}

const openDiscord = () => {
  window.open("https://discord.gg/vzbot-829828765512106054", "_blank")
}

const {data} = useFetch<{printerData: IPrinterProfile[]}>("/api/printer/profiles")

const router = useRouter()

const colorMode = useColorMode()

const logoURL = computed(() => {
  return colorMode.value === 'light' ? '/logo_with_text_dark.png' : '/logo_with_text.png'
})

</script>

<template>
  <nav class="top-0 w-full p-2 bg-background opacity-100 z-20">
    <div class="flex items-center justify-between lg:justify-start p-1 h-12 px-3 w-full rounded-xl">
      <div class="h-full w-1/3 hidden lg:flex opacity-100">
        <div @click="navigateTo('/')" class="flex hover:bg-secondary transition-colors duration-150 hover:cursor-pointer rounded-md">
          <NuxtImg class="object-contain scale-95 p-1" :src="logoURL"/>
        </div>
      </div>

      <div class="flex lg:hidden gap-2 justify-center place-items-center">
        <Drawer class="flex lg:hidden" direction="left">
          <DrawerTrigger as-child>
            <Icon class="h-10 w-10" icon="ci:hamburger-lg"></Icon>
          </DrawerTrigger>
          <DrawerContent class="h-screen top-0 right-0 mt-0 w-[75%] rounded-none">
            <div class="flex flex-col justify-centers">
              <NuxtImg @click="navigateTo('/')" class="object-contain h-12 scale-95 p-1" :src="logoURL"/>

              <div class="flex flex-col p-4 gap-8">
                <div class="flex flex-col gap-2 justify-center">
                  <p class="text-2xl text-center font-thin">Machines</p>
                  <Button v-for="printerProfile in data?.printerData" @click="navigateTo('/printers/' + printerProfile.printer.name)" variant="secondary">{{printerProfile.printer.name}}</Button>
                </div>

                <div class="flex flex-col gap-2 justify-center">
                  <p class="text-2xl text-center font-thin">Community</p>
                  <Button @click="router.push('/community')" variant="secondary">Serials</Button>
                  <Button @click="navigateTo('/authors')" variant="secondary">Meet the team</Button>
                </div>

                <div class="flex flex-col gap-2 justify-center">
                  <p class="text-2xl text-center font-thin">Other</p>
                  <Button @click="navigateTo('/posts')" variant="secondary">Blog</Button>
                  <Button @click="navigateTo('https://docs.vzbot.org/')" variant="secondary">Docs</Button>
                </div>
              </div>
            </div>
          </DrawerContent>
        </Drawer>

        <NuxtImg @click="navigateTo('/')" class="object-contain h-8 scale-95 p-1" :src="logoURL"/>
      </div>

      <div class="lg:w-1/3 hidden lg:flex justify-center">
        <DropdownMenu>
          <DropdownMenuTrigger as-child>
            <Button variant="ghost">Machines</Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            <DropdownMenuItem v-for="printerProfile in data?.printerData" @click="navigateTo('/printers/' + printerProfile.printer.name)">
              <div class="flex flex-col max-w-36">
                <p class="text-xl font-thin">{{printerProfile.printer.name}}</p>
              </div>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
        <DropdownMenu>
          <DropdownMenuTrigger>
            <Button variant="ghost">Community</Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent>
            <DropdownMenuItem @click="router.push('/community')">
              <div class="flex flex-col max-w-36">
                <p class="text-xl font-thin">Serials</p>
              </div>
            </DropdownMenuItem>
            <DropdownMenuItem @click="navigateTo('/authors')">
              <div class="flex flex-col max-w-36">
                <p class="text-xl font-thin">Meet the team</p>
              </div>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
        <Button @click="navigateTo('/posts')" variant="ghost">Blog</Button>
        <Button @click="navigateTo('https://docs.vzbot.org/')" variant="ghost">Docs</Button>
      </div>

      <div class="lg:w-1/3 flex items-center justify-end">
        <ThemeSelector class="justify-self-end"></ThemeSelector>
        <Button variant="ghost" @click="openGithub">
          <Icon icon="iconoir:github" class="h-[1.2rem] w-[1.2rem]"/>
        </Button>
        <Button variant="ghost" @click="openDiscord">
          <Icon icon="ic:baseline-discord" class="h-[1.2rem] w-[1.2rem]"/>
        </Button>
      </div>
    </div>
  </nav>
</template>