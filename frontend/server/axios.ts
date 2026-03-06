import axios from 'axios';

const config = useRuntimeConfig()

const axiosInstance = axios.create({
    baseURL: config.BACKEND_URL, // Base URL for your API
    headers: {
        'Content-Type': 'application/json',
        'token': config.BACKEND_TOKEN,
    },
});

export default axiosInstance;
