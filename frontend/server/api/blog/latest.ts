import axiosInstance from "~/server/axios";

export default defineEventHandler(async (event) => {
    const blog = (await axiosInstance.get("/blogs/latest")).data

    return {
        blog
    }
})