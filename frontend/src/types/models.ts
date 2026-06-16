export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

export interface Photo {
  id: number;
  title: string;
  diaryNote: string;
  imageUrl: string;
  thumbnailUrl: string;
  takenDate?: string;
  uploadDate: string;
  featured: boolean;
  visibility: string;
}

export interface PhotoGroup {
  month: string;
  photos: Photo[];
}

export interface MemoryEntry {
  id: number;
  title: string;
  summary: string;
  content: string;
  memoryDate?: string;
  coverImageUrl?: string;
  moodTags: string[];
  featured: boolean;
  favoriteCount?: number;
  visibility: string;
}

export interface ProfileSection {
  id: number;
  sectionKey: string;
  sectionTitle: string;
  content: string;
  sortOrder: number;
}

export interface HomeData {
  heroTitle: string;
  heroSubtitle: string;
  todayNote: string;
  heroImageUrl: string;
  latestPhotos: Photo[];
  featuredMemories: MemoryEntry[];
  anniversaryText: string;
}

export interface PageResponse<T> {
  records: T[];
  page: number;
  pageSize: number;
  total: number;
}
