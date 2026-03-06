import axios from 'axios';

const config = useRuntimeConfig()

console.log(`[axios] Backend URL: ${config.BACKEND_URL}`)

const axiosInstance = axios.create({
    baseURL: config.BACKEND_URL,
    headers: {
        'Content-Type': 'application/json',
        'token': config.BACKEND_TOKEN,
    },
});

axiosInstance.interceptors.request.use((req) => {
    console.log(`[axios] → ${req.method?.toUpperCase()} ${req.baseURL}${req.url}`)
    return req
})

axiosInstance.interceptors.response.use(
    (res) => {
        console.log(`[axios] ← ${res.status} ${res.config.url}`)
        return res
    },
    (err) => {
        console.error(`[axios] ✗ ${err.config?.url} — ${err.message}`)
        return Promise.reject(err)
    }
)

export default axiosInstance;
