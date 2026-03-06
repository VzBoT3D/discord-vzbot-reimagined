import axiosInstance from "~/server/axios";

export default defineEventHandler(async (event) => {


    const serialData = (await axiosInstance.get("/serials")).data
    const printerData = ((await axiosInstance.get("/printers/stats")).data)

    return {
        serialData: serialData,
        printerData: printerData
    }
})