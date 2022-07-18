interface ProfileType {
  id: number;
  email: string;
  displayName: string;
  profileImageUri: string;
  socialType: string;
}

interface ProfileGetResponseType {
  data: ProfileType;
}

export { ProfileType, ProfileGetResponseType };
