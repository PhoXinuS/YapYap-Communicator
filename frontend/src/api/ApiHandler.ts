const baseUrl = process.env.REACT_APP_BACKEND_URL; // Base URL for the API

const customFetch = async function (url: string, method: string, body?: any) {

    const token = localStorage.getItem('authToken'); // Get the token from local storage

    const response = await fetch(`${baseUrl}${url}`, {
        method,
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(body),
    });

    if (!response.ok) {
        throw new Error(`Failed to fetch data: ${response.statusText}`);
    }

    return response;
}

const Put = async function (url: string, body: any) {
    return customFetch(url, 'PUT', body);
}

const Patch = async function (url: string, body: any) {
    return customFetch(url, 'PATCH', body);
}

const Delete = async function (url: string) {
    return customFetch(url, 'DELETE');
}

const Post = async function (url: string, body?: any) {
    return customFetch(url, 'POST', body);
}

const Get = async function (url: string) {
    return customFetch(url, 'GET');
}

const api = { Put, Patch, Delete, Post, Get };

export default api;