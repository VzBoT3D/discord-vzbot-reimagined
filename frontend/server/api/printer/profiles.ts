import axiosInstance from "~/server/axios";

export default defineEventHandler(async (event) => {
    const printerData = (await axiosInstance.get("/printers/profiles")).data

    return {
        printerData
    }
})