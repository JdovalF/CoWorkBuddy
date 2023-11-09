import { apiClient } from "./ApiClient";

const roomsUrl = '/api/v1/rooms';

export const createRoom = (room) => apiClient.post(roomsUrl, room);

export const updateRoom = (room) => apiClient.patch(roomsUrl, room);

export const deleteRoomById = (roomId) => apiClient.delete(`${roomsUrl}/${roomId}`);

export const retrieveTasksByRoomId = (roomId) => apiClient.get(`${roomsUrl}/${roomId}/tasks`);

export const retrieveWorkersByRoomId = (roomId) => apiClient.get(`${roomsUrl}/${roomId}/workers`);