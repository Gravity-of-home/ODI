import { ILocation } from '@/stores/usePartyStore';

export interface IPlaceInfo {
  buildingName?: string | null;
  geoPoint?: {
    latitude: number;
    longitude: number;
  };
  id?: string;
  distance?: number;
  jibunAddress?: string;
  majorCategory?: string;
  placeName?: string;
  postalCode?: number;
  roadNameAddress?: string;
  sido?: string;
  sigungu?: string;
  subCategory?: string;
}

export interface IPostParty {
  title: string;
  departuresName: string;
  departuresLocation: ILocation;
  arrivalsName: string;
  arrivalsLocation: ILocation;
  departuresDate: string;
  maxParticipants: number;
  category: string;
  genderRestriction: boolean;
  content?: string;
}

export interface IInfo {
  id: number;
  roomId: string;
  createAt: string;
  modifiedAt: string;
  title: string;
  departuresName: string;
  departuresLocation: {
    latitude: number;
    longitude: number;
  };
  arrivalsName: string;
  arrivalsLocation: {
    latitude: number;
    longitude: number;
  };
  expectedCost: number;
  expectedDuration: number;
  departuresDate: string;
  currentParticipants: number;
  maxParticipants: number;
  category: string;
  genderRestriction: string;
  state: string;
  content: string;
  viewCount: number;
  requestCount: number;
  role: string;
  participants: {
    id: number;
    role: string;
    nickname: string;
    gender: string;
    ageGroup: string;
    profileImage: string;
    isPaid: boolean;
  }[];
  guests: {
    id: number;
    role: string;
    nickname: string;
    gender: string;
    ageGroup: string;
    profileImage: string;
    isPaid: boolean;
  }[];
  distance: number;
  path: number[][];
}

export interface IParticipant {
  id: number;
  role: string;
  nickname: string;
  gender: string;
  ageGroup: string;
  profileImage: string;
  isPaid: boolean;
}
