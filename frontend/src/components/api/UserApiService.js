import { apiClient } from "./ApiClient";

const usersUrl = '/api/v1/users';

export const retrieveAllUsersApi = () => apiClient.get(usersUrl);

export const retrieveUserByIdApi = (userId) => apiClient.get(`${usersUrl}/${userId}`);

export const retrieveUserByUsernameApi = (username) => apiClient.get(`${usersUrl}/username/${username}`);

export const createUserApi = (user) => apiClient.post(usersUrl, user);

export const updateUserApi = (user) => apiClient.patch(usersUrl, user);

export const retrieveRoomsByUserIdApi = (userId) => apiClient.get(`${usersUrl}/${userId}/rooms`);