import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import { ProfileType } from '@/@types/profile';

import { userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants';

import profileAPI from '@/api/profile';

import {
  imageInfo,
  imageSize,
  infoTable,
  infoTableHeader,
  myPage,
  textInfo,
} from './MyPage.styles';

function MyPage() {
  const { accessToken } = useRecoilValue(userState);

  const {
    isLoading,
    error,
    data: profileGetResponse,
  } = useQuery<AxiosResponse<ProfileType>, AxiosError>(CACHE_KEY.PROFILE, () =>
    profileAPI.get(accessToken)
  );

  if (isLoading || profileGetResponse === undefined) {
    return <div>Loading</div>;
  }

  if (error) {
    return <div>Error</div>;
  }

  const { displayName, email, profileImageUrl } = profileGetResponse.data;

  return (
    <div css={myPage}>
      <div css={infoTable}>
        <div css={infoTableHeader}>프로필</div>
        <div css={textInfo}>
          <span>이름(닉네임)</span>
          <span>{displayName}</span>
        </div>
        <div css={textInfo}>
          <span>계정</span>
          <span>{email}</span>
        </div>
        <div css={imageInfo}>
          <span>프로필 사진</span>
          <img src={profileImageUrl} css={imageSize} alt="프로필 이미지" />
        </div>
      </div>
    </div>
  );
}

export default MyPage;
