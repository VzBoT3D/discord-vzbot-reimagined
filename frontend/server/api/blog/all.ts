import axiosInstance from "~/server/axios";

export default defineEventHandler(async (event) => {
    const posts = (await axiosInstance.get("/blogs")).data

    return {
        posts
    }
})