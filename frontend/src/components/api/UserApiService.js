import { apiClient } from "./ApiClient";

const usersUrl = '/api/v1/users';

export const retrieveAllUsers = () => apiClient.get(usersUrl);

export const retrieveUserById = (userId) => apiClient.get(`${usersUrl}/${userId}`);

export const createUser = (user) => apiClient.post(usersUrl, user);

export const updateUser = (user) => apiClient.patch(usersUrl, user);

export const retrieveRoomsByUserId = (userId) => apiClient.get(`${usersUrl}/${userId}/rooms`);