import { getData, postData } from './http';
import type { HomeData, MemoryEntry, PageResponse, Photo, PhotoGroup, ProfileSection } from '../types/models';

export const fetchHome = () => getData<HomeData>('/site/home');
export const fetchAbout = () => getData<Record<string, string>>('/site/about');
export const fetchProfile = () => getData<ProfileSection[]>('/profile');
export const fetchPhotoGroups = () => getData<PhotoGroup[]>('/photos/grouped');
export const fetchPhotos = () => getData<PageResponse<Photo>>('/photos');
export const fetchPhoto = (id: string | number) => getData<Photo>(`/photos/${id}`);
export const fetchMemories = () => getData<PageResponse<MemoryEntry>>('/memories');
export const fetchMemory = (id: string | number) => getData<MemoryEntry>(`/memories/${id}`);
export const toggleMemoryFavorite = (id: string | number, favorite: boolean) =>
  postData<MemoryEntry>(`/memories/${id}/favorite`, { favorite });
