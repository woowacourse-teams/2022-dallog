import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { ProfileType } from '@/@types/profile';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import PageLayout from '@/components/@common/PageLayout/PageLayout';

import { CACHE_KEY, CONFIRM_MESSAGE, PATH } from '@/constants';

import { clearAccessToken } from '@/utils';

import profileAPI from '@/api/profile';

import {
  imageInfo,
  imageSize,
  infoTable,
  infoTableHeader,
  logoutButtonStyle,
  myPage,
  textInfo,
} from './MyPage.styles';

function MyPage() {
  const { accessToken } = useRecoilValue(userState);

  const navigate = useNavigate();

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

  const handleClickLogoutButton = () => {
    if (window.confirm(CONFIRM_MESSAGE.LOGOUT)) {
      clearAccessToken();
      navigate(PATH.MAIN);
      location.reload();
    }
  };

  const { displayName, email, profileImageUrl } = profileGetResponse.data;

  return (
    <PageLayout>
      <div css={myPage}>
        <div css={infoTable}>
          <div css={infoTableHeader}>
            <span>프로필</span>
            <Button cssProp={logoutButtonStyle} onClick={handleClickLogoutButton}>
              로그아웃
            </Button>
          </div>
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
    </PageLayout>
  );
}

export default MyPage;
