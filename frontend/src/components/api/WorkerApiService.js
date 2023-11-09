import { apiClient } from "./ApiClient";

const workerUrl = '/api/v1/workers';

export const createWorker = (worker) => apiClient.post(workerUrl, worker);

export const updateWorker = (worker) => apiClient.patch(workerUrl, worker);

export const deleteWorkerById = (workerId) => apiClient.post(`${workerUrl}/${workerId}`);