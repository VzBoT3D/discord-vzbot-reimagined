import axiosInstance from "~/server/axios";

export default defineEventHandler(async (event) => {
    try {
        const res = (await axiosInstance.get("/status"))
    } catch (e) {
        return false
    }

    return true
})

