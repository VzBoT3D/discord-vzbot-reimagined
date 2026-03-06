import axiosInstance from "~/server/axios";

export default defineEventHandler(async (event) => {
    const id = getRouterParam(event, 'id')

    const printerData = (await axiosInstance.get("/printer/" + id)).data

    return {
        printerData
    }
})