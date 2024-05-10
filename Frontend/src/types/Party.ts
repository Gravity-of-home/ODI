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
