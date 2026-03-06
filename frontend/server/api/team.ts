import axiosInstance from "~/server/axios";

export default defineEventHandler(async (event) => {
    const teamData = (await axiosInstance.get("/team")).data

    return {
        teamData: teamData,
    }
})