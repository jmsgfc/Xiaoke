import { deleteData, getData, http, postData, putData } from './http';
import type { MemoryEntry, Photo, ProfileSection } from '../types/models';

export interface LoginResult {
  token: string;
  nickname: string;
}

export interface UploadResult {
  imageUrl: string;
  thumbnailUrl: string;
  storagePath: string;
  thumbnailStoragePath: string;
}

export const login = (username: string, password: string) =>
  postData<LoginResult>('/auth/login', { username, password });

export const fetchMe = () => getData<{ username: string; nickname: string }>('/auth/me');

export async function uploadImage(file: File) {
  const form = new FormData();
  form.append('file', file);
  const response = await http.post('/admin/uploads/image', form, {
    headers: { 'Content-Type': 'multipart/form-data' }
  });
  return response.data.data as UploadResult;
}

export const createPhoto = (photo: Partial<Photo> & Record<string, unknown>) =>
  postData<Photo>('/admin/photos', photo);

export const updatePhoto = (id: number, photo: Partial<Photo> & Record<string, unknown>) =>
  putData<Photo>(`/admin/photos/${id}`, photo);

export const removePhoto = (id: number) => deleteData<boolean>(`/admin/photos/${id}`);

export const createMemory = (memory: Partial<MemoryEntry> & Record<string, unknown>) =>
  postData<MemoryEntry>('/admin/memories', memory);

export const updateMemory = (id: number, memory: Partial<MemoryEntry> & Record<string, unknown>) =>
  putData<MemoryEntry>(`/admin/memories/${id}`, memory);

export const removeMemory = (id: number) => deleteData<boolean>(`/admin/memories/${id}`);

export const updateProfileSection = (id: number, section: Partial<ProfileSection>) =>
  putData<ProfileSection>(`/admin/profile-sections/${id}`, section);

