import { apiClient } from "./ApiClient";

const rolesUrl = '/api/v1/roles';

export const retrieveAllRoles = () => apiClient.get(rolesUrl);

export const retrieveRoleById = (roleId) => apiClient.get(`${rolesUrl}/${roleId}`);