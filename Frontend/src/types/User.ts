export interface IUser {
  id: number;
  name?: string;
  gender?: string;
  email?: string;
  birth?: Date | number;
  ageGroup?: string;
  nickname?: string;
  point?: number;
  image?: string;
  isVerified?: boolean;
  brix?: number;
  error?: IUserError;
}

export interface IUserError {
  error?: string;
}
