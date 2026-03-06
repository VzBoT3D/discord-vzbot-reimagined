export async function fetcher<T>(url: string, options?: RequestInit): Promise<T> {
    const config = useRuntimeConfig()
    const defaultOptions: RequestInit = {
        headers: {
            'Content-Type': 'application/json',
            'token': config.BACKEND_TOKEN
        },
        ...options,
    };

    const response = await fetch(`${config.BACKEND_URL}${url}`, defaultOptions);

    if (!response.ok) {
        throw new Error(`Error: ${response.status} - ${response.statusText}`);
    }

    return response.json();
}
