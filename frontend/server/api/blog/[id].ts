import axiosInstance from "~/server/axios";

export default defineEventHandler(async (event) => {
    const id = getRouterParam(event, 'id')

    const result = (await axiosInstance.get("/blogs/" + id))

    if (result.status === 404) {
        throw createError({
            statusCode: 404,
            statusMessage: "Blog not found"
        })
    }

    const blog = result.data
    return {blog}
})