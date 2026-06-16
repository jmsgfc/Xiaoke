import axios from 'axios';
import type { ApiResponse } from '../types/models';

export const http = axios.create({
  baseURL: '/api',
  timeout: 15000
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('xiaoke_admin_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export async function getData<T>(url: string, params?: Record<string, unknown>) {
  const response = await http.get<ApiResponse<T>>(url, { params });
  return response.data.data;
}

export async function postData<T>(url: string, body?: unknown) {
  const response = await http.post<ApiResponse<T>>(url, body);
  return response.data.data;
}

export async function putData<T>(url: string, body?: unknown) {
  const response = await http.put<ApiResponse<T>>(url, body);
  return response.data.data;
}

export async function deleteData<T>(url: string) {
  const response = await http.delete<ApiResponse<T>>(url);
  return response.data.data;
}

