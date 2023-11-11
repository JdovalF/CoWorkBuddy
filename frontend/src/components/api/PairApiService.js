import { apiClient } from "./ApiClient";

const pairsUrl = '/api/v1/pairs';

export const createPairs = (pairs) => apiClient.post(pairsUrl, pairs);

export const recommendPairs = (roomId) => apiClient.get(`${pairsUrl}/recommend/${roomId}`);

export const deletePairsHistory = () => apiClient.delete(`${pairsUrl}/reset/all`);

export const retrievePairsByRoomId = (roomId) => apiClient.get(`${pairsUrl}/${roomId}`);