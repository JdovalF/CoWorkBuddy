import { apiClient } from "./ApiClient";

export const executeJwtAuthenticateService 
= (username, password) => apiClient.post('/auth/v1/authenticate', {username, password});