import { create } from 'zustand';

interface Location {
  longitude: number;
  latitude: number;
}

interface UserProfile {
  ageGroup: string;
  brix: number;
  gender: string;
  id: number;
  isPaid: boolean;
  nickname: string;
  paidAmount: number | null;
  profileImage: string;
  role: string;
  settleAmount: number | null;
}

interface PartyDetail {
  arrivalsLocation: Location;
  arrivalsName: string;
  category: string;
  createAt: string;
  currentParticipants: number;
  departuresDate: string;
  departuresLocation: Location;
  departuresName: string;
  genderRestriction: string;
  id: number | null;
  maxParticipants: number;
  modifiedAt: string;
  organizer: UserProfile;
  partyMemberDTOList: UserProfile[];
  state: string;
  title: string;
}

interface IPartyHistory {
  range: string;
  sort: string;
  setRange: (range: string) => void;
  setSort: (sort: string) => void;
}

interface PartyHistoryStore extends IPartyHistory {
  parties: PartyDetail[];
  setParties: (parties: PartyDetail[]) => void;
}

const usePartyHistoryStore = create<PartyHistoryStore>(set => ({
  range: 'all',
  sort: 'desc',
  setRange: range => set({ range }),
  setSort: sort => set({ sort }),
  parties: [],
  setParties: parties => set({ parties }),
}));

export default usePartyHistoryStore;
