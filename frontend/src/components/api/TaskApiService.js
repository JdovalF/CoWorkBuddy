import { apiClient } from "./ApiClient";

const taskUrl = '/api/v1/tasks';

export const createTask = (task) => apiClient.post(taskUrl, task);

export const updateTask = (task) => apiClient.patch(taskUrl, task);

export const deleteTaskById = (taskId) => apiClient.post(`${taskUrl}/${taskId}`);