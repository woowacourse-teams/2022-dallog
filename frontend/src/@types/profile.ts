interface ProfileType {
  id: number;
  email: string;
  displayName: string;
  profileImageUrl: string;
  socialType: string;
}

interface ProfileGetResponseType {
  data: ProfileType;
}

export { ProfileType, ProfileGetResponseType };
