import axiosInstance from "~/server/axios";

export default defineEventHandler(async (event) => {
    const authors = (await axiosInstance.get("/authors")).data

    return {
        authors
    }
})