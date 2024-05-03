export interface IUser {
  memberId: string;
  name: string;
  gender?: boolean;
  email?: string;
  birth?: Date | number;
  nickname?: string;
  point?: number;
  profileImg?: string;
  isVerified?: boolean;
  brix?: number;
  error?: IUserError;
}

export interface IUserError {
  error?: string;
}
